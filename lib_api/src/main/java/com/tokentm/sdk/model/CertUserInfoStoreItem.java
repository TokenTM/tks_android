package com.tokentm.sdk.model;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 用户认证信息
 */
public class CertUserInfoStoreItem {


    /**
     * name : 张三
     * identityCode : xxxx
     * identityFontImgId : xxx
     * identityBackImgId : xxx
     * identityHandImgId : xxx
     * txHash : 0xrhgdggg
     */

    private String name;
    private String identityCode;
    private String identityFontImgId;
    private String identityBackImgId;
    private String identityHandImgId;
    private String txHash;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getIdentityFontImgId() {
        return identityFontImgId;
    }

    public void setIdentityFontImgId(String identityFontImgId) {
        this.identityFontImgId = identityFontImgId;
    }

    public String getIdentityBackImgId() {
        return identityBackImgId;
    }

    public void setIdentityBackImgId(String identityBackImgId) {
        this.identityBackImgId = identityBackImgId;
    }

    public String getIdentityHandImgId() {
        return identityHandImgId;
    }

    public void setIdentityHandImgId(String identityHandImgId) {
        this.identityHandImgId = identityHandImgId;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
