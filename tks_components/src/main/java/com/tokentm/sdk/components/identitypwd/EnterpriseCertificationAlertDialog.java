package com.tokentm.sdk.components.identitypwd;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsDialogEnterpriseCertificationBinding;

import io.reactivex.functions.BiConsumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 前往企业认证弹窗
 */
public class EnterpriseCertificationAlertDialog extends BaseAlertDialog<Boolean> implements EnterpriseCertificationPresenter{


    private TksComponentsDialogEnterpriseCertificationBinding binding;

    public EnterpriseCertificationAlertDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        super(context, dialogConsumer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = this.getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //设置window背景透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        binding = TksComponentsDialogEnterpriseCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        binding.setPresenter(this);
    }

    @Override
    public void goAuthentication() {
        setResult(true);
    }
}