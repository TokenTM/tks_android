package com.tokentm.sdk.api;


import com.tokentm.sdk.core.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.ServiceEncryptDecryptDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface EncryptApiService {

    /**
     * 加密
     *
     * @param url
     * @param serviceEncryptDecryptDTO
     * @return
     */
    @POST
    Observable<ResponseDTO<String>> encrypt(@Url String url, @Body ServiceEncryptDecryptDTO serviceEncryptDecryptDTO);

    /**
     * 解密
     *
     * @param url
     * @param serviceEncryptDecryptDTO
     * @return
     */
    @POST
    Observable<ResponseDTO<String>> decrypt(@Url String url, @Body ServiceEncryptDecryptDTO serviceEncryptDecryptDTO);
}
