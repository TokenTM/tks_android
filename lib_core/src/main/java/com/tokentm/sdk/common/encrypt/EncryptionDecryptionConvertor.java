package com.tokentm.sdk.common.encrypt;

import com.google.gson.JsonParseException;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface EncryptionDecryptionConvertor {

    /**
     * 加密
     *
     * @param plaintext
     */
    String encryptString(String plaintext) throws JsonParseException;

    /**
     * 解密
     *
     * @param ciphertext
     * @return
     */
    String decryptString(String ciphertext) throws JsonParseException;
}
