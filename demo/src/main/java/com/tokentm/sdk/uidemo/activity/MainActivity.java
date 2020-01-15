package com.tokentm.sdk.uidemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.identitypwd.model.CertificationResultParams;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.ChainCertResult;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.CompanyCertInfoStoreItem;
import com.tokentm.sdk.uidemo.DemoSp;
import com.tokentm.sdk.uidemo.databinding.ActivityMainBinding;
import com.tokentm.sdk.uidemo.dialog.InputCompanyNameFragmentDialog;
import com.tokentm.sdk.uidemo.dialog.InputIdentityAndCompanyParamsDialog;
import com.tokentm.sdk.uidemo.dialog.OnInputCompanyNameListener;
import com.tokentm.sdk.uidemo.dialog.OnInputIdentityCompanyparamsListener;
import com.tokentm.sdk.uidemo.prensenter.IMainPresenter;
import com.xxf.arch.utils.ToastUtils;

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
    }

    private void initView() {
        setTitle("首页");
    }

    @Override
    public void onIdentityCertification() {
        ComponentUtils.launchUserCertActivity(
                getActivity()
                , new UserCertByIDCardParams.Builder(did)
                        .setUserName("")
                        .setUserIDCard("")
                        .build()
                , new Consumer<ChainResult>() {
                    @Override
                    public void accept(ChainResult chainResult) throws Exception {
                        saveIdentityCertification(chainResult);

                    }
                });
    }

    @Override
    public void onCompanyCertification() {
        InputCompanyNameFragmentDialog.newInstance(new OnInputCompanyNameListener() {
            @Override
            public void onInputCompanyName(@NonNull String companyName) {
                ComponentUtils.launchCompanyCertActivity(getActivity(), new CompanyCertParams.Builder(did, companyName).build()
                        , new Consumer<ChainCertResult<CompanyCertInfoStoreItem>>() {
                            @Override
                            public void accept(ChainCertResult<CompanyCertInfoStoreItem> chainResult) throws Exception {
                                saveICompanyCertification(chainResult);
                            }
                        });
            }
        }).show(getSupportFragmentManager(), InputCompanyNameFragmentDialog.class.getName());
    }

    @Override
    public void onOpenChainCertification() {
        String uTxHash = DemoSp.getInstance().getString(DemoSp.SP_KEY_IDENTITY_TX_HASH);
        String cTxHash = DemoSp.getInstance().getString(DemoSp.SP_KEY_COMPANY_TX_HASH);
        String uDid = DemoSp.getInstance().getString(DemoSp.SP_KEY_IDENTITY_DID);
        String cDid = DemoSp.getInstance().getString(DemoSp.SP_KEY_COMPANY_DID);
        ComponentUtils.launchChainCertificationActivity(getActivity(),
                uTxHash, cTxHash, uDid, cDid, new Consumer<CertificationResultParams>() {
                    @Override
                    public void accept(CertificationResultParams certificationResultParams) throws Exception {
                        saveIdentityCertification(certificationResultParams.getIdentityCertificationResult());
                        saveICompanyCertification(certificationResultParams.getCompanyCertificationResult());
                    }
                });
    }

    @Override
    public void onOpenChainCertificationOther() {
        InputIdentityAndCompanyParamsDialog.newInstance(new OnInputIdentityCompanyparamsListener() {
            @Override
            public void getIdentityCompanyParams(@NonNull String identityTxHash, @NonNull String companyTxHash, @NonNull String identityDid, @NonNull String companyDid) {
                ComponentUtils.launchChainCertificationOther(getActivity()
                        , identityTxHash
                        , companyTxHash
                        , identityDid
                        , companyDid);
            }
        }).show(getSupportFragmentManager(), InputIdentityAndCompanyParamsDialog.class.getName());

    }

    @Override
    public void onShowIdentityCertificationDialog() {
        ComponentUtils.showIdentityCertificationDialogNotForce(getActivity(), new UserCertByIDCardParams.Builder(did).build(), new Consumer<ChainResult>() {
            @Override
            public void accept(ChainResult chainResult) throws Exception {
                saveIdentityCertification(chainResult);
            }
        });
    }

    @Override
    public void onShowIdentityAndCompanyCertificationDialog() {
        ComponentUtils.showIdentityAndCompanyCertificationDialogNotForce(getActivity()
                , new UserCertByIDCardParams.Builder(did).build()
                , new CompanyCertParams.Builder(did, "").build()
                , new Consumer<CertificationResultParams>() {
                    @Override
                    public void accept(CertificationResultParams certificationResultParams) throws Exception {
                        saveIdentityCertification(certificationResultParams.getIdentityCertificationResult());
                        saveICompanyCertification(certificationResultParams.getCompanyCertificationResult());
                    }
                });
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
    public void onGoodsRecord() {
        String goodsId = DemoSp.getInstance().getString(DemoSp.SP_KEY_GOODS_ID);
        ComponentUtils.launchPropertyRightsTransferRecordsActivity(getActivity(), goodsId);
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

    private void saveIdentityCertification(ChainResult chainResult) {
        if (chainResult != null) {
            if (chainResult.getTxHash() != null && !"".equals(chainResult.getTxHash())) {
                DemoSp.getInstance().putString(DemoSp.SP_KEY_IDENTITY_TX_HASH, chainResult.getTxHash());
                DemoSp.getInstance().putString(DemoSp.SP_KEY_IDENTITY_DID, chainResult.getDid());
                ToastUtils.showToast("身份认证成功");
            } else {
                ToastUtils.showToast("身份认证失败");
            }
        }
    }

    private void saveICompanyCertification(ChainCertResult<CompanyCertInfoStoreItem> companyCertificationResult) {
        if (companyCertificationResult != null) {
            if (companyCertificationResult.getTxHash() != null && !"".equals(companyCertificationResult.getTxHash())) {
                DemoSp.getInstance().putString(DemoSp.SP_KEY_COMPANY_TX_HASH, companyCertificationResult.getTxHash());
                DemoSp.getInstance().putString(DemoSp.SP_KEY_COMPANY_DID, companyCertificationResult.getDid());
                ToastUtils.showToast("企业认证成功");
            } else {
                ToastUtils.showToast("企业认证失败");
            }
        }
    }

}
