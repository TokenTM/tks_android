package com.tokentm.sdk.api;

import com.tokentm.sdk.core.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.CompanyCertProxyReqBodyDTO;
import com.tokentm.sdk.model.UserCertByIdCardProxyReqBodyDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface CertProxyApiService {

    /**
     * 用户 身份证实名
     *
     * @param body
     * @return
     */
    @POST("auth/user")
    Observable<ResponseDTO<String>> userCertByIdCard(@Body UserCertByIdCardProxyReqBodyDTO body);

    /**
     * 是否完成了用户身份证认证
     *
     * @param did
     * @return
     */
    @GET("auth/user/authentication/{did}")
    Observable<ResponseDTO<Boolean>> isUserCertByIdCard(@Path("did") String did);


    /**
     * 用户 身份证实名
     *
     * @param body
     * @return
     */
    @POST("auth/company")
    Observable<ResponseDTO<String>> companyCert(@Body CompanyCertProxyReqBodyDTO body);


    /**
     * 是否完成了公司认证
     *
     * @param did
     * @return
     */
    @GET("auth/company/authentication/{did}")
    Observable<ResponseDTO<Boolean>> isCompanyCert(@Path("did") String did);
}
