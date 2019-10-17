package com.tokentm.sdk.components.identitypwd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.source.DidService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import java.util.LinkedHashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 设置身份密码和安全问题  返回did
 */
public class UserIdentityPwdAndSecurityQuestionSetActivity extends UserIdentityPwdAndSecurityQuestionSetBaseActivity {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserIdentityPwdAndSecurityQuestionSetActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitleBarTitle("设置身份密码");
    }

    @Override
    protected void submitForm(@NonNull String identityPwd, @NonNull LinkedHashMap<Long, String> securityQuestionAnswers) {
        TokenTmClient.getService(DidService.class)
                .createDID(identityPwd, securityQuestionAnswers)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<String>bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .compose(XXF.<String>bindToLifecycle(this))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String did) throws Exception {
                        //返回did
                        getIntent().putExtra(KEY_ACTIVITY_RESULT, did);
                        setResult(Activity.RESULT_OK, getIntent());
                        finish();
                    }
                });
    }
}
