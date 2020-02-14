package com.tokentm.sdk.components.identitypwd.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsDialogGoodsTransferTecordsDetailBinding;
import com.tokentm.sdk.components.identitypwd.viewmodel.GoodsTransferRecordsDialogVm;
import com.xxf.arch.XXF;

import java.util.Map;

import io.reactivex.functions.BiConsumer;

/**
 * 物权转移显示上链详情
 *
 * @author lqx Email:herolqx@126.com
 */
public class GoodsTransferRecordsDetailDialog extends BaseAlertDialog<Boolean> {


    private TksComponentsDialogGoodsTransferTecordsDetailBinding binding;

    public GoodsTransferRecordsDetailDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        super(context, dialogConsumer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsDialogGoodsTransferTecordsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    @Override
    protected double getWindowScale() {
        return 0.86;
    }

    public GoodsTransferRecordsDetailDialog setData(Map<String,Object> map){
        GoodsTransferRecordsDialogVm viewModel = new GoodsTransferRecordsDialogVm(XXF.getApplication());
        viewModel.chainIdentityState.set((Boolean) map.get("state"));
        viewModel.goodsName.set((String) map.get("goodsName"));
        viewModel.goodsNumber.set(String.valueOf(map.get("goodsNumber")));
        viewModel.txHash.set((String) map.get("txHash"));
        viewModel.timesTamp.set((String) map.get("time"));
        binding.setViewModel(viewModel);
        return this;
    }
    private void initView() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(false);
                dismiss();
            }
        });
    }
}
