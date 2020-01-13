package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.xxf.arch.viewmodel.XXFViewModel;

import java.util.Objects;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证
 */
public class ChainCertificationVm extends XXFViewModel {

    /**
     * 身份
     */
    public static final String IDENTITY = "identity";
    /**
     * 公司
     */
    public static final String COMPANY = "company";

    /**
     * --------------上链相关
     */
    /**
     * 身份认证上链状态  true:上链成功 false:上链失败
     */
    public ObservableBoolean chainIdentityState = new ObservableBoolean();
    /**
     * 公司认证上链状态  true:上链成功 false:上链失败
     */
    public ObservableBoolean chainCompanyState = new ObservableBoolean();
    /**
     * 记录上链失败的是身份/公司
     */
    public ObservableField<String> chainFailInfo = new ObservableField<>();


    /**
     * ---------------------认证相关
     */
    /**
     * 身份认证状态  true:认证成功 false:认证失败
     */
    public ObservableBoolean identityCertificationState = new ObservableBoolean();
    /**
     * 公司认证状态  true:认证成功 false:认证失败
     */
    public ObservableBoolean companyCertificationState = new ObservableBoolean();


    /**
     * 记录认证失败是个人/企业
     */
    public ObservableField<String> certificationFailInfo = new ObservableField<>();

    /**
     * 认证失败描述
     */
    public ObservableField<String> certificationFailDesc = new ObservableField<>();

    /**
     * 上链信息是否显示:加载更多  true:显示更多 false:隐藏更多/显示收起箭头
     */
    public ObservableBoolean showLoadMoreChainInfoView = new ObservableBoolean();

    /**
     * 认证信息是否显示:加载更多  true:显示更多 false:隐藏更多/显示收起箭头
     */
    public ObservableBoolean showLoadMoreCertificationInfoView = new ObservableBoolean();

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
     * 姓名
     */
    public ObservableField<String> identityName = new ObservableField<>("");
    /**
     * 是否显示身份认证姓名和身份证
     */
    public ObservableBoolean showIdentityName = new ObservableBoolean();
    /**
     * 身份证号
     */
    public ObservableField<String> identityCode = new ObservableField<>();
    /**
     * 是否显示公司名称和信用代码
     */
    public ObservableBoolean showCompanyName = new ObservableBoolean();

    /**
     * 公司名称
     */
    public ObservableField<String> companyName = new ObservableField<>("");
    /**
     * 公司信用代码
     */
    public ObservableField<String> companyCode = new ObservableField<>();

    /**
     * 显示或者隐藏身份认证后提示文本
     */
    public ObservableBoolean showChainServiceInfoView = new ObservableBoolean();
    /**
     * 当已经完成身份认证后提示文本
     */
    public ObservableField<String> showChainServiceInfo = new ObservableField<>();

    public ChainCertificationVm(@NonNull Application application) {
        super(application);
        chainIdentityState.set(true);
        chainCompanyState.set(true);

        //默认显示加载更多布局
        showLoadMoreChainInfoView.set(true);
        showLoadMoreCertificationInfoView.set(true);
        //当没有身份认证信息的时候隐藏身份姓名和身份证号
        identityName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showIdentityName.set(!"".equals(Objects.requireNonNull(identityName.get()).trim()));
            }
        });
        //当没有企业信息的时候隐藏名称和信用代码
        companyName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showCompanyName.set(!"".equals(Objects.requireNonNull(companyName.get()).trim()));
            }
        });
        identityName.notifyChange();
        companyName.notifyChange();
    }
}
