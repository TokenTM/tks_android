package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;
import com.tokentm.sdk.common.encrypt.SignObject;
import com.tokentm.sdk.common.encrypt.SignTargetField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class CompanyCertReqBodyDTO implements SignObject {

    @SignField(chainPKSign = true, dataPKSign = true)
    private String address;

    @SignTargetField(SignTargetField.SignType.CPK_SIGN)
    private String chainPrvKeySign;

    @SignField(chainPKSign = true, dataPKSign = true)
    private String companyName;

    @SignField(chainPKSign = true, dataPKSign = true)
    private int companyType;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String creditCode;

    @SignField(chainPKSign = true, dataPKSign = true)
    private String did;


    private SignedLegalPerson legalPerson;


    @SignField(chainPKSign = true, dataPKSign = true)
    private String licenseImgId;

    @SignField(chainPKSign = true, dataPKSign = true)
    private String targetDid;

    @SignField(chainPKSign = true, dataPKSign = true)
    private long timestamp;

    @SignTargetField(SignTargetField.SignType.DPK_SIGN)
    private String sign;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChainPrvKeySign() {
        return chainPrvKeySign;
    }

    public void setChainPrvKeySign(String chainPrvKeySign) {
        this.chainPrvKeySign = chainPrvKeySign;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public SignedLegalPerson getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(SignedLegalPerson legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLicenseImgId() {
        return licenseImgId;
    }

    public void setLicenseImgId(String licenseImgId) {
        this.licenseImgId = licenseImgId;
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

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public static class SignedLegalPerson implements SignObject {
        @SignField(chainPKSign = true, dataPKSign = true)
        private String did;
        @SignField(chainPKSign = true, dataPKSign = true)
        private String identityCode;
        @SignField(chainPKSign = true, dataPKSign = true)
        private String name;
        @SignField(chainPKSign = true, dataPKSign = true)
        private long timestamp;

        @SignTargetField(SignTargetField.SignType.DPK_SIGN)
        private String sign;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getIdentityCode() {
            return identityCode;
        }

        public void setIdentityCode(String identityCode) {
            this.identityCode = identityCode;
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
}
