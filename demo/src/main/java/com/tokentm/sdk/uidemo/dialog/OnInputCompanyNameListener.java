package com.tokentm.sdk.uidemo.dialog;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 输入公司名称回调
 */
public interface OnInputCompanyNameListener extends Serializable {

    /**
     * 回调输入的公司名称
     */
    void onInputCompanyName(@NonNull String companyName);
}