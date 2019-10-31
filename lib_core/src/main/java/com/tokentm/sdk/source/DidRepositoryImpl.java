package com.tokentm.sdk.source;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.api.DIDApiService;
import com.tokentm.sdk.common.CacheUtils;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.DIDReqDTO;
import com.tokentm.sdk.model.NodeServiceEncryptDecryptItem;
import com.tokentm.sdk.model.PwdDpkStoreItem;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.wallet.FileUtils;
import com.tokentm.sdk.wallet.WalletResult;
import com.tokentm.sdk.wallet.WalletUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.json.JsonUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import sm_crypto.Sm_crypto;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DidRepositoryImpl implements DidService, BaseRepo {
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

    private File generateKeyStoreFilePath(String did) {
        return new File(CacheUtils.getCacheDir(XXF.getApplication()), String.format("%s.keystore", did));
    }

    @Override
    public Observable<String> createUDID(String phone, String smsCode, String identityPwd) {
        return Observable
                .fromCallable(new Callable<WalletResult>() {
                    @Override
                    public WalletResult call() throws Exception {
                        return WalletUtils._createWallet(identityPwd, generateKeyStoreFilePath("temp"));
                    }
                })
                .flatMap(new Function<WalletResult, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(WalletResult walletResult) throws Exception {
                        //时间戳
                        long timestamp = System.currentTimeMillis();

                        String publicKey = Sm_crypto.c_FromPrvKey(walletResult.getPrivateKey());
                        XXF.getLogger().d("========>privateKey=" + walletResult.getPrivateKey());
                        XXF.getLogger().d("========>publicKey=" + publicKey);

                        //数据验证签名的密钥对
                        String dataPrivateKey = Sm_crypto.c_GenerateKey();
                        String dataPublicKey = Sm_crypto.c_FromPrvKey(dataPrivateKey);


                        XXF.getLogger().d("========>dataPrivateKey=" + dataPrivateKey);
                        XXF.getLogger().d("========>dataPublicKey=" + dataPublicKey);

                        DIDReqDTO didReqDTO = new DIDReqDTO();
                        didReqDTO.setTimestamp(timestamp);
                        didReqDTO.setChainAddress(walletResult.getCredentials().getAddress());
                        didReqDTO.setChainPubKey(publicKey);
                        didReqDTO.setData(null);
                        didReqDTO.setDataPubKey(dataPublicKey);
                        didReqDTO.setPhone(phone);
                        didReqDTO.setCode(smsCode);
                        didReqDTO.setChainPrvSign(SignUtils.signByChainPk(didReqDTO, walletResult.getPrivateKey()));
                        didReqDTO.setSign(SignUtils.signByDataPk(didReqDTO, dataPrivateKey));


                        return XXF.getApiService(DIDApiService.class)
                                .createDID(didReqDTO)
                                .map(new ResponseDTOSimpleFunction<String>())
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String did) throws Exception {
                                        //put dpk
                                        SDKsp.getInstance()._putDPK(did, dataPrivateKey);

                                        return Observable.zip(
                                                //备份pwd_dpk
                                                _storePwdDpk(did, dataPrivateKey, identityPwd, phone, smsCode),
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
     * 备份pwd_dpk
     *
     * @param did
     * @param dpk
     * @param identityPwd
     * @return
     */
    private Observable<Long> _storePwdDpk(String did, String dpk, String identityPwd, String phone, String smsCode) {
        String data = JsonUtils.toJsonString(new PwdDpkStoreItem(identityPwd, dpk));
        return NodeEncryptRepositoryImpl
                .getInstance()
                .encrypt(did, data, phone, smsCode)
                .map(new Function<List<NodeServiceEncryptDecryptItem>, StoreItem<String>>() {
                    @Override
                    public StoreItem<String> apply(List<NodeServiceEncryptDecryptItem> nodeServiceEncryptDecryptItems) throws Exception {
                        StoreItem<String> dpkStoreItem = new StoreItem<String>();
                        dpkStoreItem.setDid(did);
                        dpkStoreItem.setDataId(did);
                        dpkStoreItem.setDataType(Config.BackupType.TYPE_DPK.getValue());
                        dpkStoreItem.setData(JsonUtils.toJsonString(nodeServiceEncryptDecryptItems));
                        return dpkStoreItem;
                    }
                }).flatMap(new Function<StoreItem<String>, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(StoreItem<String> dpkStoreItem) throws Exception {
                        return StoreRepositoryImpl
                                .getInstance()
                                .store(dpkStoreItem);
                    }
                });
    }

    /**
     * 获取 备份pwd_dpk
     *
     * @param did
     * @param dataId
     * @param phone
     * @param smsCode
     * @return
     */
    private Observable<PwdDpkStoreItem> _getStorePwdDpk(String did,
                                                        String dataId,
                                                        String phone,
                                                        String smsCode) {
        StoreRepositoryImpl instance = (StoreRepositoryImpl) StoreRepositoryImpl.getInstance();
        return instance.getStore(did, Config.BackupType.TYPE_DPK.getValue(), dataId, phone, smsCode)
                .map(new Function<StoreItem<String>, List<NodeServiceEncryptDecryptItem>>() {
                    @Override
                    public List<NodeServiceEncryptDecryptItem> apply(StoreItem<String> stringStoreItem) throws Exception {
                        return JsonUtils.toBeanList(stringStoreItem.getData(), NodeServiceEncryptDecryptItem.class);
                    }
                })
                .flatMap(new Function<List<NodeServiceEncryptDecryptItem>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<NodeServiceEncryptDecryptItem> nodeServiceEncryptDecryptItems) throws Exception {
                        return NodeEncryptRepositoryImpl
                                .getInstance()
                                .decrypt(did, nodeServiceEncryptDecryptItems, phone, smsCode);
                    }
                })
                .map(new Function<String, PwdDpkStoreItem>() {
                    @Override
                    public PwdDpkStoreItem apply(String s) throws Exception {
                        return JsonUtils.toBean(s, PwdDpkStoreItem.class);
                    }
                }).map(new Function<PwdDpkStoreItem, PwdDpkStoreItem>() {
                    @Override
                    public PwdDpkStoreItem apply(PwdDpkStoreItem pwdDpkStoreItem) throws Exception {
                        //put dpk
                        SDKsp.getInstance()._putDPK(did, pwdDpkStoreItem.getDpk());
                        return pwdDpkStoreItem;
                    }
                });

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
                .storeEncrypt(keyStoreItem)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        File file = generateKeyStoreFilePath(did);
                        FileUtils.writeStr(file, keyStoreFileContent, false);
                    }
                });
    }

    /**
     * 获取 备份用户keystore
     *
     * @param did
     * @return
     */
    private Observable<File> _getStoreUserKeyStore(String did) {
        return StoreRepositoryImpl
                .getInstance()
                .getStoreDecrypted(did, Config.BackupType.TYPE_KEY_STORE.getValue(), did)
                .map(new Function<StoreItem<String>, File>() {
                    @Override
                    public File apply(StoreItem<String> stringStoreItem) throws Exception {
                        File file = generateKeyStoreFilePath(did);
                        FileUtils.writeStr(file, stringStoreItem.getData(), false);
                        return file;
                    }
                });
    }


    @Override
    public Observable<Boolean> resetIdentityPwd(String uDID, String oldPhone, String smsCode, String newIdentityPwd) {
        return _getStorePwdDpk(uDID, uDID, oldPhone, smsCode)
                .flatMap(new Function<PwdDpkStoreItem, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(PwdDpkStoreItem pwdDpkStoreItem) throws Exception {
                        String oldIdentityPwd = pwdDpkStoreItem.getPwd();
                        //每次都获取最新的keystore 防止其他端修改了
                        return _getStoreUserKeyStore(uDID)
                                .flatMap(new Function<File, ObservableSource<Boolean>>() {
                                    @Override
                                    public ObservableSource<Boolean> apply(File keyStoreFile) throws Exception {
                                        WalletResult walletResult = WalletUtils._resetWallet(oldIdentityPwd, newIdentityPwd, keyStoreFile);
                                        return Observable.zip(
                                                //备份pwd_dpk
                                                _storePwdDpk(uDID, pwdDpkStoreItem.getDpk(), newIdentityPwd, oldPhone, smsCode),
                                                //备份keystore
                                                _storeUserKeyStore(uDID, walletResult.getKeyStoreFileContent()),

                                                new BiFunction<Long, Long, Boolean>() {
                                                    @Override
                                                    public Boolean apply(Long aLong, Long aLong2) throws Exception {
                                                        return true;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    @Override
    public Observable<Boolean> validateIdentityPwd(String uDID, String identityPwd) {
        return _getStoreUserKeyStore(uDID)
                .map(new Function<File, Boolean>() {
                    @Override
                    public Boolean apply(File file) throws Exception {
                        try {
                            WalletResult walletResult = WalletUtils._openWallet(identityPwd, file);
                            return walletResult != null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
    }


}
