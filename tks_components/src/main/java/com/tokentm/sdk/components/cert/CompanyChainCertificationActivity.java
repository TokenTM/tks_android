package com.tokentm.sdk.components.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityChainCertificationBinding;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class CompanyChainCertificationActivity extends BaseTitleBarActivity {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    private static Intent getLauncher(Context context) {
        return new Intent(context, CompanyChainCertificationActivity.class);
    }

    TksComponentsActivityChainCertificationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityChainCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        setTitle("链信认证");
        //找回身份密码
        binding.tksComponentsUserRetrieveIdentityPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //企业认证
        binding.tksComponentsUserEnterpriseCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //物权转移记录
        binding.tksComponentsUserPropertyRightsTransferRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loadData() {
//        TokenTmClient.getService(CertService.class)
//                .getUserCertByIDCardInfo(companyCertParams.getuDid())
//                .compose(XXF.bindToLifecycle(this))
//                .compose(XXF.bindToErrorNotice())
//                .subscribe(new Consumer<CertUserInfoStoreItem>() {
//                    @Override
//                    public void accept(CertUserInfoStoreItem certUserInfoStoreItem) throws Exception {
//                        //实名认证的名字
//                        binding.legalPersonNameTv.setText(certUserInfoStoreItem.getName());
//                    }
//                });
    }

}
