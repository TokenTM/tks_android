package com.tokentm.sdk.source;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface DidService extends RepoService {


    /**
     * 生成did
     *
     * @param phone       手机号
     * @param smsCode     验证码
     * @param identityPwd 身份密码
     * @return
     */
    Observable<String> createUDID(String phone, String smsCode, String identityPwd);


    /**
     * 解开UDID
     *
     * @param phone
     * @param smsCode
     * @return
     */
    Observable<Boolean> decryptUDID(String phone, String smsCode);


    /**
     * 是否可以使用,否则需要解开
     *
     * @param uDid
     * @return
     */
    Observable<Boolean> isAccessible(String uDid);


    /**
     * 重置身份密码,通过手机号+验证码
     *
     * @param uDID
     * @param oldPhone
     * @param smsCode
     * @param newIdentityPwd
     * @return
     */
    Observable<Boolean> resetIdentityPwd(String uDID, String oldPhone, String smsCode, String newIdentityPwd);

    /**
     * 校验pwd是否正确
     *
     * @param uDID
     * @param identityPwd
     * @return
     */
    Observable<Boolean> validateIdentityPwd(String uDID, String identityPwd);


}

