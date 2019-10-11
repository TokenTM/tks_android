package com.tokentm.sdk.source;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface ChainDataSource extends RepoService {


    /**
     * 获取nonce
     *
     * @param address
     * @return
     */
    Observable<Long> getNonce(String address);


}

