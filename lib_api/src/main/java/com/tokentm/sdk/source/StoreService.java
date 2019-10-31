package com.tokentm.sdk.source;

import com.tokentm.sdk.model.StoreItem;

import java.util.List;

import io.reactivex.Observable;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 存储服务
 */
public interface StoreService extends RepoService {

    /**
     * 云端存储 公开存储
     *
     * @param storeItem
     * @return
     */
    Observable<Long> storePublic(StoreItem<String> storeItem);


    /**
     * 云端存储 私有加密存储
     *
     * @param storeItem
     * @return
     */
    Observable<Long> storePrivate(StoreItem<String> storeItem);

    /**
     * 云端存储 公开存储
     *
     * @param storeItems
     * @return
     */
    Observable<List<Long>> storePublic(List<StoreItem<String>> storeItems);


    /**
     * 云端存储 私有加密存储
     *
     * @param storeItems 自动加密
     * @return
     */
    Observable<List<Long>> storePrivate(List<StoreItem<String>> storeItems);


    /**
     * 获取云端私有存储
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    Observable<StoreItem<String>> getPrivateStore(String did,
                                                  String dataType,
                                                  String dataId);


    /**
     * 获取云端公开存储
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    Observable<StoreItem<String>> getPublicStore(String did,
                                                 String dataType,
                                                 String dataId);


    /**
     * 获取云端私有存储
     *
     * @param did
     * @param dataType
     * @return
     */
    Observable<List<StoreItem<String>>> getPrivateStore(String did,
                                                        String dataType);

    /**
     * 获取云端公开存储
     *
     * @param did
     * @param dataType
     * @return
     */
    Observable<List<StoreItem<String>>> getPublicStore(String did,
                                                       String dataType);


    /**
     * 获取云端存储  的最新版本号
     * -1 代表没有数据版本
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    Observable<Long> getStoreVersion(String did,
                                     String dataType,
                                     String dataId);

}
