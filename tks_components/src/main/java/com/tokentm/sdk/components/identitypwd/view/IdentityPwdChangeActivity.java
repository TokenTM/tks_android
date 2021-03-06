package com.tokentm.sdk.components.identitypwd.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.common.CompatUtils;
import com.tokentm.sdk.components.databinding.TksComponentsActivityIdentityPwdChangeBinding;
import com.tokentm.sdk.components.identitypwd.UserConfig;
import com.tokentm.sdk.source.IdentityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 通过原密码-->身份密码修改
 */
public class IdentityPwdChangeActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    public static void launch(@NonNull Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    public static Intent getLauncher(@NonNull Context context, String did) {
        return new Intent(context, IdentityPwdChangeActivity.class)
                .putExtra(KEY_DID, did);
    }

    TksComponentsActivityIdentityPwdChangeBinding binding;
    String did;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityIdentityPwdChangeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("身份密码修改");
        did = getIntent().getStringExtra(KEY_DID);

        //长度限制
        binding.identityOriginalPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.identityRepwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});


        //设置密文或者明文显示
        binding.identityOriginalPwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    CompatUtils.showTextForPlain(binding.identityOriginalPwdEt);
                } else {
                    CompatUtils.showTextForCipher(binding.identityOriginalPwdEt);
                }
            }
        });
        binding.identityPwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    CompatUtils.showTextForPlain(binding.identityPwdEt);
                } else {
                    CompatUtils.showTextForCipher(binding.identityPwdEt);
                }
            }
        });
        binding.identityRepwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    CompatUtils.showTextForPlain(binding.identityRepwdEt);
                } else {
                    CompatUtils.showTextForCipher(binding.identityRepwdEt);
                }
            }
        });


        //监听
        binding.identityPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //设置密码校验提示
                binding.identityPwdCheckLengthTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                binding.identityPwdCheckCombineTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                binding.identityPwdCheckLengthTv.setSelected(s.length() >= UserConfig.MINI_LENTH_PWD && s.length() <= UserConfig.MAX_LENTH_PWD);
                binding.identityPwdCheckCombineTv.setSelected(UserConfig.PATTERN_PWD.matcher(s).matches());
            }
        });


        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private boolean checkInputLegal() {
        if (TextUtils.isEmpty(binding.identityOriginalPwdEt.getText())) {
            ToastUtils.showToast("请输入原身份密码!");
            return false;
        }
        if (TextUtils.isEmpty(binding.identityPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码!");
            return false;
        }
        if (!UserConfig.PATTERN_PWD.matcher(binding.identityPwdEt.getText()).matches()) {
            ToastUtils.showToast("请输入6-20位数字及字母!");
            return false;
        }
        if (!TextUtils.equals(binding.identityPwdEt.getText(), binding.identityRepwdEt.getText())) {
            ToastUtils.showToast("身份密码与确认身份密码输入不一致!");
            return false;
        }
        return true;
    }

    @SuppressLint("CheckResult")
    private void submit() {
        if (!checkInputLegal()) {
            return;
        }
        String originalPwd = binding.identityOriginalPwdEt.getText().toString().trim();
        String newPwd = binding.identityPwdEt.getText().toString().trim();
        TokenTmClient.getService(IdentityService.class)
                .resetIdentityPwd(did, originalPwd, newPwd)
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, aBoolean));
                        finish();
                    }
                });
    }
}
