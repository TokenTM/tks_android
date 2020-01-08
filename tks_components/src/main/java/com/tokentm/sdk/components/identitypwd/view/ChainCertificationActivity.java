package com.tokentm.sdk.components.identitypwd.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.ActivityChainCertificationBinding;
import com.tokentm.sdk.components.identitypwd.presenter.IChainCertificationPresenter;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class ChainCertificationActivity extends BaseTitleBarActivity implements IChainCertificationPresenter {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    private static Intent getLauncher(Context context) {
        return new Intent(context, ChainCertificationActivity.class);
    }

    ActivityChainCertificationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChainCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setPresenter(this);
        initView();
        loadData();
    }


    private void initView() {
        setTitle("链信认证");
    }

    private void loadData() {

    }
}
