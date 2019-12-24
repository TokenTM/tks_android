package com.tokentm.sdk.uidemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.identitypwd.model.BindUDID;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.uidemo.databinding.DidActivityBinding;

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
        did = DemoSp.getInstance().getLoginDID();
        binding.didText.setText(TextUtils.isEmpty(did) ? "" : "已创建did:\n" + did);
    }

    private void initView() {
        setTitle("Did");
        binding.didBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchUserIdentityConfirmActivity(
                        DidDemoActivity.this,
                        "17611639080",
                        new Consumer<BindUDID>() {
                            @Override
                            public void accept(BindUDID bindUDID) throws Exception {
                                //TODO 中心化系统和userId进行绑定
                                did = bindUDID.getDid();
                            }
                        });
            }
        });


        binding.checkPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
