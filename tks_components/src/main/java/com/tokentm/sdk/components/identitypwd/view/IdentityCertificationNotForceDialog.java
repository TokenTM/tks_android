package com.tokentm.sdk.components.identitypwd.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsDialogIdentityCertificationBinding;

import io.reactivex.functions.BiConsumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 前往身份认证弹窗(非强制)
 */
public class IdentityCertificationNotForceDialog extends BaseAlertDialog<Boolean> {

    private TksComponentsDialogIdentityCertificationBinding binding;

    public IdentityCertificationNotForceDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        super(context, dialogConsumer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsDialogIdentityCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
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
