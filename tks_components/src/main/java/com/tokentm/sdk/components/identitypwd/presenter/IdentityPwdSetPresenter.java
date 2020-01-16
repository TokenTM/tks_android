package com.tokentm.sdk.components.identitypwd.presenter;

import android.annotation.SuppressLint;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;

import com.tokentm.sdk.components.identitypwd.model.IdentityLayout;

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
     * 忘记身份密码
     *
     * @param phone
     * @param uDID
     */
    void onForgetIdentity(ObservableField<String> phone,ObservableField<String> uDID);


    /**
     * 设置身份密码设置
     *
     * @param phone
     * @param smsCode
     * @param identityPwd
     */
    @SuppressLint("CheckResult")
    void onIdentitySet(ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd, ObservableField<String> invitationCode);

    /**
     * 解开身份密码
     *
     * @param uDID
     * @param phone
     * @param smsCode
     * @param identityPwd
     */
    void onIdentityDecrypt(ObservableField<String> uDID, ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd);

    /**
     * 选择身份密码操作类型
     */
    void onIdentityChoice(ObservableField<IdentityLayout> identityLayout, ObservableField<String> uDID, ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd, ObservableField<String> invitationCode);
}
