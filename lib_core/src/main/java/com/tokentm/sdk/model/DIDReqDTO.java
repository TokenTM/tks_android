package com.tokentm.sdk.model;

public class DIDReqDTO {

    /**
     * chainAddress : string
     * chainPrvSign : string
     * chainPubKey : string
     * data : string
     * dataPubKey : string
     * sign : string
     * timestamp : 0
     */

    private String chainAddress;
    private String chainPrvSign;
    private String chainPubKey;
    private String data;
    private String dataPubKey;
    private String sign;
    private long timestamp;

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
}
