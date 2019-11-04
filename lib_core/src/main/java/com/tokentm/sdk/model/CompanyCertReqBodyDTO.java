package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;
import com.tokentm.sdk.common.encrypt.SignObject;
import com.tokentm.sdk.common.encrypt.SignTargetField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class CompanyCertReqBodyDTO implements SignObject {


    @SignField(dataPKSign = true)
    private String companyName;

    @SignField(dataPKSign = true)
    private int companyType;

    @SignField(dataPKSign = true)
    private String creditCode;

    @SignField(dataPKSign = true)
    private String did;

    @SignField(dataPKSign = true)
    private String legalPersonIdentityCode;

    @SignField(dataPKSign = true)
    private String legalPersonName;

    @SignField(dataPKSign = true)
    private String licenseImgId;

    @SignField(dataPKSign = true)
    private long timestamp;

    @SignTargetField(SignTargetField.SignType.DPK_SIGN)
    private String sign;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getLegalPersonIdentityCode() {
        return legalPersonIdentityCode;
    }

    public void setLegalPersonIdentityCode(String legalPersonIdentityCode) {
        this.legalPersonIdentityCode = legalPersonIdentityCode;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLicenseImgId() {
        return licenseImgId;
    }

    public void setLicenseImgId(String licenseImgId) {
        this.licenseImgId = licenseImgId;
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
