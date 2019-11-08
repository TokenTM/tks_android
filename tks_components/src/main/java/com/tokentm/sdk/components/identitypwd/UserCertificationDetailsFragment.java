package com.tokentm.sdk.components.identitypwd;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.common.BaseFragment;
import com.tokentm.sdk.components.databinding.TksComponentsUserFragmentCertificationDetailsBinding;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情 被添加到认证说明activity和认证详情activity中作为共有显示
 */
public class UserCertificationDetailsFragment extends BaseFragment implements CompanyCertificationInstructionsPresenter {

    public static UserCertificationDetailsFragment newInstance() {
        return new UserCertificationDetailsFragment();
    }

    TksComponentsUserFragmentCertificationDetailsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsUserFragmentCertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        CompanyCertificationInstructionsVM viewModel = ViewModelProviders.of(this).get(CompanyCertificationInstructionsVM.class);
        binding.setViewModel(viewModel);
        binding.setPresenter(this);
        viewModel.loadData(this);
    }

}
