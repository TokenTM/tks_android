package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.common.CacheUtils;
import com.tokentm.sdk.common.SDKsp;
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
public class IdentityPwdRepositoryImpl implements IdentityPwdService, BaseRepo {
    private static volatile IdentityPwdRepositoryImpl INSTANCE;

    public static IdentityPwdRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (IdentityPwdService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IdentityPwdRepositoryImpl();
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
                        String chainPublicKey = Sm_crypto.c_FromPrvKey(walletResult.getPrivateKey());

                        //数据验证签名的密钥对
                        String dataPrivateKey = Sm_crypto.c_GenerateKey();
                        String dataPublicKey = Sm_crypto.c_FromPrvKey(dataPrivateKey);
                        return DIDRepositoryImpl
                                .getInstance()
                                .createDID(
                                        phone,
                                        smsCode,
                                        walletResult.getCredentials().getAddress(),
                                        chainPublicKey, walletResult.getPrivateKey(),
                                        null,
                                        dataPublicKey, dataPrivateKey
                                )
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

    @Override
    public Observable<Boolean> decryptUDID(String uDID, String phone, String smsCode) {
        return _getStorePwdDpk(uDID, phone, smsCode)
                .map(new Function<PwdDpkStoreItem, Boolean>() {
                    @Override
                    public Boolean apply(PwdDpkStoreItem pwdDpkStoreItem) throws Exception {
                        //put dpk to disk
                        SDKsp.getInstance()._putDPK(uDID, pwdDpkStoreItem.getDpk());

                        return pwdDpkStoreItem != null;
                    }
                });
    }

    /**
     * @param uDID
     * @return
     */
    @Override
    public Observable<Boolean> isUDIDAccessible(String uDID) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    return !TextUtils.isEmpty(getUserDPK(uDID));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
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
                                .storePublic(dpkStoreItem);
                    }
                });
    }

    /**
     * 获取 备份pwd_dpk
     *
     * @param did
     * @param phone
     * @param smsCode
     * @return
     */
    private Observable<PwdDpkStoreItem> _getStorePwdDpk(String did,
                                                        String phone,
                                                        String smsCode) {
        StoreRepositoryImpl instance = (StoreRepositoryImpl) StoreRepositoryImpl.getInstance();
        return instance.getStore(did, Config.BackupType.TYPE_DPK.getValue(), did, phone, smsCode)
                .map(new Function<StoreItem<String>, List<NodeServiceEncryptDecryptItem>>() {
                    @Override
                    public List<NodeServiceEncryptDecryptItem> apply(StoreItem<String> stringStoreItem) throws Exception {
                        return JsonUtils.toBeanList(stringStoreItem.getData(), NodeServiceEncryptDecryptItem.class);
                    }
                })
                .flatMap(new Function<List<NodeServiceEncryptDecryptItem>, ObservableSource<PwdDpkStoreItem>>() {
                    @Override
                    public ObservableSource<PwdDpkStoreItem> apply(List<NodeServiceEncryptDecryptItem> nodeServiceEncryptDecryptItems) throws Exception {
                        return NodeEncryptRepositoryImpl
                                .getInstance()
                                .decrypt(did, nodeServiceEncryptDecryptItems, phone, smsCode)
                                .map(new Function<String, PwdDpkStoreItem>() {
                                    @Override
                                    public PwdDpkStoreItem apply(String s) throws Exception {
                                        return JsonUtils.toBean(s, PwdDpkStoreItem.class);
                                    }
                                });
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
                .storePrivate(keyStoreItem)
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
                .getPrivateStore(did, Config.BackupType.TYPE_KEY_STORE.getValue(), did)
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
        return _getStorePwdDpk(uDID, oldPhone, smsCode)
                .flatMap(new Function<PwdDpkStoreItem, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(PwdDpkStoreItem pwdDpkStoreItem) throws Exception {
                        //put dpk to disk
                        SDKsp.getInstance()._putDPK(uDID, pwdDpkStoreItem.getDpk());

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


    /**
     * 获取用户钱包
     * ⚠️ 不暴露出去
     *
     * @param uDID
     * @param identityPwd
     * @return
     */
    public Observable<WalletResult> openUserWallet(String uDID, String identityPwd) {
        return _getStoreUserKeyStore(uDID)
                .map(new Function<File, WalletResult>() {
                    @Override
                    public WalletResult apply(File file) throws Exception {
                        return WalletUtils._openWallet(identityPwd, file);
                    }
                });
    }


}