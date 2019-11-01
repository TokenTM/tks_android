package com.tokentm.sdk.model;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 认证状态结果
 */
public class CompanyCertResult implements Serializable {

    private String cDid;
    private String txHash;

    public String getcDid() {
        return cDid;
    }

    public void setcDid(String cDid) {
        this.cDid = cDid;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    @Override
    public String toString() {
        return "CompanyCertResult{" +
                "cDid='" + cDid + '\'' +
                ", txHash='" + txHash + '\'' +
                '}';
    }
}
