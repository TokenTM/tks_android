package com.tokentm.sdk.components.identitypwd;

import android.databinding.ObservableField;
import android.databinding.ObservableLong;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface IdentityPwdSetPresenter {

    /**
     * 请求验证码
     *
     * @param phone        手机号
     * @param smsCountdown 倒计时
     */
    void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown);


    /**
     * 设置身份密码
     *
     * @param phone
     * @param smsCode
     * @param identityPwd
     */
    void onIdentityPwdSet(ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd);
}
