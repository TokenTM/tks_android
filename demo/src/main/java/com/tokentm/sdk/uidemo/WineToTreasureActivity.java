package com.tokentm.sdk.uidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tokentm.sdk.components.cert.CompanyChainCertificationActivity;
import com.tokentm.sdk.components.identitypwd.CompanyCompanyEnterpriseCertificationAlertDialog;
import com.tokentm.sdk.components.identitypwd.UserCertificationDetailsActivity;
import com.tokentm.sdk.components.identitypwd.CompanyCertificationInstructionsActivity;
import com.tokentm.sdk.components.cert.UserUserPropertyRightsTransferRecordsActivity;
import com.tokentm.sdk.components.identitypwd.UserUserIdentityAuthenticationAlertDialog;
import com.tokentm.sdk.uidemo.databinding.WineToTreasureBinding;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;

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
                v.getContext().startActivity(UserUserPropertyRightsTransferRecordsActivity.getLauncher(v.getContext()));
            }
        });
        //开启认证说明
        binding.btCreateCertificationInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), CompanyCertificationInstructionsActivity.class));
            }
        });
        //开启认证详情
        binding.btCreateCertificationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), UserCertificationDetailsActivity.class));
            }
        });
        //开启企业认证
        binding.btCreateIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CompanyCompanyEnterpriseCertificationAlertDialog(v.getContext(), new BiConsumer<DialogInterface, Boolean>() {
                    @Override
                    public void accept(DialogInterface dialogInterface, Boolean s) throws Exception {
                        new UserUserIdentityAuthenticationAlertDialog(v.getContext(), new BiConsumer<DialogInterface, Boolean>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, Boolean s) throws Exception {
                                ToastUtils.showToast("身份认证");
                            }
                        }).show();
                    }
                }).show();
            }
        });
        //开启链信认证
        binding.btCreateChainCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyChainCertificationActivity.launch(v.getContext());
            }
        });

    }
}
