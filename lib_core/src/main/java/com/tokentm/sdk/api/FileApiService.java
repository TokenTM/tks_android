package com.tokentm.sdk.api;

import com.tokentm.sdk.core.BuildConfig;
import com.tokentm.sdk.http.DefaultRxHttpCacheDirectoryProvider;
import com.tokentm.sdk.http.GlobalGsonConvertInterceptor;
import com.tokentm.sdk.http.ResponseDTO;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;
import com.xxf.arch.annotation.RxHttpCacheProvider;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
@BaseUrl(BuildConfig.API_FILE_URL)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface FileApiService {

    /**
     * 文件上传
     *
     * @param did
     * @param file
     * @param fileMd5
     * @param sign
     * @param targetDid
     * @param timestamp
     * @return
     */
    @Multipart
    @POST("file/upload")
    Observable<ResponseDTO<String>> upload(@Query("did") String did,
                                           @Part MultipartBody.Part file,
                                           @Query("fileMd5") String fileMd5,
                                           @Query("sign") String sign,
                                           @Query("targetDid") String targetDid,
                                           @Query("timestamp") long timestamp
    );
}
