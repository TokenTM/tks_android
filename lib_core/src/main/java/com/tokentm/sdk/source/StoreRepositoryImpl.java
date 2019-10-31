package com.tokentm.sdk.source;

import com.tokentm.sdk.api.StoreApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreItemDecrypted;
import com.tokentm.sdk.model.StoreItemEncrypted;
import com.tokentm.sdk.model.StoreItemSigned;
import com.xxf.arch.XXF;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.json.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class StoreRepositoryImpl implements StoreService, BaseRepo {
    private static volatile StoreService INSTANCE;

    public static StoreService getInstance() {
        if (INSTANCE == null) {
            synchronized (StoreService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StoreRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    StoreApiService storeApiService;

    private StoreRepositoryImpl() {
        storeApiService = XXFHttp.getApiService(StoreApiService.class);
    }


    @Override
    public Observable<Long> store(StoreItem<String> storeItem) {
        return this.store(Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public Observable<Long> storeEncrypt(StoreItem<String> storeItem) {
        return this.storeEncrypt(Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public Observable<List<Long>> store(List<StoreItem<String>> storeItems) {
        return _attachNewVersionInner(storeItems)
                .map(new Function<List<StoreItem<String>>, List<StoreItemSigned>>() {
                    @Override
                    public List<StoreItemSigned> apply(List<StoreItem<String>> newVersionStoreItems) throws Exception {
                        //转化成自动签名的json
                        String jsonString = JsonUtils.toJsonString(newVersionStoreItems);
                        List<StoreItemSigned> storeStringItems = JsonUtils.toBeanList(jsonString, StoreItemSigned.class);
                        return storeStringItems;
                    }
                })
                .flatMap(new Function<List<StoreItemSigned>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<StoreItemSigned> storeItems) throws Exception {
                        return storeApiService
                                .store(storeItems)
                                .map(new ResponseDTOSimpleFunction<List<Long>>());
                    }
                });
    }

    /**
     * 通过手机号+验证码获取数据
     * ⚠️ 不暴露出去,不推荐
     *
     * @param did
     * @param dataType
     * @param dataId
     * @param phone
     * @param smsCode
     * @return
     */
    public Observable<StoreItem<String>> getStore(String did,
                                                  String dataType,
                                                  String dataId,
                                                  String phone,
                                                  String smsCode) {
        return XXF.getApiService(StoreApiService.class)
                .getStore(did, dataType, dataId, phone, smsCode)
                .map(new ResponseDTOSimpleFunction<>());
    }


    /**
     * 附加最新版本号
     *
     * @param storeItems
     * @return
     */
    private Observable<List<StoreItem<String>>> _attachNewVersionInner(List<StoreItem<String>> storeItems) {
        return Observable.just(storeItems)
                .flatMap(new Function<List<StoreItem<String>>, ObservableSource<List<StoreItem<String>>>>() {
                    @Override
                    public ObservableSource<List<StoreItem<String>>> apply(List<StoreItem<String>> storeItems) throws Exception {
                        List<Observable<StoreItem<String>>> storeItemObservable = new ArrayList<>();
                        for (StoreItem<String> storeItem : storeItems) {
                            storeItemObservable.add(
                                    getStoreVersion(
                                            storeItem.getDid(),
                                            storeItem.getDataType(),
                                            storeItem.getDataId())
                                            .map(new Function<Long, StoreItem<String>>() {
                                                @Override
                                                public StoreItem<String> apply(Long aLong) throws Exception {
                                                    if (aLong >= 0) {
                                                        //将新版本的version+1传递
                                                        long newVersion = aLong + 1;
                                                        storeItem.setVersion(newVersion);
                                                    }
                                                    return storeItem;
                                                }
                                            })
                            );
                        }
                        return Observable.zip(storeItemObservable, new Function<Object[], List<StoreItem<String>>>() {
                            @Override
                            public List<StoreItem<String>> apply(Object[] objects) throws Exception {
                                List<StoreItem<String>> attachedVersionList = new ArrayList<>();
                                for (Object obj : objects) {
                                    attachedVersionList.add((StoreItem<String>) obj);
                                }
                                return attachedVersionList;
                            }
                        });
                    }
                });
    }

    @Override
    public Observable<List<Long>> storeEncrypt(List<StoreItem<String>> storeItems) {
        return _attachNewVersionInner(storeItems)
                .map(new Function<List<StoreItem<String>>, List<StoreItemEncrypted>>() {
                    @Override
                    public List<StoreItemEncrypted> apply(List<StoreItem<String>> newVersionStoreItems) throws Exception {
                        //json 转化自动签名和加解密的模型
                        String jsonString = JsonUtils.toJsonString(newVersionStoreItems);
                        List<StoreItemEncrypted> storeItemEncrypteds = JsonUtils.toBeanList(jsonString, StoreItemEncrypted.class);
                        return storeItemEncrypteds;
                    }
                })
                .flatMap(new Function<List<StoreItemEncrypted>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<StoreItemEncrypted> storeItems) throws Exception {
                        return storeApiService
                                .storeEncrypt(storeItems)
                                .map(new ResponseDTOSimpleFunction<List<Long>>());
                    }
                });
    }

    @Override
    public Observable<StoreItem<String>> getStoreDecrypted(String did, String dataType, String dataId) {
        long timestamp = System.currentTimeMillis();

        // api sign
        Map<String, String> signMap = new HashMap<>();
        signMap.put("dataId", dataId);
        signMap.put("dataType", dataType);
        signMap.put("did", did);
        signMap.put("timestamp", String.valueOf(timestamp));

        String sign = SignUtils.sign(signMap, _getDPK(did));

        return storeApiService
                .getStoreDecrypted(did, dataType, dataId, sign, timestamp)
                .map(new ResponseDTOSimpleFunction<StoreItemDecrypted>());
    }

    /**
     * 获取数据
     *
     * @param did
     * @param dataType
     * @param dataId
     * @return
     */
    private Observable<StoreItem<String>> _getStoreInner(String did, String dataType, String dataId) {
        long timestamp = System.currentTimeMillis();
        // api sign
        Map<String, String> signMap = new HashMap<>();
        signMap.put("dataId", dataId);
        signMap.put("dataType", dataType);
        signMap.put("did", did);
        signMap.put("timestamp", String.valueOf(timestamp));

        String sign = SignUtils.sign(signMap, _getDPK(did));
        return storeApiService
                .getStore(did, dataType, dataId, sign, timestamp)
                .map(new ResponseDTOSimpleFunction<>());
    }

    @Override
    public Observable<Long> getStoreVersion(String did, String dataType, String dataId) {
        return _getStoreInner(did, dataType, dataId)
                .map(new Function<StoreItem<String>, Long>() {
                    @Override
                    public Long apply(StoreItem<String> stringStoreItem) throws Exception {
                        return stringStoreItem.getVersion();
                    }
                })
                .onErrorReturn(new Function<Throwable, Long>() {
                    @Override
                    public Long apply(Throwable throwable) throws Exception {
                        //如果数据data 等于null 说明服务器没有对应的版本
                      /*  if (throwable instanceof ResponseException
                                && ((ResponseException) throwable).code == ResponseDTO.CODE_BODY_NULL) {
                            return -1L;
                        }
                        throw new RuntimeException(throwable);*/
                        return -1L;
                    }
                });
    }

}
