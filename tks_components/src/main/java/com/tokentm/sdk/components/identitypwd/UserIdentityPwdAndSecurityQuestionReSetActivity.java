package com.tokentm.sdk.components.identitypwd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.xxf.arch.utils.ToastUtils;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 重置身份密码与安全问题
 */
public class UserIdentityPwdAndSecurityQuestionReSetActivity
        extends UserIdentityPwdAndSecurityQuestionSetActivity {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserIdentityPwdAndSecurityQuestionReSetActivity.class);
    }

    String userIdentitySecretKeySafeBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitleBarTitle("重置身份密码");


        //重置身份密码,本地密钥一定不为空
        //TODO youxuan
//        userIdentitySecretKeySafeBlock = UserProviderUtils.getUserIdentityPwdProvider()
//                .getUserIdentitySecretKeySafeBlock();
        if (TextUtils.isEmpty(userIdentitySecretKeySafeBlock)) {
            ToastUtils.showToast("身份密钥为null,不应该进入此页面!");
            finish();
        }
    }

    @Override
    protected final String onCreateIdentitySecretKey() {
        //重置身份密码,本地密钥是本地的
        return userIdentitySecretKeySafeBlock;
    }
}
