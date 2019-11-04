package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;
import com.tokentm.sdk.common.encrypt.SignObject;
import com.tokentm.sdk.common.encrypt.SignTargetField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class UserCertByIdCardReqBodyDTO implements SignObject {

    @SignField(dataPKSign = true)
    private String did;

    @SignField(dataPKSign = true)
    private String identityBackImgId;

    @SignField(dataPKSign = true)
    private String identityCode;

    @SignField(dataPKSign = true)
    private String identityFontImgId;

    @SignField(dataPKSign = true)
    private String identityHandImgId;

    @SignField(dataPKSign = true)
    private String name;

    @SignField(dataPKSign = true)
    private long timestamp;

    @SignTargetField(SignTargetField.SignType.DPK_SIGN)
    private String sign;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
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
}
