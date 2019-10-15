package com.tokentm.sdk.source;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.api.ConfigApiService;
import com.tokentm.sdk.api.DIDApiService;
import com.tokentm.sdk.common.CacheUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.DIDReqDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.wallet.SignUtils;
import com.tokentm.sdk.wallet.WalletResult;
import com.tokentm.sdk.wallet.WalletUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.http.XXFHttp;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import sm_crypto.Sm_crypto;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DidRepositoryImpl implements DidService {
    private static volatile DidService INSTANCE;

    public static DidService getInstance() {
        if (INSTANCE == null) {
            synchronized (DidService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DidRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<SecurityQuestionDTO>> getSecurityQuestionTemplate() {
        return XXFHttp.getApiService(ConfigApiService.class)
                .getSecurityQuestionTemplate()
                .map(new ResponseDTOSimpleFunction<List<SecurityQuestionDTO>>());
    }


    @Override
    public Observable<String> createDID(String identityPwd, Map<Long, String> securityQuestionAnswers) {
        return Observable
                .fromCallable(new Callable<WalletResult>() {
                    @Override
                    public WalletResult call() throws Exception {
                        return WalletUtils._createWallet(identityPwd, new File(CacheUtils.getCacheDir(XXF.getApplication()), "temp.keystore"));
                    }
                })
                .flatMap(new Function<WalletResult, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(WalletResult walletResult) throws Exception {
                        //时间戳
                        long timestamp = System.currentTimeMillis();

                        XXF.getLogger().d("========>privateKey=" + walletResult.getPrivateKey());
                        String publicKey = Sm_crypto.c_FromPrvKey(walletResult.getPrivateKey());
                        XXF.getLogger().d("========>publicKey=" + publicKey);

                        //数据验证签名的密钥对
                        String dataPrivateKey = Sm_crypto.c_GenerateKey();
                        String dataPublicKey = Sm_crypto.c_FromPrvKey(dataPrivateKey);


                        XXF.getLogger().d("========>dataPrivateKey=" + dataPrivateKey);
                        XXF.getLogger().d("========>dataPublicKey=" + dataPublicKey);


                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("timestamp", String.valueOf(timestamp));
                        signMap.put("chainAddress", walletResult.getCredentials().getAddress());

                        DIDReqDTO didReqDTO = new DIDReqDTO();
                        didReqDTO.setTimestamp(timestamp);
                        didReqDTO.setChainAddress(walletResult.getCredentials().getAddress());
                        didReqDTO.setChainPrvSign(SignUtils.sign(signMap, walletResult.getPrivateKey()));
                        didReqDTO.setChainPubKey(publicKey);
                        didReqDTO.setData(null);
                        didReqDTO.setDataPubKey(dataPublicKey);
                        didReqDTO.setSign(SignUtils.sign(signMap, dataPrivateKey));


                        return XXF.getApiService(DIDApiService.class)
                                .createDID(didReqDTO)
                                .map(new ResponseDTOSimpleFunction<String>())
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String did) throws Exception {
                                        //1.备份身份密码
                                        //2.备份keystore
                                        //3.备份数据密钥
                                        StoreItem identityPwdStoreItem = new StoreItem();
                                        identityPwdStoreItem.setDid(did);
                                        identityPwdStoreItem.setDataId(did);
                                        identityPwdStoreItem.setDataType(Config.BackupType.TYPE_BACK_UP_SECRETKEY.getValue());
                                        //TODO 身份密码存储模型 转json
                                        //identityPwdStoreItem.setData();

                                        StoreItem keyStoreItem = new StoreItem();
                                        keyStoreItem.setDid(did);
                                        keyStoreItem.setDataId(did);
                                        keyStoreItem.setDataType(Config.BackupType.TYPE_USER_KEY_STORE.getValue());
                                        keyStoreItem.setData(walletResult.getKeyStoreFileContent());


                                        StoreItem dataPrivateKeyStoreItem = new StoreItem();
                                        dataPrivateKeyStoreItem.setDid(did);
                                        dataPrivateKeyStoreItem.setDataId(did);
                                        dataPrivateKeyStoreItem.setDataType(Config.BackupType.TYPE_USER_SIGN_PRIVATE_KEY.getValue());
                                        dataPrivateKeyStoreItem.setData(dataPrivateKey);

                                        List<StoreItem> storeList = Arrays.asList(
                                                identityPwdStoreItem,
                                                keyStoreItem,
                                                dataPrivateKeyStoreItem
                                        );

                                        return StoreRepositoryImpl
                                                .getInstance()
                                                .store(storeList)
                                                .map(new Function<List<Long>, String>() {
                                                    @Override
                                                    public String apply(List<Long> longs) throws Exception {
                                                        return did;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    @Override
    public Observable<List<SecurityQuestionDTO>> getSecurityQuestions(String DID) {
        return getSecurityQuestionTemplate()
                .flatMap(new Function<List<SecurityQuestionDTO>, ObservableSource<List<SecurityQuestionDTO>>>() {
                    @Override
                    public ObservableSource<List<SecurityQuestionDTO>> apply(List<SecurityQuestionDTO> securityQuestionTemplates) throws Exception {
                        //查找设置的安全问题ids
                        return null;
                    }
                });
    }

    @Override
    public Observable<Boolean> decrypt(String DID, String identityPwd) {
        return null;
    }

    @Override
    public Observable<Boolean> decrypt(String DID, Map<Long, String> securityQuestionAnswers) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, String oldIdentityPwd, String newIdentityPwd) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, Map<Long, String> oldSecurityQuestionAnswers, Map<Long, String> newSecurityQuestionAnswers) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, Map<Long, String> oldSecurityQuestionAnswers, String newIdentityPwd) {
        return null;
    }
}
