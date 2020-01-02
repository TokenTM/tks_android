package com.tokentm.sdk.uidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.source.StoreService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.databinding.ActivityGetBackupBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 备份数据
 */
public class GetBackupActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static void launch(Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    private static Intent getLauncher(Context context, String did) {
        return new Intent(context, GetBackupActivity.class)
                .putExtra(KEY_DID, did);
    }

    ActivityGetBackupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetBackupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("获取备份");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tvGetBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TokenTmClient.getService(StoreService.class)
                        .getStore(did,binding.etDataType.getText().toString().trim(), binding.etDataId.getText().toString().trim())
                        .compose(XXF.bindToErrorNotice())
                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(GetBackupActivity.this)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<StoreItem<String>>() {
                            @Override
                            public void accept(StoreItem<String> stringStoreItem) throws Exception {
                                binding.tvDataContent.setText(stringStoreItem.getData());
                            }
                        });
            }
        });
    }
}
