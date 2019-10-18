package com.tokentm.sdk.api;

import com.tokentm.sdk.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2019-06-14
 * @Description 上链相关api
 */

@BaseUrl(BuildConfig.API_URL_CONFIG)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface ConfigApiService {
    /**
     * 获取备份配置的安全问题
     *
     * @return
     */
    @GET("question")
    Observable<ResponseDTO<List<SecurityQuestionDTO>>> getSecurityQuestionTemplate();
}
