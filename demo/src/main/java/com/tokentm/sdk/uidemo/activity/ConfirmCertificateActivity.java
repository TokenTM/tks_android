package com.tokentm.sdk.uidemo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.CertificateInfoDTO;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.source.CertificateService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityConfirmCertificateBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 确认证书
 */
public class ConfirmCertificateActivity extends BaseTitleBarActivity {

    private static final String KEY_CERTIFICATE_ID = "certificate_id";

    private String certificateId;

    public static void launch(Context context, String certificateId) {
        context.startActivity(getLauncher(context, certificateId));
    }

    private static Intent getLauncher(Context context, String certificateId) {
        return new Intent(context, ConfirmCertificateActivity.class)
                .putExtra(KEY_CERTIFICATE_ID, certificateId);
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

    private void initData() {
        TokenTmClient.getService(CertificateService.class)
                .getCertificate(certificateId)
                .compose(XXF.bindToLifecycle(getActivity()))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ConfirmCertificateActivity.this)))
                .subscribe(new Consumer<CertificateInfoDTO>() {
                    @Override
                    public void accept(CertificateInfoDTO certificateInfoDTO) throws Exception {
                        binding.etCertificateContent.setText(certificateInfoDTO.getContent());
                        binding.etToReceive.setText(certificateInfoDTO.getDid());
                        binding.etCertificateType.setText(certificateInfoDTO.getType());
                        if (certificateInfoDTO.getSignature() != null && certificateInfoDTO.getSignature().size() > 0) {
                            for (int i = 0; i < certificateInfoDTO.getSignature().size(); i++) {
                                binding.etCertificateOtherContent.setText(certificateInfoDTO.getSignature().get(i).getData());
                            }
                        }
                    }
                });

    }

    private void initView() {
        setTitle("确认证书");
        certificateId = getIntent().getStringExtra(KEY_CERTIFICATE_ID);
        binding.etCertificateId.setText(certificateId);
        binding.tvConfirmCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getLoginDID();
                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        getActivity(), did,
                        new BiConsumer<DialogInterface, String>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                String content = binding.etCertificateContent.getText().toString().trim();
                                String extraData = binding.etCertificateOtherContent.getText().toString().trim();
                                String toDid = binding.etToReceive.getText().toString().trim();
                                TokenTmClient.getService(CertificateService.class)
                                        .confirm(did, identityPwd, certificateId, content, extraData, toDid)
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ConfirmCertificateActivity.this)))
                                        .subscribe(new Consumer<ChainResult>() {
                                            @Override
                                            public void accept(ChainResult chainResult) throws Exception {
                                                if (chainResult.getTxHash() != null) {
                                                    ToastUtils.showToast("确认证书成功");
                                                    DemoSp.getInstance().putString(DemoSp.SP_KEY_TX_HASH, chainResult.getTxHash());
                                                } else {
                                                    ToastUtils.showToast("确认证书失败");
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }
}
