package com.tokentm.sdk.api;

import com.tokentm.sdk.model.UserPropertyRightsTransferRecords;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.RxHttpCache;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 物权转移记录服务接口
 */
@BaseUrl("http://www.mocky.io/")
public interface PropertyRightsTransferRecordsService {

    /**
     * 获取物权转移记录服
     * TODO 此服务接口是假的,要和后台调试
     * @return
     */
    @GET("v2/5dc26da52f00005b004bdfff")
    @RxHttpCache(value = RxHttpCache.CacheType.firstCache)
    Observable<List<UserPropertyRightsTransferRecords>> getUserPropertyRightsTransferRecords();

}
