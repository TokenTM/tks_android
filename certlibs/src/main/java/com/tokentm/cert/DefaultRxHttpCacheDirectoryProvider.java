package com.tokentm.cert;

import com.xxf.arch.http.cache.HttpCacheDirectoryProvider;

import java.io.File;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description rx http缓存
 */
public class DefaultRxHttpCacheDirectoryProvider implements HttpCacheDirectoryProvider {

    @Override
    public long maxSize() {
        //100M
        return 100 * 1024 * 1024;
    }

    @Override
    public String getDirectory() {
        File file = new File(CertClient.get_application().getCacheDir(), "rxHttpCache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
