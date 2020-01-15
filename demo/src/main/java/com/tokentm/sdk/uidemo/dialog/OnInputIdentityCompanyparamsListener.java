package com.tokentm.sdk.uidemo.dialog;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 输入公司名称回调
 */
public interface OnInputIdentityCompanyparamsListener extends Serializable {

    /**
     * 回调输入的公司名称
     */
    void getIdentityCompanyParams(@NonNull String identityTxHash, @NonNull String companyTxHash, @NonNull String identityDid, @NonNull String companyDid);
}