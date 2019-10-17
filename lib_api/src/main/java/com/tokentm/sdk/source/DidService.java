package com.tokentm.sdk.source;

import com.tokentm.sdk.model.BackupPwdSecurityQuestionDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;

import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface DidService extends RepoService {

    /**
     * 获取身份密码安全问题模版
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
    Observable<String> createDID(String identityPwd, LinkedHashMap<Long, String> securityQuestionAnswers);


    /**
     * 获取设置的身份密码与安全稳日
     *
     * @param DID
     * @return
     */
    Observable<BackupPwdSecurityQuestionDTO> getPwdSecurityQuestions(String DID);

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
     * @param oldIdentityPwd
     * @return
     */
    Observable<Boolean> decrypt(String DID, String oldIdentityPwd);


    /**
     * 解密身份密码
     *
     * @param DID                     did 链上唯一标识
     * @param oldSecurityQuestionAnswers 身份密码安全问题与答案
     * @return
     */
    Observable<Boolean> decrypt(String DID, LinkedHashMap<Long, String> oldSecurityQuestionAnswers);


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
    Observable<Boolean> reset(String DID, LinkedHashMap<Long, String> oldSecurityQuestionAnswers, LinkedHashMap<Long, String> newSecurityQuestionAnswers);


    /**
     * 重置身份密码,通过旧的安全问题
     *
     * @param DID
     * @param oldSecurityQuestionAnswers
     * @param newIdentityPwd
     * @return
     */
    Observable<Boolean> reset(String DID, LinkedHashMap<Long, String> oldSecurityQuestionAnswers, String newIdentityPwd);



    /**
     * 重置身份密码安全问题,通过旧的安全问题
     *
     * @param DID
     * @param oldSecurityQuestionAnswers
     * @param newSecurityQuestionAnswers
     * @return
     */
    Observable<Boolean> reset(String DID, LinkedHashMap<Long, String> oldSecurityQuestionAnswers, LinkedHashMap<Long, String> newSecurityQuestionAnswers,String newIdentityPwd);
}
