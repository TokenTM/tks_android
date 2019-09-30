package com.token.card.utils.gm;

import sm_crypto.Sm_crypto;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NativeGoCryptoImpl implements NativeGoCrypto {
    public static volatile NativeGoCryptoImpl INSTANCE = new NativeGoCryptoImpl();

    @Override
    public String c_FromPrvKey(String privateKey) {
        return Sm_crypto.c_FromPrvKey(privateKey);
    }

    @Override
    public String c_GeneratePrivateKey() {
        return Sm_crypto.c_GenerateKey();
    }

    @Override
    public String C_Sign(String message, String privateKey) {
        return Sm_crypto.c_Sign(message, privateKey);
    }

    @Override
    public String C_Hash256Bysha3(String message) {
        return Sm_crypto.c_Hash256Bysha3(message);
    }

    @Override
    public boolean C_VerifySignature(String signature, String r, String v) {
        return Sm_crypto.c_VerifySignature(signature, r, v);
    }

    @Override
    public String C_FromPrvKey(String privateKey) {
        return Sm_crypto.c_FromPrvKey(privateKey);
    }
}
