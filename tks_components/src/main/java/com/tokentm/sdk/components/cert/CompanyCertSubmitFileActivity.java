package com.tokentm.sdk.components.cert;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityCompanySubmitFileBinding;
import com.tokentm.sdk.components.identitypwd.view.IdentityPwdInputDialog;
import com.tokentm.sdk.components.utils.IgnoreSpacesInputFilter;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.ChainCertResult;
import com.tokentm.sdk.model.CompanyAuthMode;
import com.tokentm.sdk.model.CompanyCertInfoStoreItem;
import com.tokentm.sdk.model.CompanyType;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.FragmentUtils;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import java.io.File;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 公司认证 提交文件
 * 返回值 @{@link ChainCertResult<CompanyCertInfoStoreItem>}
 */
public class CompanyCertSubmitFileActivity extends BaseTitleBarActivity {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    /**
     * 默认是法人认证
     */
    private CompanyAuthMode companyAuthMode = CompanyAuthMode.BY_LEGAL_PERSON;

    public static void launch(Context context, CompanyCertParams companyCertParams) {
        context.startActivity(getLauncher(context, companyCertParams));
    }

    public static Intent getLauncher(Context context, CompanyCertParams companyCertParams) {
        return new Intent(context, CompanyCertSubmitFileActivity.class)
                .putExtra(KEY_CERT_PARAMS, companyCertParams);
    }

    TksComponentsActivityCompanySubmitFileBinding binding;
    CompanyCertParams companyCertParams;
    SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityCompanySubmitFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {

        companyCertParams = (CompanyCertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);
        CompanyType companyType = companyCertParams.getCompanyType();
        if (companyType.equals(CompanyType.TYPE_COMPANY)) {
            setTitle("企业认证");
        } else {
            setTitle("组织认证");
        }
        String companyName = companyCertParams.getCompanyName();
        String companyCreditCode = companyCertParams.getCompanyCreditCode();
        //当传过来的值是空,那就可编辑,否则不可编辑
        if (companyName != null || !"".equals(companyName.trim())) {
            binding.companyNameTv.setText(companyName);
            binding.companyNameTv.setFocusable(false);
            binding.companyNameTv.setFocusableInTouchMode(false);
        }
        if (companyCreditCode == null || "".equals(companyCreditCode.trim())) {
            binding.companyCreditCodeTv.setText(companyCreditCode);
        }

        //去空格和限制6个字
        binding.legalPersonNameTv.setFilters(new InputFilter[]{new IgnoreSpacesInputFilter(), new InputFilter.LengthFilter(6)});

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.rbBusinessLicense.getId()) {
                    companyAuthMode = CompanyAuthMode.BY_LEGAL_PERSON;
                    binding.companyCreditCodeTv.setHint("必填");
                } else if (checkedId == binding.rbOfficialLetter.getId()) {
                    binding.companyCreditCodeTv.setHint("选填");
                    companyAuthMode = CompanyAuthMode.BY_OFFICIAL_LETTER;
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
                .getLatestUserCertStoreInfo(companyCertParams.getuDid())
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
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        if (binding.radioGroup.getCheckedRadioButtonId() == binding.rbBusinessLicense.getId()
                && TextUtils.isEmpty(binding.companyCreditCodeTv.getText())) {
            ToastUtils.showToast("请填写企业社会统一信用代码!");
            return;
        }

        if (TextUtils.isEmpty(binding.companyNameTv.getText().toString().trim())) {
            ToastUtils.showToast("请填写工商注册名字");
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
            IdentityPwdInputDialog.showUserIdentityPwdInputDialogNoStampAnim(this,
                    companyCertParams.getuDid(),
                    new BiConsumer<DialogInterface, String>() {
                        @Override
                        public void accept(DialogInterface dialogInterface, String pwd) throws Exception {
                            dialogInterface.dismiss();
                            String companyCreditCode = binding.companyCreditCodeTv.getText().toString().trim();
                            //注意 接口不让传递为""这样的空串,否则报签名问题
                            if (TextUtils.isEmpty(companyCreditCode)) {
                                companyCreditCode = null;
                            }
                            TokenTmClient.getService(CertService.class)
                                    .companyCert(companyCertParams.getuDid(),
                                            pwd,
                                            companyAuthMode,
                                            binding.companyNameTv.getText().toString().trim(),
                                            companyCertParams.getCompanyType(),
                                            companyCreditCode,
                                            new File(picPath)
                                    )
                                    .compose(XXF.bindToLifecycle(CompanyCertSubmitFileActivity.this))
                                    .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(CompanyCertSubmitFileActivity.this)))
                                    .subscribe(new Consumer<ChainCertResult<CompanyCertInfoStoreItem>>() {
                                        @Override
                                        public void accept(ChainCertResult<CompanyCertInfoStoreItem> companyCertInfoStoreItemChainCertResult) throws Exception {
                                            setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, companyCertInfoStoreItemChainCertResult));
                                            finish();
                                        }
                                    });
                        }
                    }).show();

        }
    }

}
