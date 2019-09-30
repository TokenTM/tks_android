package com.tokentm.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;
import com.tokentm.sdk.source.ChainDataSource;


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
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TokenTmClient.getService(ChainDataSource.class);
            }
        });
    }
}
