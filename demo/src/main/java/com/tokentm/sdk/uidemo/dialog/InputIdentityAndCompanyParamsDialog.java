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

    @Override
    protected double getWindowScale() {
        return 0.72;
    }

    private void initView() {
        setCancelable(false);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etIdentityDid = binding.etIdentityDid.getText().toString().trim();
                String etIdentityTxHash = binding.etIdentityTxHash.getText().toString().trim();
                String etCompanyDid = binding.etCompanyDid.getText().toString().trim();
                String etCompanyTxHash = binding.etCompanyTxHash.getText().toString().trim();
                if (TextUtils.isEmpty(etIdentityDid) || TextUtils.isEmpty(etIdentityTxHash)) {
                    ToastUtils.showToast("身份did和身份txHash缺一不可");
                    return;
                }

                if (!TextUtils.isEmpty(etCompanyDid) || !TextUtils.isEmpty(etCompanyTxHash)){
                    ToastUtils.showToast("企业did和企业txHash缺一不可");
                    return;
                }
                InputIdentityCompanyParams inputIdentityCompanyParams = new InputIdentityCompanyParams(etIdentityTxHash, etCompanyTxHash, etIdentityDid, etCompanyDid);
                setResult(inputIdentityCompanyParams);
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
