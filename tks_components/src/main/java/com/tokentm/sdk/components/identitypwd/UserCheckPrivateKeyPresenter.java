package com.tokentm.sdk.components.identitypwd;

import android.databinding.ObservableField;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 校验私钥
 */
public interface UserCheckPrivateKeyPresenter {

    /**
     * 校验输入的私钥
     * @param privateKey
     */
    void onCheckPrivateKey(ObservableField<String> privateKey);

    /**
     * 取消弹窗
     * @param privateKey
     */
    void onCancelCheckPrivateKey(ObservableField<String> privateKey);
}
