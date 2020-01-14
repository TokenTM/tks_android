package com.tokentm.sdk.components.identitypwd.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemChainStateOtherBinding;
import com.tokentm.sdk.components.identitypwd.model.ChainServiceOtherItem;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信说明  别人查看展示adapter
 */
public class ChainServiceOtherAdapter extends BaseBindableAdapter<TksComponentsAdapterItemChainStateOtherBinding, ChainServiceOtherItem> {

    /**
     * 链信服务状态显示类型
     */
    public static final int VIEW_TYPE_STATE = 101;
    /**
     * 链信说明 正文部分显示
     */
    public static final int VIEW_TYPE_CHAIN_SERVICE_INFO = 103;
    /**
     * 10dp分割线
     */
    public static final int VIEW_TYPE_CHAIN_DIVIDING_LINE = 104;

    @Override
    protected TksComponentsAdapterItemChainStateOtherBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {

        return TksComponentsAdapterItemChainStateOtherBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, TksComponentsAdapterItemChainStateOtherBinding binding, @Nullable ChainServiceOtherItem model, int index) {
        if (model == null) {
            return;
        }
        binding.tvTitle.setText(model.getTitle());
        binding.tvContent.setText(model.getContent());
        switch (model.getViewType()) {
            case VIEW_TYPE_STATE:
                binding.viewLine1.setVisibility(model.isState() ? View.VISIBLE : View.GONE);
                binding.tvContent.setVisibility(View.GONE);
                binding.viewLine10.setVisibility(View.GONE);
                binding.flState.setVisibility(View.VISIBLE);
                binding.tvSuccess.setVisibility(model.isState() ? View.VISIBLE : View.GONE);
                binding.tvFail.setVisibility(model.isState() ? View.GONE : View.VISIBLE);
                break;
            case VIEW_TYPE_CHAIN_SERVICE_INFO:

                binding.tvContent.setVisibility(View.VISIBLE);
                binding.viewLine10.setVisibility(View.GONE);
                binding.flState.setVisibility(View.GONE);
                if (model.getTitle().startsWith("时间戳") || model.getTitle().startsWith("统一社会信用代码")){
                    binding.viewLine1.setVisibility(View.GONE);
                }else {
                    binding.viewLine1.setVisibility(View.VISIBLE);
                }
                break;
            case VIEW_TYPE_CHAIN_DIVIDING_LINE:
                binding.viewLine1.setVisibility(View.GONE);
                binding.tvContent.setVisibility(View.GONE);
                binding.tvTitle.setVisibility(View.GONE);
                binding.viewLine10.setVisibility(View.VISIBLE);
                binding.flState.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
