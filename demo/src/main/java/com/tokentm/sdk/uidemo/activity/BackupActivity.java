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
import com.tokentm.sdk.uidemo.databinding.ActivityBackupBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 备份数据
 */
public class BackupActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static void launch(Context context,String did) {
        context.startActivity(getLauncher(context,did));
    }
    private static Intent getLauncher(Context context, String did) {
        return new Intent(context, BackupActivity.class)
                .putExtra(KEY_DID, did);
    }

    ActivityBackupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("备份数据");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tvBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()){
                    return;
                }
                StoreItem<String> storeItem = new StoreItem<>();
                storeItem.setDid(did);
                storeItem.setDataId(binding.etDataId.getText().toString().trim());
                storeItem.setDataType(binding.etDataType.getText().toString().trim());
                storeItem.setData(binding.etDataContent.getText().toString().trim());
                TokenTmClient.getService(StoreService.class)
                        .store(storeItem)
                        .compose(XXF.bindToErrorNotice())
                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(BackupActivity.this)))
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                ToastUtils.showToast("备份成功");
                                finish();
                            }
                        });
            }
        });
    }
}
