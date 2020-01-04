package com.tokentm.sdk.uidemo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.identitypwd.model.BindUDID;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityDidBinding;
import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 生成did
 */
public class DidDemoActivity extends BaseTitleBarActivity {
    ActivityDidBinding binding;

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    private static Intent getLauncher(Context context) {
        return new Intent(context, DidDemoActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDidBinding.inflate(getLayoutInflater());
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
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                ComponentUtils.launchUserIdentityConfirmActivity(
                        DidDemoActivity.this,
                        "",
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
                if (!RAUtils.isLegalDefault()) {
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
