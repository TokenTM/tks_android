package com.tokentm.sdk.source;

import com.tokentm.sdk.api.ConfigApiService;
import com.tokentm.sdk.api.DIDApiService;
import com.tokentm.sdk.common.CacheUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.DIDRequestDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.tokentm.sdk.wallet.SignUtils;
import com.tokentm.sdk.wallet.WalletResult;
import com.tokentm.sdk.wallet.WalletUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.http.XXFHttp;

import java.io.File;
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

                        //数据验证签名的密钥对
                        String dataPrivateKey = Sm_crypto.c_GenerateKey();
                        String dataPublicKey = Sm_crypto.c_FromPrvKey(dataPrivateKey);

                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("timestamp", String.valueOf(timestamp));
                        signMap.put("chainAddress", walletResult.getCredentials().getAddress());

                        DIDRequestDTO didRequestDTO = new DIDRequestDTO();
                        didRequestDTO.setTimestamp(timestamp);
                        didRequestDTO.setChainAddress(walletResult.getCredentials().getAddress());
                        didRequestDTO.setChainPrvSign(SignUtils.sign(signMap, walletResult.getPrivateKey()));
                        didRequestDTO.setData(null);
                        didRequestDTO.setDataPubKey(dataPublicKey);
                        didRequestDTO.setSign(SignUtils.sign(signMap, dataPrivateKey));

                        return XXF.getApiService(DIDApiService.class)
                                .createDID(didRequestDTO)
                                .map(new ResponseDTOSimpleFunction<String>())
                                .flatMap(new Function<String, ObservableSource<String>>() {
                                    @Override
                                    public ObservableSource<String> apply(String did) throws Exception {
                                        //备份keystore
                                        //备份身份密码

                                        return null;
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
