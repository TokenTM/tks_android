package com.tokentm.sdk;

import android.app.Application;
import android.content.Context;

import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.source.BackupDataSource;
import com.tokentm.sdk.source.BackupRepositoryImpl;
import com.tokentm.sdk.source.ChainDataSource;
import com.tokentm.sdk.source.ChainRepositoryImpl;
import com.tokentm.sdk.source.DidDataSource;
import com.tokentm.sdk.source.DidRepositoryImpl;
import com.tokentm.sdk.source.RepoService;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class TokenTmClient {
    private static Application _application;
    private static final ConcurrentHashMap<Class, RepoService> SERVICE_MAP = new ConcurrentHashMap<>();

    public static void init(Context context) {
        _application = (Application) Objects.requireNonNull(context.getApplicationContext());
        DefaultRxHttpCacheDirectoryProvider.init(_application);
        registerService();
    }

//    /**
//     * fix java.security.NoSuchAlgorithmException: KeyPairGenerator ECDSA
//     */
//    private static void setupBouncyCastle() {
//        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
//        if (provider == null) {
//            // Web3j will set up the provider lazily when it's first used.
//            return;
//        }
//        if (provider.getClass().equals(BouncyCastleProvider.class)) {
//            // BC with same package name, shouldn't happen in real life.
//            return;
//        }
//        // Android registers its own BC provider. As it might be outdated and might not include
//        // all needed ciphers, we substitute it with a known BC bundled in the app.
//        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
//        // of that it's possible to have another BC implementation loaded in VM.
//        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
//        Security.insertProviderAt(new BouncyCastleProvider(), 1);
//    }

    /**
     * 注册数据服务
     */
    private static void registerService() {
        SERVICE_MAP.put(BackupDataSource.class, BackupRepositoryImpl.getInstance());
        SERVICE_MAP.put(ChainDataSource.class, ChainRepositoryImpl.getInstance());
        SERVICE_MAP.put(DidDataSource.class, DidRepositoryImpl.getInstance());
    }


    static Application get_application() {
        return _application;
    }

    /**
     * 获取服务
     *
     * @param repoService
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> repoService) {
        return (T) SERVICE_MAP.get(repoService);
    }
}
