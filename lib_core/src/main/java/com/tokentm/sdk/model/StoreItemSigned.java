package com.tokentm.sdk.model;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.wallet.SignUtils;
import com.xxf.arch.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自动签名
 */
@JsonAdapter(StoreItemSigned.SignedJsonSerializer.class)
public class StoreItemSigned extends StoreItem<String> {

    /**
     * 只签名,不加密
     */
    public static class SignedJsonSerializer implements JsonSerializer<StoreItemSigned> {

        /**
         * 获取数据私钥
         *
         * @param did
         * @return
         */
        private String _getDPK(String did) {
            String dpk = SDKsp.getInstance()._getDPK(did);
            if (TextUtils.isEmpty(dpk)) {
                throw new RuntimeException("dpk is null");
            }
            return dpk;
        }

        @Override
        public JsonElement serialize(StoreItemSigned src, Type typeOfSrc, JsonSerializationContext context) {
            //加密数据
            String data = src.getData();
            //时间戳
            long timestamp = System.currentTimeMillis();
            JsonObject storeItemJsonObject = new JsonObject();
            storeItemJsonObject.addProperty("dataId", src.getDataId());
            storeItemJsonObject.addProperty("dataType", src.getDataType());
            storeItemJsonObject.addProperty("data", data);
            storeItemJsonObject.addProperty("did", src.getDid());
            storeItemJsonObject.addProperty("timestamp", timestamp);
            storeItemJsonObject.addProperty("version", src.getVersion());

            //data sign
            Map<String, String> dataSignMap = new HashMap<>();
            dataSignMap.put("data", data);
            String dataSign = SignUtils.sign(dataSignMap, _getDPK(src.getDid()));
            storeItemJsonObject.addProperty("dataSign", dataSign);

            // api sign
            Map<String, String> signMap = JsonUtils.toMap(storeItemJsonObject.toString(), new TypeToken<Map<String, String>>() {
            });
            String sign = SignUtils.sign(signMap, _getDPK(src.getDid()));
            storeItemJsonObject.addProperty("sign", sign);

            return storeItemJsonObject;
        }
    }

}
