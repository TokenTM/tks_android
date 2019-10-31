package com.tokentm.sdk.source;

import com.tokentm.sdk.api.CertApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;
import com.tokentm.sdk.model.UserCertByIdCardReqDTO;
import com.tokentm.sdk.wallet.WalletResult;
import com.xxf.arch.XXF;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

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

    private Observable<NodeServiceItem> getUserCertByIDCardNodeService() {
        return NodeRepositoryImpl
                .getInstance()
                .getNodeService(NodeServiceType.TYPE_USER_AUTHENTICATE)
                .map(new Function<List<NodeServiceItem>, NodeServiceItem>() {
                    @Override
                    public NodeServiceItem apply(List<NodeServiceItem> nodeServiceItems) throws Exception {
                        return nodeServiceItems.get(0);
                    }
                });
    }

    private Observable<WalletResult> getUserWalletFile(String uDid) {
        return Observable.just(new com.tokentm.sdk.wallet.WalletResult());
    }

    @Override
    public Observable<String> userCertByIDCard(String uDid,
                                               String identityPwd,
                                               String userName,
                                               String IDCard,
                                               File identityFontImg,
                                               File identityBackImg,
                                               File identityHandImg) {
        final String[] targetDid = {null};
        return getUserCertByIDCardNodeService()
                .onErrorReturnItem(new NodeServiceItem())
                .flatMap(new Function<NodeServiceItem, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(NodeServiceItem nodeServiceItem) throws Exception {
                        //TODO 获取实名认证的serviceId
                        targetDid[0] = uDid;

                        return Observable.zip(
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDid, identityFontImg, targetDid[0]),
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDid, identityBackImg, targetDid[0]),
                                FileRepositoryImpl
                                        .getInstance()
                                        .upload(uDid, identityHandImg, targetDid[0]),
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
                    public ObservableSource<String> apply(List<String> strings) throws Exception {

                        return getUserWalletFile(uDid)
                                .flatMap(new Function<WalletResult, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(WalletResult walletResult) throws Exception {
                                        UserCertByIdCardReqDTO userCertBody = new UserCertByIdCardReqDTO();
                                        userCertBody.setDid(uDid);
                                        userCertBody.setAddress(walletResult.getCredentials().getAddress());
                                        userCertBody.setName(userName);
                                        userCertBody.setIdentityCode(IDCard);
                                        userCertBody.setForce(true);
                                        userCertBody.setTargetDid(targetDid[0]);
                                        userCertBody.setTimestamp(System.currentTimeMillis());
                                        userCertBody.setIdentityFontImgId(strings.get(0));
                                        userCertBody.setIdentityBackImgId(strings.get(1));
                                        userCertBody.setIdentityHandImgId(strings.get(2));

                                        userCertBody.setSign(SignUtils.signByDataPk(userCertBody, getUserDPK(userCertBody.getDid())));
                                        userCertBody.setChainPrvKeySign(SignUtils.signByChainPk(userCertBody, walletResult.getPrivateKey()));

                                        return XXF.getApiService(CertApiService.class)
                                                .userCertByIdCard(userCertBody)
                                                .map(new ResponseDTOSimpleFunction<>());
                                    }
                                });

                    }
                });
    }

}
