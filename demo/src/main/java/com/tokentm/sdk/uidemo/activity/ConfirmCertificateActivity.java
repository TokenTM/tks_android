package com.tokentm.sdk.uidemo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.source.CertificateService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.databinding.ActivityConfirmCertificateBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 确认证书
 * 要求收证方正确的输入证书id和证书内容才可以正确收证,(证书附加内容和证书的收证人did)可空
 */
public class ConfirmCertificateActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static void launch(Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    private static Intent getLauncher(Context context, String did) {
        return new Intent(context, ConfirmCertificateActivity.class)
                .putExtra(KEY_DID, did);
    }

    ActivityConfirmCertificateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmCertificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    private void initView() {
        setTitle("确认证书");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tvConfirmCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                String etCertificateId = binding.etCertificateId.getText().toString().trim();
                String content = binding.etCertificateContent.getText().toString().trim();
                String extraData = binding.etCertificateOtherContent.getText().toString().trim();
                String toDid = binding.etToReceiveDid.getText().toString().trim();
                if (TextUtils.isEmpty(etCertificateId)) {
                    ToastUtils.showToast("请输入证书id");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast("请输入证书内容");
                    return;
                }
                ComponentUtils.showIdentityPwdDialog(
                        getActivity(), did,
                        new BiConsumer<DialogInterface, String>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                TokenTmClient.getService(CertificateService.class)
                                        .confirm(did, identityPwd, etCertificateId, content, extraData, toDid)
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ConfirmCertificateActivity.this)))
                                        .subscribe(new Consumer<ChainResult>() {
                                            @Override
                                            public void accept(ChainResult chainResult) throws Exception {
                                                if (chainResult.getTxHash() != null) {
                                                    binding.etToCertificateHash.setText(chainResult.getTxHash());
                                                    ToastUtils.showToast("确认证书成功");
                                                } else {
                                                    binding.etToCertificateHash.setText(chainResult.getTxHash());
                                                    ToastUtils.showToast("确认证书失败");
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }

    @SuppressLint("CheckResult")
    private void initData() {

    }


}
