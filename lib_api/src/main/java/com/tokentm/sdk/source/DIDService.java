package com.tokentm.sdk.source;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description did服务
 */
public interface DIDService extends RepoService {

    /**
     * 创建did
     *
     * @param phone
     * @param smsCode
     * @param chainAddress
     * @param chainPubKey
     * @param chainPrivateKey
     * @param data
     * @param dataPubKey
     * @param dataPrivateKey
     * @return
     */
    Observable<String> createDID(String phone,
                                 String smsCode,
                                 String chainAddress,
                                 String chainPubKey,
                                 String chainPrivateKey,
                                 @Nullable String data,
                                 String dataPubKey,
                                 String dataPrivateKey
    );

    /**
     * fork did
     *
     * @param fromDid
     * @param chainAddress
     * @param chainPubKey
     * @param chainPrivateKey
     * @param data
     * @param dataPubKey
     * @param dataPrivateKey
     * @return
     */
    Observable<String> forkDID(String fromDid,
                               String chainAddress,
                               String chainPubKey,
                               String chainPrivateKey,
                               @Nullable String data,
                               String dataPubKey,
                               String dataPrivateKey);

}
