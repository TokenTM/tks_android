package com.token.card.utils.gm;

import java.math.BigInteger;

public class SM2Signer {


    final NativeGoCrypto nativeGoCrypto;

    /**
     * 构建签名实例
     *
     */
    public SM2Signer() {
        nativeGoCrypto =NativeGoCryptoImpl.INSTANCE;
    }

    /**
     * 对信息签名
     *
     * @param msg        需要签名的信息
     * @param privateKey 私钥
     * @return
     */
    public Signature signature(byte[] msg, byte[] privateKey) {
        String message = toHexString(msg, true);
        String signatureHex = nativeGoCrypto.C_Sign(message, toHexString(privateKey, false));
        signatureHex = cleanHexPrefix(signatureHex);
        String rHex = signatureHex.substring(0, 64);
        String sHex = signatureHex.substring(64);
        return new Signature(rHex, sHex);
    }

    /**
     * SHA3.Hash256 信息摘要
     *
     * @param msg 需要计算摘要的信息
     * @return
     */
    public String sha3Hash256Hex(byte[] msg) {
        String message = toHexString(msg, false);
        return nativeGoCrypto.C_Hash256Bysha3(message);
    }

    /**
     * SHA3.Hash256 信息摘要
     *
     * @param msg 需要计算摘要的信息
     * @return
     */
    public byte[] sha3Hash256(byte[] msg) {
        return hexStringToByteArray(sha3Hash256Hex(msg));
    }

    /**
     * 签名验证
     *
     * @param signature 签名信息
     * @param rHex      R
     * @param sHex      S
     * @return
     */
    public boolean verify(String signature, String rHex, String sHex) {
        return nativeGoCrypto.C_VerifySignature(signature, rHex, sHex);
    }

    /**
     * 私钥转公钥
     *
     * @param privateKey 私钥
     * @return
     */
    public byte[] privateKeyToPublicKey(byte[] privateKey) {
        return hexStringToByteArray(privateKeyToPublicKeyHex(privateKey));
    }

    /**
     * 私钥转公钥
     *
     * @param privateKey 私钥
     * @return
     */
    public String privateKeyToPublicKeyHex(byte[] privateKey) {
        return nativeGoCrypto.C_FromPrvKey(toHexString(privateKey, false));
    }

    public static String cleanHexPrefix(String input) {
        return containsHexPrefix(input) ? input.substring(2) : input;
    }

    public static boolean containsHexPrefix(String input) {
        return input != null && input.length() > 0 && input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    public static byte[] hexStringToByteArray(String input) {
        String cleanInput = cleanHexPrefix(input);
        int len = cleanInput.length();
        if (len == 0) {
            return new byte[0];
        } else {
            byte[] data;
            byte startIdx;
            if (len % 2 != 0) {
                data = new byte[len / 2 + 1];
                data[0] = (byte) Character.digit(cleanInput.charAt(0), 16);
                startIdx = 1;
            } else {
                data = new byte[len / 2];
                startIdx = 0;
            }

            for (int i = startIdx; i < len; i += 2) {
                data[(i + 1) / 2] = (byte) ((Character.digit(cleanInput.charAt(i), 16) << 4) + Character.digit(cleanInput.charAt(i + 1), 16));
            }

            return data;
        }
    }

    public static String toHexString(byte[] input, boolean withPrefix) {
        return toHexString(input, 0, input.length, withPrefix);
    }

    public static String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (withPrefix) {
            stringBuilder.append("0x");
        }

        for (int i = offset; i < offset + length; ++i) {
            stringBuilder.append(String.format("%02x", input[i] & 255));
        }

        return stringBuilder.toString();
    }

    public static class Signature {

        private final BigInteger r;

        private final BigInteger s;

        private final String rHex;

        private final String sHex;

        public Signature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
            this.rHex = r.toString(16);
            this.sHex = s.toString(16);
        }

        public Signature(String rHex, String sHex) {
            this.rHex = rHex;
            this.sHex = sHex;
            this.r = new BigInteger(rHex, 16);
            this.s = new BigInteger(sHex, 16);
        }

        public BigInteger getR() {
            return r;
        }

        public BigInteger getS() {
            return s;
        }

        public String getRHex() {
            return rHex;
        }

        public String getSHex() {
            return sHex;
        }
    }
}




