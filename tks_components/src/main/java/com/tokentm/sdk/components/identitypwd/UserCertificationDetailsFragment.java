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

    private final static String TX_HASH = "tx_hash";

    public static UserCertificationDetailsFragment newInstance(String txHash) {
        UserCertificationDetailsFragment userCertificationDetailsFragment = new UserCertificationDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TX_HASH, txHash);
        userCertificationDetailsFragment.setArguments(bundle);
        return userCertificationDetailsFragment;
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            String txHash = arguments.getString(TX_HASH);
            CompanyCertificationInstructionsVM viewModel = ViewModelProviders.of(this).get(CompanyCertificationInstructionsVM.class);
            binding.setViewModel(viewModel);
            binding.setPresenter(this);
            viewModel.loadData(this, txHash);
        }

    }

}
