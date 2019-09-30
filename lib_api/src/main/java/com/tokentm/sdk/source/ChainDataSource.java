package com.tokentm.sdk.source;

import android.util.SparseArray;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface ChainDataSource extends RepoService {


    /**
     * 获取nonce
     *
     * @param address
     * @return
     */
    Observable<Long> getNonce(String address);


    /**
     * 生成DID
     *
     * @param identityPwd             身份密码
     * @param securityQuestionAnswers 身份密码安全问题与答案
     * @return
     */
    Observable<String> createDID(String identityPwd, SparseArray<String> securityQuestionAnswers);
}

