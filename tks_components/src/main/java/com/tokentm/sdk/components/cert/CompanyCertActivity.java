package com.tokentm.sdk.components.cert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityCompanySelectTypeBinding;
import com.tokentm.sdk.model.ChainCertResult;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.CompanyCertInfoStoreItem;
import com.tokentm.sdk.model.CompanyType;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 企业/组织认证
 * 返回值 @{@link ChainCertResult < CompanyCertInfoStoreItem >}
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

    TksComponentsActivityCompanySelectTypeBinding binding;
    CompanyCertParams companyCertParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityCompanySelectTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("选择企业类型");
        companyCertParams = (CompanyCertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);
        binding.llCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext(CompanyType.TYPE_COMPANY);
            }
        });
        binding.llOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext(CompanyType.TYPE_ORGANIZATION);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void goNext(CompanyType companyType) {
        XXF.startActivityForResult(
                getActivity(),
                CompanyCertSubmitFileActivity.getLauncher(
                        getActivity(),
                        new CompanyCertParams.Builder(companyCertParams)
                                .setCompanyType(companyType)
                                .build()),
                1001)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .compose(XXF.bindUntilEvent(getActivity(), Lifecycle.Event.ON_DESTROY))
                .take(1)
                .map(new Function<ActivityResult, ChainCertResult<CompanyCertInfoStoreItem>>() {
                    @Override
                    public ChainCertResult<CompanyCertInfoStoreItem> apply(ActivityResult activityResult) throws Exception {
                        return (ChainCertResult<CompanyCertInfoStoreItem>) activityResult.getData().getSerializableExtra(KEY_ACTIVITY_RESULT);
                    }
                })
                .subscribe(new Consumer<ChainCertResult<CompanyCertInfoStoreItem>>() {
                    @Override
                    public void accept(ChainCertResult<CompanyCertInfoStoreItem> companyCertInfoStoreItemChainCertResult) throws Exception {
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, companyCertInfoStoreItemChainCertResult));
                        finish();
                    }
                });
    }
}
