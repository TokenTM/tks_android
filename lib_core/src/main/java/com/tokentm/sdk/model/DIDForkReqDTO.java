package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;
import com.tokentm.sdk.common.encrypt.SignObject;
import com.tokentm.sdk.common.encrypt.SignTargetField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DIDForkReqDTO implements SignObject {

    @SignField(chainPKSign = true, dataPKSign = true)
    private String forkDid;

    @SignField(chainPKSign = true, dataPKSign = true)
    private String chainAddress;

    @SignTargetField(SignTargetField.SignType.CPK_SIGN)
    private String chainPrvSign;

    @SignTargetField(SignTargetField.SignType.DPK_SIGN)
    private String dataPrvSign;

    private String chainPubKey;

    @SignField(chainPKSign = true, dataPKSign = true)
    private String data;

    private String dataPubKey;

    @SignTargetField(SignTargetField.SignType.DPK_SIGN)
    private String sign;

    @SignField(chainPKSign = true, dataPKSign = true)
    private long timestamp;

    public String getForkDid() {
        return forkDid;
    }

    public void setForkDid(String forkDid) {
        this.forkDid = forkDid;
    }

    public String getChainAddress() {
        return chainAddress;
    }

    public void setChainAddress(String chainAddress) {
        this.chainAddress = chainAddress;
    }

    public String getChainPrvSign() {
        return chainPrvSign;
    }

    public void setChainPrvSign(String chainPrvSign) {
        this.chainPrvSign = chainPrvSign;
    }

    public String getChainPubKey() {
        return chainPubKey;
    }

    public void setChainPubKey(String chainPubKey) {
        this.chainPubKey = chainPubKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataPubKey() {
        return dataPubKey;
    }

    public void setDataPubKey(String dataPubKey) {
        this.dataPubKey = dataPubKey;
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

    public String getDataPrvSign() {
        return dataPrvSign;
    }

    public void setDataPrvSign(String dataPrvSign) {
        this.dataPrvSign = dataPrvSign;
    }
}
