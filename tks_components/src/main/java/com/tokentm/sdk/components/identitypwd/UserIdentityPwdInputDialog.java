package com.tokentm.sdk.components.identitypwd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.tokentm.sdk.components.databinding.UserDialogBackupDecryptedByPwdBinding;
import com.tokentm.sdk.source.DidRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 身份密码输入对话框
 */
public class UserIdentityPwdInputDialog extends BaseDialog<Boolean> {


    private UserDialogBackupDecryptedByPwdBinding binding;
    String uDid;

    public UserIdentityPwdInputDialog(@NonNull Context context,
                                      String uDid, OnDialogClickListener<Boolean> onDialogClickListener) {
        super(context, onDialogClickListener);
        this.uDid = uDid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        binding = UserDialogBackupDecryptedByPwdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        binding.backupPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        //忘记密码
        binding.backupPwdForgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotIdentityPwd();
            }
        });
    }

    private void forgotIdentityPwd() {
        UserIdentityPwdReSetActivity.launch(getContext(), null);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @SuppressLint("CheckResult")
    private void submit() {
        if (TextUtils.isEmpty(binding.backupPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码");
            return;
        }
        DidRepositoryImpl
                .getInstance()
                .validatePwd(uDid, binding.backupPwdEt.getText().toString())
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            confirm(true);
                        } else {
                            ToastUtils.showToast("密码不正确");
                        }
                    }
                });
    }
}
