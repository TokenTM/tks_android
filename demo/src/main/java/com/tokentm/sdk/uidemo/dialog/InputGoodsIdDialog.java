package com.tokentm.sdk.uidemo.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.uidemo.databinding.InputGoodsIdDialogBinding;

import io.reactivex.functions.BiConsumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 输入商品id
 */
public class InputGoodsIdDialog extends BaseAlertDialog<String> {

    InputGoodsIdDialogBinding binding;

    public InputGoodsIdDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        super(context, dialogConsumer);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = InputGoodsIdDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setCancelable(false);
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodsId = binding.etInputGoodsId.getText().toString();
                setResult(goodsId);
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
