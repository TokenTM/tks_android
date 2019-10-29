package com.tokentm.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public enum NodeServiceType implements Serializable {

    /**
     * 解密
     */
    @SerializedName("DECRYPT")
    TYPE_DECRYPT("DECRYPT"),

    /**
     * 加密
     */
    @SerializedName("ENCRYPT")
    TYPE_ENCRYPT("ENCRYPT");

    private String value;

    NodeServiceType(String value) {
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }
}