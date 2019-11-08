package com.tokentm.sdk.components.identitypwd;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseFragment;
import com.tokentm.sdk.components.databinding.TksComponentsUserFragmentCertificationDetailsBinding;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情 被添加到认证说明activity和认证详情activity中作为共有显示
 */
public class UserCertificationDetailsFragment extends BaseFragment {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static UserCertificationDetailsFragment newInstance(CompanyCertParams companyCertParams) {
        UserCertificationDetailsFragment fragment = new UserCertificationDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CERT_PARAMS, companyCertParams);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserCertificationDetailsFragment newInstance() {
        return new UserCertificationDetailsFragment();
    }

    TksComponentsUserFragmentCertificationDetailsBinding binding;
    CompanyCertParams companyCertParams;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsUserFragmentCertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
//        companyCertParams = (CompanyCertParams) getArguments().getSerializable(KEY_CERT_PARAMS);
    }

}
