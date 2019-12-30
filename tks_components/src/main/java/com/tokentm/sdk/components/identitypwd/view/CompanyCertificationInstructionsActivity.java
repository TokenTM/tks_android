package com.tokentm.sdk.components.identitypwd.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyActivityCertificationInstructionsBinding;
import com.tokentm.sdk.components.identitypwd.viewmodel.CompanyCertificationInstructionsVm;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明
 */
public class CompanyCertificationInstructionsActivity extends BaseTitleBarActivity {

    private static final String TX_HASH = "tx_hash";
    private static final String DID = "did";

    TksComponentsCompanyActivityCertificationInstructionsBinding binding;


    public static Intent getLauncher(FragmentActivity activity, String txHash, String did) {
        Intent intent = new Intent(activity, CompanyCertificationInstructionsActivity.class);
        intent.putExtra(TX_HASH, txHash);
        intent.putExtra(DID, did);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyActivityCertificationInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    protected void initData() {
        CompanyCertificationInstructionsVm viewModel = ViewModelProviders.of(this).get(CompanyCertificationInstructionsVm.class);
        binding.setViewModel(viewModel);
        String txHash = getIntent().getStringExtra(TX_HASH);
        String did = getIntent().getStringExtra(DID);
        viewModel.loadData(this, txHash,did);
    }

    protected void initView() {
        setTitle("认证说明");
    }


}
