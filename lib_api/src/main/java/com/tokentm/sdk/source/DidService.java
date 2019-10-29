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
     * 重置身份密码,通过手机号+验证码
     *
     * @param uDID
     * @param oldPhone
     * @param smsCode
     * @param newIdentityPwd
     * @return
     */
    Observable<Boolean> resetPwd(String uDID, String oldPhone, String smsCode, String newIdentityPwd);

    /**
     * 校验pwd是否正确
     *
     * @param uDID
     * @param pwd
     * @return
     */
    Observable<Boolean> validatePwd(String uDID, String pwd);

}

