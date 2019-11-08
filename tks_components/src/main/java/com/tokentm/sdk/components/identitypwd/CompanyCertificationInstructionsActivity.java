package com.tokentm.sdk.components.identitypwd;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyActivityCertificationInstructionsBinding;
import com.xxf.arch.utils.FragmentUtils;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明
 */
public class CompanyCertificationInstructionsActivity extends BaseTitleBarActivity implements CompanyCertificationInstructionsPresenter {

    TksComponentsCompanyActivityCertificationInstructionsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyActivityCertificationInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        setTitle("认证说明");
        FragmentUtils.addFragment(getSupportFragmentManager(), UserCertificationDetailsFragment.newInstance(),binding.flContent.getId());
    }
}
