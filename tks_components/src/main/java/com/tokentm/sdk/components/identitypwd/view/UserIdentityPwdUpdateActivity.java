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
import com.tokentm.sdk.model.IdentityInfoStoreItem;
import com.tokentm.sdk.model.NodeServiceDecryptedPartItem;
import com.tokentm.sdk.source.IdentityService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 身份密码修改
 */
public class UserIdentityPwdUpdateActivity extends BaseTitleBarActivity implements IdentityPwdUpdatePresenter {

    private static final String KEY_DID_INFO = "did_info";
    private static final String KEY_DECRPT_PWD = "decrpt_pwd";

    public static void launch(@NonNull Context context, @NonNull IdentityInfoStoreItem identityInfoStoreItem, @Nullable ArrayList<NodeServiceDecryptedPartItem> decryptedParts) {
        context.startActivity(getLauncher(context, identityInfoStoreItem, decryptedParts));
    }

    public static Intent getLauncher(@NonNull Context context, @NonNull IdentityInfoStoreItem identityInfoStoreItem, @Nullable ArrayList<NodeServiceDecryptedPartItem> decryptedParts) {
        return new Intent(context, UserIdentityPwdUpdateActivity.class)
                .putExtra(KEY_DID_INFO, identityInfoStoreItem)
                .putExtra(KEY_DECRPT_PWD, decryptedParts);
    }

    TksComponentsUserActivityIdentityPwdUpdateBinding binding;
    private IdentityInfoStoreItem identityInfoStoreItem;
    ArrayList<NodeServiceDecryptedPartItem> decryptedParts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsUserActivityIdentityPwdUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("重置身份密码");
        identityInfoStoreItem = (IdentityInfoStoreItem) getIntent().getSerializableExtra(KEY_DID_INFO);
        decryptedParts = (ArrayList<NodeServiceDecryptedPartItem>) getIntent().getSerializableExtra(KEY_DECRPT_PWD);

        binding.setPresenter(this);
        binding.setViewModel(ViewModelProviders.of(this).get(IdentityPwdUpdateVM.class));
    }

    @Override
    public void onIdentityUpdate(ObservableField<String> identityPwd) {
        TokenTmClient.getService(IdentityService.class)
                .resetIdentityPwd(identityInfoStoreItem, decryptedParts, identityPwd.get())
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
