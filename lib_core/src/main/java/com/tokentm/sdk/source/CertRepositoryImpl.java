package com.tokentm.sdk.source;

import android.databinding.ObservableField;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.api.CertApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.model.CompanyCertReqBodyDTO;
import com.tokentm.sdk.model.CompanyCertResult;
import com.tokentm.sdk.model.CompanyType;
import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.UserCertByIdCardReqBodyDTO;
import com.tokentm.sdk.wallet.WalletResult;
import com.xxf.arch.XXF;
import com.xxf.arch.json.JsonUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public Observable<String> userCertByIDCard(String uDid,
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

                        return Observable.zip(
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDid, uDid, identityFontImg, targetDid.get()),
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDid, uDid, identityBackImg, targetDid.get()),
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDid, uDid, identityHandImg, targetDid.get()),
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

                        return getUserWalletFile(uDid, identityPwd)
                                .flatMap(new Function<WalletResult, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(WalletResult walletResult) throws Exception {
                                        String identityFontImgId = remotePicIds.get(0);
                                        String identityBackImgId = remotePicIds.get(1);
                                        String identityHandImgId = remotePicIds.get(2);

                                        UserCertByIdCardReqBodyDTO userCertBody = new UserCertByIdCardReqBodyDTO();
                                        userCertBody.setDid(uDid);
                                        userCertBody.setAddress(walletResult.getCredentials().getAddress());
                                        userCertBody.setName(userName);
                                        userCertBody.setIdentityCode(IDCard);
                                        userCertBody.setForce(true);
                                        userCertBody.setTargetDid(targetDid.get());
                                        userCertBody.setTimestamp(System.currentTimeMillis());
                                        userCertBody.setIdentityFontImgId(identityFontImgId);
                                        userCertBody.setIdentityBackImgId(identityBackImgId);
                                        userCertBody.setIdentityHandImgId(identityHandImgId);

                                        userCertBody.setSign(SignUtils.signByDataPk(userCertBody, getUserDPK(userCertBody.getDid())));
                                        userCertBody.setChainPrvKeySign(SignUtils.signByChainPk(userCertBody, walletResult.getPrivateKey()));

                                        return XXF.getApiService(CertApiService.class)
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
                                                        certInfoStore.setDid(uDid);
                                                        certInfoStore.setDataId(uDid);
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
                });
    }

    @Override
    public Observable<CertUserInfoStoreItem> getUserCertByIDCardInfo(String uDid) {
        return StoreRepositoryImpl
                .getInstance()
                .getPrivateStore(uDid, Config.BackupType.TYPE_USER_CERT_INFO.getValue(), uDid)
                .map(new Function<StoreItem<String>, CertUserInfoStoreItem>() {
                    @Override
                    public CertUserInfoStoreItem apply(StoreItem<String> stringStoreItem) throws Exception {
                        CertUserInfoStoreItem certUserInfoStoreItem = JsonUtils.toBean(stringStoreItem.getData(), CertUserInfoStoreItem.class);
                        return certUserInfoStoreItem;
                    }
                });
    }

    @Override
    public Observable<CompanyCertResult> companyCert(String uDid, String identityPwd, String companyName, CompanyType companyType, String companyCreditCode, File licenseImg) {
        return getUserWalletFile(uDid, identityPwd)
                .flatMap(new Function<WalletResult, ObservableSource<CompanyCertResult>>() {
                    @Override
                    public ObservableSource<CompanyCertResult> apply(WalletResult walletResult) throws Exception {
                        String chainPublicKey = Sm_crypto.c_FromPrvKey(walletResult.getPrivateKey());
                        String chainPrivateKey = walletResult.getPrivateKey();
                        String dataPrivateKey = getUserDPK(uDid);
                        String dataPublicKey = Sm_crypto.c_FromPrvKey(dataPrivateKey);
                        return DIDRepositoryImpl
                                .getInstance()
                                .forkDID(uDid,
                                        walletResult.getCredentials().getAddress(),
                                        chainPublicKey,
                                        chainPrivateKey,
                                        null,
                                        dataPublicKey,
                                        dataPrivateKey
                                )
                                .flatMap(new Function<String, ObservableSource<CompanyCertResult>>() {
                                    @Override
                                    public ObservableSource<CompanyCertResult> apply(String cDid) throws Exception {
                                        return getRandomNodeService(NodeServiceType.TYPE_COMPANY_AUTHENTICATE)
                                                .flatMap(new Function<NodeServiceItem, ObservableSource<CompanyCertReqBodyDTO>>() {
                                                    @Override
                                                    public ObservableSource<CompanyCertReqBodyDTO> apply(NodeServiceItem nodeServiceItem) throws Exception {
                                                        String targetDid = nodeServiceItem.getDid();
                                                        return Observable.zip(
                                                                //上传文件
                                                                FileRepositoryImpl
                                                                        .getInstance()
                                                                        .upload(uDid, cDid, licenseImg, targetDid),
                                                                //获取实名认证信息
                                                                CertRepositoryImpl
                                                                        .getInstance()
                                                                        .getUserCertByIDCardInfo(uDid),
                                                                new BiFunction<String, CertUserInfoStoreItem, CompanyCertReqBodyDTO>() {
                                                                    @Override
                                                                    public CompanyCertReqBodyDTO apply(String remoteFileId, CertUserInfoStoreItem certUserInfoStoreItem) throws Exception {
                                                                        long timestamp = System.currentTimeMillis();
                                                                        CompanyCertReqBodyDTO.SignedLegalPerson signedLegalPerson = new CompanyCertReqBodyDTO.SignedLegalPerson();
                                                                        signedLegalPerson.setDid(uDid);
                                                                        signedLegalPerson.setName(certUserInfoStoreItem.getName());
                                                                        signedLegalPerson.setTimestamp(timestamp);
                                                                        signedLegalPerson.setIdentityCode(certUserInfoStoreItem.getIdentityCode());
                                                                        //用户数据签名
                                                                        String legalPersonSign = SignUtils.signByDataPk(signedLegalPerson, dataPrivateKey);
                                                                        signedLegalPerson.setSign(legalPersonSign);


                                                                        CompanyCertReqBodyDTO companyCertBody = new CompanyCertReqBodyDTO();
                                                                        companyCertBody.setDid(cDid);//公司cid
                                                                        companyCertBody.setAddress(walletResult.getCredentials().getAddress());
                                                                        companyCertBody.setCompanyName(companyName);
                                                                        companyCertBody.setCompanyType(companyType.getValue());
                                                                        companyCertBody.setCreditCode(companyCreditCode);
                                                                        companyCertBody.setLegalPerson(signedLegalPerson);
                                                                        companyCertBody.setTimestamp(timestamp);
                                                                        companyCertBody.setTargetDid(targetDid);
                                                                        companyCertBody.setLicenseImgId(remoteFileId);

                                                                        //包括legalPerson的签名

                                                                        Map<String, String> legalPersonChainPKSignFields = SignUtils.getChainPKSignFields(companyCertBody.getLegalPerson());
                                                                        Map<String, String> legalPersonDataPKSignFields = SignUtils.getDataPKSignFields(companyCertBody.getLegalPerson());
                                                                        legalPersonChainPKSignFields = SignUtils.wrapperChildSignFields("legalPerson_", legalPersonChainPKSignFields);
                                                                        legalPersonDataPKSignFields = SignUtils.wrapperChildSignFields("legalPerson_", legalPersonDataPKSignFields);


                                                                        Map<String, String> companyChainPKSignFields = SignUtils.getChainPKSignFields(companyCertBody);
                                                                        companyChainPKSignFields.putAll(legalPersonChainPKSignFields);

                                                                        Map<String, String> companyDataPKSignFields = SignUtils.getDataPKSignFields(companyCertBody);
                                                                        companyDataPKSignFields.putAll(legalPersonDataPKSignFields);

                                                                        companyCertBody.setChainPrvKeySign(SignUtils.sign(companyChainPKSignFields, chainPrivateKey));
                                                                        companyCertBody.setSign(SignUtils.sign(companyDataPKSignFields, dataPrivateKey));
                                                                        return companyCertBody;
                                                                    }
                                                                }
                                                        );
                                                    }
                                                })
                                                .flatMap(new Function<CompanyCertReqBodyDTO, ObservableSource<CompanyCertResult>>() {
                                                    @Override
                                                    public ObservableSource<CompanyCertResult> apply(CompanyCertReqBodyDTO companyCertReqBodyDTO) throws Exception {
                                                        return XXF.getApiService(CertApiService.class)
                                                                .companyCert(companyCertReqBodyDTO)
                                                                .map(new ResponseDTOSimpleFunction<String>())
                                                                .map(new Function<String, CompanyCertResult>() {
                                                                    @Override
                                                                    public CompanyCertResult apply(String txHash) throws Exception {
                                                                        CompanyCertResult companyCertResult = new CompanyCertResult();
                                                                        companyCertResult.setcDid(cDid);
                                                                        companyCertResult.setTxHash(txHash);
                                                                        return companyCertResult;
                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                });
                    }
                });
    }


}
