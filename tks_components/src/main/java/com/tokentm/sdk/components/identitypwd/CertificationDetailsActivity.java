package com.tokentm.sdk.components.identitypwd;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityCertificationDetailsBinding;
import com.xxf.arch.utils.FragmentUtils;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情
 */
public class CertificationDetailsActivity extends BaseTitleBarActivity {

    TksComponentsActivityCertificationDetailsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityCertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        setTitle("认证详情");
        FragmentUtils.addFragment(getSupportFragmentManager(),CertificationDetailsFragment.newInstance(),binding.flContent.getId());
    }
}
