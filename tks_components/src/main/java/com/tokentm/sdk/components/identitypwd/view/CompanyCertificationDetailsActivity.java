package com.tokentm.sdk.components.identitypwd.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokentm.sdk.components.identitypwd.viewmodel.CompanyCertificationDetailsVm;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情
 */
public class CompanyCertificationDetailsActivity extends CompanyCertificationInstructionsActivity {

    private static final String TX_HASH = "tx_hash";


    public static Intent getLauncher(FragmentActivity activity, String txHash) {
        return new Intent(activity, CompanyCertificationDetailsActivity.class)
                .putExtra(TX_HASH, txHash);
    }

    @Override
    protected void initView() {
        setTitle("认证详情");
    }

    @Override
    protected void initData() {
        CompanyCertificationDetailsVm viewModel = ViewModelProviders.of(this).get(CompanyCertificationDetailsVm.class);
        binding.setViewModel(viewModel);
        String txHash = getIntent().getStringExtra(TX_HASH);
        viewModel.loadData(this, txHash);
    }
}
