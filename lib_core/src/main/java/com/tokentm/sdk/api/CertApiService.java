package com.tokentm.sdk.api;

import com.tokentm.sdk.core.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.CompanyCertReqBodyDTO;
import com.tokentm.sdk.model.UserCertByIdCardReqBodyDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface CertApiService {

    /**
     * 用户 身份证实名
     *
     * @param body
     * @return
     */
    @POST("auth/user")
    Observable<ResponseDTO<String>> userCertByIdCard(@Body UserCertByIdCardReqBodyDTO body);


    /**
     * 用户 身份证实名
     *
     * @param body
     * @return
     */
    @POST("auth/company")
    Observable<ResponseDTO<String>> companyCert(@Body CompanyCertReqBodyDTO body);
}
