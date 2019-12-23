package com.tokentm.sdk.components.cert;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyActivityCompanySubmitFileBinding;
import com.tokentm.sdk.components.identitypwd.view.UserIdentityPwdInputDialog;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.model.CompanyType;
import com.tokentm.sdk.source.CertService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.FragmentUtils;
import com.xxf.arch.utils.ToastUtils;

import java.io.File;

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

    TksComponentsCompanyActivityCompanySubmitFileBinding binding;
    CompanyCertParams companyCertParams;
    SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyActivityCompanySubmitFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        setTitle("企业认证");
        companyCertParams = (CompanyCertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);

        binding.companyNameTv.setText(companyCertParams.getCompanyName());
        binding.companyCreditCodeTv.setText(companyCertParams.getCompanyCreditCode());


        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.rbBusinessLicense.getId()) {
                    binding.companyCreditCodeTv.setHint("必填");
                } else if (checkedId == binding.rbOfficialLetter.getId()) {
                    binding.companyCreditCodeTv.setHint("选填");
                }
                FragmentUtils.switchFragment(getSupportFragmentManager(), getOrNewFragment(checkedId), binding.flContent.getId());
            }
        });

        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyCert();
            }
        });
        binding.rbBusinessLicense.setChecked(true);

        binding.companyUserTypeTv.setText(companyCertParams.getCompanyType() == CompanyType.TYPE_COMPANY ? "法定代表人" : "姓名");
    }

    private void loadData() {
        TokenTmClient.getService(CertService.class)
                .getUserCertByIDCardInfo(companyCertParams.getuDid())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<CertUserInfoStoreItem>() {
                    @Override
                    public void accept(CertUserInfoStoreItem certUserInfoStoreItem) throws Exception {
                        //实名认证的名字
                        binding.legalPersonNameTv.setText(certUserInfoStoreItem.getName());
                    }
                });
    }

    private Fragment getOrNewFragment(@IdRes int checkedId) {
        Fragment fragment = fragmentSparseArray.get(checkedId);
        if (fragment == null) {
            if (checkedId == binding.rbBusinessLicense.getId()) {
                fragment = ChooseBusinessLicenseFragment.newInstance(companyCertParams);
            } else if (checkedId == binding.rbOfficialLetter.getId()) {
                fragment = ChooseOfficialLetterFragment.newInstance(companyCertParams);
            } else {
                throw new RuntimeException("no support");
            }
            fragmentSparseArray.put(checkedId, fragment);
        }
        return fragment;
    }

    private void companyCert() {
        if (binding.radioGroup.getCheckedRadioButtonId() == binding.rbBusinessLicense.getId()
                && TextUtils.isEmpty(binding.companyCreditCodeTv.getText())) {
            ToastUtils.showToast("请填写企业社会统一信用代码!");
            return;
        }

        Fragment currFragment = getOrNewFragment(binding.radioGroup.getCheckedRadioButtonId());
        if (currFragment instanceof PicSelectPresenter) {
            PicSelectPresenter picSelectPresenter = (PicSelectPresenter) currFragment;
            String picPath = picSelectPresenter.getSelectedPic();
            if (TextUtils.isEmpty(picSelectPresenter.getSelectedPic())) {
                RadioButton button = findViewById(binding.radioGroup.getCheckedRadioButtonId());
                ToastUtils.showToast(String.format("请上传%s的资料", button.getText()));
                return;
            }
            UserIdentityPwdInputDialog.showUserIdentityPwdInputDialogNoStampAnim(this,
                    companyCertParams.getuDid(),
                    new BiConsumer<DialogInterface, String>() {
                        @Override
                        public void accept(DialogInterface dialogInterface, String pwd) throws Exception {
                            dialogInterface.dismiss();
                            TokenTmClient.getService(CertService.class)
                                    .companyCert(companyCertParams.getuDid(),
                                            pwd,
                                            companyCertParams.getCompanyName(),
                                            companyCertParams.getCompanyType(),
                                            binding.companyCreditCodeTv.getText().toString().trim(),
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
