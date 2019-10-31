package com.tokentm.sdk.common.encrypt;

import android.support.annotation.NonNull;

import com.xxf.arch.XXF;

import org.web3j.utils.Numeric;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import io.reactivex.functions.Function3;
import sm_crypto.Sm_crypto;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class SignUtils {

    /**
     * 参数按照字典顺序升序排列，组装成：[参数1=参数值1&参数2=参数值2]的形式，参数为空不参与签名，然后对拼接后的字符串执行SHA2HASH256摘要算法，
     * 得到摘要值：[X]，之后对[X]使用数据私钥进行国密签名。
     *
     * @param map
     * @param key
     * @return
     */
    public static String sign(@NonNull Map<String, String> map, String key) {
        //去除:value和key为null的情况
        Hashtable<String, String> filterNullTable = new Hashtable<>(map);
        //排序:自然升序
        TreeMap<String, String> stringStringTreeMap =
                new TreeMap<>(filterNullTable);
        //拼接字符串
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : stringStringTreeMap.entrySet()) {
            stringBuilder.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
        }
        String data = stringBuilder.toString().replaceFirst("&", "");

        String sha3Data = Sm_crypto.c_Hash256Bysha3(Numeric.toHexString(data.getBytes(StandardCharsets.UTF_8)));
        String sign = Sm_crypto.c_Sign(sha3Data, key);

        XXF.getLogger().d("========>data=" + data);
        XXF.getLogger().d("========>sha3Data=" + sha3Data);
        XXF.getLogger().d("========>sign=" + sign);
        return sign;
    }

    /**
     * 链私钥签名
     *
     * @param signObject
     * @param chainPrivateKey
     * @return
     * @throws Exception
     */
    public static String signByChainPk(@NonNull SignObject signObject, String chainPrivateKey) throws Exception {
        return sign(signObject,
                new SignFieldFilter() {
                    @Override
                    public Boolean apply(SignObject signObject, Field field, SignField signField) throws Exception {
                        return signField.chainPKSign() && super.apply(signObject, field, signField);
                    }
                },
                chainPrivateKey
        );
    }

    /**
     * 链私钥签名
     *
     * @param signObject
     * @param dataPrivateKey
     * @return
     * @throws Exception
     */
    public static String signDataPk(@NonNull SignObject signObject, String dataPrivateKey) throws Exception {
        return sign(signObject,
                new SignFieldFilter() {
                    @Override
                    public Boolean apply(SignObject signObject, Field field, SignField signField) throws Exception {
                        return signField.dataPKSign() && super.apply(signObject, field, signField);
                    }
                },
                dataPrivateKey
        );
    }

    /**
     * @param signObject
     * @param signFieldFilter
     * @param key
     * @return
     * @throws Exception
     */
    private static String sign(@NonNull SignObject signObject, SignFieldFilter signFieldFilter, String key) throws Exception {
        Objects.requireNonNull(signObject);
        Map<String, String> values = new LinkedHashMap<String, String>();
        Class<?> clazz = signObject.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            SignField sf = field.getAnnotation(SignField.class);
            if (!signFieldFilter.apply(signObject, field, sf)) {
                String fieldName = field.getName();
                Object value = field.get(signObject);
                values.put(fieldName, String.valueOf(value));
            }
        }
        return sign(values, key);
    }

    /**
     * @author youxuan  E-mail:xuanyouwu@163.com
     * @Description 签名过滤器
     */
    private static class SignFieldFilter implements Function3<SignObject, Field, SignField, Boolean> {

        /**
         * 代表是否过滤 true过滤
         *
         * @param signObject
         * @param field
         * @param signField
         * @return
         * @throws Exception
         */
        @Override
        public Boolean apply(SignObject signObject, Field field, SignField signField) throws Exception {
            if (!field.getType().isPrimitive()) {
                throw new RuntimeException("signField annotation only support primitive type field");
            }
            Object value = field.get(signObject);
            return signField == null || (value == null && signField.ignoreNullValue());
        }
    }
}
