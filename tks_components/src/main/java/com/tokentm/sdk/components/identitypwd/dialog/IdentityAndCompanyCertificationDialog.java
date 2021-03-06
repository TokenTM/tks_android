package com.tokentm.sdk.components.identitypwd.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsDialogIdentityCompanyCertificationBinding;

import io.reactivex.functions.BiConsumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 前往身份认证和企业认证弹窗  非强制
 */
public class IdentityAndCompanyCertificationDialog extends BaseAlertDialog<Boolean> {


    private TksComponentsDialogIdentityCompanyCertificationBinding binding;

    public IdentityAndCompanyCertificationDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        super(context, dialogConsumer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsDialogIdentityCompanyCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    @Override
    protected double getWindowScale() {
        return 0.72;
    }

    private void initView() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(false);
                dismiss();
            }
        });
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(true);
                dismiss();
            }
        });
    }
}
