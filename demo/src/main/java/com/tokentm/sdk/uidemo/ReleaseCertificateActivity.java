package com.tokentm.sdk.uidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.CertificateInitiateResultDTO;
import com.tokentm.sdk.source.CommodityService;
import com.tokentm.sdk.uidemo.databinding.ReleaseCertificateBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

import static com.tokentm.sdk.uidemo.DemoSp.SP_KEY_CERTIFICATE_ID;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 发布证书demo
 */
public class ReleaseCertificateActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    private String did;

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, ReleaseCertificateActivity.class)
                .putExtra(KEY_DID, did);
    }

    ReleaseCertificateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReleaseCertificateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setTitle("发布证书");
        did = getIntent().getStringExtra(KEY_DID);
        binding.tksComponentsCompanyConfirmReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer commodityCount = Integer.valueOf(binding.commodityCount.getText().toString());
                    //弹出校验身份密码
                    ComponentUtils.showIdentityPwdDialog(
                            ReleaseCertificateActivity.this,
                            did,
                            new BiConsumer<DialogInterface, String>() {
                                @Override
                                public void accept(DialogInterface dialogInterface, String identityPwd) throws Exception {
                                    dialogInterface.dismiss();
                                    initiate(did
                                            , identityPwd
                                            , binding.sellerNameEt.getText().toString()
                                            , binding.commodityName.getText().toString()
                                            , commodityCount, did
                                            , binding.toBuyerName.getText().toString());
                                }
                            });
                } catch (NumberFormatException e) {
                    ToastUtils.showToast("商品数量为数字");
                }

            }
        });
    }


    /**
     * 发货
     *
     * @param did            发货人
     * @param identityPwd    发货人身份密码
     * @param sellerName     商家名称
     * @param commodityName  商品名称
     * @param commodityCount 商品发货数量
     * @param toBuyerUdDid   收货人
     * @param toBuyerName    收货人姓名
     */
    private void initiate(String did, String identityPwd, String sellerName, String commodityName, int commodityCount, String toBuyerUdDid, String toBuyerName) {
        TokenTmClient.getService(CommodityService.class)
                .send(did, identityPwd, sellerName, commodityName, commodityCount, did, toBuyerName)
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ReleaseCertificateActivity.this)))
                .subscribe(new Consumer<CertificateInitiateResultDTO>() {
                    @Override
                    public void accept(CertificateInitiateResultDTO certificateInitiateResultDTO) throws Exception {
                        //存储certificate_id
                        DemoSp.getInstance().putString(SP_KEY_CERTIFICATE_ID, certificateInitiateResultDTO.getId());
                        finish();
                    }
                });
    }
}
