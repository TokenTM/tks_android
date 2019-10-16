package com.tokentm.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.DidService;
import com.xxf.arch.XXF;
import com.xxf.arch.json.JsonUtils;
import com.xxf.arch.utils.ToastUtils;

import java.util.LinkedHashMap;

import io.reactivex.functions.Consumer;


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
                LinkedHashMap<Long, String> securityQuestionAnswers = new LinkedHashMap<>();
                securityQuestionAnswers.put(1L, "a");
                securityQuestionAnswers.put(2L, "a");
                securityQuestionAnswers.put(3L, "a");
                TokenTmClient.getService(DidService.class)
                        .createDID("aaa", securityQuestionAnswers)
                        .compose(XXF.bindToErrorNotice())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String did) throws Exception {
                                ToastUtils.showToast("did:" + did);
                            }
                        });
            }
        });
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XXF.getLogger().d("=========>s:" + JsonUtils.toJsonString("xxx"));
                XXF.getLogger().d("=========>s:" + JsonUtils.toBean("xx", String.class));
                TokenTmClient.getService(ChainService.class);
            }
        });
    }
}
