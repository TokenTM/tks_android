package com.tokentm.sdk.components.cert.model;

import android.support.annotation.NonNull;

import com.tokentm.sdk.model.CompanyType;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 认证参数
 */
public class CompanyCertParams implements Serializable {
    @Override
    public String toString() {
        return "CompanyCertParams{" +
                "uDid='" + uDid + '\'' +
                ", companyType=" + companyType +
                ", companyName='" + companyName + '\'' +
                ", companyCreditCode='" + companyCreditCode + '\'' +
                ", tag=" + tag +
                '}';
    }

    public static class Builder {
        private String uDid;
        private CompanyType companyType;
        private String companyName;
        private String companyCreditCode;
        private Serializable tag;

        public Builder(@NonNull String uDid, @NonNull String companyName) {
            this.uDid = Objects.requireNonNull(uDid);
            this.companyName = Objects.requireNonNull(companyName);
        }

        public Builder(CompanyCertParams companyCertParams) {
            this.uDid = companyCertParams.uDid;
            this.companyName = companyCertParams.companyName;
            this.companyCreditCode = companyCertParams.companyCreditCode;
            this.companyType = companyCertParams.companyType;
            this.tag = companyCertParams.tag;
        }

        public Builder setCompanyCreditCode(String companyCreditCode) {
            this.companyCreditCode = companyCreditCode;
            return this;
        }

        public Builder setCompanyType(CompanyType companyType) {
            this.companyType = companyType;
            return this;
        }

        public Builder setTag(Serializable tag) {
            this.tag = tag;
            return this;
        }

        public CompanyCertParams build() {
            return new CompanyCertParams(uDid, companyType, companyName, companyCreditCode, tag);
        }
    }

    /**
     * user did
     */
    private String uDid;
    /**
     * 认证类型
     */
    private CompanyType companyType;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司统一社会信用代码
     */
    private String companyCreditCode;


    /**
     * 自定义的tag
     */
    private Serializable tag;


    public CompanyType getCompanyType() {
        return companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    private CompanyCertParams(String uDid, CompanyType companyType, String companyName, String companyCreditCode, Serializable tag) {
        this.uDid = uDid;
        this.companyType = companyType;
        this.companyName = companyName;
        this.companyCreditCode = companyCreditCode;
        this.tag = tag;
    }

    public String getCompanyCreditCode() {
        return companyCreditCode;
    }

    public Serializable getTag() {
        return tag;
    }

    public String getuDid() {
        return uDid;
    }
}
