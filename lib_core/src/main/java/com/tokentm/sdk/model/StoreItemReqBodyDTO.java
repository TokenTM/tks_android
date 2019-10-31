package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;
import com.tokentm.sdk.common.encrypt.SignObject;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class StoreItemReqBodyDTO implements SignObject {

    /**
     * data : string
     * dataId : string
     * dataSign : string
     * dataType : string
     * did : string
     * sign : string
     * timestamp : 0
     * version : 0
     */

    @SignField(chainPKSign = false, dataPKSign = true)
    private String data;

    @SignField(chainPKSign = false, dataPKSign = true)
    private String dataId;

    @SignField(chainPKSign = false, dataPKSign = true)
    private String dataType;

    @SignField(chainPKSign = false, dataPKSign = true)
    private String did;

    @SignField(chainPKSign = false, dataPKSign = true)
    private long timestamp;

    @SignField(chainPKSign = false, dataPKSign = true)
    private long version;

    @SignField(chainPKSign = false, dataPKSign = true)
    private String dataSign;

    private String sign;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDataSign() {
        return dataSign;
    }

    public void setDataSign(String dataSign) {
        this.dataSign = dataSign;
    }
}
