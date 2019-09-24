package com.tokentm.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tokentm.sdk.BackupChunkDTO;
import com.tokentm.sdk.CertDataSource;
import com.tokentm.sdk.demo.databinding.ActivityMainBinding;

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
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CertDataSource.getInstance()
                        .backupData(new BackupChunkDTO(null, null, null, null, 0))
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.d("======>", "v:" + aLong);
                            }
                        });
            }
        });
    }
}
