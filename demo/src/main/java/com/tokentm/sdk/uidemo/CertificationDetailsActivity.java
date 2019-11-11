package com.tokentm.sdk.uidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokentm.sdk.components.ComponentUtils;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.uidemo.databinding.CertificationDetailsBinding;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证详情页面
 */
public class CertificationDetailsActivity extends BaseTitleBarActivity {


    CertificationDetailsBinding binding;

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
                ComponentUtils.launchUserPropertyRightsTransferRecordsActivity(
                        CertificationDetailsActivity.this,
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
                ComponentUtils.launchCompanyCertificationInstructionsActivity(
                        CertificationDetailsActivity.this,
                        new Consumer<CompanyCertResult>() {
                            @Override
                            public void accept(CompanyCertResult companyCertResult) throws Exception {
                                //TODO 处理认证说明返回值
                            }
                        });
            }
        });
        //开启认证详情
        binding.btCreateCertificationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchUserCertificationDetailsActivity(
                        CertificationDetailsActivity.this,
                        new Consumer<CompanyCertResult>() {
                            @Override
                            public void accept(CompanyCertResult companyCertResult) throws Exception {
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

    }
}
