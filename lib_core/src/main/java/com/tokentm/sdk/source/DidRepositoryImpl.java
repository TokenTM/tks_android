package com.tokentm.sdk.source;

import android.util.SparseArray;

import io.reactivex.Observable;

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
    public Observable<String> createDID(String identityPwd, SparseArray<String> securityQuestionAnswers) {
        return null;
    }

    @Override
    public Observable<Boolean> decrypt(String DID, String identityPwd) {
        return null;
    }

    @Override
    public Observable<Boolean> decrypt(String DID, SparseArray<String> securityQuestionAnswers) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, String oldIdentityPwd, String newIdentityPwd) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, SparseArray<String> oldSecurityQuestionAnswers, SparseArray<String> newSecurityQuestionAnswers) {
        return null;
    }

    @Override
    public Observable<Boolean> reset(String DID, SparseArray<String> oldSecurityQuestionAnswers, String newIdentityPwd) {
        return null;
    }
}
