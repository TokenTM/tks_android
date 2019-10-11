package com.tokentm.sdk.source;

import com.tokentm.sdk.api.ConfigApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.xxf.arch.http.XXFHttp;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DidRepositoryImpl implements DidDataSource {
    private static volatile DidDataSource INSTANCE;

    public static DidDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (DidDataSource.class) {
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
        //1. 创建did
        //2. 备份身份密码和安全找回问题

        return null;
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
