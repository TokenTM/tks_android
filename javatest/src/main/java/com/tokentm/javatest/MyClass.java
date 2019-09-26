package com.tokentm.javatest;

import com.token.card.utils.gm.SM2Signer;

import org.bouncycastle.jcajce.provider.digest.SM3;
import org.web3j.utils.Numeric;

import java.security.MessageDigest;

public class MyClass {
    public static void main(String[] args) {
        loadSoFileJAVA();
    }

    private static void loadSoFileJAVA() {
        try {
            final String privateKey = "8e10e5f4806902afad620e720c0c84c5ce9d741f94c3072a090b1d1e52c07055";

            byte[] rlpBytes = "Hello".getBytes();
            MessageDigest digest = new SM3.Digest();
            digest.update(rlpBytes);
            rlpBytes = digest.digest();
            String message = Numeric.toHexString(rlpBytes);

            SM2Signer sm2Signer = new SM2Signer("/Users/wuyouxuan/Desktop/new_chain_so_file/armeabi-v7a/crypto.so");

            SM2Signer.Signature signature = sm2Signer.signature(rlpBytes, Numeric.hexStringToByteArray(privateKey));
            System.out.println("=====>" + signature.getRHex());
            System.out.println("======>" + signature.getSHex());
            // NativeGoCrypto.INSTANCE.C_Sign(new GoString(message), new GoString(privateKey));

            //System.out.println(Numeric.toHexString(sm2Signer.privateKeyToPublicKey(privateKey)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
