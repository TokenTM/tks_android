package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.TEAUtils;
import com.tokentm.sdk.wallet.SignUtils;
import com.xxf.arch.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@JsonAdapter(StoreItemEncrypted.EncryptJsonSerializer.class)
public class StoreItemEncrypted extends StoreItem<String> {
    /**
     * 自动签名和加密
     */
    public static class EncryptJsonSerializer implements JsonSerializer<StoreItemEncrypted> {

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
        public JsonElement serialize(StoreItemEncrypted src, Type typeOfSrc, JsonSerializationContext context) {
            //加密数据
            String encryptData = TEAUtils.encryptString(src.getData(), _getDPK(src.getDid()));
            //时间戳
            long timestamp = System.currentTimeMillis();
            JsonObject storeItemJsonObject = new JsonObject();
            storeItemJsonObject.addProperty("dataId", src.getDataId());
            storeItemJsonObject.addProperty("dataType", src.getDataType());
            storeItemJsonObject.addProperty("data", encryptData);
            storeItemJsonObject.addProperty("did", src.getDid());
            storeItemJsonObject.addProperty("timestamp", timestamp);
            storeItemJsonObject.addProperty("version", src.getVersion());

            //data sign
            Map<String, String> dataSignMap = new HashMap<>();
            dataSignMap.put("data", encryptData);
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
