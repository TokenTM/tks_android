package com.tokentm.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.DidService;
import com.xxf.arch.XXF;

import java.util.HashMap;

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
                TokenTmClient.getService(DidService.class)
                        .createDID("123", new HashMap<>())
                        .compose(XXF.bindToErrorNotice())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String did) throws Exception {

                            }
                        });
            }
        });
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TokenTmClient.getService(ChainService.class);
            }
        });
    }
}
