package com.tokentm.sdk.uidemo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.source.CertificateService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityCertificationDetailsBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

import static com.tokentm.sdk.uidemo.DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_CONTENT;
import static com.tokentm.sdk.uidemo.DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_EXTRA_DATA;
import static com.tokentm.sdk.uidemo.DemoSp.SP_KEY_TO_DID;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情页面demo
 */
public class CertificationDetailsActivity extends BaseTitleBarActivity {

    private ActivityCertificationDetailsBinding binding;

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    private static Intent getLauncher(Context context) {
        return new Intent(context, CertificationDetailsActivity.class);
    }

    String did;

    @Override
    protected void onResume() {
        super.onResume();
        did = DemoSp.getInstance().getLoginDID();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCertificationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("认证详情");
        //开启物权转移记录
        binding.btCreatePropertyRightsTransferRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchPropertyRightsTransferRecordsActivity(
                        getActivity(), did);
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
                ComponentUtils.launchCertificationInstructionsActivity(
                        getActivity(), did);
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
                ComponentUtils.launchCertificationDetailsActivity(
                        getActivity(), txHash);
            }
        });
        //开启 企业认证 弹窗
        binding.btCreateIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.showEnterpriseCertificationAlertDialog(
                        getActivity(),
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
                ComponentUtils.showIdentityAuthenticationAlertDialog(
                        getActivity(),
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
            }
        });

        //发起证书
        binding.btReleaseCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启发证demo页面
                ReleaseCertificateActivity.launch(getActivity(), did);
            }
        });

        //确认证书
        binding.btConfirmCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String certificateId = DemoSp.getInstance().getString(DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_ID);
                if (TextUtils.isEmpty(certificateId)) {
                    ToastUtils.showToast("请先发证");
                    return;
                }
                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        getActivity(), did,
                        new BiConsumer<DialogInterface, String>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                String content = DemoSp.getInstance().getString(SP_KEY_CERTIFICATION_CERTIFICATE_CONTENT);
                                String extraData = DemoSp.getInstance().getString(SP_KEY_CERTIFICATION_CERTIFICATE_EXTRA_DATA);
                                String toDid = DemoSp.getInstance().getString(SP_KEY_TO_DID);
                                TokenTmClient.getService(CertificateService.class)
                                        .confirm(did, identityPwd, certificateId, content, extraData, toDid)
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(CertificationDetailsActivity.this)))
                                        .subscribe(new Consumer<ChainResult>() {
                                            @Override
                                            public void accept(ChainResult chainResult) throws Exception {
                                                ToastUtils.showToast(chainResult.getTxHash());
                                                DemoSp.getInstance().putString(DemoSp.SP_KEY_TX_HASH, chainResult.getTxHash());
                                            }
                                        });
                            }
                        });
            }
        });

    }
}
