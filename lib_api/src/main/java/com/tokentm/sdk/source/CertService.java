package com.tokentm.sdk.source;

import com.tokentm.sdk.model.CertRecord;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.model.CompanyType;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 认证服务
 */
public interface CertService extends RepoService {

    /**
     * 用户 身份证认证
     *
     * @param uDID
     * @param identityPwd     身份密码
     * @param userName        姓名
     * @param IDCard          身份证号
     * @param identityFontImg 身份证正面照片
     * @param identityBackImg 身份证背面照片
     * @param identityHandImg 身份证手持照片
     * @return 返回交易txHash
     */
    Observable<String> userCertByIDCard(String uDID,
                                        String identityPwd,
                                        String userName,
                                        String IDCard,
                                        File identityFontImg,
                                        File identityBackImg,
                                        File identityHandImg
    );

    /**
     * 是否完成了实名认证
     *
     * @param uDID
     * @return
     */
    Observable<Boolean> isUserCert(String uDID);

    /**
     * 获取身份证认证信息
     *
     * @param uDID
     * @return
     */
    Observable<CertUserInfoStoreItem> getUserCertByIDCardInfo(String uDID);

    /**
     * 公司认证
     *
     * @param uDID
     * @param identityPwd       身份密码
     * @param companyName       公司名字
     * @param companyType       公司类型
     * @param companyCreditCode 公司社会信用代码
     * @param licenseImg        公函/营业执照
     * @return
     */
    Observable<CompanyCertResult> companyCert(String uDID,
                                              String identityPwd,
                                              String companyName,
                                              CompanyType companyType,
                                              String companyCreditCode,
                                              File licenseImg
    );

    /**
     * 是否完成了公司认证
     *
     * @param cDID
     * @return
     */
    Observable<Boolean> isCompanyCert(String cDID);

    /**
     * 获取认证记录
     *
     * @param DIDs
     * @return
     */
    Observable<List<CertRecord>> getCertRecords(List<String> DIDs);

}
