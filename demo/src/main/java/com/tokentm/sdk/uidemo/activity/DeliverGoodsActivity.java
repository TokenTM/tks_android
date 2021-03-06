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
import com.tokentm.sdk.model.TransferInitResult;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.databinding.ActivityDeliverGoodsBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 发货
 */
public class DeliverGoodsActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static void launch(Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    private static Intent getLauncher(Context context, String did) {
        return new Intent(context, DeliverGoodsActivity.class)
                .putExtra(KEY_DID, did);
    }

    ActivityDeliverGoodsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliverGoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("确认发货");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tvSendGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                //弹出校验身份密码
                ComponentUtils.showIdentityPwdDialog(
                        DeliverGoodsActivity.this,
                        did,
                        new BiConsumer<DialogInterface, String>() {
                            @Override
                            public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                dialogInterface.dismiss();
                                TokenTmClient.getService(CommodityService.class)
                                        .send(did
                                                , identityPwd
                                                , binding.etSellerName.getText().toString().trim()
                                                , binding.etCommodityType.getText().toString().trim()
                                                , binding.etCommodityName.getText().toString().trim()
                                                , Integer.valueOf(binding.etCommodityCount.getText().toString().trim())
                                                , binding.etToBuyerDid.getText().toString().trim()
                                                , binding.etToBuyerName.getText().toString().trim())
                                        .compose(XXF.bindToLifecycle(getActivity()))
                                        .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(DeliverGoodsActivity.this)))
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<TransferInitResult>() {
                                            @Override
                                            public void accept(TransferInitResult transferInitResult) throws Exception {
                                                if (!TextUtils.isEmpty(transferInitResult.getId())) {
                                                    binding.tvShowGoodsId.setText(transferInitResult.getId());
                                                    ToastUtils.showToast("发货成功");
                                                } else {
                                                    ToastUtils.showToast("发货失败");
                                                }

                                            }
                                        });
                            }
                        });
            }
        });
    }

    /**
     * 发货
     */
    private void initiate(final String uDID, final String identityPwd, final String sellerName, String commodityType, String commodityName, final int commodityCount, final String toBuyerName, final String toBuyerUDID) {

    }
}
