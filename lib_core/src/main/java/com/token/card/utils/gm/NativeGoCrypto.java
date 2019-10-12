package com.token.card.utils.gm;

public interface NativeGoCrypto {

    String C_GeneratePrivateKey();

    String C_Sign(String message, String privateKey);

    String C_Hash256Bysha3(String message);

    boolean C_VerifySignature(String signature, String r, String v);

    String C_FromPrvKey(String privateKey);

}
