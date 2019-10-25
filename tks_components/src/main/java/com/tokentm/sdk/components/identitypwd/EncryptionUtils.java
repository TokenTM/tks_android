package com.tokentm.sdk.components.identitypwd;

import com.xxf.arch.XXF;

import org.xxtea.XXTEA;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 备份加密
 */
public final class EncryptionUtils {

    /**
     * 加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encodeString(String content, String key) {
        XXF.getLogger().d(String.format("========>encryptString content:%s key: %s", content, key));
        String result = XXTEA.encryptToBase64String(content, key);
        XXF.getLogger().d(String.format("========>encryptString result:%s", result));
        //TODO 写日志
        return result;
    }


    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decodeString(String content, String key) {
        XXF.getLogger().d(String.format("========>decryptString content:%s key: %s", content, key));
        String result = XXTEA.decryptBase64StringToString(content, key);
        XXF.getLogger().d(String.format("========>decryptString result:%s", result));
        //TODO 写日志
        return result;
    }
}
