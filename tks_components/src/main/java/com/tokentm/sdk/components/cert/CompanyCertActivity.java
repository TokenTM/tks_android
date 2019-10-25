package com.tokentm.sdk.components.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.CompanyType;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.CompanyActivityCompanySelectTypeBinding;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 公司认证
 */
public class CompanyCertActivity extends BaseTitleBarActivity {

    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static void launch(Context context, CompanyCertParams companyCertParams) {
        context.startActivity(getLauncher(context, companyCertParams));
    }

    public static Intent getLauncher(Context context, CompanyCertParams companyCertParams) {
        return new Intent(context, CompanyCertActivity.class)
                .putExtra(KEY_CERT_PARAMS, companyCertParams);
    }

    CompanyActivityCompanySelectTypeBinding binding;
    CompanyCertParams companyCertParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CompanyActivityCompanySelectTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        companyCertParams = (CompanyCertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);
        binding.llCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyCertSubmitFileActivity.launch(getActivity(),
                        new CompanyCertParams.Builder(companyCertParams)
                                .setCompanyType(CompanyType.TYPE_COMPANY)
                                .build());
            }
        });
        binding.llOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyCertSubmitFileActivity.launch(getActivity(),
                        new CompanyCertParams.Builder(companyCertParams)
                                .setCompanyType(CompanyType.TYPE_ORGANIZATION)
                                .build());
            }
        });
    }
}
