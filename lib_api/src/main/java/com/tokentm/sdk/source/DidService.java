package com.tokentm.sdk.source;

import com.tokentm.sdk.model.SecurityQuestionDTO;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface DidService extends RepoService {

    /**
     * 获取身份密码模版
     *
     * @return
     */
    Observable<List<SecurityQuestionDTO>> getSecurityQuestionTemplate();

    /**
     * 生成DID
     *
     * @param identityPwd             身份密码
     * @param securityQuestionAnswers 身份密码安全问题id与答案 key==id,value==答案
     * @return 返回did
     */
    Observable<String> createDID(String identityPwd, Map<Long, String> securityQuestionAnswers);


    /**
     * 获取创建did设置的安全问题
     *
     * @param DID
     * @return
     */
    Observable<List<SecurityQuestionDTO>> getSecurityQuestions(String DID);

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
    Observable<Boolean> decrypt(String DID, Map<Long, String> securityQuestionAnswers);


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
    Observable<Boolean> reset(String DID, Map<Long, String> oldSecurityQuestionAnswers, Map<Long, String> newSecurityQuestionAnswers);


    /**
     * 重置身份密码,通过旧的安全问题
     *
     * @param DID
     * @param oldSecurityQuestionAnswers
     * @param newIdentityPwd
     * @return
     */
    Observable<Boolean> reset(String DID, Map<Long, String> oldSecurityQuestionAnswers, String newIdentityPwd);
}

