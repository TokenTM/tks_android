package com.tokentm.sdk.uidemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityMainBinding;
import com.tokentm.sdk.uidemo.dialog.InputCompanyNameFragmentDialog;
import com.tokentm.sdk.uidemo.dialog.OnInputCompanyNameListener;
import com.tokentm.sdk.uidemo.prensenter.IMainPresenter;
import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
        TokenTmClient.getService(CertService.class)
                .isUserCert(did)
                .compose(XXF.bindToLifecycle(getActivity()))
                .compose(XXF.bindToErrorNotice())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
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
        ComponentUtils.launchUserCertActivity(
                getActivity()
                , new UserCertByIDCardParams.Builder(did)
                        .setUserName("")
                        .setUserIDCard("")
                        .build(), new Consumer<ChainResult>() {
                    @Override
                    public void accept(ChainResult chainResult) throws Exception {
                        if (chainResult.getTxHash() != null && !"".equals(chainResult.getTxHash())) {
                            ToastUtils.showToast("认证成功");
                        } else {
                            ToastUtils.showToast("认证失败");
                        }
                    }
                });
    }

    @Override
    public void onIdentityDescription() {
        ComponentUtils.launchCertificationInstructionsActivity(getActivity(), did);
    }

    @Override
    public void onOpenChainCertification() {
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
    public void onDeliverGoods() {
        //开启发货页面
        DeliverGoodsActivity.launch(getActivity(), did);
    }

    @Override
    public void onReceiveGoods() {
        String goodsId = DemoSp.getInstance().getString(DemoSp.SP_KEY_GOODS_ID);
        if (TextUtils.isEmpty(goodsId)) {
            ToastUtils.showToast("请先发货");
            return;
        }
        ReceiveGoodsActivity.launch(getActivity(), goodsId);
    }

    @Override
    public void onCertification() {
        //开启发证页面
        ReleaseCertificateActivity.launch(getActivity(), did);
    }

    @Override
    public void onConfirmCertificate() {
        String certificateId = DemoSp.getInstance().getString(DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_ID);
        if (TextUtils.isEmpty(certificateId)) {
            ToastUtils.showToast("请先发证");
            return;
        }
        ConfirmCertificateActivity.launch(getActivity(), certificateId);
    }

    @Override
    public void onDisabledCertificate() {
        String certificateId = DemoSp.getInstance().getString(DemoSp.SP_KEY_CERTIFICATION_CERTIFICATE_ID);
        if (TextUtils.isEmpty(certificateId)) {
            ToastUtils.showToast("请先发证");
            return;
        }
        CancelCertificateActivity.launch(getActivity(), certificateId);
    }

    @Override
    public void onBackup() {
        BackupActivity.launch(getActivity(), did);
    }

    @Override
    public void onGetBackup() {
        GetBackupActivity.launch(getActivity(), did);
    }

    @Override
    public void onLogout() {
        DemoSp.getInstance().logout();
        LoginOrRegisterActivity.launch(getActivity());
        finish();
    }
}
