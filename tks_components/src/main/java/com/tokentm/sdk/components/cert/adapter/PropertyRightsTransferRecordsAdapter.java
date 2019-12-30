package com.tokentm.sdk.components.cert.adapter;


import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.common.BarCodeUtil;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemPropertyRightsTransferRecordsBinding;
import com.tokentm.sdk.model.CertificateCommodityActionDTO;
import com.tokentm.sdk.model.SellerBuyerinfoDTO;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

import java.text.SimpleDateFormat;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录adapter
 */
public class PropertyRightsTransferRecordsAdapter extends BaseBindableAdapter<TksComponentsAdapterItemPropertyRightsTransferRecordsBinding, CertificateCommodityActionDTO> {

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    @Override
    protected TksComponentsAdapterItemPropertyRightsTransferRecordsBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return TksComponentsAdapterItemPropertyRightsTransferRecordsBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, TksComponentsAdapterItemPropertyRightsTransferRecordsBinding binding, @Nullable CertificateCommodityActionDTO certificateCommodityActionDTO, int index) {
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

        //描述信息
        //sellerName:"每日一淘“,//买家(指店家,非个体)
        //buyerName:”人名”//买家(个体)
        binding.userPropertyRightsTransferRecordsDescriptionTv.setText(sellerBuyerInfo.getSellerName() + sellerBuyerInfo.getBuyerName());
        //物品名称
        binding.userPropertyRightsTransferRecordsItemNameTv.setText("物品名称:" + sellerBuyerInfo.getCommodityName());
        //发货时间
        binding.userPropertyRightsTransferRecordsDeliveryTimeTv.setText("发货时间:" + formatTime(certificateCommodityActionDTO.getTimestamp()));
        //购买数量
        binding.userPropertyRightsTransferRecordsBuyNumberTv.setText("购买数量:" + sellerBuyerInfo.getCommodityCount());
        //条形码右上角时间
        binding.userCertificationTimeTv.setText(formatTime(certificateCommodityActionDTO.getTimestamp()));

        if (txHash != null && !"".equals(txHash)) {
            //去掉前面开头的0x或者0X
            String startsWithLowerCase = "0x";
            String startsWithCapital = "0X";
            if (txHash.startsWith(startsWithLowerCase) || txHash.startsWith(startsWithCapital)) {
                txHash = txHash.substring(2);
            }
            //显示hash值
            binding.userCertificationCodeTv.setText(txHash);
            String finalTxHash = txHash;
            binding.userBarCode.post(new Runnable() {
                @Override
                public void run() {
                    binding.userBarCode.setImageBitmap(BarCodeUtil.createBarcode(finalTxHash, binding.userBarCode.getWidth()
                            , binding.userBarCode.getHeight()));
                }
            });
        }
    }

    private String formatTime(long time) {
        try {
            return simpleDateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
