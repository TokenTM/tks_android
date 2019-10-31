package com.tokentm.sdk.source;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 认证服务
 */
public interface CertService extends RepoService {

    /**
     * 用户 身份证认证
     *
     * @param uDid
     * @param identityPwd     身份密码
     * @param userName        姓名
     * @param IDCard          身份证号
     * @param identityFontImg 身份证正面照片
     * @param identityBackImg 身份证背面照片
     * @param identityHandImg 身份证手持照片
     * @return
     */
    Observable<String> userCertByIDCard(String uDid,
                                        String identityPwd,
                                        String userName,
                                        String IDCard,
                                        File identityFontImg,
                                        File identityBackImg,
                                        File identityHandImg
    );
}
