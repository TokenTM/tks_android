package com.tokentm.sdk.http;

import android.app.Application;

import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;

import java.io.File;
import java.util.Objects;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description rx http缓存
 */
public class DefaultRxHttpCacheDirectoryProvider implements HttpCacheDirectoryProvider {
    static Application context;

    public static void init(Application application) {
        context = Objects.requireNonNull(application);
    }

    @Override
    public long maxSize() {
        //100M
        return 100 * 1024 * 1024;
    }

    @Override
    public String getDirectory() {
        if (context != null) {
            File file = new File(context.getCacheDir(), "rxHttpCache");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();

        }
        return null;
    }
}
