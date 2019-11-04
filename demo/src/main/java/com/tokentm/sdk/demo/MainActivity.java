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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
                String str = "abcdefghijkuipjhdghfgfgdgfdggjffgjdgjfegvywqgttygiyggfdaig";
                int length = str.length();
                int sliceCount = new Random().nextInt(3) + 4;
                System.out.println("========>count:" + sliceCount);
                List<Integer> splitIndexs = new ArrayList<>(Arrays.asList(0, length));
                while (splitIndexs.size() < sliceCount - 1) {
                    int index = new Random().nextInt(length);
                    if (!splitIndexs.contains(index)) {
                        splitIndexs.add(index);
                    }
                }
                Collections.sort(splitIndexs);
                System.out.println("========>splitIndexs:" + splitIndexs);
                List<String> splitStringList = new ArrayList<>();
                for (int i = 0; i < splitIndexs.size() - 1; i++) {
                    splitStringList.add(str.substring(splitIndexs.get(i), splitIndexs.get(i + 1)));
                }

                StringBuilder sb = new StringBuilder();
                for (String s : splitStringList) {
                    sb.append(s);
                }
                System.out.println("========>" + TextUtils.equals(str, sb.toString()));

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
                        new CompanyCertParams.Builder(did, "北京百度科技有限公司", "李彦宏").build(),
                        new Consumer<CompanyCertResult>() {
                            @Override
                            public void accept(CompanyCertResult companyCertResult) throws Exception {
                                //TODO 中心化系统进行记录
                                ToastUtils.showToast("公司认证成功:" + companyCertResult);
                            }
                        });
            }
        });
    }
}
