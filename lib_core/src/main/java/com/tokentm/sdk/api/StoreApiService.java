package com.tokentm.sdk.api;

import com.tokentm.sdk.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreItemDecrypted;
import com.tokentm.sdk.model.StoreItemEncrypted;
import com.tokentm.sdk.model.StoreItemSigned;
import com.tokentm.sdk.model.StoreQueryByTypeReqBodyDTO;
import com.tokentm.sdk.model.StoreQueryLimitReqBodyDTO;
import com.tokentm.sdk.model.StoreQueryReqBodyDTO;
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
     * 获取备份配置的安全问题
     * @return
     */
    @GET("store/pub/questions")
    Observable<ResponseDTO<List<SecurityQuestionDTO>>> getSecurityQuestionTemplate();

    /**
     * 云端存储
     *
     * @param body
     * @return
     */
    @POST("store")
    Observable<ResponseDTO<List<Long>>> store(@Body List<StoreItemSigned> body);


    /**
     * 云端存储 自动加密
     *
     * @param encryptBody
     * @return
     */
    @POST("store")
    Observable<ResponseDTO<List<Long>>> storeEncrypt(@Body List<StoreItemEncrypted> encryptBody);

    /**
     * 获取 云端存储
     *
     * @return
     */
    @GET("store")
    Observable<ResponseDTO<StoreItem<String>>> getStore(@Body StoreQueryReqBodyDTO body);


    /**
     * 获取 云端存储  自动解密
     *
     * @param body
     * @return
     */
    @GET("store")
    Observable<ResponseDTO<StoreItemDecrypted>> getStoreDecrypted(@Body StoreQueryReqBodyDTO body);


    /**
     * 获取 云端存储 次数限制的(1天一次),不建议使用!
     *
     * @param body
     * @return
     */
    @GET("store/limited")
    Observable<ResponseDTO<StoreItem<String>>> getStoreLimited(@Body StoreQueryLimitReqBodyDTO body);

    /**
     * 按类型查询 云端存储
     *
     * @param body
     * @return
     */
    @GET("store/data_type")
    Observable<ResponseDTO<List<StoreItem<String>>>> getStoreByType(@Body StoreQueryByTypeReqBodyDTO body);


    /**
     * 按类型查询 云端存储  自动解密
     *
     * @param body
     * @return
     */
    @GET("store/data_type")
    Observable<ResponseDTO<List<StoreItemDecrypted>>> getStoreByTypeDecrypted(@Body StoreQueryByTypeReqBodyDTO body);
}
