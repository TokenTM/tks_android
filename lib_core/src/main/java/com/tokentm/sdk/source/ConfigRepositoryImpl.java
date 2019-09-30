package com.tokentm.sdk.source;

import com.tokentm.sdk.api.ConfigApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.xxf.arch.http.XXFHttp;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ConfigRepositoryImpl implements ConfigDataSource {
    private static volatile ConfigDataSource INSTANCE;

    public static ConfigDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (ChainDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConfigRepositoryImpl();
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
}
