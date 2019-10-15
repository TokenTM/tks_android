package com.tokentm.sdk.api;

import com.tokentm.sdk.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.DIDReqDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description did相关  http://192.168.21.110:8205/swagger-ui.html#/
 */
@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface DIDApiService {
    /**
     * 创建did
     * @param body
     * @return
     */
    @POST("did")
    Observable<ResponseDTO<String>> createDID(@Body DIDReqDTO body);
}
