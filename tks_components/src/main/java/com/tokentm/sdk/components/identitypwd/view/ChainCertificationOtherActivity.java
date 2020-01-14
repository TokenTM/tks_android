package com.tokentm.sdk.components.identitypwd.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokentm.sdk.components.cert.DataChangeAdapterObserver;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityChainCertificationOtherBinding;
import com.tokentm.sdk.components.identitypwd.adapter.ChainServiceOtherAdapter;
import com.tokentm.sdk.components.identitypwd.model.ChainServiceOtherItem;
import com.tokentm.sdk.components.utils.TimeUtils;
import com.tokentm.sdk.model.ChainInfoDTO;
import com.tokentm.sdk.model.ChainedContractStoreInfoDTO;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;
import com.xxf.view.databinding.statelayout.IStateLayoutVM;
import com.xxf.view.databinding.statelayout.StateLayoutVM;
import com.xxf.view.loading.ViewState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.tokentm.sdk.components.identitypwd.adapter.ChainServiceOtherAdapter.VIEW_TYPE_CHAIN_DIVIDING_LINE;
import static com.tokentm.sdk.components.identitypwd.adapter.ChainServiceOtherAdapter.VIEW_TYPE_CHAIN_SERVICE_INFO;
import static com.tokentm.sdk.components.identitypwd.adapter.ChainServiceOtherAdapter.VIEW_TYPE_STATE;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证  别人查看此页面信息
 */
public class ChainCertificationOtherActivity extends BaseTitleBarActivity {

    private static final String KEY_O_TX_HASH = "o_tx_hash";
    private static final String KEY_C_TX_HASH = "c_tx_hash";
    private static final String KEY_O_DID = "o_did";
    private static final String KEY_C_DID = "c_did";

    private String oDid;
    private String oTxHash;
    private String cDid;
    private String cTxHash;
    private ChainServiceOtherAdapter chainServiceOtherAdapter;

    public static void launch(Context context, String oTxHash, String cTxHash, String oDid, String cDid) {
        context.startActivity(getLauncher(context, oTxHash, cTxHash, oDid, cDid));
    }

    private static Intent getLauncher(Context context, String oTxHash, String cTxHash, String oDid, String cDid) {
        return new Intent(context, ChainCertificationOtherActivity.class)
                .putExtra(KEY_O_TX_HASH, oTxHash)
                .putExtra(KEY_C_TX_HASH, cTxHash)
                .putExtra(KEY_O_DID, oDid)
                .putExtra(KEY_C_DID, cDid);
    }

    public IStateLayoutVM stateLayoutVM = new StateLayoutVM(new Action() {
        @Override
        public void run() throws Exception {
            loadData();
        }
    });


    TksComponentsActivityChainCertificationOtherBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityChainCertificationOtherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setStateLayoutVM(stateLayoutVM);
        initView();
        loadData();
    }


    private void initView() {
        setTitle("链信认证");
        oDid = getIntent().getStringExtra(KEY_O_DID);
        oTxHash = getIntent().getStringExtra(KEY_O_TX_HASH);
        cDid = getIntent().getStringExtra(KEY_C_DID);
        cTxHash = getIntent().getStringExtra(KEY_C_TX_HASH);
        chainServiceOtherAdapter = new ChainServiceOtherAdapter();
        binding.rvShowOtherChainService.setAdapter(chainServiceOtherAdapter);
        chainServiceOtherAdapter.registerAdapterDataObserver(new DataChangeAdapterObserver() {
            @Override
            protected void updateUI() {
                stateLayoutVM.setLayoutState(chainServiceOtherAdapter.getDataSize() > 0 ? ViewState.VIEW_STATE_CONTENT : ViewState.VIEW_STATE_EMPTY);
            }
        });
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
        List<ChainServiceOtherItem> chainItems = new ArrayList<>();
        List<ChainServiceOtherItem> certificationItems = new ArrayList<>();
        if (!TextUtils.isEmpty(oTxHash) && !TextUtils.isEmpty(cTxHash) && !TextUtils.isEmpty(oDid) && !TextUtils.isEmpty(cDid)) {
            //1. 判断身份上链状态
            TokenTmClient.getService(ChainService.class)
                    .getChainInfo(oTxHash)
                    .filter(new Predicate<ChainInfoDTO>() {
                        @Override
                        public boolean test(ChainInfoDTO chainInfoDTO) throws Exception {
                            boolean state = chainInfoDTO.isSuccess();
                            chainItems.clear();
                            if (state) {
                                chainItems.add(new ChainServiceOtherItem("上链状态：", "", VIEW_TYPE_STATE, true));
                                chainItems.add(new ChainServiceOtherItem("上链序号：", chainInfoDTO.getHash(), VIEW_TYPE_CHAIN_SERVICE_INFO, chainInfoDTO.isSuccess()));
                                chainItems.add(new ChainServiceOtherItem("区块高度：", chainInfoDTO.getBlockNumber(), VIEW_TYPE_CHAIN_SERVICE_INFO, chainInfoDTO.isSuccess()));
                                chainItems.add(new ChainServiceOtherItem("时间戳：", TimeUtils.formatUtc(chainInfoDTO.getTimestamp()), VIEW_TYPE_CHAIN_SERVICE_INFO, chainInfoDTO.isSuccess()));
                                chainItems.add(new ChainServiceOtherItem("10dp线：", "", VIEW_TYPE_CHAIN_DIVIDING_LINE, chainInfoDTO.isSuccess()));
                            } else {
                                chainItems.add(new ChainServiceOtherItem("上链状态：", "", VIEW_TYPE_STATE, false));
                                //如果失败就没有下面的认证信息
                                certificationItems.clear();
                            }
                            return state;
                        }
                    })
                    .flatMap(new Function<ChainInfoDTO, ObservableSource<ChainedContractStoreInfoDTO>>() {
                        @Override
                        public ObservableSource<ChainedContractStoreInfoDTO> apply(ChainInfoDTO chainInfoDTO) throws Exception {
                            return TokenTmClient.getService(CertService.class)
                                    .getUserCertContractInfo(oDid);
                        }
                    })
                    .filter(new Predicate<ChainedContractStoreInfoDTO>() {
                        @Override
                        public boolean test(ChainedContractStoreInfoDTO chainedContractStoreInfoDTO) throws Exception {
                            boolean active = chainedContractStoreInfoDTO.isActived();
                            if (active) {
                                certificationItems.add(new ChainServiceOtherItem("认证状态：", "", VIEW_TYPE_STATE, true));
                                certificationItems.add(new ChainServiceOtherItem("认证说明：", "链信认证将您提交的认证信息进行校验，认证结果加密上链存储。每项信息会生成数据指纹，方便第三方查询比对，确认可信性。", VIEW_TYPE_CHAIN_SERVICE_INFO, false));
                                certificationItems.add(new ChainServiceOtherItem("姓名：", chainedContractStoreInfoDTO.getClaimMap().get("name").getValue(), VIEW_TYPE_CHAIN_SERVICE_INFO, false));
                                certificationItems.add(new ChainServiceOtherItem("身份证号：", chainedContractStoreInfoDTO.getClaimMap().get("identityCode").getValue(), VIEW_TYPE_CHAIN_SERVICE_INFO, false));
                            } else {
                                certificationItems.add(new ChainServiceOtherItem("认证状态：", "", VIEW_TYPE_STATE, false));
                            }
                            return active;
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
                            boolean state = chainInfoDTO.isSuccess();
                            chainItems.clear();
                            if (state) {
                                chainItems.add(new ChainServiceOtherItem("上链状态：", "", VIEW_TYPE_STATE, true));
                                chainItems.add(new ChainServiceOtherItem("上链序号：", chainInfoDTO.getHash(), VIEW_TYPE_CHAIN_SERVICE_INFO, chainInfoDTO.isSuccess()));
                                chainItems.add(new ChainServiceOtherItem("区块高度：", chainInfoDTO.getBlockNumber(), VIEW_TYPE_CHAIN_SERVICE_INFO, chainInfoDTO.isSuccess()));
                                chainItems.add(new ChainServiceOtherItem("时间戳：", TimeUtils.formatUtc(chainInfoDTO.getTimestamp()), VIEW_TYPE_CHAIN_SERVICE_INFO, chainInfoDTO.isSuccess()));
                                chainItems.add(new ChainServiceOtherItem("10dp线：", "", VIEW_TYPE_CHAIN_DIVIDING_LINE, chainInfoDTO.isSuccess()));
                            } else {
                                chainItems.add(new ChainServiceOtherItem("上链状态：", "", VIEW_TYPE_STATE, false));
                                //如果失败就没有下面的认证信息
                                certificationItems.clear();
                            }
                            return state;
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
                            boolean active = chainedContractStoreInfoDTO.isActived();
                            if (active) {
                                certificationItems.add(new ChainServiceOtherItem("企业名称：", chainedContractStoreInfoDTO.getClaimMap().get("name").getValue(), VIEW_TYPE_CHAIN_SERVICE_INFO, false));
                                certificationItems.add(new ChainServiceOtherItem("统一社会信用代码：", chainedContractStoreInfoDTO.getClaimMap().get("creditCode").getValue(), VIEW_TYPE_CHAIN_SERVICE_INFO, false));
                            } else {
                                certificationItems.clear();
                                chainItems.add(new ChainServiceOtherItem("认证状态", "", VIEW_TYPE_STATE, false));
                            }
                            return active;
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
                            chainItems.addAll(certificationItems);
                            chainServiceOtherAdapter.bindData(true, chainItems);
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
}
