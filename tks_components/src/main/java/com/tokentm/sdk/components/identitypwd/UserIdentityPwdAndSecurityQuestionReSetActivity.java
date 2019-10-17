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
 * @Description 重置身份密码与安全问题
 */
public class UserIdentityPwdAndSecurityQuestionReSetActivity
        extends UserIdentityPwdAndSecurityQuestionSetBaseActivity {

    private static final String KEY_DID = "did";
    private static final String KEY_OLD_SECURITY_QUESTION_ANSWERS = "oldSecurityQuestionAnswers";

    public static void launch(Context context, String did, LinkedHashMap<Long, String> oldSecurityQuestionAnswers) {
        context.startActivity(getLauncher(context, did, oldSecurityQuestionAnswers));
    }

    public static Intent getLauncher(Context context, String did, LinkedHashMap<Long, String> oldSecurityQuestionAnswers) {
        return new Intent(context, UserIdentityPwdAndSecurityQuestionReSetActivity.class)
                .putExtra(KEY_DID, did)
                .putExtra(KEY_OLD_SECURITY_QUESTION_ANSWERS, oldSecurityQuestionAnswers);
    }

    LinkedHashMap<Long, String> oldSecurityQuestionAnswers;
    String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitleBarTitle("重置身份密码");
        did = getIntent().getStringExtra(KEY_DID);
        oldSecurityQuestionAnswers = (LinkedHashMap<Long, String>) getIntent().getSerializableExtra(KEY_OLD_SECURITY_QUESTION_ANSWERS);
    }

    @Override
    protected void submitForm(@NonNull String identityPwd, @NonNull LinkedHashMap<Long, String> securityQuestionAnswers) {
        TokenTmClient.getService(DidService.class)
                .reset(did, oldSecurityQuestionAnswers, securityQuestionAnswers, identityPwd)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean reseted) throws Exception {
                        if (reseted) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    }
                });
    }

}
