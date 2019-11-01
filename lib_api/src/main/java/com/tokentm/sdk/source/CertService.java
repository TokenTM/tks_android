package com.tokentm.sdk.source;

import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.model.CompanyType;

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
     * @return 返回交易txHash
     */
    Observable<String> userCertByIDCard(String uDid,
                                        String identityPwd,
                                        String userName,
                                        String IDCard,
                                        File identityFontImg,
                                        File identityBackImg,
                                        File identityHandImg
    );


    /**
     * 获取认证信息
     *
     * @param uDid
     * @return
     */
    Observable<CertUserInfoStoreItem> getUserCertByIDCardInfo(String uDid);

    /**
     * 公司认证
     *
     * @param uDid
     * @param identityPwd
     * @param companyName
     * @param companyType
     * @param companyCreditCode
     * @param licenseImg
     * @return
     */
    Observable<CompanyCertResult> companyCert(String uDid,
                                              String identityPwd,
                                              String companyName,
                                              CompanyType companyType,
                                              String companyCreditCode,
                                              File licenseImg
    );

}
