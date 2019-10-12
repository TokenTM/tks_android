package com.tokentm.sdk.source;

import com.tokentm.sdk.model.StoreItem;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface StoreService extends RepoService {


    /**
     * 云端存储
     *
     * @param did
     * @param dataType
     * @param data
     * @param version
     * @return 最新的version
     */
    Observable<Long> store(String did,
                           String dataType,
                           String data,
                           long version);


    /**
     * 获取云端存储
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    Observable<StoreItem> getStore(String did, String dataType, String dataId);

    /**
     * 批量云端存储
     *
     * @param did
     * @return
     */
    Observable<List<StoreItem>> getStore(String did);
}
