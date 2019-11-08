package com.tokentm.sdk.components.identitypwd;

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

    TksComponentsCompanyActivityCertificationDetailsBinding binding;

    public static Intent getLauncher(FragmentActivity activity) {
        return new Intent(activity, CompanyCertificationDetailsActivity.class);
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
        FragmentUtils.addFragment(getSupportFragmentManager(), UserCertificationDetailsFragment.newInstance(),binding.flContent.getId());
    }
}
