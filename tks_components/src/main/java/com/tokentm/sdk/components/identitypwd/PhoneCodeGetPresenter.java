package com.tokentm.sdk.components.identitypwd;

import android.databinding.ObservableField;
import android.databinding.ObservableLong;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 校验验证码presenter
 */
public interface PhoneCodeGetPresenter {

    /**
     * 请求验证码
     *
     * @param phone        手机号
     * @param smsCountdown 倒计时
     */
    void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown);


    /**
     * 校验验证码
     *
     * @param phone
     * @param smsCode
     */
    void onCheckPhoneCode(ObservableField<String> phone, ObservableField<String> smsCode);
}
