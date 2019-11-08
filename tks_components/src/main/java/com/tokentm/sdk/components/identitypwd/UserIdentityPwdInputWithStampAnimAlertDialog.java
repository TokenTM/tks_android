package com.tokentm.sdk.components.identitypwd;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsUserDialogDecryptedByPwdWithStampAnimBinding;
import com.tokentm.sdk.source.IdentityPwdService;
import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 身份密码输入对话框 带盖章效果
 */
public class UserIdentityPwdInputWithStampAnimAlertDialog extends BaseAlertDialog<String> {

    private TksComponentsUserDialogDecryptedByPwdWithStampAnimBinding binding;
    String uDid;

    public UserIdentityPwdInputWithStampAnimAlertDialog(@NonNull Context context, String uDid, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        super(context, dialogConsumer);
        this.uDid = uDid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = this.getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //设置window背景透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        binding = TksComponentsUserDialogDecryptedByPwdWithStampAnimBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        //忘记密码
        binding.identityPwdForgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotIdentityPwd();
            }
        });
        //延时一段时间在执行动画,因为进来先弹键盘
        binding.tksComponentsUserDialogDecryptedWithRedChapterIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                PropertyAnimUtils.startStampAnim(binding.tksComponentsUserDialogDecryptedWithRedChapterIv
                        , binding.tksComponentsUserDialogDecryptedByReadChapter);
            }
        }, 800);
    }

    private void forgotIdentityPwd() {
        UserIdentityPwdReSetActivity.launch(getContext(), null);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void submit() {
        if (TextUtils.isEmpty(binding.identityPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码");
            return;
        }
        String pwd = binding.identityPwdEt.getText().toString().trim();
        TokenTmClient.getService(IdentityPwdService.class)
                .validateIdentityPwd(uDid, pwd)
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            setResult(pwd);
                        } else {
                            ToastUtils.showToast("密码不正确");
                        }
                    }
                });
    }
}
