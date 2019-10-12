package com.tokentm.sdk.wallet;

import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import sm_crypto.Sm_crypto;

public class SignUtils {

    /**
     * 参数按照字典顺序升序排列，组装成：[参数1=参数值1&参数2=参数值2]的形式，参数为空不参与签名，然后对拼接后的字符串执行SHA2HASH256摘要算法，
     * 得到摘要值：[X]，之后对[X]使用数据私钥进行国密签名。
     *
     * @param map
     * @param dataPublicKey
     * @return
     */
    public static String sign(Map<String, String> map, String dataPublicKey) {
        //去除:value和key为null的情况
        Hashtable<String, String> filterNullTable = new Hashtable<>(map);
        //排序:自然升序
        TreeMap<String, String> stringStringTreeMap = new TreeMap<>(filterNullTable);
        //拼接字符串
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : stringStringTreeMap.entrySet()) {
            stringBuilder.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
        }
        String data = stringBuilder.toString().replaceFirst("&", "");
        String sha3Data = Sm_crypto.c_Hash256Bysha3(data);
        return Sm_crypto.c_Sign(sha3Data, dataPublicKey);
    }
}
