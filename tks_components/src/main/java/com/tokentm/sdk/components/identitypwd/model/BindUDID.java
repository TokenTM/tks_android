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
    private String phone;
    private String chainAddress;


    public BindUDID(String did,String phone, String chainAddress) {
        this.did = did;
        this.phone=phone;
        this.chainAddress = chainAddress;
    }

    public String getDid() {
        return did;
    }

    public String getPhone() {
        return phone;
    }

    public String getChainAddress() {
        return chainAddress;
    }

    @Override
    public String toString() {
        return "BindUDID{" +
                "did='" + did + '\'' +
                ", phone='" + phone + '\'' +
                ", chainAddress='" + chainAddress + '\'' +
                '}';
    }
}
