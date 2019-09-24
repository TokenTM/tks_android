package com.tokentm.sdk;

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
interface CertApiService {


    /**
     * 备份
     *
     * @param backupChunkDTO
     * @return
     */
    @POST("backup")
    Observable<ResponseDTO<Long>> backupData(@Body BackupChunkDTO backupChunkDTO);


    /**
     * 备份 批量
     *
     * @param backupChunkDTOS
     * @return
     */
    @POST("backup/list")
    Observable<ResponseDTO<Long>> backupDatas(@Body List<BackupChunkDTO> backupChunkDTOS);


    /**
     * 获取备份
     *
     * @param version
     * @return
     */
    @GET("backup")
    Observable<ResponseDTO<List<BackupChunkDTO>>> getBackupDatas(@Query("version") long version);


    /**
     * 获取备份 按类别查询
     *
     * @param type
     * @param version
     * @return
     */
    @GET("backup/type")
    Observable<ResponseDTO<List<BackupChunkDTO>>> getBackupDatas(@Query("type") String type, @Query("version") long version);


}
