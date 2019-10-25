package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NodeServiceEncryptedItem {
    private String encryptedServiceDid;
    private String encryptedData;

    public NodeServiceEncryptedItem(String encryptedServiceDid, String encryptedData) {
        this.encryptedServiceDid = encryptedServiceDid;
        this.encryptedData = encryptedData;
    }

    public String getEncryptedServiceDid() {
        return encryptedServiceDid;
    }

    public String getEncryptedData() {
        return encryptedData;
    }
}
