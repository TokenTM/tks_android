package com.tokentm.sdk.source;

import com.tokentm.sdk.api.ChainApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.xxf.arch.http.XXFHttp;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ChainRepositoryImpl implements ChainDataSource {
    private static volatile ChainDataSource INSTANCE;

    public static ChainDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (ChainDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChainRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<Long> getNonce(String address) {
        return XXFHttp.getApiService(ChainApiService.class)
                .getNonce(address)
                .map(new ResponseDTOSimpleFunction<Long>());
    }
}
