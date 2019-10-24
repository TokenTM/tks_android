package com.tokentm.sdk.source;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.api.DIDApiService;
import com.tokentm.sdk.common.CacheUtils;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.EncryptionUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.DIDReqDTO;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.wallet.SignUtils;
import com.tokentm.sdk.wallet.WalletResult;
import com.tokentm.sdk.wallet.WalletUtils;
import com.xxf.arch.XXF;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
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
    public Observable<String> createUDID(String phone, String smsCode, String identityPwd) {
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
                        signMap.put("phone", phone);
                        signMap.put("code", smsCode);

                        DIDReqDTO didReqDTO = new DIDReqDTO();
                        didReqDTO.setTimestamp(timestamp);
                        didReqDTO.setChainAddress(walletResult.getCredentials().getAddress());
                        didReqDTO.setChainPrvSign(SignUtils.sign(signMap, walletResult.getPrivateKey()));
                        didReqDTO.setChainPubKey(publicKey);
                        didReqDTO.setData(null);
                        didReqDTO.setDataPubKey(dataPublicKey);
                        didReqDTO.setPhone(phone);
                        didReqDTO.setCode(smsCode);
                        didReqDTO.setSign(SignUtils.sign(signMap, dataPrivateKey));


                        return XXF.getApiService(DIDApiService.class)
                                .createDID(didReqDTO)
                                .map(new ResponseDTOSimpleFunction<String>())
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String did) throws Exception {
                                        //put dpk
                                        SDKsp.getInstance()._putDPK(did, dataPrivateKey);

                                        return Observable.zip(
                                                //备份dpk
                                                _storeDpk(did, dataPrivateKey, identityPwd),
                                                //备份keystore
                                                _storeUserKeyStore(did, walletResult.getKeyStoreFileContent()),

                                                new BiFunction<Long, Long, String>() {
                                                    @Override
                                                    public String apply(Long aLong, Long aLong2) throws Exception {
                                                        //返回didi
                                                        return did;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }


    /**
     * 备份dpk
     *
     * @param did
     * @param dpk
     * @param identityPwd
     * @return
     */
    private Observable<Long> _storeDpk(String did, String dpk, String identityPwd) {
        StoreItem<String> dpkStoreItem = new StoreItem<String>();
        dpkStoreItem.setDid(did);
        dpkStoreItem.setDataId(did);
        dpkStoreItem.setDataType(Config.BackupType.TYPE_DPK.getValue());
        dpkStoreItem.setData(EncryptionUtils.encodeString(dpk, identityPwd));
        return StoreRepositoryImpl
                .getInstance()
                .store(dpkStoreItem);
    }

    /**
     * 备份用户keystore
     *
     * @param did
     * @param keyStoreFileContent
     * @return
     */
    private Observable<Long> _storeUserKeyStore(String did, String keyStoreFileContent) {
        //2.备份keystore
        StoreItem<String> keyStoreItem = new StoreItem<String>();
        keyStoreItem.setDid(did);
        keyStoreItem.setDataId(did);
        keyStoreItem.setDataType(Config.BackupType.TYPE_KEY_STORE.getValue());
        keyStoreItem.setData(keyStoreFileContent);
        return StoreRepositoryImpl
                .getInstance()
                .storeEncrypt(keyStoreItem);
    }


    @Override
    public Observable<Boolean> resetPwd(String uDID, String oldIdentityPwd, String newIdentityPwd) {
//        return this.getPwdSecurityQuestions(DID)
//                .flatMap(new Function<BackupPwdSecurityQuestionDTO, ObservableSource<Boolean>>() {
//                    @Override
//                    public ObservableSource<Boolean> apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        String dpk = EncryptionUtils.decodeString(backupPwdSecurityQuestionDTO.getPwdEncryptedSecretKey(), oldIdentityPwd);
//                        if (TextUtils.isEmpty(dpk)) {
//                            throw new RuntimeException("旧密码不正确");
//                        }
//                        //用新密码加密密钥
//                        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(dpk, newIdentityPwd);
//                        backupPwdSecurityQuestionDTO.setPwdEncryptedSecretKey(pwdEncryptedSecretKey);
//
//                        //1.备份身份密码
//                        StorePwdSecurityQuestionItem identityPwdStoreItem = new StorePwdSecurityQuestionItem(DID, backupPwdSecurityQuestionDTO);
//                        return StoreRepositoryImpl
//                                .getInstance()
//                                .store(identityPwdStoreItem)
//                                .map(new Function<Long, Boolean>() {
//                                    @Override
//                                    public Boolean apply(Long aLong) throws Exception {
//                                        return true;
//                                    }
//                                });
//                    }
//                });
        //TODO youxuan
        return Observable.just(true)
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Boolean resetPwded) throws Exception {
                        if (resetPwded) {
                            //重置钱包密码
                            File keyStoreFile = null;
                            WalletResult walletResult = WalletUtils._resetWallet(oldIdentityPwd, newIdentityPwd, keyStoreFile);
                            //备份钱包密码
                            return _storeUserKeyStore(uDID, walletResult.getKeyStoreFileContent())
                                    .map(new Function<Long, Boolean>() {
                                        @Override
                                        public Boolean apply(Long aLong) throws Exception {
                                            return resetPwded;
                                        }
                                    });
                        }
                        return Observable.just(resetPwded);
                    }
                });
    }

    @Override
    public Observable<Boolean> resetPwd(String uDID, String oldPhone, String smsCode, String newIdentityPwd) {
        //TODO 旧密码服务器获取(手机号+验证码)
        return Observable.just("123")
                .flatMap(new Function<String, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(String oldIdentityPwd) throws Exception {
                        return resetPwd(uDID, oldIdentityPwd, newIdentityPwd);
                    }
                });
    }

}
