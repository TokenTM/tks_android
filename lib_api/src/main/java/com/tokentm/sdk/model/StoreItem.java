package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 存储块item
 */
public class StoreItem<T> {
    private String dataId;
    private String dataType;
    private T data;
    private String did;
    private long version;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
