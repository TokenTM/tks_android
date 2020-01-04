package com.tokentm.sdk.uidemo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.SellerBuyerinfoDTO;
import com.tokentm.sdk.model.TransferCommodityActionDTO;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityReceiveGoodsBinding;
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
 * @Description 收货
 */
public class ReceiveGoodsActivity extends BaseTitleBarActivity {

    private static final String KEY_GOODS_ID = "goods_id";

    private String goodsId;

    public static void launch(Context context, String goodsId) {
        context.startActivity(getLauncher(context, goodsId));
    }

    private static Intent getLauncher(Context context, String did) {
        return new Intent(context, ReceiveGoodsActivity.class)
                .putExtra(KEY_GOODS_ID, did);
    }

    ActivityReceiveGoodsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveGoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    @SuppressLint("CheckResult")
    private void initData() {
        TokenTmClient.getService(CommodityService.class)
                .getCommodityTransferActionRecords(goodsId)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.bindToLifecycle(getActivity()))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ReceiveGoodsActivity.this)))
                .subscribe(new Consumer<List<TransferCommodityActionDTO>>() {
                    @Override
                    public void accept(List<TransferCommodityActionDTO> transferCommodityActionDTOS) throws Exception {
                        if (transferCommodityActionDTOS != null && transferCommodityActionDTOS.size() > 0) {
                            SellerBuyerinfoDTO sellerBuyerinfo = transferCommodityActionDTOS.get(0).getSellerBuyerinfo();
                            binding.etCommodityCount.setText(String.valueOf(sellerBuyerinfo.getCommodityCount()));
                            binding.etCommodityName.setText(sellerBuyerinfo.getCommodityName());
                            binding.etSellerName.setText(sellerBuyerinfo.getSellerName());
                            binding.etToBuyerName.setText(sellerBuyerinfo.getBuyerName());
                            binding.etToBuyerDid.setText(sellerBuyerinfo.getBuyerDID());
                        }
                    }
                });
    }

    private void initView() {
        setTitle("收货");
        goodsId = getIntent().getStringExtra(KEY_GOODS_ID);
        binding.etToBuyerDid.setText("");
        binding.tvReceiveGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                String did = DemoSp.getInstance().getLoginDID();
                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        getActivity(), did,
                        new BiConsumer<DialogInterface, String>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                TokenTmClient.getService(CommodityService.class)
                                        .receive(did,
                                                identityPwd,
                                                goodsId,
                                                binding.etCommodityName.getText().toString().trim(),
                                                Integer.valueOf(binding.etCommodityCount.getText().toString().trim()))
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ReceiveGoodsActivity.this)))
                                        .subscribe(new Consumer<ChainResult>() {
                                            @Override
                                            public void accept(ChainResult chainResult) throws Exception {
                                                if (chainResult.getTxHash() != null) {
                                                    ToastUtils.showToast("确认收货成功");
                                                    DemoSp.getInstance().putString(DemoSp.SP_KEY_TX_HASH, chainResult.getTxHash());
                                                    finish();
                                                } else {
                                                    ToastUtils.showToast("确认收货失败");
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }
}
