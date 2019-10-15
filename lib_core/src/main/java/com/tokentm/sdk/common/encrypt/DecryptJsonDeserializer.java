package com.tokentm.sdk.common.encrypt;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 解密
 */
public class DecryptJsonDeserializer implements JsonDeserializer<String> {


    private final String _getIdentityPwdSecretKey() {
        //TODO 身份密钥
        return "123";
    }

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String cipherText = json.getAsString();
        String decodeString = EncryptionUtils.decodeString(cipherText, _getIdentityPwdSecretKey());
        return decodeString;
    }
}
