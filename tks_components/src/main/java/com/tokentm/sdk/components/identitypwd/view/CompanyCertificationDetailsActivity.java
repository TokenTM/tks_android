package com.tokentm.sdk.components.identitypwd.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyActivityCertificationDetailsBinding;
import com.xxf.arch.utils.FragmentUtils;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情
 */
public class CompanyCertificationDetailsActivity extends BaseTitleBarActivity {

    private static final String TX_HASH = "tx_hash";

    TksComponentsCompanyActivityCertificationDetailsBinding binding;

    public static Intent getLauncher(FragmentActivity activity, String txHash) {
        return new Intent(activity, CompanyCertificationDetailsActivity.class)
                .putExtra(TX_HASH, txHash);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyActivityCertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        setTitle("认证详情");
        String txHash = getIntent().getStringExtra(TX_HASH);
        FragmentUtils.addFragment(getSupportFragmentManager(), UserCertificationDetailsFragment.newInstance(txHash), binding.flContent.getId());
    }
}
