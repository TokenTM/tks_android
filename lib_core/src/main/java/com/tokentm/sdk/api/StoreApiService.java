package com.tokentm.sdk.api;

import com.tokentm.sdk.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.StoreQueryByTypeReqBodyDTO;
import com.tokentm.sdk.model.StoreQueryReqBodyDTO;
import com.tokentm.sdk.model.StoreReqBodyDTO;
import com.tokentm.sdk.model.DecryptedStoreItem;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@BaseUrl(BuildConfig.API_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface StoreApiService {


    /**
     * 云端存储
     *
     * @param body
     * @return
     */
    @POST("store")
    Observable<ResponseDTO<List<Long>>> store(@Body List<StoreReqBodyDTO> body);


    /**
     * 获取 云端存储
     *
     * @return
     */
    @GET("store")
    Observable<ResponseDTO<DecryptedStoreItem>> getStore(@Body StoreQueryReqBodyDTO body);


    /**
     * 按类型查询 云端存储
     *
     * @return
     */
    @GET("store/data_type")
    Observable<ResponseDTO<List<DecryptedStoreItem>>> getStoreByType(@Body StoreQueryByTypeReqBodyDTO body);
}
