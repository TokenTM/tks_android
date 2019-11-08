package com.tokentm.sdk.components.identitypwd;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.model.ChainInfoDTO;
import com.tokentm.sdk.source.ChainService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明VM
 */
public class CompanyCertificationInstructionsVM extends XXFViewModel {

    /**
     * 上链状态
     */
    public ObservableInt chainState = new ObservableInt();
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
    public CompanyCertificationInstructionsVM(@NonNull Application application) {
        super(application);
        //进行上链状态监听,当上链状态变了后会调用下面的方法
        chainState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                switch (chainState.get()) {
                    case 1:
                        chainStateDesc.set("成功");
                        break;
                }
            }
        });
    }

    /**
     * 请求认证说明接口数据
     */
    public void loadData(ProgressHUDProvider progressHUDProvider) {
        TokenTmClient.getService(ChainService.class)
                .getChainInfo("1")
                .compose(bindToLifecycle())
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(progressHUDProvider)))
                .subscribe(new Consumer<ChainInfoDTO>() {
                    @Override
                    public void accept(ChainInfoDTO chainInfoDTO) throws Exception {
                        //设置上链状态
                        chainState.set(chainInfoDTO.getChainState());
                        fromAddr.set(chainInfoDTO.getFromAddr());
                        toAddr.set(chainInfoDTO.getToAddr());
                        txHash.set(chainInfoDTO.getTxHash());
                        timesTamp.set(chainInfoDTO.getTimestamp());
                        block.set(chainInfoDTO.getBlock());
                    }
                });
    }
}
