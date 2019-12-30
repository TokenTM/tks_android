package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.tokentm.sdk.model.ChainInfoDTO;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.widget.progresshud.ProgressHUDProvider;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明VM
 */
public class CompanyCertificationDetailsVm extends CompanyCertificationInstructionsVm {

    /**
     * VM初始化
     *
     * @param application
     */
    public CompanyCertificationDetailsVm(@NonNull Application application) {
        super(application);
        isShowTop.set(false);
    }

    /**
     * 请求认证说明接口数据
     */
    public void loadData(ProgressHUDProvider progressHUDProvider, String txHashString) {
        TokenTmClient.getService(ChainService.class)
                .getChainInfo(txHashString)
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
