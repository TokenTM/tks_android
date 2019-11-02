package com.tokentm.sdk.common.encrypt;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxf.arch.XXF;

import org.web3j.utils.Numeric;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import io.reactivex.functions.BiFunction;
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
     * 获取链私钥签名字段
     *
     * @param signObject
     * @return
     * @throws Exception
     */
    public static Map<String, String> getCPKSignFields(@NonNull SignObject signObject) throws Exception {
        return treeSign(
                signObject, new BiFunction<SignTargetField.SignType, Map<String, String>, String>() {
                    @Override
                    public String apply(SignTargetField.SignType signType, Map<String, String> stringStringMap) throws Exception {
                        //不自动签名
                        return null;
                    }
                })
                .get(SignTargetField.SignType.CPK_SIGN);
    }

    /**
     * 获取数据签名字段
     *
     * @param signObject
     * @return
     * @throws Exception
     */
    public static Map<String, String> getDPKSignFields(@NonNull SignObject signObject) throws Exception {
        return treeSign(
                signObject, new BiFunction<SignTargetField.SignType, Map<String, String>, String>() {
                    @Override
                    public String apply(SignTargetField.SignType signType, Map<String, String> stringStringMap) throws Exception {

                        //不自动签名
                        return null;
                    }
                })
                .get(SignTargetField.SignType.DPK_SIGN);
    }


    /**
     * 链私钥签名
     *
     * @param signObject
     * @param cpk
     * @throws Exception
     */
    public static void signByCPK(@NonNull SignObject signObject, String cpk) throws Exception {
        sign(SignTargetField.SignType.CPK_SIGN, signObject, cpk);
    }

    /**
     * 数据私钥签名
     *
     * @param signObject
     * @param dpk
     * @throws Exception
     */
    public static void signByDPK(@NonNull SignObject signObject, String dpk) throws Exception {
        sign(SignTargetField.SignType.DPK_SIGN, signObject, dpk);
    }

    /**
     * 签名 所有类型
     *
     * @param signObject
     * @param cpk
     * @param dpk
     * @throws Exception
     */
    public static void signAll(@NonNull SignObject signObject, String cpk, String dpk) throws Exception {
        treeSign(signObject, new BiFunction<SignTargetField.SignType, Map<String, String>, String>() {
            @Override
            public String apply(SignTargetField.SignType signType, Map<String, String> stringStringMap) throws Exception {
                if (stringStringMap != null && !stringStringMap.isEmpty()) {
                    switch (signType) {
                        case CPK_SIGN:
                            return sign(stringStringMap, cpk);
                        case DPK_SIGN:
                            return sign(stringStringMap, dpk);
                        default:
                            throw new RuntimeException("not support signType:" + signType);
                    }
                }
                return null;
            }
        });
    }

    public static void sign(SignTargetField.SignType type, @NonNull SignObject signObject, String key) throws Exception {
        treeSign(signObject, new BiFunction<SignTargetField.SignType, Map<String, String>, String>() {
            @Override
            public String apply(SignTargetField.SignType signType, Map<String, String> stringStringMap) throws Exception {
                if (stringStringMap != null && !stringStringMap.isEmpty()) {
                    if (type == signType) {
                        return sign(stringStringMap, key);
                    }
                }
                return null;
            }
        });
    }


    /**
     * 优先深度遍历
     *
     * @param signObject
     * @param signFieldFunction
     * @return
     * @throws Exception
     */
    public static Map<SignTargetField.SignType, Map<String, String>> treeSign(@NonNull SignObject signObject,
                                                                              BiFunction<SignTargetField.SignType, Map<String, String>, String> signFieldFunction) throws Exception {
        Map<SignTargetField.SignType, Map<String, String>> signFieldMap = new HashMap<>();
        for (Field field : signObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            //是child 签名对象
            if (SignObject.class.isAssignableFrom(field.getType())) {
                //child
                SignObject childSignObject = (SignObject) field.get(signObject);
                Map<SignTargetField.SignType, Map<String, String>> childSignFieldMap = treeSign(childSignObject, signFieldFunction);

                for (Map.Entry<SignTargetField.SignType, Map<String, String>> childTypeEntry : childSignFieldMap.entrySet()) {
                    SignTargetField.SignType signType = childTypeEntry.getKey();
                    //遍历
                    for (Map.Entry<String, String> childEntry : childTypeEntry.getValue().entrySet()) {
                        getNoNullSignFieldType(signFieldMap, signType).put(String.format("%s_%s", fieldName, childEntry.getKey()), childEntry.getValue());
                    }
                }
            } else {
                if (!_validateSignFieldsAnnotation(signObject, field)) {
                    continue;
                }
                SignField sf = field.getAnnotation(SignField.class);
                if (sf.chainPKSign()) {
                    getNoNullSignFieldType(signFieldMap, SignTargetField.SignType.CPK_SIGN)
                            .put(field.getName(), String.valueOf(field.get(signObject)));
                }
                if (sf.dataPKSign()) {
                    getNoNullSignFieldType(signFieldMap, SignTargetField.SignType.DPK_SIGN)
                            .put(field.getName(), String.valueOf(field.get(signObject)));
                }
            }
        }

        //为签名字段赋值
        for (Field field : signObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (!_validateSignTargetFieldsAnnotation(signObject, field)) {
                continue;
            }
            SignTargetField sf = field.getAnnotation(SignTargetField.class);
            String signedValue = signFieldFunction.apply(sf.value(), getNoNullSignFieldType(signFieldMap, sf.value()));
            if (!TextUtils.isEmpty(signedValue)) {
                field.set(signObject, signedValue);
            }
        }
        return signFieldMap;
    }

    /**
     * 避免为null
     *
     * @param signFieldMap
     * @param signType
     * @return
     */
    private static Map<String, String> getNoNullSignFieldType(Map<SignTargetField.SignType, Map<String, String>> signFieldMap, SignTargetField.SignType signType) {
        //避免为null
        Map<String, String> signTypeFieldMap = signFieldMap.get(signType);
        if (signTypeFieldMap == null) {
            signTypeFieldMap = new HashMap<>();
            signFieldMap.put(signType, signTypeFieldMap);
        }
        return signTypeFieldMap;
    }


    /**
     * 检验 {@link SignField}
     *
     * @param signObject
     * @param field
     * @return
     * @throws Exception
     */
    private static boolean _validateSignFieldsAnnotation(SignObject signObject, Field field) throws Exception {
        Objects.requireNonNull(signObject);
        Objects.requireNonNull(field);
        SignField signField = field.getAnnotation(SignField.class);
        if (signField == null) {
            return false;
        }
        Class<?> fieldClass = field.getType();
        boolean isSupportType = (!fieldClass.isAnnotation()
                &&
                (String.class.isAssignableFrom(fieldClass) || fieldClass.isPrimitive())
        );
        if (!isSupportType) {
            throw new RuntimeException(String.format("%s_%s_signField annotation only support Primitive type or String field!", signObject.getClass().toString(), field.getName()));
        }
        Object value = field.get(signObject);
        if (value == null && signField.ignoreNullValue()) {
            return false;
        }
        return true;
    }

    /**
     * 检验 {@link SignTargetField}
     *
     * @param signObject
     * @param field
     * @return
     * @throws Exception
     */
    private static boolean _validateSignTargetFieldsAnnotation(SignObject signObject, Field field) throws Exception {
        Objects.requireNonNull(signObject);
        Objects.requireNonNull(field);
        SignTargetField signTargetField = field.getAnnotation(SignTargetField.class);
        if (signTargetField == null) {
            return false;
        }
        Class<?> fieldClass = field.getType();
        boolean isSupportType = String.class.isAssignableFrom(fieldClass);
        if (!isSupportType) {
            throw new RuntimeException(String.format("%s_%s_signTargetField annotation only support String field!", signObject.getClass().toString(), field.getName()));
        }
        return true;
    }
}
