package com.tokentm.sdk.components.cert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.cert.adapter.GoodsTransferRecordsAdapter;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityGoodsTransferRecordsBinding;
import com.tokentm.sdk.components.databinding.TksComponentsAdapterItemGoodsTransferRecordsBinding;
import com.tokentm.sdk.components.identitypwd.dialog.GoodsTransferRecordsDetailDialog;
import com.tokentm.sdk.components.utils.TimeUtils;
import com.tokentm.sdk.model.SellerBuyerinfoDTO;
import com.tokentm.sdk.model.TransferCommodityActionDTO;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.view.databinding.statelayout.IStateLayoutVM;
import com.xxf.view.databinding.statelayout.StateLayoutVM;
import com.xxf.view.loading.ViewState;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemChildClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录
 */
public class GoodsTransferRecordsActivity extends BaseTitleBarActivity {

    private static final String KEY_GOODS_ID = "goods_id";
    private String goodsId;
    private TksComponentsActivityGoodsTransferRecordsBinding binding;
    private GoodsTransferRecordsAdapter userCertificationRecordAdapter;

    public IStateLayoutVM stateLayoutVM = new StateLayoutVM(new Action() {
        @Override
        public void run() throws Exception {
            loadData();
        }
    });
    private String commodityName;
    private int commodityCount;

    public static void launch(Context context, String goodsId) {
        context.startActivity(getLauncher(context, goodsId));
    }

    public static Intent getLauncher(Context context, String goodsId) {
        return new Intent(context, GoodsTransferRecordsActivity.class)
                .putExtra(KEY_GOODS_ID, goodsId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityGoodsTransferRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        setTitle("物权转移记录");
        goodsId = getIntent().getStringExtra(KEY_GOODS_ID);
        binding.setStateLayoutVM(stateLayoutVM);
        binding.recyclerView.setAdapter(userCertificationRecordAdapter = new GoodsTransferRecordsAdapter());
        userCertificationRecordAdapter.registerAdapterDataObserver(new DataChangeAdapterObserver() {
            @Override
            protected void updateUI() {
                stateLayoutVM.setLayoutState(userCertificationRecordAdapter.getDataSize() > 0 ? ViewState.VIEW_STATE_CONTENT : ViewState.VIEW_STATE_EMPTY);
            }
        });
        userCertificationRecordAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View childView, int index) {
                TksComponentsAdapterItemGoodsTransferRecordsBinding binding = holder.getBinding();
                TransferCommodityActionDTO item = (TransferCommodityActionDTO) adapter.getItem(index);
                if (item == null || binding == null) {
                    return;
                }
                if (binding.tvSigned == childView) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("goodsName",commodityName);
                    dataMap.put("goodsNumber",commodityCount);
                    dataMap.put("state",true);
                    dataMap.put("txHash",item.getTxHash());
                    dataMap.put("time", TimeUtils.formatUtc(item.getTimestamp()));
                    GoodsTransferRecordsDetailDialog goodsTransferRecordsDetailDialog = new GoodsTransferRecordsDetailDialog(getActivity(), new BiConsumer<DialogInterface, Boolean>() {
                        @Override
                        public void accept(DialogInterface dialogInterface, Boolean aBoolean) throws Exception {

                        }
                    });
                    goodsTransferRecordsDetailDialog.show();
                    goodsTransferRecordsDetailDialog.setData(dataMap);
                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        TokenTmClient.getService(CommodityService.class)
                .getCommodityTransferActionRecords(goodsId)
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TransferCommodityActionDTO>>() {
                    @Override
                    public void accept(List<TransferCommodityActionDTO> transferCommodityActionDTOS) throws Exception {
                        if (transferCommodityActionDTOS != null && transferCommodityActionDTOS.size() > 0) {
                            SellerBuyerinfoDTO sellerBuyerinfo = transferCommodityActionDTOS.get(transferCommodityActionDTOS.size() - 1).getSellerBuyerinfo();
                            commodityName = sellerBuyerinfo.getCommodityName();
                            binding.tvGoodsName.setText(String.format("物品名称 : %s", commodityName));
                            commodityCount = sellerBuyerinfo.getCommodityCount();
                            binding.tvGoodsNumber.setText(String.format("交易数量 : %s", commodityCount));
                            userCertificationRecordAdapter.bindData(true, transferCommodityActionDTOS);
                        }
                    }
                });

    }
}
