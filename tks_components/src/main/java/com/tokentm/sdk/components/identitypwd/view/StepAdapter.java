package com.tokentm.sdk.components.identitypwd.view;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemStepBinding;
import com.tokentm.sdk.components.identitypwd.model.StepModel;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class StepAdapter extends BaseBindableAdapter<TksComponentsAdapterItemStepBinding, StepModel> {

    private int step;

    public void setStep(@IntRange(from = 0) int step) {
        this.step = step;
        notifyDataSetChanged();
    }

    @Override
    protected TksComponentsAdapterItemStepBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return TksComponentsAdapterItemStepBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, TksComponentsAdapterItemStepBinding binding, @Nullable StepModel stepModel, int index) {
        boolean isSelected = (index + 1) <= step;
        binding.stepLine.setVisibility(index > 0 ? View.VISIBLE : View.GONE);
        binding.stepLine.setBackgroundColor(isSelected ? 0xff00c1ce : 0xffe5e5e5);
        binding.stepTv.setBackgroundResource(isSelected ? stepModel.getSelectIcon() : stepModel.getUnSelectIcon());
        binding.stepTv.setText(String.valueOf(index + 1));
        binding.stepTv.setTextColor(isSelected ? 0xff0099db : 0xFFFFFF);
    }
}
