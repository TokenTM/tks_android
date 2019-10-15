package com.tokentm.sdk.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.tokentm.sdk.common.encrypt.EncryptionUtils;

import java.lang.reflect.Type;

/**
 * 解密
 */
class DecryptJsonDeserializer implements JsonDeserializer<DecryptedStoreItem> {

    /**
     * 获取数据私钥
     *
     * @param did
     * @return
     */
    private String _getDataPrivateKey(String did) {
        //TODO
        return "";
    }

    @Override
    public DecryptedStoreItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DecryptedStoreItem deserialize = context.deserialize(json, typeOfT);
        deserialize.setData(EncryptionUtils.decodeString(deserialize.getData(), _getDataPrivateKey(deserialize.getDid())));
        return deserialize;
    }
}

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自动注入解密
 */
@JsonAdapter(DecryptJsonDeserializer.class)
public class DecryptedStoreItem extends StoreItem {

}
