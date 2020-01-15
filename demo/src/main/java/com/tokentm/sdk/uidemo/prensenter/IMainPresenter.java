package com.tokentm.sdk.uidemo.prensenter;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 首页P
 */
public interface IMainPresenter {

    /**
     * 身份认证
     */
    void onIdentityCertification();
 /**
     * 企业认证
     */
    void onCompanyCertification();

    /**
     * 身份说明
     */
    void onIdentityDescription();

    /**
     * 开启链信认证页面
     */
    void onOpenChainCertification();

    /**
     * 开启链信认证页面 别人查看
     */
    void onOpenChainCertificationOther();

    /**
     * 开启身份认证弹窗(强制)
     */
    void onShowIdentityCertificationDialog();

    /**
     * 身份和企业认证弹窗
     */
    void onShowIdentityAndCompanyCertificationDialog();

    /**
     * 发货
     */
    void onDeliverGoods();

    /**
     * 收货
     */
    void onReceiveGoods();

    /**
     * 物权转移记录
     */
    void onGoodsRecord();

    /**
     * 发证
     */
    void onCertification();

    /**
     * 确认证
     */
    void onConfirmCertificate();

    /**
     * 取消证书
     */
    void onDisabledCertificate();

    /**
     * 备份
     */
    void onBackup();

    /**
     * 获取备份
     */
    void onGetBackup();

    /**
     * 退出登录
     */
    void onLogout();

}

