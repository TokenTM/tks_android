package com.tokentm.sdk.source;

import android.databinding.ObservableField;

import com.google.gson.JsonElement;
import com.tokentm.sdk.Config;
import com.tokentm.sdk.api.CertApiServce;
import com.tokentm.sdk.api.CertProxyApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.CompanyCertProxyReqBodyDTO;
import com.tokentm.sdk.model.CompanyCertReqBodyDTO;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.model.CompanyType;
import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.UserCertByIdCardProxyReqBodyDTO;
import com.tokentm.sdk.model.UserCertByIdCardReqBodyDTO;
import com.tokentm.sdk.wallet.WalletResult;
import com.xxf.arch.XXF;
import com.xxf.arch.json.JsonUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import sm_crypto.Sm_crypto;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class CertRepositoryImpl implements CertService, BaseRepo {
    private static volatile CertService INSTANCE;

    public static CertService getInstance() {
        if (INSTANCE == null) {
            synchronized (CertRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CertRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private Observable<NodeServiceItem> getRandomNodeService(NodeServiceType nodeServiceType) {
        return NodeRepositoryImpl
                .getInstance()
                .getNodeService(nodeServiceType)
                .map(new Function<List<NodeServiceItem>, NodeServiceItem>() {
                    @Override
                    public NodeServiceItem apply(List<NodeServiceItem> nodeServiceItems) throws Exception {
                        //随机
                        return nodeServiceItems.get(new Random().nextInt(nodeServiceItems.size()));
                    }
                });
    }

    private Observable<WalletResult> getUserWalletFile(String uDid, String identityPwd) {
        return IdentityPwdRepositoryImpl
                .getInstance()
                .openUserWallet(uDid, identityPwd);
    }

    @Override
    public Observable<String> userCertByIDCard(String uDID,
                                               String identityPwd,
                                               String userName,
                                               String IDCard,
                                               File identityFontImg,
                                               File identityBackImg,
                                               File identityHandImg) {
        final ObservableField<String> targetDid = new ObservableField<>();
        return getRandomNodeService(NodeServiceType.TYPE_USER_AUTHENTICATE)
                .onErrorReturnItem(new NodeServiceItem())
                .flatMap(new Function<NodeServiceItem, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(NodeServiceItem nodeServiceItem) throws Exception {
                        targetDid.set(nodeServiceItem.getDid());

                        //上传文件
                        return Observable.zip(
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDID, uDID, identityFontImg, targetDid.get()),
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDID, uDID, identityBackImg, targetDid.get()),
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDID, uDID, identityHandImg, targetDid.get()),
                                new Function3<String, String, String, List<String>>() {
                                    @Override
                                    public List<String> apply(String s, String s2, String s3) throws Exception {
                                        return Arrays.asList(s, s2, s3);
                                    }
                                });
                    }
                })
                .flatMap(new Function<List<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<String> remotePicIds) throws Exception {
                        String identityFontImgId = remotePicIds.get(0);
                        String identityBackImgId = remotePicIds.get(1);
                        String identityHandImgId = remotePicIds.get(2);

                        //两方认证,可以同时发起
                        return Observable.zip(
                                userCertByIDCardByCertParty(uDID, userName, IDCard, identityFontImgId, identityBackImgId, identityHandImgId),
                                userCertByIDCardByProxyParty(uDID, identityPwd, userName, IDCard, identityFontImgId, identityBackImgId, identityHandImgId, targetDid.get()),
                                new BiFunction<JsonElement, String, String>() {
                                    @Override
                                    public String apply(JsonElement jsonElement, String txHash) throws Exception {
                                        return txHash;
                                    }
                                });
                    }
                });
    }

    /**
     * 代理方认证
     *
     * @param uDID
     * @param userName
     * @param IDCard
     * @param identityFontImgId
     * @param identityBackImgId
     * @param identityHandImgId
     * @return
     */
    private Observable<String> userCertByIDCardByProxyParty(String uDID,
                                                            String identityPwd,
                                                            String userName,
                                                            String IDCard,
                                                            String identityFontImgId,
                                                            String identityBackImgId,
                                                            String identityHandImgId,
                                                            String targetDid) {
        return getUserWalletFile(uDID, identityPwd)
                .flatMap(new Function<WalletResult, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(WalletResult walletResult) throws Exception {

                        UserCertByIdCardProxyReqBodyDTO userCertBody = new UserCertByIdCardProxyReqBodyDTO();
                        userCertBody.setDid(uDID);
                        userCertBody.setAddress(walletResult.getCredentials().getAddress());
                        userCertBody.setName(userName);
                        userCertBody.setIdentityCode(IDCard);
                        userCertBody.setForce(true);
                        userCertBody.setTargetDid(targetDid);
                        userCertBody.setTimestamp(System.currentTimeMillis());
                        userCertBody.setIdentityFontImgId(identityFontImgId);
                        userCertBody.setIdentityBackImgId(identityBackImgId);
                        userCertBody.setIdentityHandImgId(identityHandImgId);

                        //签名
                        SignUtils.signAll(userCertBody, walletResult.getPrivateKey(), getUserDPK(userCertBody.getDid()));

                        return XXF.getApiService(CertProxyApiService.class)
                                .userCertByIdCard(userCertBody)
                                .map(new ResponseDTOSimpleFunction<String>())
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String txHash) throws Exception {

                                        //认证信息
                                        CertUserInfoStoreItem certUserInfoStoreItem = new CertUserInfoStoreItem();
                                        certUserInfoStoreItem.setName(userName);
                                        certUserInfoStoreItem.setIdentityCode(IDCard);
                                        certUserInfoStoreItem.setTxHash(txHash);
                                        certUserInfoStoreItem.setIdentityFontImgId(identityFontImgId);
                                        certUserInfoStoreItem.setIdentityBackImgId(identityBackImgId);
                                        certUserInfoStoreItem.setIdentityHandImgId(identityHandImgId);

                                        StoreItem<String> certInfoStore = new StoreItem<>();
                                        certInfoStore.setDataType(Config.BackupType.TYPE_USER_CERT_INFO.getValue());
                                        certInfoStore.setDid(uDID);
                                        certInfoStore.setDataId(uDID);
                                        certInfoStore.setData(JsonUtils.toJsonString(certUserInfoStoreItem));

                                        //备份认证信息
                                        return StoreRepositoryImpl
                                                .getInstance()
                                                .storePrivate(certInfoStore)
                                                .map(new Function<Long, String>() {
                                                    @Override
                                                    public String apply(Long aLong) throws Exception {
                                                        return txHash;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    /**
     * 认证方认证
     *
     * @param uDID
     * @param userName
     * @param IDCard
     * @param identityFontImgId
     * @param identityBackImgId
     * @param identityHandImgId
     * @return
     */
    private Observable<JsonElement> userCertByIDCardByCertParty(String uDID,
                                                                String userName,
                                                                String IDCard,
                                                                String identityFontImgId,
                                                                String identityBackImgId,
                                                                String identityHandImgId) {
        return Observable
                .fromCallable(new Callable<UserCertByIdCardReqBodyDTO>() {
                    @Override
                    public UserCertByIdCardReqBodyDTO call() throws Exception {
                        UserCertByIdCardReqBodyDTO certByIdCardReqBodyDTO = new UserCertByIdCardReqBodyDTO();
                        certByIdCardReqBodyDTO.setDid(uDID);
                        certByIdCardReqBodyDTO.setName(userName);
                        certByIdCardReqBodyDTO.setIdentityCode(IDCard);
                        certByIdCardReqBodyDTO.setIdentityBackImgId(identityBackImgId);
                        certByIdCardReqBodyDTO.setIdentityFontImgId(identityFontImgId);
                        certByIdCardReqBodyDTO.setIdentityHandImgId(identityHandImgId);
                        certByIdCardReqBodyDTO.setTimestamp(System.currentTimeMillis());
                        return certByIdCardReqBodyDTO;
                    }
                })
                .flatMap(new Function<UserCertByIdCardReqBodyDTO, ObservableSource<JsonElement>>() {
                    @Override
                    public ObservableSource<JsonElement> apply(UserCertByIdCardReqBodyDTO userCertByIdCardReqBodyDTO) throws Exception {
                        SignUtils.signByDPK(userCertByIdCardReqBodyDTO, getUserDPK(userCertByIdCardReqBodyDTO.getDid()));
                        return XXF.getApiService(CertApiServce.class)
                                .userCertByIdCard(userCertByIdCardReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<>());
                    }
                });
    }

    @Override
    public Observable<Boolean> isUserCert(String uDid) {
        return XXF.getApiService(CertProxyApiService.class)
                .isUserCertByIdCard(uDid)
                .map(new ResponseDTOSimpleFunction<Boolean>());
    }

    @Override
    public Observable<CertUserInfoStoreItem> getUserCertByIDCardInfo(String uDID) {
        return StoreRepositoryImpl
                .getInstance()
                .getPrivateStore(uDID, Config.BackupType.TYPE_USER_CERT_INFO.getValue(), uDID)
                .map(new Function<StoreItem<String>, CertUserInfoStoreItem>() {
                    @Override
                    public CertUserInfoStoreItem apply(StoreItem<String> stringStoreItem) throws Exception {
                        CertUserInfoStoreItem certUserInfoStoreItem = JsonUtils.toBean(stringStoreItem.getData(), CertUserInfoStoreItem.class);
                        return certUserInfoStoreItem;
                    }
                });
    }

    @Override
    public Observable<CompanyCertResult> companyCert(String uDID, String identityPwd, String companyName, CompanyType companyType, String companyCreditCode, File licenseImg) {

        return getUserWalletFile(uDID, identityPwd)
                .flatMap(new Function<WalletResult, ObservableSource<CompanyCertResult>>() {
                    @Override
                    public ObservableSource<CompanyCertResult> apply(WalletResult walletResult) throws Exception {
                        String chainAddress = walletResult.getCredentials().getAddress();
                        String chainPublicKey = Sm_crypto.c_FromPrvKey(walletResult.getPrivateKey());
                        String chainPrivateKey = walletResult.getPrivateKey();
                        String dataPrivateKey = getUserDPK(uDID);
                        String dataPublicKey = Sm_crypto.c_FromPrvKey(dataPrivateKey);

                        final ObservableField<String> targetDid = new ObservableField<>();
                        final ObservableField<String> cDID = new ObservableField<>();
                        final ObservableField<String> licenseImgId = new ObservableField<>();


                        return DIDRepositoryImpl
                                .getInstance()
                                .forkDID(uDID,
                                        walletResult.getCredentials().getAddress(),
                                        chainPublicKey,
                                        chainPrivateKey,
                                        null,
                                        dataPublicKey,
                                        dataPrivateKey
                                )
                                .flatMap(new Function<String, ObservableSource<NodeServiceItem>>() {
                                    @Override
                                    public ObservableSource<NodeServiceItem> apply(String cDid) throws Exception {
                                        cDID.set(cDid);
                                        return getRandomNodeService(NodeServiceType.TYPE_COMPANY_AUTHENTICATE);
                                    }
                                })
                                .flatMap(new Function<NodeServiceItem, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(NodeServiceItem nodeServiceItem) throws Exception {
                                        targetDid.set(nodeServiceItem.getDid());
                                        //上传文件
                                        return FileRepositoryImpl
                                                .getInstance()
                                                .upload(uDID, cDID.get(), licenseImg, targetDid.get());
                                    }
                                })
                                .flatMap(new Function<String, ObservableSource<CertUserInfoStoreItem>>() {
                                    @Override
                                    public ObservableSource<CertUserInfoStoreItem> apply(String fileId) throws Exception {
                                        licenseImgId.set(fileId);
                                        return CertRepositoryImpl
                                                .getInstance()
                                                .getUserCertByIDCardInfo(uDID);
                                    }
                                })
                                .flatMap(new Function<CertUserInfoStoreItem, ObservableSource<CompanyCertResult>>() {
                                    @Override
                                    public ObservableSource<CompanyCertResult> apply(CertUserInfoStoreItem certUserInfoStoreItem) throws Exception {
                                        //两方确认
                                        return Observable.zip(
                                                companyCertByCertPart(
                                                        uDID, certUserInfoStoreItem.getName(), certUserInfoStoreItem.getIdentityCode(),
                                                        cDID.get(), companyName, companyType, companyCreditCode, licenseImgId.get()),
                                                companyCertByProxyPart(
                                                        uDID, certUserInfoStoreItem.getName(), certUserInfoStoreItem.getIdentityCode(),
                                                        cDID.get(), companyName, companyType, companyCreditCode,
                                                        licenseImgId.get(), targetDid.get(),
                                                        chainAddress, chainPrivateKey, dataPrivateKey), new BiFunction<JsonElement, CompanyCertResult, CompanyCertResult>() {
                                                    @Override
                                                    public CompanyCertResult apply(JsonElement jsonElement, CompanyCertResult companyCertResult) throws Exception {
                                                        return companyCertResult;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    /**
     * 认证方认证
     *
     * @param uDID
     * @param legalPersonName
     * @param legalPersonIdentityCode
     * @param cDID
     * @param companyName
     * @param companyType
     * @param companyCreditCode
     * @param licenseImgId
     * @return
     */
    public Observable<JsonElement> companyCertByCertPart(String uDID, String legalPersonName, String legalPersonIdentityCode,
                                                         String cDID, String companyName, CompanyType companyType, String companyCreditCode, String licenseImgId) {

        return Observable
                .fromCallable(new Callable<CompanyCertReqBodyDTO>() {
                    @Override
                    public CompanyCertReqBodyDTO call() throws Exception {
                        long timestamp = System.currentTimeMillis();
                        CompanyCertReqBodyDTO companyCertBody = new CompanyCertReqBodyDTO();
                        companyCertBody.setDid(cDID);//公司cid
                        companyCertBody.setCompanyName(companyName);
                        companyCertBody.setCompanyType(companyType.getValue());
                        companyCertBody.setCreditCode(companyCreditCode);
                        companyCertBody.setTimestamp(timestamp);
                        companyCertBody.setLicenseImgId(licenseImgId);
                        companyCertBody.setLegalPersonName(legalPersonName);
                        companyCertBody.setLegalPersonIdentityCode(legalPersonIdentityCode);
                        return companyCertBody;
                    }
                })
                .flatMap(new Function<CompanyCertReqBodyDTO, ObservableSource<JsonElement>>() {
                    @Override
                    public ObservableSource<JsonElement> apply(CompanyCertReqBodyDTO companyCertReqBodyDTO) throws Exception {
                        SignUtils.signByDPK(companyCertReqBodyDTO, getUserDPK(uDID));
                        return XXF.getApiService(CertApiServce.class)
                                .companyCert(companyCertReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<>());
                    }
                });
    }

    /**
     * 代理方认证
     *
     * @param uDID
     * @param legalPersonName
     * @param legalPersonIdentityCode
     * @param cDID
     * @param companyName
     * @param companyType
     * @param companyCreditCode
     * @param licenseImgId
     * @param targetDid
     * @param chainAddress
     * @param chainPrivateKey
     * @param dataPrivateKey
     * @return
     */
    public Observable<CompanyCertResult> companyCertByProxyPart(String uDID, String legalPersonName, String legalPersonIdentityCode,
                                                                String cDID, String companyName, CompanyType companyType, String companyCreditCode,
                                                                String licenseImgId, String targetDid,
                                                                String chainAddress, String chainPrivateKey, String dataPrivateKey) {
        return Observable
                .fromCallable(new Callable<CompanyCertProxyReqBodyDTO>() {
                    @Override
                    public CompanyCertProxyReqBodyDTO call() throws Exception {

                        long timestamp = System.currentTimeMillis();
                        CompanyCertProxyReqBodyDTO.SignedLegalPerson signedLegalPerson = new CompanyCertProxyReqBodyDTO.SignedLegalPerson();
                        signedLegalPerson.setDid(uDID);
                        signedLegalPerson.setName(legalPersonName);
                        signedLegalPerson.setTimestamp(timestamp);
                        signedLegalPerson.setIdentityCode(legalPersonIdentityCode);


                        CompanyCertProxyReqBodyDTO companyCertBody = new CompanyCertProxyReqBodyDTO();
                        companyCertBody.setDid(cDID);//公司cid
                        companyCertBody.setAddress(chainAddress);
                        companyCertBody.setCompanyName(companyName);
                        companyCertBody.setCompanyType(companyType.getValue());
                        companyCertBody.setCreditCode(companyCreditCode);
                        companyCertBody.setLegalPerson(signedLegalPerson);
                        companyCertBody.setTimestamp(timestamp);
                        companyCertBody.setTargetDid(targetDid);
                        companyCertBody.setLicenseImgId(licenseImgId);
                        return companyCertBody;
                    }
                }).flatMap(new Function<CompanyCertProxyReqBodyDTO, ObservableSource<CompanyCertResult>>() {
                    @Override
                    public ObservableSource<CompanyCertResult> apply(CompanyCertProxyReqBodyDTO companyCertProxyReqBodyDTO) throws Exception {
                        //签名
                        SignUtils.signAll(companyCertProxyReqBodyDTO, chainPrivateKey, dataPrivateKey);

                        return XXF.getApiService(CertProxyApiService.class)
                                .companyCert(companyCertProxyReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<String>())
                                .map(new Function<String, CompanyCertResult>() {
                                    @Override
                                    public CompanyCertResult apply(String txHash) throws Exception {
                                        CompanyCertResult companyCertResult = new CompanyCertResult();
                                        companyCertResult.setcDid(cDID);
                                        companyCertResult.setTxHash(txHash);
                                        return companyCertResult;
                                    }
                                });
                    }
                });
    }

    @Override
    public Observable<Boolean> iscompanyCert(String cDID) {
        return XXF.getApiService(CertProxyApiService.class)
                .isCompanyCert(cDID)
                .map(new ResponseDTOSimpleFunction<Boolean>());
    }


}
