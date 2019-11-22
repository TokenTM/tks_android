package com.tokentm.sdk.components.identitypwd.presenter;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface IdentityPwdDecryptPresenter {

    /**
     * 请求验证码
     *
     * @param phone        手机号
     * @param smsCountdown 倒计时
     */
    void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown);


    /**
     * 解开身份密码
     *
     * @param step
     * @param phone
     * @param smsCode
     * @param decryptNodeName
     */
    void onIdentityPwdDecrypt(ObservableInt step, ObservableField<String> phone, ObservableField<String> smsCode,ObservableField<String> decryptNodeName);
}
