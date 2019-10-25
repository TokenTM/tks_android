package com.tokentm.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.cert.CompanyCertActivity;
import com.tokentm.sdk.components.cert.UserCertByIDCardActivity;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;
import com.xxf.arch.utils.ToastUtils;


public class MainActivity extends Activity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        binding.btCreateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletActivity.launch(v.getContext());
            }
        });
        binding.btCreateDid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DidDemoActivity.class));
            }
        });
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.btCertByIdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getString("did");
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("请先生成did");
                    return;
                }
                UserCertByIDCardActivity.launch(v.getContext(),
                        new UserCertByIDCardParams.Builder(did)
                                .setUserName("小炫风")
                                .setUserIDCard("511324198901090148")
                                .build()
                );
            }
        });
        binding.btCertCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getString("did");
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("请先生成did");
                    return;
                }
                CompanyCertActivity.launch(v.getContext(),
                        new CompanyCertParams.Builder(did, "北京百度科技有限公司", "李彦宏")
                                .build()
                );
            }
        });
    }
}
