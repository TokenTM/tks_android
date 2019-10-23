package com.tokentm.sdk.source;

import java.io.InputStream;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface BasicService extends RepoService {


    /**
     * 获取公函模版
     *
     * @param did
     * @param orgName
     * @param userIdentityCode
     * @param userName
     * @return
     */
    Observable<InputStream> getOrgLetterImage(String did,
                                              String orgName,
                                              String userIdentityCode,
                                              String userName
    );


    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    Observable<Boolean> sendSmsCode(String phone);
}
