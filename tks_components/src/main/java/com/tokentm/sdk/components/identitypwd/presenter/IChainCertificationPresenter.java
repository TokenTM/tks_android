package com.tokentm.sdk.components.identitypwd.presenter;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 链信认证p
 */
public interface IChainCertificationPresenter {

    /**
     * 上链信息的加载更多
     */
    void clickChainInfo();

    /**
     * 认证信息的加载更多
     */
    void clickCertificationInfo();

    /**
     * 重新上链
     */
    void retryChain();

    /**
     * 重新认证
     */
    void retryCertification();
}
