package com.tokentm.sdk.components.identitypwd.view;

import android.databinding.ViewDataBinding;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemStepCircularBinding;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemStepLineBinding;
import com.tokentm.sdk.components.identitypwd.model.StepModel;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class StepAdapter extends BaseBindableAdapter<ViewDataBinding, StepModel> {

    public static final int CIRCULAR = 0;
    public static final int LINE = 1;
    private int step;

    public void setStep(@IntRange(from = 0) int step) {
        this.step = step;
        notifyDataSetChanged();
    }

    @Override
    protected ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        if (viewType == CIRCULAR){
            return TksComponentsAdapterItemStepCircularBinding.inflate(inflater, viewGroup, false);
        }else {
            return TksComponentsAdapterItemStepLineBinding.inflate(inflater, viewGroup, false);
        }

    }

    @Override
    public void onBindHolder(BaseViewHolder holder, ViewDataBinding binding, @Nullable StepModel stepModel, int index) {
        if (binding instanceof TksComponentsAdapterItemStepCircularBinding){
            TksComponentsAdapterItemStepCircularBinding binding1 = (TksComponentsAdapterItemStepCircularBinding)binding;
//            boolean isSelected = (index + 1) <= step;
////            binding1.stepLine.setVisibility(index > 0 ? View.VISIBLE : View.GONE);
////            binding1.stepLine.setBackgroundColor(isSelected ? 0xff00c1ce : 0xffe5e5e5);
//            binding1.stepTv.setBackgroundResource(isSelected ? stepModel.getSelectIcon() : stepModel.getUnSelectIcon());
//            binding1.stepTv.setText(String.valueOf(index + 1));
//            binding1.stepTv.setTextColor(isSelected ? 0xff0099db : 0xFFFFFF);
        }

        if (binding instanceof TksComponentsAdapterItemStepLineBinding){
            TksComponentsAdapterItemStepLineBinding binding1 = (TksComponentsAdapterItemStepLineBinding)binding;
        }

    }

    @Override
    public int getViewType(int index) {
        return getData().get(index).getViewType();
    }
}
