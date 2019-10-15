package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 存储块item
 */
public class StoreItem {
    private String dataId;
    private String dataType;
    private String data;
    private String did;
    private long version;

    public String getDataId() {
        return dataId;
    }

    public String getDataType() {
        return dataType;
    }

    public String getData() {
        return data;
    }

    public String getDid() {
        return did;
    }

    public long getVersion() {
        return version;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
