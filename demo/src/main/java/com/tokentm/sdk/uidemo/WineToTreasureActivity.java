package com.tokentm.sdk.uidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tokentm.sdk.components.ComponentUtils;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.uidemo.databinding.WineToTreasureBinding;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 酒来宝测试页面
 */
public class WineToTreasureActivity extends FragmentActivity {


    WineToTreasureBinding binding;

    public static Intent getLauncher(Context context) {
        return new Intent(context, WineToTreasureActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WineToTreasureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        //开启物权转移记录
        binding.btCreatePropertyRightsTransferRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchUserPropertyRightsTransferRecordsActivity(
                        WineToTreasureActivity.this,
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
                        WineToTreasureActivity.this,
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
                        WineToTreasureActivity.this,
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
                        WineToTreasureActivity.this,
                        new BiConsumer<DialogInterface, Boolean>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, Boolean identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                //TODO 点击下一步回调
                            }
                        });
            }
        });

        //开启 身份认证 弹窗
        binding.btCreateUserIdentityAuthenticationAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.showUserIdentityAuthenticationAlertDialog(
                        WineToTreasureActivity.this,
                        new BiConsumer<DialogInterface, Boolean>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, Boolean identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                //TODO 点击下一步回调
                            }
                        });
            }
        });
        //开启链信认证
        binding.btCreateChainCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchCompanyChainCertificationActivity(
                        WineToTreasureActivity.this,
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
