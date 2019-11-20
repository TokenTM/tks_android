package com.tokentm.sdk.components.identitypwd.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsUserActivityIdentityPwdUpdateBinding;
import com.tokentm.sdk.components.identitypwd.presenter.IdentityPwdUpdatePresenter;
import com.tokentm.sdk.components.identitypwd.viewmodel.IdentityPwdUpdateVM;
import com.tokentm.sdk.source.IdentityService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 身份密码修改
 */
public class UserIdentityPwdUpdateActivity extends BaseTitleBarActivity implements IdentityPwdUpdatePresenter {

    private static final String KEY_DID = "did";
    private static final String KEY_OLD_IDENTITY_PWD = "old_identity_pwd";


    public static void launch(@NonNull Context context, @NonNull String did, @Nullable String oldIdentityPwd) {
        context.startActivity(getLauncher(context, did, oldIdentityPwd));
    }

    public static Intent getLauncher(@NonNull Context context, @NonNull String did, @Nullable String oldIdentityPwd) {
        return new Intent(context, UserIdentityPwdUpdateActivity.class)
                .putExtra(KEY_DID, did)
                .putExtra(KEY_OLD_IDENTITY_PWD, oldIdentityPwd);
    }

    TksComponentsUserActivityIdentityPwdUpdateBinding binding;
    private String did;
    private String oldIdentityPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsUserActivityIdentityPwdUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("重置身份密码");
        did = getIntent().getStringExtra(KEY_DID);
        oldIdentityPwd = getIntent().getStringExtra(KEY_OLD_IDENTITY_PWD);

        binding.setPresenter(this);
        binding.setViewModel(ViewModelProviders.of(this).get(IdentityPwdUpdateVM.class));
    }

    @Override
    public void onIdentityUpdate(ObservableField<String> identityPwd) {
        TokenTmClient.getService(IdentityService.class)
                .resetIdentityPwd(did, oldIdentityPwd, identityPwd.get())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        setResult(Activity.RESULT_OK, getIntent());
                        finish();
                    }
                });

    }
}
