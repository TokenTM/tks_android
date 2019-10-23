package com.tokentm.sdk.components.cert.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 认证方式
 */
public enum CompanyType implements Serializable {
    //1代表公司，5代表组织
    /**
     * 1代表公司，5代表组织
     */
    @SerializedName("1")
    TYPE_COMPANY(1),
    /**
     * 1代表公司，5代表组织
     */
    @SerializedName("5")
    TYPE_ORGANIZATION(5);

    int value;

    public int getValue() {
        return value;
    }

    CompanyType(int value) {
        this.value = value;
    }
}
