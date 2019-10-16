package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.api.ConfigApiService;
import com.tokentm.sdk.api.DIDApiService;
import com.tokentm.sdk.common.CacheUtils;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.EncryptionUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.BackupPwdSecurityQuestionDTO;
import com.tokentm.sdk.model.DIDReqDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StorePwdSecurityQuestionItem;
import com.tokentm.sdk.wallet.SignUtils;
import com.tokentm.sdk.wallet.WalletResult;
import com.tokentm.sdk.wallet.WalletUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.http.XXFHttp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    public Observable<List<SecurityQuestionDTO>> getSecurityQuestionTemplate() {
        return XXFHttp.getApiService(ConfigApiService.class)
                .getSecurityQuestionTemplate()
                .map(new ResponseDTOSimpleFunction<List<SecurityQuestionDTO>>());
    }


    private BackupPwdSecurityQuestionDTO generate(String secretKey, String identityPwd, LinkedHashMap<Long, String> securityQuestionAnswers) {
        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(secretKey, identityPwd);

        StringBuffer questionAnswers = new StringBuffer();
        List<Long> securityQuestionIds = new ArrayList<>();
        for (Map.Entry<Long, String> entry : securityQuestionAnswers.entrySet()) {
            securityQuestionIds.add(entry.getKey());
            questionAnswers.append(entry.getValue());
        }
        String securityQuestionEncryptedSecretKey = EncryptionUtils.encodeString(secretKey, questionAnswers.toString());

        return new BackupPwdSecurityQuestionDTO(
                pwdEncryptedSecretKey,
                securityQuestionEncryptedSecretKey,
                securityQuestionIds);
    }

    @Override
    public Observable<String> createDID(String identityPwd, LinkedHashMap<Long, String> securityQuestionAnswers) {
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
                                        new BackupPwdSecurityQuestionDTO();
                                        //1.备份身份密码
                                        StoreItem<BackupPwdSecurityQuestionDTO> identityPwdStoreItem = new StoreItem<BackupPwdSecurityQuestionDTO>();
                                        identityPwdStoreItem.setDid(did);
                                        identityPwdStoreItem.setDataId(did);
                                        identityPwdStoreItem.setDataType(Config.BackupType.TYPE_BACK_UP_SECRETKEY.getValue());
                                        identityPwdStoreItem.setData(generate(dataPrivateKey, identityPwd, securityQuestionAnswers));

                                        //2.备份keystore
                                        StoreItem<String> keyStoreItem = new StoreItem<String>();
                                        keyStoreItem.setDid(did);
                                        keyStoreItem.setDataId(did);
                                        keyStoreItem.setDataType(Config.BackupType.TYPE_USER_KEY_STORE.getValue());
                                        keyStoreItem.setData(walletResult.getKeyStoreFileContent());

                                        //put dpk
                                        SDKsp.getInstance()._putDPK(did, dataPrivateKey);

                                        return Observable.zip(
                                                //TODO 不加密
                                                StoreRepositoryImpl
                                                        .getInstance()
                                                        .storeEncrypt(identityPwdStoreItem),
                                                //加密
                                                StoreRepositoryImpl
                                                        .getInstance()
                                                        .storeEncrypt(keyStoreItem),
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

    @Override
    public Observable<BackupPwdSecurityQuestionDTO> getPwdSecurityQuestions(String DID) {
        return StoreRepositoryImpl
                .getInstance()
                .getStore(StorePwdSecurityQuestionItem.class, DID, Config.BackupType.TYPE_BACK_UP_SECRETKEY.getValue(), DID)
                .map(new Function<StorePwdSecurityQuestionItem, BackupPwdSecurityQuestionDTO>() {
                    @Override
                    public BackupPwdSecurityQuestionDTO apply(StorePwdSecurityQuestionItem storePwdSecurityQuestionItem) throws Exception {
                        return storePwdSecurityQuestionItem.getData();
                    }
                });
    }

    @Override
    public Observable<List<SecurityQuestionDTO>> getSecurityQuestions(String DID) {
        return Observable.zip(
                this.getSecurityQuestionTemplate(),
                this.getPwdSecurityQuestions(DID),
                new BiFunction<List<SecurityQuestionDTO>, BackupPwdSecurityQuestionDTO, List<SecurityQuestionDTO>>() {
                    @Override
                    public List<SecurityQuestionDTO> apply(List<SecurityQuestionDTO> securityQuestionDTOS, BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
                        List<SecurityQuestionDTO> list = new ArrayList<>();
                        for (SecurityQuestionDTO securityQuestionDTO : securityQuestionDTOS) {
                            if (backupPwdSecurityQuestionDTO.getSecurityQuestionIds().contains(securityQuestionDTO.id)) {
                                list.add(securityQuestionDTO);
                            }
                        }
                        return list;
                    }
                });
    }

    @Override
    public Observable<Boolean> decrypt(String DID, String identityPwd) {
        return this.getPwdSecurityQuestions(DID)
                .map(new Function<BackupPwdSecurityQuestionDTO, Boolean>() {
                    @Override
                    public Boolean apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
                        String dpk = EncryptionUtils.decodeString(backupPwdSecurityQuestionDTO.getPwdEncryptedSecretKey(), identityPwd);
                        return !TextUtils.isEmpty(dpk);
                    }
                });
    }

    @Override
    public Observable<Boolean> decrypt(String DID, LinkedHashMap<Long, String> securityQuestionAnswers) {
        return this.getPwdSecurityQuestions(DID)
                .map(new Function<BackupPwdSecurityQuestionDTO, Boolean>() {
                    @Override
                    public Boolean apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Map.Entry<Long, String> entry : securityQuestionAnswers.entrySet()) {
                            stringBuilder.append(entry.getValue());
                        }
                        String dpk = EncryptionUtils.decodeString(backupPwdSecurityQuestionDTO.getSecurityQuestionEncryptedSecretKey(), stringBuilder.toString());
                        return !TextUtils.isEmpty(dpk);
                    }
                });
    }

    @Override
    public Observable<Boolean> reset(String DID, String oldIdentityPwd, String newIdentityPwd) {
        return this.getPwdSecurityQuestions(DID)
                .flatMap(new Function<BackupPwdSecurityQuestionDTO, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
                        String dpk = EncryptionUtils.decodeString(backupPwdSecurityQuestionDTO.getPwdEncryptedSecretKey(), oldIdentityPwd);
                        if (TextUtils.isEmpty(dpk)) {
                            throw new RuntimeException("旧密码不正确");
                        }
                        //用新密码加密密钥
                        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(dpk, newIdentityPwd);
                        backupPwdSecurityQuestionDTO.setPwdEncryptedSecretKey(pwdEncryptedSecretKey);

                        //1.备份身份密码
                        StoreItem<BackupPwdSecurityQuestionDTO> identityPwdStoreItem = new StoreItem<BackupPwdSecurityQuestionDTO>();
                        identityPwdStoreItem.setDid(DID);
                        identityPwdStoreItem.setDataId(DID);
                        identityPwdStoreItem.setDataType(Config.BackupType.TYPE_BACK_UP_SECRETKEY.getValue());
                        identityPwdStoreItem.setData(backupPwdSecurityQuestionDTO);
                        //TODO 不加密
                        return StoreRepositoryImpl
                                .getInstance()
                                .storeEncrypt(identityPwdStoreItem)
                                .map(new Function<Long, Boolean>() {
                                    @Override
                                    public Boolean apply(Long aLong) throws Exception {
                                        return true;
                                    }
                                });
                    }
                });
    }

    @Override
    public Observable<Boolean> reset(String DID, LinkedHashMap<Long, String> oldSecurityQuestionAnswers, LinkedHashMap<Long, String> newSecurityQuestionAnswers) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, LinkedHashMap<Long, String> oldSecurityQuestionAnswers, String newIdentityPwd) {
        return null;
    }
}
