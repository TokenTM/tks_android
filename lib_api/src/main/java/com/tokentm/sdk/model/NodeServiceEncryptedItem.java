package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NodeServiceEncryptedItem {
    private String service_did;
    private String value;

    public NodeServiceEncryptedItem(String service_did, String value) {
        this.service_did = service_did;
        this.value = value;
    }

    public String getService_did() {
        return service_did;
    }

    public String getValue() {
        return value;
    }
}
