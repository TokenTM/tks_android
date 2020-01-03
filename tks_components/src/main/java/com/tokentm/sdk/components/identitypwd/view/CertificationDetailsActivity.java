package com.tokentm.sdk.components.identitypwd.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.identitypwd.viewmodel.CompanyCertificationDetailsVm;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情
 */
public class CertificationDetailsActivity extends CertificationInstructionsActivity {

    private static final String DID = "did";

    public static void launch(@NonNull Context context, @Nullable String did) {
        context.startActivity(getLauncher(context, did));
    }

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, CertificationDetailsActivity.class)
                .putExtra(DID, did);
    }

    @Override
    protected void initView() {
        setTitle("认证详情");
    }

    @Override
    protected void initData() {
        CompanyCertificationDetailsVm viewModel = ViewModelProviders.of(this).get(CompanyCertificationDetailsVm.class);
        binding.setViewModel(viewModel);
        String did = getIntent().getStringExtra(DID);
        loadData(did);
    }
}
