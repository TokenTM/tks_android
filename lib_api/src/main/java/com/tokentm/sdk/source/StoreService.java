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
     * 云端存储 自动加密
     *
     * @param storeItem
     * @return
     */
    Observable<Long> storeEncrypt(StoreItem storeItem);

    /**
     * 云端存储
     *
     * @param storeItems
     * @return
     */
    Observable<List<Long>> store(List<StoreItem> storeItems);


    /**
     * 云端存储
     *
     * @param storeItems 自动加密
     * @return
     */
    Observable<List<Long>> storeEncrypt(List<StoreItem> storeItems);

    /**
     * 获取云端存储
     *
     * @param t        解析的data类型
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    <T extends StoreItem> Observable<T> getStore(Class<T> t,
                                          String did,
                                          String dataType,
                                          String dataId);


    /**
     * 获取云端存储  自动解密
     *
     * @param t        解析的data类型
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    <T> Observable<StoreItem<T>> getStoreDecrypted(Class<StoreItem<T>> t,
                                                   String did,
                                                   String dataType,
                                                   String dataId);

    /**
     * 批量 云端存储
     *
     * @param t        解析的data类型
     * @param did
     * @param dataType
     * @return
     */
    <T> Observable<List<StoreItem<T>>> getStore(
            Class<StoreItem<T>> t,
            String did,
            String dataType
    );

    /**
     * 批量 云端存储
     *
     * @param t        解析的data类型
     * @param did
     * @param dataType
     * @return
     */
    <T> Observable<List<StoreItem<T>>> getStoreDecrypted(
            Class<StoreItem<T>> t,
            String did,
            String dataType
    );
}
