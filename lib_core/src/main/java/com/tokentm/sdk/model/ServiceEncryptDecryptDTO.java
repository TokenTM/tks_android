package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ServiceEncryptDecryptDTO {
    private String did;
    private String phone;
    private String code;
    private String data;

    public ServiceEncryptDecryptDTO(String did, String phone, String code, String data) {
        this.did = did;
        this.phone = phone;
        this.code = code;
        this.data = data;
    }
}
