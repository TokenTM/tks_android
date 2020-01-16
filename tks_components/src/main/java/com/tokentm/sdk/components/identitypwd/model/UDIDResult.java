package com.tokentm.sdk.components.identitypwd.model;

import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.ChainSignedResult;
import com.tokentm.sdk.model.DIDSignature;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 绑定DID
 */
public class UDIDResult implements Serializable {

    private String phone;
    private ChainSignedResult<DIDSignature> chainSignedResult;


    public UDIDResult(String phone, ChainSignedResult<DIDSignature> chainSignedResult) {
        this.phone = phone;
        this.chainSignedResult = chainSignedResult;
    }

    public String getPhone() {
        return phone;
    }

    public ChainSignedResult<DIDSignature> getChainSignedResult() {
        return chainSignedResult;
    }
}
