package com.tokentm.sdk.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.ComponentUtils;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;
import com.tokentm.sdk.model.CompanyCertResult;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;


public class MainActivity extends FragmentActivity {


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
                ComponentUtils.launchUserCertActivity(
                        MainActivity.this,
                        new UserCertByIDCardParams.Builder(did)
                                .setUserName("小炫风")
                                .setUserIDCard("511324198901090148")
                                .build(),
                        new Consumer<String>() {
                            @Override
                            public void accept(String txHash) throws Exception {
                                //TODO 中心化系统进行记录
                                ToastUtils.showToast("实名认证成功:" + txHash);
                            }
                        });
            }
        });
        binding.btCertCompany.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getString("did");
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("请先生成did");
                    return;
                }
                ComponentUtils.launchCompanyCertActivity(
                        MainActivity.this,
                        new CompanyCertParams.Builder(did, "北京百度科技有限公司").build(),
                        new Consumer<CompanyCertResult>() {
                            @Override
                            public void accept(CompanyCertResult companyCertResult) throws Exception {
                                //TODO 中心化系统进行记录
                                ToastUtils.showToast("公司认证成功:" + companyCertResult);
                            }
                        });
            }
        });
        //开启酒来宝页面
        binding.btWineToTreasure.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(WineToTreasureActivity.getLauncher(v.getContext()));
            }
        });
    }
}
