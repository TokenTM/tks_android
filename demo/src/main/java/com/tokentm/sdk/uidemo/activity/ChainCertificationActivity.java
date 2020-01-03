package com.tokentm.sdk.uidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityChainCertificationBinding;
import com.tokentm.sdk.uidemo.dialog.InputCompanyNameFragmentDialog;
import com.tokentm.sdk.uidemo.dialog.OnInputCompanyNameListener;
import com.tokentm.sdk.uidemo.prensenter.IChainCertificationPresenter;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class ChainCertificationActivity extends BaseTitleBarActivity implements IChainCertificationPresenter {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    private static Intent getLauncher(Context context) {
        return new Intent(context, ChainCertificationActivity.class);
    }

    ActivityChainCertificationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChainCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setPresenter(this);
        initView();
        loadData();
    }


    private void initView() {
        setTitle("链信认证");
    }

    private void loadData() {

    }

    @Override
    public void onOpenCompanyCert() {
        String did = DemoSp.getInstance().getLoginDID();
        InputCompanyNameFragmentDialog.newInstance(new OnInputCompanyNameListener() {
            @Override
            public void onInputCompanyName(@NonNull String companyName) {
                ComponentUtils.launchCompanyCertActivity(getActivity(),
                        new CompanyCertParams.Builder(did, companyName).build(), new Consumer<ChainResult>() {
                            @Override
                            public void accept(ChainResult chainResult) throws Exception {
                                if (chainResult.getTxHash() != null) {
                                    ToastUtils.showToast("认证成功");
                                }
                            }
                        });
            }
        }).show(getSupportFragmentManager(), InputCompanyNameFragmentDialog.class.getSimpleName());
    }

    @Override
    public void onRetrieveIdentityPassword() {
        String did = DemoSp.getInstance().getLoginDID();
        ComponentUtils.launchForgotIdentityPwd(getActivity(), did, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean b) throws Exception {
                if (b) {
                    ToastUtils.showToast("修改成功");
                }
            }
        });
    }

    @Override
    public void onPropertyRightsTransferRecords() {
        String goodsId = DemoSp.getInstance().getString(DemoSp.SP_KEY_GOODS_ID);
        if ("".equals(goodsId)) {
            ToastUtils.showToast("请先发货");
            return;
        }
        ComponentUtils.launchPropertyRightsTransferRecordsActivity(
                getActivity(), goodsId);
    }
}
