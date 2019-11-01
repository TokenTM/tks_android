package com.tokentm.sdk.components.cert;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.CompanyActivityCompanySubmitFileBinding;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdInputAlertDialog;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.source.CertRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.BaseFragmentAdapter;

import java.io.File;
import java.util.Arrays;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 公司认证 提交文件
 * 返回值 @{@link CompanyCertResult}
 */
public class CompanyCertSubmitFileActivity extends BaseTitleBarActivity {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static void launch(Context context, CompanyCertParams companyCertParams) {
        context.startActivity(getLauncher(context, companyCertParams));
    }

    public static Intent getLauncher(Context context, CompanyCertParams companyCertParams) {
        return new Intent(context, CompanyCertSubmitFileActivity.class)
                .putExtra(KEY_CERT_PARAMS, companyCertParams);
    }

    CompanyActivityCompanySubmitFileBinding binding;
    CompanyCertParams companyCertParams;
    BaseFragmentAdapter baseFragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CompanyActivityCompanySubmitFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("企业认证");
        companyCertParams = (CompanyCertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);

        binding.companyNameTv.setText(companyCertParams.getCompanyName());
        binding.companyCreditCodeTv.setText(companyCertParams.getCompanyCreditCode());
        binding.legalPersonNameTv.setText(companyCertParams.getLegalPersonName());

        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.viewPager.setAdapter(baseFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager()));
        baseFragmentAdapter.bindTitle(true, Arrays.asList("营业执照认证", "公函认证"));

        baseFragmentAdapter.bindData(true,
                Arrays.asList(
                        ChooseBusinessLicenseFragment.newInstance(companyCertParams),
                        ChooseOfficialLetterFragment.newInstance(companyCertParams)
                )
        );

        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCert();
            }
        });
    }

    private void companyCert() {
        Fragment currFragment = baseFragmentAdapter.getFragmentsList().get(binding.tabLayout.getSelectedTabPosition());
        if (currFragment instanceof PicSelectPresenter) {
            PicSelectPresenter picSelectPresenter = (PicSelectPresenter) currFragment;
            String picPath = picSelectPresenter.getSelectedPic();
            if (TextUtils.isEmpty(picSelectPresenter.getSelectedPic())) {
                ToastUtils.showToast(String.format("请上传%s的资料", baseFragmentAdapter.getPageTitle(binding.tabLayout.getSelectedTabPosition())));
                return;
            }
            new UserIdentityPwdInputAlertDialog(this,
                    companyCertParams.getuDid(),
                    new BiConsumer<DialogInterface, String>() {
                        @Override
                        public void accept(DialogInterface dialogInterface, String pwd) throws Exception {
                            dialogInterface.dismiss();
                            CertRepositoryImpl
                                    .getInstance()
                                    .companyCert(companyCertParams.getuDid(),
                                            pwd,
                                            companyCertParams.getCompanyName(),
                                            companyCertParams.getCompanyType(),
                                            companyCertParams.getCompanyCreditCode(),
                                            new File(picPath)
                                    )
                                    .compose(XXF.bindToLifecycle(CompanyCertSubmitFileActivity.this))
                                    .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(CompanyCertSubmitFileActivity.this)))
                                    .subscribe(new Consumer<CompanyCertResult>() {
                                        @Override
                                        public void accept(CompanyCertResult companyCertResult) throws Exception {
                                            setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, companyCertResult));
                                            finish();
                                        }
                                    });
                        }
                    }).show();

        }
    }

}
