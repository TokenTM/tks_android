package com.tokentm.sdk.api;

import com.tokentm.sdk.core.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface BasicApiService {

    /**
     * 获取组织公函
     *
     * @param did
     * @param orgName
     * @param sign
     * @param timestamp
     * @param userIdentityCode
     * @param userName
     * @return
     */
    @GET("basic/letter/image/org")
    Observable<ResponseBody> getOrgLetterImage(@Query("did") String did,
                                               @Query("orgName") String orgName,
                                               @Query("sign") String sign,
                                               @Query("timestamp") long timestamp,
                                               @Query("userIdentityCode") String userIdentityCode,
                                               @Query("userName") String userName
    );

    @GET("basic/code/phone/{phone}")
    Observable<ResponseDTO<Boolean>> sendSmsCode(@Path("phone") String phone);

}
