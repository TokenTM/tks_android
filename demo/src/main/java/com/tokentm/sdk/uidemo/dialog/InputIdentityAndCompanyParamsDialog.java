package com.tokentm.sdk.uidemo.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.uidemo.databinding.InputIdentityCompanyTxHashDidDialogBinding;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 查看他人链信认证的时候, 调用
 */
public class InputIdentityAndCompanyParamsDialog extends BaseAlertDialog<InputIdentityCompanyParams> {

    InputIdentityCompanyTxHashDidDialogBinding binding;

    public InputIdentityAndCompanyParamsDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, InputIdentityCompanyParams> dialogConsumer) {
        super(context, dialogConsumer);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InputIdentityCompanyTxHashDidDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setCancelable(false);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etIdentityTxHash = binding.etIdentityTxHash.getText().toString().trim();
                String etCompanyTxHash = binding.etCompanyTxHash.getText().toString().trim();
                String etIdentityDid = binding.etIdentityDid.getText().toString().trim();
                String etCompanyDid = binding.etCompanyDid.getText().toString().trim();
                if (!TextUtils.isEmpty(etIdentityTxHash)
                        && !TextUtils.isEmpty(etCompanyTxHash)
                        && !TextUtils.isEmpty(etIdentityDid)
                        && !TextUtils.isEmpty(etCompanyDid)) {
                    InputIdentityCompanyParams inputIdentityCompanyParams = new InputIdentityCompanyParams(etIdentityTxHash, etCompanyTxHash, etIdentityDid, etCompanyDid);
                    setResult(inputIdentityCompanyParams);
                } else {
                    ToastUtils.showToast("数据不能为空");
                }
                dismiss();
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
