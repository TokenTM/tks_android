package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class GoodsTransferRecordsDialogVm extends XXFViewModel {

    /**
     * --------------上链相关
     */
    /**
     * 身份认证上链状态  true:上链成功 false:上链失败
     */
    public ObservableBoolean chainIdentityState = new ObservableBoolean();

    /**
     * 上链序号 txHash
     */
    public ObservableField<String> txHash = new ObservableField<>();

    /**
     * 区块高度
     */
    public ObservableField<String> blockNumber = new ObservableField<>();
    /**
     * 时间戳
     */
    public ObservableField<String> timesTamp = new ObservableField<>();

    /**
     * 商品名称
     */
    public ObservableField<String> goodsName = new ObservableField<>("");

    /**
     * 交易数量
     */
    public ObservableField<String> goodsNumber = new ObservableField<>("");

    public GoodsTransferRecordsDialogVm(@NonNull Application application) {
        super(application);
        chainIdentityState.set(true);
    }
}
