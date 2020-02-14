package com.tokentm.sdk.components.cert.adapter;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.common.BarCodeUtil;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemGoodsTransferRecordsBinding;
import com.tokentm.sdk.components.utils.TimeUtils;
import com.tokentm.sdk.model.SellerBuyerinfoDTO;
import com.tokentm.sdk.model.TransferCommodityActionDTO;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录adapter
 */
public class GoodsTransferRecordsAdapter extends BaseBindableAdapter<TksComponentsAdapterItemGoodsTransferRecordsBinding, TransferCommodityActionDTO> {

    @Override
    protected TksComponentsAdapterItemGoodsTransferRecordsBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return TksComponentsAdapterItemGoodsTransferRecordsBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, TksComponentsAdapterItemGoodsTransferRecordsBinding binding, @Nullable TransferCommodityActionDTO certificateCommodityActionDTO, int index) {
        //发货收货 物品转移记录( 存证模型实现)的包装模型
        if (certificateCommodityActionDTO == null) {
            return;
        }
        //发货收货 物品转移记录( 存证模型实现)的模型
        SellerBuyerinfoDTO sellerBuyerInfo = certificateCommodityActionDTO.getSellerBuyerinfo();
        if (sellerBuyerInfo == null) {
            return;
        }
        String txHash = certificateCommodityActionDTO.getTxHash();

        switch (sellerBuyerInfo.getState()) {
            //发货
            case 1:
                binding.tvGoodsTransferRecordsDescription.setText(String.format("%s已经发货给%s", sellerBuyerInfo.getSellerName(), sellerBuyerInfo.getBuyerName()));
                break;
            case 2:
                binding.tvGoodsTransferRecordsDescription.setText(String.format("%s已确认收货", sellerBuyerInfo.getBuyerName()));
                break;
            default:
                break;
        }

        binding.tvGoodsTransferRecordsChainInfo.setText(String.format("%s签名上链信息", sellerBuyerInfo.getBuyerName()));
        binding.tvGoodsTime.setText(TimeUtils.formatUtc(certificateCommodityActionDTO.getTimestamp()));
        //显示hash值
        binding.tvShowHash.setText(txHash);
        if (!TextUtils.isEmpty(txHash)){
            binding.ivBarCode.post(new Runnable() {
                @Override
                public void run() {
                    binding.ivBarCode.setImageBitmap(BarCodeUtil.createBarcode(txHash, binding.ivBarCode.getWidth()
                            , binding.ivBarCode.getHeight()));
                }
            });
        }
        //TODO 暂时隐藏弹窗
//        holder.bindChildClick(binding.tvSigned);




//        if (txHash != null && !"".equals(txHash)) {
//            //去掉前面开头的0x或者0X
//            String startsWithLowerCase = "0x";
//            String startsWithCapital = "0X";
//            if (txHash.startsWith(startsWithLowerCase) || txHash.startsWith(startsWithCapital)) {
//                txHash = txHash.substring(2);
//            }
//            //显示hash值
//            binding.tvShowHash.setText(txHash);
//            String finalTxHash = txHash;
//            binding.ivBarCode.post(new Runnable() {
//                @Override
//                public void run() {
//                    binding.ivBarCode.setImageBitmap(BarCodeUtil.createBarcode(finalTxHash, binding.ivBarCode.getWidth()
//                            , binding.ivBarCode.getHeight()));
//                }
//            });
//        }
    }


}
