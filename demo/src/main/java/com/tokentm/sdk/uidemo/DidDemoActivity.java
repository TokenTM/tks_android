package com.tokentm.sdk.uidemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.uidemo.databinding.DidActivityBinding;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 生成did
 */
public class DidDemoActivity extends BaseTitleBarActivity {
    DidActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DidActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    String did;

    @Override
    protected void onResume() {
        super.onResume();
        did = DemoSp.getInstance().getString(DemoSp.SP_KEY_DID);
        binding.didText.setText(TextUtils.isEmpty(did) ? "" : "已创建did:\n" + did);
    }

    private void initView() {
        setTitle("Did创建");
        binding.didBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchUserIdentityConfirmActivity(
                        DidDemoActivity.this,
                        "17611639080",
                        new Consumer<String>() {
                            @Override
                            public void accept(String uDID) throws Exception {
                                //TODO 中心化系统和userId进行绑定
                                did = uDID;
                                DemoSp.getInstance().putString(DemoSp.SP_KEY_DID, uDID);
                                binding.didText.setText("did:" + uDID);
                            }
                        });
            }
        });

        binding.updatePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("先创建did");
                    return;
                }
                ComponentUtils
                        .launchUserIdentityPwdReSetctivity(
                                DidDemoActivity.this,
                                did,
                                "17611639080",
                                new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean reseted) throws Exception {
                                        ToastUtils.showToast("重置成功?" + String.valueOf(reseted));
                                    }
                                });
            }
        });

        binding.checkPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("先创建did");
                    return;
                }
                ComponentUtils.showIdentityPwdDialog(
                        DidDemoActivity.this,
                        did,
                        new BiConsumer<DialogInterface, String>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
//                                dialogInterface.dismiss();
                                //TODO 下一步
                            }
                        });
            }
        });
    }
}
