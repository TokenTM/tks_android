package com.tokentm.sdk.uidemo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.CertificateInitResult;
import com.tokentm.sdk.source.CertificateService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.databinding.ActivityReleaseCertificateBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 发布证书
 */
public class ReleaseCertificateActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static void launch(Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    private static Intent getLauncher(Context context, String did) {
        return new Intent(context, ReleaseCertificateActivity.class)
                .putExtra(KEY_DID, did);
    }

    ActivityReleaseCertificateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseCertificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("发布证书");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tvSendCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                String etCertificateType = binding.etCertificateType.getText().toString().trim();
                String etCertificateContent = binding.etCertificateContent.getText().toString().trim();
                //发布证书的时候toDid为空,任何人都可以接受证书
                String etToReceiveDid = binding.etToReceiveDid.getText().toString().trim();
                String etCertificateOtherContent = binding.etCertificateOtherContent.getText().toString().trim();
                if (TextUtils.isEmpty(etCertificateType)){
                    ToastUtils.showToast("请输入证书类型");
                    return;
                }if (TextUtils.isEmpty(etCertificateContent)){
                    ToastUtils.showToast("请输入证书内容");
                    return;
                }
                ComponentUtils.showIdentityPwdDialog(
                        ReleaseCertificateActivity.this,
                        did,
                        new BiConsumer<DialogInterface, String>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                TokenTmClient.getService(CertificateService.class)
                                        .initiate(did
                                                , identityPwd
                                                , etCertificateType
                                                , etCertificateContent
                                                , etCertificateOtherContent
                                                , System.currentTimeMillis()
                                                , etToReceiveDid)
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ReleaseCertificateActivity.this)))
                                        .subscribe(new Consumer<CertificateInitResult>() {
                                            @Override
                                            public void accept(CertificateInitResult certificateInitResult) throws Exception {
                                                if (certificateInitResult != null && certificateInitResult.getId() != null) {
                                                    binding.tvCertificateId.setText(certificateInitResult.getId());
                                                    binding.etToCertificateHash.setText(certificateInitResult.getTxHash());
                                                    ToastUtils.showToast("发布证书成功");
                                                } else {
                                                    binding.etToCertificateHash.setText("");
                                                    binding.tvCertificateId.setText("");
                                                    ToastUtils.showToast("发布证书失败");
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }
}
