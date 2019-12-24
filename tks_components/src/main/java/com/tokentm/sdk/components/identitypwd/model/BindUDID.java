package com.tokentm.sdk.components.identitypwd.model;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 绑定DID
 */
public class BindUDID implements Serializable {


    /**
     * chainAddress : string
     * did : string
     */
    private String did;
    private String chainAddress;


    public BindUDID(String did, String chainAddress) {
        this.did = did;
        this.chainAddress = chainAddress;
    }

    public String getChainAddress() {
        return chainAddress;
    }

    public void setChainAddress(String chainAddress) {
        this.chainAddress = chainAddress;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    @Override
    public String toString() {
        return "BindUDID{" +
                "chainAddress='" + chainAddress + '\'' +
                ", did='" + did + '\'' +
                '}';
    }
}
