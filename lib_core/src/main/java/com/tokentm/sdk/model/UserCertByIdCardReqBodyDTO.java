package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;
import com.tokentm.sdk.common.encrypt.SignObject;
import com.tokentm.sdk.common.encrypt.SignTargetField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class UserCertByIdCardReqBodyDTO implements SignObject {

    @SignField(chainPKSign = true, dataPKSign = true)
    private String address;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String did;


    @SignField(chainPKSign = true, dataPKSign = true)
    private boolean force;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String identityBackImgId;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String identityCode;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String identityFontImgId;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String identityHandImgId;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String name;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String targetDid;


    @SignField(chainPKSign = true, dataPKSign = true)
    private long timestamp;

    @SignTargetField(SignTargetField.SignType.DPK_SIGN)
    private String sign;

    @SignTargetField(SignTargetField.SignType.CPK_SIGN)
    private String chainPrvKeySign;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getIdentityBackImgId() {
        return identityBackImgId;
    }

    public void setIdentityBackImgId(String identityBackImgId) {
        this.identityBackImgId = identityBackImgId;
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

    public String getIdentityHandImgId() {
        return identityHandImgId;
    }

    public void setIdentityHandImgId(String identityHandImgId) {
        this.identityHandImgId = identityHandImgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetDid() {
        return targetDid;
    }

    public void setTargetDid(String targetDid) {
        this.targetDid = targetDid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getChainPrvKeySign() {
        return chainPrvKeySign;
    }

    public void setChainPrvKeySign(String chainPrvKeySign) {
        this.chainPrvKeySign = chainPrvKeySign;
    }
}
