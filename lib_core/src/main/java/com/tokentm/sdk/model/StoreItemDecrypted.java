package com.tokentm.sdk.model;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.JsonAdapter;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.TEAUtils;

import java.lang.reflect.Type;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自动注入解密
 */
@JsonAdapter(StoreItemDecrypted.DecryptJsonDeserializer.class)
public class StoreItemDecrypted extends StoreItem<String> {
    /**
     * 自动解密
     */
    public static class DecryptJsonDeserializer implements JsonDeserializer<StoreItemDecrypted> {

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
        public StoreItemDecrypted deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            StoreItemDecrypted deserialize = context.deserialize(json, typeOfT);
            String decodeString = TEAUtils.decryptString(deserialize.getData(), _getDPK(deserialize.getDid()));
            deserialize.setData(decodeString);
            return deserialize;
        }
    }

}
