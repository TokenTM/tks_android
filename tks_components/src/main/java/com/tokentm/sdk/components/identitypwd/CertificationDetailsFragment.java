package com.tokentm.sdk.components.identitypwd;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseFragment;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyFragmentCertificationDetailsBinding;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情 被添加到认证说明activity和认证详情activity中作为共有显示
 */
public class CertificationDetailsFragment extends BaseFragment {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static CertificationDetailsFragment newInstance(CompanyCertParams companyCertParams) {
        CertificationDetailsFragment fragment = new CertificationDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CERT_PARAMS, companyCertParams);
        fragment.setArguments(args);
        return fragment;
    }

    public static CertificationDetailsFragment newInstance() {
        return new CertificationDetailsFragment();
    }

    TksComponentsCompanyFragmentCertificationDetailsBinding binding;
    CompanyCertParams companyCertParams;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyFragmentCertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
//        companyCertParams = (CompanyCertParams) getArguments().getSerializable(KEY_CERT_PARAMS);
    }

}
