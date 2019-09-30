package com.tokentm.sdk.source;

import android.util.SparseArray;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface DidDataSource extends RepoService {

    /**
     * 生成DID
     *
     * @param identityPwd             身份密码
     * @param securityQuestionAnswers 身份密码安全问题与答案
     * @return did 链上唯一标识
     */
    Observable<String> createDID(String identityPwd, SparseArray<String> securityQuestionAnswers);


    /**
     * 解密身份密码
     *
     * @param DID         did 链上唯一标识
     * @param identityPwd
     * @return
     */
    Observable<Boolean> decrypt(String DID, String identityPwd);


    /**
     * 解密身份密码
     *
     * @param DID                     did 链上唯一标识
     * @param securityQuestionAnswers 身份密码安全问题与答案
     * @return
     */
    Observable<Boolean> decrypt(String DID, SparseArray<String> securityQuestionAnswers);


    /**
     * 重置身份密码,通过旧密码
     *
     * @param DID
     * @param oldIdentityPwd
     * @param newIdentityPwd
     * @return
     */
    Observable<Boolean> reset(String DID, String oldIdentityPwd, String newIdentityPwd);

    /**
     * 重置身份密码安全问题,通过旧的安全问题
     *
     * @param DID
     * @param oldSecurityQuestionAnswers
     * @param newSecurityQuestionAnswers
     * @return
     */
    Observable<Boolean> reset(String DID, SparseArray<String> oldSecurityQuestionAnswers, SparseArray<String> newSecurityQuestionAnswers);


    /**
     * 重置身份密码,通过旧的安全问题
     *
     * @param DID
     * @param oldSecurityQuestionAnswers
     * @param newIdentityPwd
     * @return
     */
    Observable<Boolean> reset(String DID, SparseArray<String> oldSecurityQuestionAnswers, String newIdentityPwd);
}

