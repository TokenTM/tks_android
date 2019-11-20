package com.tokentm.sdk.components.identitypwd.presenter;

import android.databinding.ObservableField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface IdentityPwdUpdatePresenter {


    /**
     * 设置身份密码设置
     *
     * @param identityPwd
     */
    void onIdentityUpdate(ObservableField<String> identityPwd);

}
