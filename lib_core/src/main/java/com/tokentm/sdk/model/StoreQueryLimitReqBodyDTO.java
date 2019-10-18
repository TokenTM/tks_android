package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class StoreQueryLimitReqBodyDTO {
    private String dataId;
    private String dataType;
    private String did;
    private String sign;
    private String tempDataPubKey;
    private long timestamp;

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

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTempDataPubKey() {
        return tempDataPubKey;
    }

    public void setTempDataPubKey(String tempDataPubKey) {
        this.tempDataPubKey = tempDataPubKey;
    }
}
