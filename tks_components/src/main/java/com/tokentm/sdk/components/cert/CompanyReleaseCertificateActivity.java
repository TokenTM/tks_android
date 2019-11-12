package com.tokentm.sdk.components.cert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyActivityCompanyReleaseCertificateBinding;
import com.tokentm.sdk.model.CertificateInitiateResultDTO;
import com.tokentm.sdk.source.CommodityService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 发布证书
 */
public class CompanyReleaseCertificateActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, CompanyReleaseCertificateActivity.class)
                .putExtra(KEY_DID, did);
    }

    TksComponentsCompanyActivityCompanyReleaseCertificateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyActivityCompanyReleaseCertificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("发布证书");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tksComponentsCompanyConfirmReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        CompanyReleaseCertificateActivity.this,
                        did,
                        new BiConsumer<DialogInterface, String>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                initiate(did, identityPwd, did, binding.tksComponentsCompanyProductName.getText().toString());
                            }
                        });
            }
        });
    }

    /**
     * 发起证书
     *
     * @param did
     * @param identityPwd  身份密码
     * @param toDid 发给谁
     */
    @SuppressLint("CheckResult")
    private void initiate(String did, String identityPwd, String toDid, String content) {
        TokenTmClient.getService(CommodityService.class)
                .send(did, identityPwd, toDid, content)
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(CompanyReleaseCertificateActivity.this)))
                .subscribe(new Consumer<CertificateInitiateResultDTO>() {
                    @Override
                    public void accept(CertificateInitiateResultDTO certificateInitiateResultDTO) throws Exception {
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, certificateInitiateResultDTO.getId()));
                        finish();
                    }
                });
    }
}
