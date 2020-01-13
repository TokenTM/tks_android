package com.tokentm.sdk.components.identitypwd.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityCertificationInstructionsBinding;
import com.tokentm.sdk.components.identitypwd.viewmodel.CompanyCertificationInstructionsVm;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.ChainInfoDTO;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明
 */
public class CertificationInstructionsActivity extends BaseTitleBarActivity {

    private static final String DID = "did";

    TksComponentsActivityCertificationInstructionsBinding binding;

    public static void launch(@NonNull Context context, @Nullable String did) {
        context.startActivity(getLauncher(context, did));
    }

    public static Intent getLauncher(Context context, String did) {
        Intent intent = new Intent(context, CertificationInstructionsActivity.class);
        intent.putExtra(DID, did);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityCertificationInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
    }

    protected void initData() {
        CompanyCertificationInstructionsVm viewModel = ViewModelProviders.of(this).get(CompanyCertificationInstructionsVm.class);
        binding.setViewModel(viewModel);
        String did = getIntent().getStringExtra(DID);
        loadData(did);
    }

    protected void initView() {
        setTitle("认证说明");
    }

    /**
     * 请求认证说明接口数据
     */
    @SuppressLint("CheckResult")
    public void loadData( String did) {
        TokenTmClient.getService(CertService.class)
                .getLatestUserCertStoreInfo(did)
                .flatMap(new Function<CertUserInfoStoreItem, ObservableSource<ChainInfoDTO>>() {
                    @Override
                    public ObservableSource<ChainInfoDTO> apply(CertUserInfoStoreItem certUserInfoStoreItem) throws Exception {
                        return TokenTmClient.getService(ChainService.class)
                                .getChainInfo(certUserInfoStoreItem.getTxHash())
                                .compose(XXF.bindToLifecycle(CertificationInstructionsActivity.this));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<ChainInfoDTO>() {
                    @Override
                    public void accept(ChainInfoDTO chainInfoDTO) throws Exception {
                        //实名认证并且上链成功才显示v标
                        binding.getViewModel().isShowV.set(chainInfoDTO.isSuccess() && chainInfoDTO.isConfirm());
                        //设置上链状态,失败则隐藏显示
                        binding.getViewModel().chainState.set(chainInfoDTO.isSuccess());
                        binding.getViewModel().fromAddr.set(chainInfoDTO.getFrom());
                        binding.getViewModel().toAddr.set(chainInfoDTO.getTo());
                        binding.getViewModel().txHash.set(chainInfoDTO.getHash());
                        binding.getViewModel().timesTamp.set(chainInfoDTO.getTimestamp() + "");
                        binding.getViewModel().block.set(chainInfoDTO.getBlockNumber());
                    }
                });
    }

}
