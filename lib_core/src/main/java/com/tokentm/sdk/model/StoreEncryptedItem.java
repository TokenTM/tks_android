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
import com.tokentm.sdk.common.encrypt.EncryptionUtils;
import com.tokentm.sdk.wallet.SignUtils;
import com.xxf.arch.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密
 */
class EncryptJsonSerializer implements JsonSerializer<StoreEncryptedItem> {

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
    public JsonElement serialize(StoreEncryptedItem src, Type typeOfSrc, JsonSerializationContext context) {
        //加密数据
        String encryptData = EncryptionUtils.encodeString(src.getData(), _getDPK(src.getDid()));
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

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@JsonAdapter(EncryptJsonSerializer.class)
public class StoreEncryptedItem extends StoreItem<String> {

}