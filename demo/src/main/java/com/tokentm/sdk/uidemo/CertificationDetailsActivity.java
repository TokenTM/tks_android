package com.tokentm.sdk.uidemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.uidemo.databinding.CertificationDetailsBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情页面demo
 */
public class CertificationDetailsActivity extends BaseTitleBarActivity {


    private CertificationDetailsBinding binding;

    public static Intent getLauncher(Context context) {
        return new Intent(context, CertificationDetailsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("认证详情");
        //开启物权转移记录
        binding.btCreatePropertyRightsTransferRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getString(DemoSp.SP_KEY_DID);
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("请先生成did");
                    return;
                }
                ComponentUtils.launchUserPropertyRightsTransferRecordsActivity(
                        CertificationDetailsActivity.this, did,
                        new Consumer<CompanyCertResult>() {
                            @Override
                            public void accept(CompanyCertResult companyCertResult) throws Exception {
                                //TODO 处理物权转移记录返回值
                            }
                        });
            }
        });
        //开启认证说明
        binding.btCreateCertificationInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txHash = DemoSp.getInstance().getString(DemoSp.SP_KEY_TX_HASH);
                if (TextUtils.isEmpty(txHash)) {
                    ToastUtils.showToast("请先发布证书 并 确认证书");
                    return;
                }
                ComponentUtils.launchCompanyCertificationInstructionsActivity(
                        CertificationDetailsActivity.this, txHash,
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                //TODO 处理认证说明返回值
                            }
                        });
            }
        });
        //开启认证详情
        binding.btCreateCertificationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txHash = DemoSp.getInstance().getString(DemoSp.SP_KEY_TX_HASH);
                if (TextUtils.isEmpty(txHash)) {
                    ToastUtils.showToast("请先发布证书 并 确认证书");
                    return;
                }
                ComponentUtils.launchUserCertificationDetailsActivity(
                        CertificationDetailsActivity.this, txHash,
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                //TODO 处理认证详情返回值
                            }
                        });
            }
        });
        //开启 企业认证 弹窗
        binding.btCreateIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.showCompanyCompanyEnterpriseCertificationAlertDialog(
                        CertificationDetailsActivity.this,
                        new BiConsumer<DialogInterface, Boolean>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, Boolean identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                //TODO 点击回调
                            }
                        });
            }
        });

        //开启 身份认证 弹窗
        binding.btCreateUserIdentityAuthenticationAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.showUserIdentityAuthenticationAlertDialog(
                        CertificationDetailsActivity.this,
                        new BiConsumer<DialogInterface, Boolean>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, Boolean identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                //TODO 点击回调
                            }
                        });
            }
        });
        //开启链信认证
        binding.btCreateChainCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchCompanyChainCertificationActivity(
                        CertificationDetailsActivity.this,
                        new Consumer<CompanyCertResult>() {
                            @Override
                            public void accept(CompanyCertResult companyCertResult) throws Exception {
                                //TODO 处理开启链信认证回值
                            }
                        });
            }
        });

        //开启发布证书
        binding.btReleaseCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getString(DemoSp.SP_KEY_DID);
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("请先生成did");
                    return;
                }
                //开启发证demo页面
                v.getContext().startActivity(ReleaseCertificateActivity.getLauncher(CertificationDetailsActivity.this, did));
            }
        });

        //确认证书
        binding.btConfirmCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String did = DemoSp.getInstance().getString(DemoSp.SP_KEY_DID);
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("请先生成did");
                    return;
                }
                String certificateId = DemoSp.getInstance().getString(DemoSp.SP_KEY_CERTIFICATE_ID);
                if (TextUtils.isEmpty(certificateId)) {
                    ToastUtils.showToast("请先发证");
                    return;
                }
                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        CertificationDetailsActivity.this, did,
                        new BiConsumer<DialogInterface, String>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                TokenTmClient.getService(CommodityService.class)
                                        .receive(did, identityPwd, certificateId)
                                        .compose(XXF.bindToLifecycle(CertificationDetailsActivity.this))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(CertificationDetailsActivity.this)))
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String txHash) throws Exception {
                                                ToastUtils.showToast(txHash);
                                                DemoSp.getInstance().putString(DemoSp.SP_KEY_TX_HASH, txHash);
                                            }
                                        });
                            }
                        });
            }
        });

    }
}
