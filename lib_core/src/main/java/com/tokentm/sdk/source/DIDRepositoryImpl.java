package com.tokentm.sdk.source;

import com.tokentm.sdk.api.DIDApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.DIDForkReqDTO;
import com.tokentm.sdk.model.DIDReqDTO;
import com.xxf.arch.XXF;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DIDRepositoryImpl implements DIDService {
    private static volatile DIDService INSTANCE;

    public static DIDService getInstance() {
        if (INSTANCE == null) {
            synchronized (DIDRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DIDRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public Observable<String> createDID(String phone, String smsCode, String chainAddress, String chainPubKey, String chainPrivateKey, String data, String dataPubKey, String dataPrivateKey) {
        return Observable
                .fromCallable(new Callable<DIDReqDTO>() {
                    @Override
                    public DIDReqDTO call() throws Exception {
                        //时间戳
                        long timestamp = System.currentTimeMillis();

                        XXF.getLogger().d("========>chainPublicKey=" + chainPubKey);
                        XXF.getLogger().d("========>dataPrivateKey=" + dataPrivateKey);
                        XXF.getLogger().d("========>dataPublicKey=" + dataPubKey);

                        DIDReqDTO didReqDTO = new DIDReqDTO();
                        didReqDTO.setTimestamp(timestamp);
                        didReqDTO.setChainAddress(chainAddress);
                        didReqDTO.setChainPubKey(chainPubKey);
                        didReqDTO.setData(null);
                        didReqDTO.setDataPubKey(dataPubKey);
                        didReqDTO.setPhone(phone);
                        didReqDTO.setCode(smsCode);
                        didReqDTO.setChainPrvSign(SignUtils.signByChainPk(didReqDTO, chainPrivateKey));
                        didReqDTO.setSign(SignUtils.signByDataPk(didReqDTO, dataPrivateKey));
                        return didReqDTO;
                    }
                })
                .flatMap(new Function<DIDReqDTO, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(DIDReqDTO didReqDTO) throws Exception {
                        return XXF.getApiService(DIDApiService.class)
                                .createDID(didReqDTO)
                                .map(new ResponseDTOSimpleFunction<>());
                    }
                });
    }

    @Override
    public Observable<String> forkDID(String fromDid, String chainAddress, String chainPubKey, String chainPrivateKey, String data, String dataPubKey, String dataPrivateKey) {
        return Observable
                .fromCallable(new Callable<DIDForkReqDTO>() {
                    @Override
                    public DIDForkReqDTO call() throws Exception {
                        //时间戳
                        long timestamp = System.currentTimeMillis();

                        XXF.getLogger().d("========>chainPublicKey=" + chainPubKey);
                        XXF.getLogger().d("========>dataPrivateKey=" + dataPrivateKey);
                        XXF.getLogger().d("========>dataPublicKey=" + dataPubKey);

                        DIDForkReqDTO didReqDTO = new DIDForkReqDTO();
                        didReqDTO.setTimestamp(timestamp);
                        didReqDTO.setChainAddress(chainAddress);
                        didReqDTO.setChainPubKey(chainPubKey);
                        didReqDTO.setData(null);
                        didReqDTO.setDataPubKey(dataPubKey);
                        didReqDTO.setChainPrvSign(SignUtils.signByChainPk(didReqDTO, chainPrivateKey));
                        didReqDTO.setSign(SignUtils.signByDataPk(didReqDTO, dataPrivateKey));

                        return didReqDTO;
                    }
                })
                .flatMap(new Function<DIDForkReqDTO, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(DIDForkReqDTO didReqDTO) throws Exception {
                        return XXF.getApiService(DIDApiService.class)
                                .forkDID(didReqDTO)
                                .map(new ResponseDTOSimpleFunction<>());
                    }
                });
    }

}
