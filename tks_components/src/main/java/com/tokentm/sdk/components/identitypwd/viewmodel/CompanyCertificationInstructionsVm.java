package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.tokentm.sdk.model.ChainInfoDTO;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明VM
 */
public class CompanyCertificationInstructionsVm extends XXFViewModel {

    /**
     * 是否显示认证说明顶部buju
     */
    public ObservableBoolean isShowTop = new ObservableBoolean();
    /**
     * 是否显示v标
     */
    public ObservableBoolean isShowV = new ObservableBoolean();
    /**
     * 上链状态
     */
    public ObservableBoolean chainState = new ObservableBoolean();
    /**
     * 上链状态描述
     */
    public ObservableField<String> chainStateDesc = new ObservableField<>();

    /**
     * 上链序号
     */
    public ObservableField<String> txHash = new ObservableField<>();

    /**
     * 块:
     */
    public ObservableField<String> block = new ObservableField<>();
    /**
     * 从:
     */
    public ObservableField<String> fromAddr = new ObservableField<>();
    /**
     * 至:
     */
    public ObservableField<String> toAddr = new ObservableField<>();
    /**
     * 时间戳:
     */
    public ObservableField<String> timesTamp = new ObservableField<>();

    /**
     * VM初始化
     *
     * @param application
     */
    public CompanyCertificationInstructionsVm(@NonNull Application application) {
        super(application);
        //进行上链状态监听,当上链状态变了后会调用下面的方法
        chainState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (chainState.get()) {
                    chainStateDesc.set("成功");
                }
            }
        });
        isShowTop.set(true);
    }

    /**
     * 请求认证说明接口数据
     */
    public void loadData(ProgressHUDProvider progressHUDProvider, String txHashString, String did) {
        io.reactivex.Observable.zip(
                TokenTmClient.getService(ChainService.class)
                        .getChainInfo(txHashString)
                , TokenTmClient.getService(CertService.class)
                        .isUserCert(did)
                , new BiFunction<ChainInfoDTO, Boolean, ChainInfoDTO>() {
                    @Override
                    public ChainInfoDTO apply(ChainInfoDTO chainInfoDTO, Boolean aBoolean) throws Exception {
                        //实名认证并且上链成功才显示v标
                        isShowV.set(aBoolean && chainInfoDTO.isSuccess());
                        return chainInfoDTO;
                    }
                })
                .compose(bindToLifecycle())
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(progressHUDProvider)))
                .subscribe(new Consumer<ChainInfoDTO>() {
                    @Override
                    public void accept(ChainInfoDTO chainInfoDTO) throws Exception {
                        //设置上链状态,失败则隐藏显示
                        chainState.set(chainInfoDTO.isSuccess());
                        fromAddr.set(chainInfoDTO.getFrom());
                        toAddr.set(chainInfoDTO.getTo());
                        txHash.set(chainInfoDTO.getHash());
                        timesTamp.set(chainInfoDTO.getTimestamp() + "");
                        block.set(chainInfoDTO.getBlockNumber());
                    }
                });
    }

}