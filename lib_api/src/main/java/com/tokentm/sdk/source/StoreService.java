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
    Observable<Long> store(StoreItem<String> storeItem);


    /**
     * 云端存储 自动加密
     *
     * @param storeItem
     * @return
     */
    Observable<Long> storeEncrypt(StoreItem<String> storeItem);

    /**
     * 云端存储
     *
     * @param storeItems
     * @return
     */
    Observable<List<Long>> store(List<StoreItem<String>> storeItems);


    /**
     * 云端存储
     *
     * @param storeItems 自动加密
     * @return
     */
    Observable<List<Long>> storeEncrypt(List<StoreItem<String>> storeItems);


    /**
     * 获取云端存储  自动解密
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    Observable<StoreItem<String>> getStoreDecrypted(String did,
                                                    String dataType,
                                                    String dataId);


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
