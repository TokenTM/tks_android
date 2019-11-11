package com.tokentm.sdk.components.cert.adapter;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokentm.sdk.components.cert.recyclerview.BaseBindableAdapter;
import com.tokentm.sdk.components.common.BarCodeUtil;
import com.tokentm.sdk.components.databinding.TksComponentsUserAdapterItemPropertyRightsTransferRecordsBinding;
import com.tokentm.sdk.model.CommodityOwnershipRecordDTO;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

import java.text.SimpleDateFormat;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录adapter
 */
public class UserPropertyRightsTransferRecordsAdapter extends BaseBindableAdapter<TksComponentsUserAdapterItemPropertyRightsTransferRecordsBinding, CommodityOwnershipRecordDTO> {
    @Override
    protected TksComponentsUserAdapterItemPropertyRightsTransferRecordsBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return TksComponentsUserAdapterItemPropertyRightsTransferRecordsBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, TksComponentsUserAdapterItemPropertyRightsTransferRecordsBinding binding, @Nullable CommodityOwnershipRecordDTO commodityOwnershipRecordDTO, int index) {
        if (commodityOwnershipRecordDTO == null) {
            return;
        }

        binding.userCertficationDateTv.setText(formatDate(commodityOwnershipRecordDTO.getTime()));
        binding.userCertficationActionTv.setText(commodityOwnershipRecordDTO.getInfo());
        binding.userCertficationTimeTv.setText(formatTime(commodityOwnershipRecordDTO.getTime()));
        binding.userCertficationCodeTv.setText(commodityOwnershipRecordDTO.getTxHash());
        binding.userBarCode.post(new Runnable() {
            @Override
            public void run() {
                binding.userBarCode.setImageBitmap(BarCodeUtil.createBarcode(commodityOwnershipRecordDTO.getTxHash(),binding.userBarCode.getWidth()
                        ,binding.userBarCode.getHeight()));
            }
        });


        binding.userCertficationCodeLl.setVisibility(TextUtils.isEmpty(commodityOwnershipRecordDTO.getTxHash()) ? View.GONE : View.VISIBLE);
        binding.userCertficationTopLine.setVisibility(index > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    private String formatTime(long time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            return simpleDateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String formatDate(long time) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
