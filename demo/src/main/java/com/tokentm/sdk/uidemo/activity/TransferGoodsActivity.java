package com.tokentm.sdk.uidemo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.SellerBuyerinfoDTO;
import com.tokentm.sdk.model.TransferCommodityActionDTO;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.databinding.ActivityTransferGoodsBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 转移货物
 */
public class TransferGoodsActivity extends BaseTitleBarActivity {

    private static final String KEY_GOODS_ID = "goods_id";
    private static final String KEY_DID = "did";

    private String goodsId;
    private String did;

    public static void launch(Context context, String did, String goodsId) {
        context.startActivity(getLauncher(context, did, goodsId));
    }

    private static Intent getLauncher(Context context, String did, String goodsId) {
        return new Intent(context, TransferGoodsActivity.class)
                .putExtra(KEY_DID, did)
                .putExtra(KEY_GOODS_ID, goodsId);
    }

    ActivityTransferGoodsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferGoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    private void initData() {
        TokenTmClient.getService(CommodityService.class)
                .getCommodityTransferActionRecords(goodsId)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.bindToLifecycle(getActivity()))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(TransferGoodsActivity.this)))
                .subscribe(new Consumer<List<TransferCommodityActionDTO>>() {
                    @Override
                    public void accept(List<TransferCommodityActionDTO> transferCommodityActionDTOS) throws Exception {
                        //收货的时候获取物权记录的最后一条
                        if (transferCommodityActionDTOS != null && transferCommodityActionDTOS.size() > 0) {
                            SellerBuyerinfoDTO sellerBuyerinfo = transferCommodityActionDTOS.get(transferCommodityActionDTOS.size() - 1).getSellerBuyerinfo();
                            //state: 1-发货状态   2收货状态
                            //收货状态并且买家did和你当前的did一致才能收
                            if (sellerBuyerinfo.getState() == 2 && sellerBuyerinfo.getBuyerDID().equals(did)) {
                                binding.etCommodityCount.setText(String.valueOf(sellerBuyerinfo.getCommodityCount()));
                                binding.etCommodityName.setText(sellerBuyerinfo.getCommodityName());
                                binding.etSellerName.setText(sellerBuyerinfo.getSellerName());
                                binding.etToBuyerName.setText(sellerBuyerinfo.getBuyerName());
                                binding.etToBuyerDid.setText(sellerBuyerinfo.getBuyerDID());
                                binding.etGoodsId.setText(goodsId);
                            } else {
                                binding.etGoodsId.setText(goodsId);
                                ToastUtils.showToast("没有和你相关的获取信息");
                            }
                        } else {
                            binding.etGoodsId.setText(goodsId);
                            ToastUtils.showToast("没有和你相关的获取信息");
                        }
                    }
                });
    }

    private void initView() {
        setTitle("转移物品");
        did = getIntent().getStringExtra(KEY_DID);
        goodsId = getIntent().getStringExtra(KEY_GOODS_ID);
        binding.tvTransferGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                String etToTransferBuyerDid = binding.etToTransferBuyerDid.getText().toString().trim();
                String etToTransferBuyerName = binding.etToTransferBuyerName.getText().toString().trim();
                if (TextUtils.isEmpty(etToTransferBuyerDid) ||
                        TextUtils.isEmpty(etToTransferBuyerName)) {
                    ToastUtils.showToast("请输入接收人did和名字");
                    return;
                }

                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        TransferGoodsActivity.this,
                        did,
                        new BiConsumer<DialogInterface, String>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                TokenTmClient.getService(CommodityService.class)
                                        .transfer(did
                                                , identityPwd
                                                , goodsId
                                                , etToTransferBuyerDid
                                                , etToTransferBuyerName
                                        )
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(TransferGoodsActivity.this)))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<ChainResult>() {
                                            @Override
                                            public void accept(ChainResult chainResult) throws Exception {
                                                if (chainResult != null && !TextUtils.isEmpty(chainResult.getTxHash())) {
                                                    binding.etChainHash.setText(chainResult.getTxHash());
                                                    ToastUtils.showToast("转移成功");
                                                } else {
                                                    ToastUtils.showToast("转移失败");
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }
}
