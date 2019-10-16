package com.tokentm.sdk.model;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.EncryptionUtils;

import java.lang.reflect.Type;

/**
 * 解密
 */
class DecryptJsonDeserializer implements JsonDeserializer<StoreDecryptedItem> {

    /**
     * 获取数据私钥
     *
     * @param did
     * @return
     */
    private String _getDPK(String did) throws RuntimeException {
        String dpk = SDKsp.getInstance()._getDPK(did);
        if (TextUtils.isEmpty(dpk)) {
            throw new RuntimeException("dpk is null");
        }
        return dpk;
    }

    @Override
    public StoreDecryptedItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StoreDecryptedItem deserialize = context.deserialize(json, typeOfT);
        String decodeString = EncryptionUtils.decodeString(deserialize.getData(), _getDPK(deserialize.getDid()));
        deserialize.setData(decodeString);
        return deserialize;
    }
}

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自动注入解密
 */
@JsonAdapter(DecryptJsonDeserializer.class)
public class StoreDecryptedItem extends StoreItem<String> {

}
