package com.tokentm.sdk.common.encrypt;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自动加解密的Gson TypeAdapter
 */
public class AutoEncryptionDecryptionJsonTypeAdapter
        extends TypeAdapter<String> implements EncryptionDecryptionConvertor {
    public static transient volatile EncryptionDecryptionConvertor INSTANCE = new AutoEncryptionDecryptionJsonTypeAdapter();

    /**
     * 注意:序列化成加密字符串
     *
     * @param out
     * @param value
     * @throws IOException
     */
    @Override
    public final void write(JsonWriter out, String value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(encryptString(value));
        }
    }


    /**
     * 加密字符串反序列化成明文模型
     *
     * @param in
     * @return
     * @throws IOException
     */
    @Override
    public final String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case STRING:
                return decryptString(in.nextString());
            default:
                throw new JsonParseException("Expected STRING but was " + peek);
        }
    }

    private final String _getIdentityPwdSecretKey() {
        //TODO 身份密钥
        return "123";
    }


    @Override
    public String encryptString(String plaintext) throws JsonParseException {
        String secretKey = _getIdentityPwdSecretKey();
        return EncryptionUtils.encodeString(plaintext, secretKey);
    }

    @Override
    public String decryptString(String ciphertext) throws JsonParseException {
        String decodeString = EncryptionUtils.decodeString(ciphertext, _getIdentityPwdSecretKey());
        return decodeString;
    }


}