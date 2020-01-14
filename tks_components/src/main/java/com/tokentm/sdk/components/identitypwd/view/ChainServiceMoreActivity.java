package com.tokentm.sdk.components.identitypwd.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityChainServiceMoreBinding;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信服务更多功能页
 */
public class ChainServiceMoreActivity extends BaseTitleBarActivity {

    private static final String DID = "did";
    private String did;

    public static void launch(@NonNull Context context, @Nullable String did) {
        context.startActivity(getLauncher(context, did));
    }

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, ChainServiceMoreActivity.class)
                .putExtra(DID, did);
    }

    TksComponentsActivityChainServiceMoreBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityChainServiceMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("链信服务");
        did = getIntent().getStringExtra(DID);
        binding.tvIdentityPwdModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchChangeIdentityPwd(getActivity(), did, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ToastUtils.showToast("修改成功");
                        }
                    }
                });
            }
        });
        binding.tvIdentityPwdForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchForgotIdentityPwd(getActivity(), did, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            ToastUtils.showToast("修改成功");
                        }
                    }
                });
            }
        });
        binding.tvContactCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.launch(getActivity(),"https://bcard.tokentm.net/h5/share/relate-service.html");
            }
        });
    }
}
