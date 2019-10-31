package com.tokentm.sdk.model;

import com.tokentm.sdk.common.encrypt.SignField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DIDForkReqDTO extends DIDReqDTO {

    @SignField(chainPKSign = true, dataPKSign = true)
    private String forkDid;

    public String getForkDid() {
        return forkDid;
    }

    public void setForkDid(String forkDid) {
        this.forkDid = forkDid;
    }
}
