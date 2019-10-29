package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NodeServiceEncryptDecryptItem {
    private int no;
    private String serviceDid;
    private String value;

    public NodeServiceEncryptDecryptItem(int no, String serviceDid, String value) {
        this.no = no;
        this.serviceDid = serviceDid;
        this.value = value;
    }

    public int getNo() {
        return no;
    }

    public String getServiceDid() {
        return serviceDid;
    }

    public String getValue() {
        return value;
    }
}
