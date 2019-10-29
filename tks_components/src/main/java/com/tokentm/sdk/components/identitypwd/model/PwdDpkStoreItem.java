package com.tokentm.sdk.components.identitypwd.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class PwdDpkStoreItem {

    private String pwd;
    private String dpk;

    public PwdDpkStoreItem(String pwd, String dpk) {
        this.pwd = pwd;
        this.dpk = dpk;
    }

    public String getDpk() {
        return dpk;
    }

    public String getPwd() {
        return pwd;
    }
}
