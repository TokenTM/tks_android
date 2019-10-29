package com.tokentm.sdk.api;

import com.tokentm.sdk.core.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreItemDecrypted;
import com.tokentm.sdk.model.StoreItemEncrypted;
import com.tokentm.sdk.model.StoreItemSigned;
import com.tokentm.sdk.model.StoreQueryByTypeReqBodyDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
     * 手机号验证码获取数据
     *
     * @param did
     * @param dataType
     * @param dataId
     * @param phone
     * @param smsCode
     * @return
     */
    @GET("store/phone")
    Observable<ResponseDTO<StoreItem<String>>> getStore(@Query("did") String did,
                                                        @Query("dataType") String dataType,
                                                        @Query("dataId") String dataId,
                                                        @Query("phone") String phone,
                                                        @Query("code") String smsCode);


    /**
     * 获取 云端存储  自动解密
     *
     * @param did
     * @param dataType
     * @param dataId
     * @param sign
     * @param timestamp
     * @return
     */
    @GET("store")
    Observable<ResponseDTO<StoreItemDecrypted>> getStoreDecrypted(@Query("did") String did,
                                                                  @Query("dataType") String dataType,
                                                                  @Query("dataId") String dataId,
                                                                  @Query("sign") String sign,
                                                                  @Query("timestamp") long timestamp);


    /**
     * 获取 云端存储
     *
     * @param did
     * @param dataType
     * @param dataId
     * @param sign
     * @param timestamp
     * @return
     */
    @GET("store")
    Observable<ResponseDTO<StoreItem<String>>> getStore(@Query("did") String did,
                                             @Query("dataType") String dataType,
                                             @Query("dataId") String dataId,
                                             @Query("sign") String sign,
                                             @Query("timestamp") long timestamp);


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
