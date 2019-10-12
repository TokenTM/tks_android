package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description user keystore, 【自动加解密】
 */
public final class BackupChunkDTO {
    private String data;

    private String dataId;

    private String type;

    private String userId;

    private long version;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getdataId() {
        return dataId;
    }

    public void setdataId(String dataId) {
        this.dataId = dataId;
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

    public BackupChunkDTO(String data, String dataId, String type, String userId, long version) {
        this.data = data;
        this.dataId = dataId;
        this.type = type;
        this.userId = userId;
        this.version = version;
    }
}
