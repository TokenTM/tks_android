package com.tokentm.sdk.uidemo.dialog;

import java.io.Serializable;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 输入公司名称回调
 */
public class InputIdentityCompanyParams implements Serializable {

    private String identityTxHash;
    private String companyTxHash;
    private String identityDid;
    private String companyDid;

    public InputIdentityCompanyParams(String identityTxHash, String companyTxHash, String identityDid, String companyDid) {
        this.identityTxHash = identityTxHash;
        this.companyTxHash = companyTxHash;
        this.identityDid = identityDid;
        this.companyDid = companyDid;
    }

    public String getIdentityTxHash() {
        return identityTxHash;
    }

    public String getCompanyTxHash() {
        return companyTxHash;
    }

    public String getIdentityDid() {
        return identityDid;
    }

    public String getCompanyDid() {
        return companyDid;
    }
}