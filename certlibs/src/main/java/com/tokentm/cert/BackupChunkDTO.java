package com.tokentm.cert;

import com.google.gson.annotations.SerializedName;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description user keystore, 【自动加解密】
 */
public final class BackupChunkDTO {
    @SerializedName("data")
    private String data;

    @SerializedName("itemId")
    private String itemId;

    @SerializedName("type")
    private String type;

    @SerializedName("userId")
    private String userId;

    @SerializedName("version")
    private long version;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public BackupChunkDTO(String data, String itemId, String type, String userId, long version) {
        this.data = data;
        this.itemId = itemId;
        this.type = type;
        this.userId = userId;
        this.version = version;
    }
}
