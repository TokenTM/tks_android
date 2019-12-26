package com.tokentm.sdk.components.identitypwd.model;

import com.tokentm.sdk.model.ChainResult;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 绑定DID
 */
public class BindUDID extends ChainResult implements Serializable {

    private String phone;

    public BindUDID(String did, String chainAddress, String txHash, String chainPublicKey, String dataPublicKey, String phone) {
        super(did, chainAddress, txHash, chainPublicKey, dataPublicKey);
        this.phone = phone;
    }

    public BindUDID(ChainResult chainResult, String phone) {
        super(chainResult.getDid(), chainResult.getChainAddress(), chainResult.getTxHash(), chainResult.getChainPublicKey(), chainResult.getDataPublicKey());
        this.phone = phone;
    }


    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "BindUDID{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
