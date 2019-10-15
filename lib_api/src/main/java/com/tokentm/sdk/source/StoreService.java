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
     * @param storeItem
     * @return
     */
    Observable<Long> store(StoreItem storeItem);


    /**
     * 云端存储
     *
     * @param storeItems
     * @return
     */
    Observable<List<Long>> store(List<StoreItem> storeItems);


    /**
     * 获取云端存储
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    Observable<StoreItem> getStore(String did,
                                   String dataType,
                                   String dataId);

    /**
     * 批量 云端存储
     *
     * @param did
     * @param dataType
     * @return
     */
    Observable<List<StoreItem>> getStore(String did,
                                         String dataType
    );
}
