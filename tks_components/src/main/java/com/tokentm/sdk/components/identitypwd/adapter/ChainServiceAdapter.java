package com.tokentm.sdk.components.identitypwd.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemChainServiceBinding;
import com.tokentm.sdk.components.identitypwd.model.ChainServiceModel;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证顶部链信服务展示
 */
public class ChainServiceAdapter extends BaseBindableAdapter<TksComponentsAdapterItemChainServiceBinding, ChainServiceModel> {

    @Override
    protected TksComponentsAdapterItemChainServiceBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return TksComponentsAdapterItemChainServiceBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, TksComponentsAdapterItemChainServiceBinding binding, @Nullable ChainServiceModel chainServiceModel, int index) {
        if (chainServiceModel == null) {
            return;
        }
        binding.tvTitle.setText(chainServiceModel.getTitle());
        binding.getRoot().setBackgroundResource(chainServiceModel.getSelectIcon());
    }
}
