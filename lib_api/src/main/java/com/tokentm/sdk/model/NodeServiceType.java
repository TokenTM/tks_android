package com.tokentm.sdk.model;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public enum NodeServiceType implements Serializable {
    /**
     * 用户实名认证
     */
    TYPE_USER_AUTHENTICATE("USER_AUTHENTICATE"),

    /**
     * 公司认证
     */
    TYPE_COMPANY_AUTHENTICATE("COMPANY_AUTHENTICATE"),
    /**
     * 解密
     */
    TYPE_DECRYPT("DECRYPT"),

    /**
     * 加密
     */
    TYPE_ENCRYPT("ENCRYPT");

    private String value;

    NodeServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
