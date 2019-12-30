package com.tokentm.sdk.uidemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.uidemo.databinding.ActivityChainCertificationBinding;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class ChainCertificationActivity extends BaseTitleBarActivity {

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
        initView();
        loadData();
    }


    private void initView() {
        setTitle("链信认证");
        //找回身份密码
        binding.flRetrieveIdentityPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginDID = DemoSp.getInstance().getLoginDID();
                ComponentUtils.launchForgotIdentityPwd(getActivity(),ChainCertificationActivity.this,loginDID);
            }
        });
    }

    private void loadData() {
    }

}
