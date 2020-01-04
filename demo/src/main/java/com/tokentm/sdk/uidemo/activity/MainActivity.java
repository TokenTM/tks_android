package com.tokentm.sdk.uidemo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityMainBinding;
import com.tokentm.sdk.uidemo.prensenter.IMainPresenter;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 首页
 */
public class MainActivity extends BaseTitleBarActivity implements IMainPresenter {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setPresenter(this);
        initView();
    }

    String did;

    @Override
    protected void onResume() {
        super.onResume();
        did = DemoSp.getInstance().getLoginDID();
        ComponentUtils.isShowIdentityDescription(this, did, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                binding.tvIdentityAuthentication.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initView() {
        setTitle("首页");
    }

    @Override
    public void onIdentityAuthentication() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        ComponentUtils.launchUserCertActivity(
                getActivity()
                , new UserCertByIDCardParams.Builder(did)
                        .setUserName("")
                        .setUserIDCard("")
                        .build(), new Consumer<ChainResult>() {
                    @Override
                    public void accept(ChainResult chainResult) throws Exception {
                        ToastUtils.showToast("认证成功");
                    }
                });
    }

    @Override
    public void onIdentityDescription() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        ComponentUtils.launchCertificationInstructionsActivity(getActivity(), did);
    }

    @Override
    public void onOpenChainCertification() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        ChainCertificationActivity.launch(getActivity());
    }

    @Override
    public void onDeliverGoods() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        //开启发货页面
        DeliverGoodsActivity.launch(getActivity(), did);
    }

    @Override
    public void onReceiveGoods() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        String goodsId = DemoSp.getInstance().getString(DemoSp.SP_KEY_GOODS_ID);
        if (TextUtils.isEmpty(goodsId)) {
            ToastUtils.showToast("请先发货");
            return;
        }
        ReceiveGoodsActivity.launch(getActivity(), goodsId);
    }

    @Override
    public void onCertification() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        //开启发证页面
        ReleaseCertificateActivity.launch(getActivity(), did);
    }

    @Override
    public void onConfirmCertificate() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        String certificateId = DemoSp.getInstance().getString(DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_ID);
        if (TextUtils.isEmpty(certificateId)) {
            ToastUtils.showToast("请先发证");
            return;
        }
        ConfirmCertificateActivity.launch(getActivity(), certificateId);
    }

    @Override
    public void onDisabledCertificate() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        String certificateId = DemoSp.getInstance().getString(DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_ID);
        if (TextUtils.isEmpty(certificateId)) {
            ToastUtils.showToast("请先发证");
            return;
        }
        CancelCertificateActivity.launch(getActivity(), certificateId);
    }

    @Override
    public void onBackup() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        BackupActivity.launch(getActivity(), did);
    }

    @Override
    public void onGetBackup() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        GetBackupActivity.launch(getActivity(), did);
    }

    @Override
    public void onLogout() {
        if (!RAUtils.isLegalDefault()) {
            return;
        }
        DemoSp.getInstance().logout();
        LoginOrRegisterActivity.launch(getActivity());
        finish();
    }
}
