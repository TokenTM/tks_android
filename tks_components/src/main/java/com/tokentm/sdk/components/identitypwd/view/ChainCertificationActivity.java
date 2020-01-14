package com.tokentm.sdk.components.identitypwd.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.R;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityChainCertificationBinding;
import com.tokentm.sdk.components.identitypwd.adapter.ChainServiceAdapter;
import com.tokentm.sdk.components.identitypwd.model.ChainServiceModel;
import com.tokentm.sdk.components.identitypwd.presenter.IChainCertificationPresenter;
import com.tokentm.sdk.components.identitypwd.viewmodel.ChainCertificationVm;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.components.utils.TimeUtils;
import com.tokentm.sdk.model.ChainInfoDTO;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.ChainedContractStoreInfoDTO;
import com.tokentm.sdk.model.CompanyType;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.view.databinding.statelayout.IStateLayoutVM;
import com.xxf.view.databinding.statelayout.StateLayoutVM;
import com.xxf.view.loading.ViewState;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.tokentm.sdk.components.identitypwd.model.ChainServiceType.COMPANY_AUTHENTICATION;
import static com.tokentm.sdk.components.identitypwd.model.ChainServiceType.IDENTITY_AUTHENTICATION;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class ChainCertificationActivity extends BaseTitleBarActivity implements IChainCertificationPresenter {

    private static final String KEY_U_TX_HASH = "u_tx_hash";
    private static final String KEY_C_TX_HASH = "c_tx_hash";
    private static final String KEY_U_DID = "u_did";
    private static final String KEY_C_DID = "c_did";

    private String uDid;
    private String uTxHash;
    private String cDid;
    private String cTxHash;
    private ChainCertificationVm viewModel;

    private static void launch(Context context, @NonNull String uTxHash, @NonNull String cTxHash, @NonNull String uDid, @NonNull String cDid) {
        context.startActivity(getLauncher(context, uTxHash, cTxHash, uDid, cDid));
    }

    public static Intent getLauncher(Context context, String uTxHash, String cTxHash, String uDid, String cDid) {
        return new Intent(context, ChainCertificationActivity.class)
                .putExtra(KEY_U_TX_HASH, uTxHash)
                .putExtra(KEY_C_TX_HASH, cTxHash)
                .putExtra(KEY_U_DID, uDid)
                .putExtra(KEY_C_DID, cDid);
    }

    public IStateLayoutVM stateLayoutVM = new StateLayoutVM(new Action() {
        @Override
        public void run() throws Exception {
            loadData();
        }
    });


    TksComponentsActivityChainCertificationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityChainCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setPresenter(this);
        binding.setStateLayoutVM(stateLayoutVM);
        viewModel = new ChainCertificationVm(getApplication());
        binding.setViewModel(viewModel);
        initView();
        loadData();
    }


    private void initView() {
        uDid = getIntent().getStringExtra(KEY_U_DID);
        uTxHash = getIntent().getStringExtra(KEY_U_TX_HASH);
        cDid = getIntent().getStringExtra(KEY_C_DID);
        cTxHash = getIntent().getStringExtra(KEY_C_TX_HASH);
        getTitleBar().setTitleBarTitle("链信认证")
                .setTitleBarRightText("更多", new io.reactivex.functions.Action() {
                    @Override
                    public void run() {
                        ChainServiceMoreActivity.launch(getActivity(),uDid);
                    }
                });
        ChainServiceAdapter chainServiceAdapter = new ChainServiceAdapter();
        List<ChainServiceModel> chainServiceModelList = new ArrayList<>();
        chainServiceModelList.add(new ChainServiceModel(R.mipmap.tks_components_identity_authentication, "身份认证", IDENTITY_AUTHENTICATION));
        chainServiceModelList.add(new ChainServiceModel(R.mipmap.tks_components_company_authentication, "企业认证", COMPANY_AUTHENTICATION));
        chainServiceAdapter.bindData(true, chainServiceModelList);
        chainServiceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                ChainServiceModel chainServiceModel = (ChainServiceModel) adapter.getData().get(index);
                switch (chainServiceModel.getChainServiceType()) {
                    case IDENTITY_AUTHENTICATION:
                        //如果
                        if (viewModel.identityCertificationState.get()) {
                            viewModel.showChainServiceInfoView.set(true);
                            viewModel.showChainServiceInfo.set("您已完成身份认证");
                        } else {
                            viewModel.showChainServiceInfoView.set(false);
                            //跳转到身份认证
                            ComponentUtils.launchUserCertActivity(getActivity(),
                                    new UserCertByIDCardParams
                                            .Builder(uDid)
                                            .build()
                                    , new Consumer<ChainResult>() {
                                        @Override
                                        public void accept(ChainResult chainResult) throws Exception {
                                            //将结果返回
                                            setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, chainResult));
                                            finish();
                                        }
                                    });
                        }
                        break;
                    case COMPANY_AUTHENTICATION:
                        //如果
                        if (viewModel.companyCertificationState.get()) {
                            viewModel.showChainServiceInfoView.set(true);
                            viewModel.showChainServiceInfo.set("您已完成企业认证");
                        } else {
                            viewModel.showChainServiceInfoView.set(false);
                            //跳转到企业码认证
                            ComponentUtils.launchCompanyCertActivity(getActivity()
                                    , new CompanyCertParams
                                            .Builder(uDid, "")
                                            .setCompanyType(CompanyType.TYPE_COMPANY)
                                            .build()
                                    , new Consumer<ChainResult>() {
                                        @Override
                                        public void accept(ChainResult chainResult) throws Exception {
                                            setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, chainResult));
                                            finish();
                                        }
                                    });
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvChainService.setAdapter(chainServiceAdapter);
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        getChainInfo();
    }

    /**
     * 获取企业链信息
     */
    @SuppressLint("CheckResult")
    private void getChainInfo() {
        if (!TextUtils.isEmpty(uTxHash) && !TextUtils.isEmpty(cTxHash) && !TextUtils.isEmpty(uDid) && !TextUtils.isEmpty(cDid)) {
            //1. 判断身份上链状态
            TokenTmClient.getService(ChainService.class)
                    .getChainInfo(uTxHash)
                    .filter(new Predicate<ChainInfoDTO>() {
                        @Override
                        public boolean test(ChainInfoDTO chainInfoDTO) throws Exception {
                            viewModel.txHash.set(chainInfoDTO.getHash());
                            viewModel.blockNumber.set(chainInfoDTO.getBlockNumber());
                            viewModel.timesTamp.set(TimeUtils.formatUtc(chainInfoDTO.getTimestamp()));
                            viewModel.chainIdentityState.set(chainInfoDTO.isSuccess());
                            //记录身份认证上链失败
                            viewModel.chainFailInfo.set(ChainCertificationVm.IDENTITY);
                            return viewModel.chainIdentityState.get();
                        }
                    })
                    .flatMap(new Function<ChainInfoDTO, ObservableSource<ChainedContractStoreInfoDTO>>() {
                        @Override
                        public ObservableSource<ChainedContractStoreInfoDTO> apply(ChainInfoDTO chainInfoDTO) throws Exception {
                            return TokenTmClient.getService(CertService.class)
                                    .getUserCertContractInfo(uDid);
                        }
                    })
                    .filter(new Predicate<ChainedContractStoreInfoDTO>() {
                        @Override
                        public boolean test(ChainedContractStoreInfoDTO chainedContractStoreInfoDTO) throws Exception {
                            viewModel.identityCertificationState.set(chainedContractStoreInfoDTO.isActived());
                            //身份认证成功
                            if (viewModel.identityCertificationState.get()) {
                                viewModel.identityName.set(chainedContractStoreInfoDTO.getClaimMap().get("name").getValue());
                                viewModel.identityCode.set(chainedContractStoreInfoDTO.getClaimMap().get("identityCode").getValue());
                            } else {
                                //认证失败,需要再次认证,此处记录需要认证的是哪个
                                viewModel.certificationFailInfo.set(ChainCertificationVm.IDENTITY);
                                viewModel.certificationFailDesc.set("您的身份认证未通过审核，请再次认证");
                            }
                            return viewModel.identityCertificationState.get();
                        }
                    })
                    .flatMap(new Function<ChainedContractStoreInfoDTO, ObservableSource<ChainInfoDTO>>() {
                        @Override
                        public ObservableSource<ChainInfoDTO> apply(ChainedContractStoreInfoDTO chainedContractStoreInfoDTO) throws Exception {
                            //请求企业上链信息
                            return TokenTmClient.getService(ChainService.class)
                                    .getChainInfo(cTxHash);
                        }
                    })
                    .filter(new Predicate<ChainInfoDTO>() {
                        @Override
                        public boolean test(ChainInfoDTO chainInfoDTO) throws Exception {
                            viewModel.txHash.set(chainInfoDTO.getHash());
                            viewModel.blockNumber.set(chainInfoDTO.getBlockNumber());
                            viewModel.timesTamp.set(TimeUtils.formatUtc(chainInfoDTO.getTimestamp()));
                            viewModel.chainCompanyState.set(chainInfoDTO.isSuccess());
                            //记录企业认证上链失败
                            viewModel.chainFailInfo.set(ChainCertificationVm.COMPANY);
                            return viewModel.chainCompanyState.get();
                        }
                    })
                    .flatMap(new Function<ChainInfoDTO, ObservableSource<ChainedContractStoreInfoDTO>>() {
                        @Override
                        public ObservableSource<ChainedContractStoreInfoDTO> apply(ChainInfoDTO chainInfoDTO) throws Exception {
                            return TokenTmClient.getService(CertService.class)
                                    .getCompanyCertContractInfo(cDid);
                        }
                    })
                    .filter(new Predicate<ChainedContractStoreInfoDTO>() {
                        @Override
                        public boolean test(ChainedContractStoreInfoDTO chainedContractStoreInfoDTO) throws Exception {
                            //企业认证详情
                            viewModel.companyCertificationState.set(chainedContractStoreInfoDTO.isActived());
                            if (viewModel.companyCertificationState.get()) {
                                viewModel.companyName.set(chainedContractStoreInfoDTO.getClaimMap().get("name").getValue());
                                viewModel.companyCode.set(chainedContractStoreInfoDTO.getClaimMap().get("creditCode").getValue());
                            } else {
                                //认证失败的是企业,需要重试,此处记录需要重试的是哪个
                                viewModel.certificationFailInfo.set(ChainCertificationVm.COMPANY);
                                viewModel.certificationFailDesc.set("您的企业认证未通过审核，请再次认证");
                            }
                            return viewModel.companyCertificationState.get();
                        }
                    })
                    .compose(XXF.bindToLifecycle(this))
                    .compose(new UILifeTransformerImpl<ChainedContractStoreInfoDTO>() {
                        @Override
                        public void onSubscribe() {
                            stateLayoutVM.setLayoutState(ViewState.VIEW_STATE_LOADING);
                        }

                        @Override
                        public void onNext(ChainedContractStoreInfoDTO chainedContractStoreInfoDTO) {

                        }

                        @Override
                        public void onComplete() {
                            stateLayoutVM.setLayoutState(ViewState.VIEW_STATE_CONTENT);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            stateLayoutVM.setLayoutState(ViewState.VIEW_STATE_ERROR);
                        }

                        @Override
                        public void onCancel() {

                        }
                    })
                    .subscribe();
        } else {
            //数据为空
            stateLayoutVM.setLayoutState(ViewState.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void clickChainInfo() {
        viewModel.showLoadMoreChainInfoView.set(!viewModel.showLoadMoreChainInfoView.get());
    }

    @Override
    public void clickCertificationInfo() {
        viewModel.showLoadMoreCertificationInfoView.set(!viewModel.showLoadMoreCertificationInfoView.get());
    }

    @Override
    public void retryChain() {
        ComponentUtils.showIdentityPwdDialog(getActivity(), uDid, new BiConsumer<DialogInterface, String>() {
            @Override
            public void accept(DialogInterface dialogInterface, String s) throws Exception {
                switch (viewModel.chainFailInfo.get()) {
                    case ChainCertificationVm.IDENTITY:
                        TokenTmClient.getService(CertService.class)
                                .reUserCert(uDid, s, uTxHash)
                                .compose(XXF.bindToLifecycle(getActivity()))
                                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ChainCertificationActivity.this)))
                                .subscribe(new Consumer<ChainResult>() {
                                    @Override
                                    public void accept(ChainResult chainResult) throws Exception {
                                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, chainResult));
                                        finish();
                                    }
                                });
                        break;
                    case ChainCertificationVm.COMPANY:
                        TokenTmClient.getService(CertService.class)
                                .reCompanyCert(uDid, s, cTxHash)
                                .compose(XXF.bindToLifecycle(getActivity()))
                                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(ChainCertificationActivity.this)))
                                .subscribe(new Consumer<ChainResult>() {
                                    @Override
                                    public void accept(ChainResult chainResult) throws Exception {
                                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, chainResult));
                                        finish();
                                    }
                                });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void retryCertification() {
        switch (viewModel.chainFailInfo.get()) {
            case ChainCertificationVm.IDENTITY:
                ComponentUtils.launchUserCertActivity(getActivity(), new UserCertByIDCardParams.Builder(uDid)
                                .build()
                        , new Consumer<ChainResult>() {
                            @Override
                            public void accept(ChainResult chainResult) throws Exception {
                                setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, chainResult));
                                finish();
                            }
                        });
                break;
            case ChainCertificationVm.COMPANY:
                ComponentUtils.launchCompanyCertActivity(getActivity(), new CompanyCertParams.Builder(uDid, "")
                                .setCompanyType(CompanyType.TYPE_COMPANY)
                                .build()
                        , new Consumer<ChainResult>() {
                            @Override
                            public void accept(ChainResult chainResult) throws Exception {
                                setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, chainResult));
                                finish();
                            }
                        });
                break;
            default:
                break;
        }
    }
}
