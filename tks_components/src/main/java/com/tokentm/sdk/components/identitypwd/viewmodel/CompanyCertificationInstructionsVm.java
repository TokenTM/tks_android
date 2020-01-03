package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明VM
 */
public class CompanyCertificationInstructionsVm extends XXFViewModel {

    /**
     * 是否显示认证说明顶部buju
     */
    public ObservableBoolean isShowTop = new ObservableBoolean(true);
    /**
     * 是否显示v标
     */
    public ObservableBoolean isShowV = new ObservableBoolean(true);
    /**
     * 上链状态
     */
    public ObservableBoolean chainState = new ObservableBoolean(true);
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
                } else {
                    chainStateDesc.set("失败");
                }
            }
        });
        isShowTop.set(true);
    }

}
