package com.tokentm.sdk.components.identitypwd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.source.DidService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import io.reactivex.functions.Consumer;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 重置用户身份密码设置
 */
public class UserIdentityPwdReSetActivity extends UserIdentityPwdSetActivity {

    private static final String KEY_DID = "did";
    private static final String KEY_PHONE = "phone";

    public static void launch(@NonNull Context context, @NonNull String did, @Nullable String phone) {
        context.startActivity(getLauncher(context, did, phone));
    }

    public static Intent getLauncher(@NonNull Context context, @NonNull String did, @Nullable String phone) {
        return new Intent(context, UserIdentityPwdReSetActivity.class)
                .putExtra(KEY_DID, did)
                .putExtra(KEY_PHONE, phone);
    }

    String did;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("重置身份密码");
        did = getIntent().getStringExtra(KEY_DID);
    }

    @Override
    public void onIdentityPwdSet(ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd) {
        TokenTmClient.getService(DidService.class)
                .resetIdentityPwd(did, phone.get(), smsCode.get(), identityPwd.get())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean resetPwded) throws Exception {
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, resetPwded.booleanValue()));
                        finish();
                    }
                });
    }
}
