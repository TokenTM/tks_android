package com.tokentm.sdk.source;

import android.support.annotation.RestrictTo;

import com.tokentm.sdk.api.StoreApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.common.encrypt.TEAUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreItemReqBodyDTO;
import com.tokentm.sdk.model.StoreQueryByTypeReqBodyDTO;
import com.xxf.arch.XXF;
import com.xxf.arch.http.XXFHttp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
    public Observable<Long> storePublic(StoreItem<String> storeItem) {
        return this.storePublic(Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public Observable<Long> storePrivate(StoreItem<String> storeItem) {
        return this.storePrivate(Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public Observable<List<Long>> storePublic(List<StoreItem<String>> storeItems) {
        return _attachNewVersionInner(storeItems)
                .map(new Function<List<StoreItem<String>>, List<StoreItemReqBodyDTO>>() {
                    @Override
                    public List<StoreItemReqBodyDTO> apply(List<StoreItem<String>> newVersionStoreItems) throws Exception {
                        List<StoreItemReqBodyDTO> storeItemReqBodyDTOS = new ArrayList<>();
                        for (StoreItem<String> storeItem : newVersionStoreItems) {
                            StoreItemReqBodyDTO storeItemReqBodyDTO = new StoreItemReqBodyDTO();
                            storeItemReqBodyDTO.setDid(storeItem.getDid());
                            storeItemReqBodyDTO.setDataType(storeItem.getDataType());
                            storeItemReqBodyDTO.setDataId(storeItem.getDataId());
                            storeItemReqBodyDTO.setData(storeItem.getData());
                            storeItemReqBodyDTO.setVersion(storeItem.getVersion());
                            storeItemReqBodyDTO.setTimestamp(System.currentTimeMillis());


                            //data sign
                            Map<String, String> dataSignMap = new HashMap<>();
                            dataSignMap.put("data", storeItem.getData());
                            String dataSign = SignUtils.sign(dataSignMap, getUserDPK(storeItemReqBodyDTO.getDid()));
                            storeItemReqBodyDTO.setDataSign(dataSign);

                            storeItemReqBodyDTO.setSign(SignUtils.signByDataPk(storeItemReqBodyDTO, getUserDPK(storeItemReqBodyDTO.getDid())));

                            storeItemReqBodyDTOS.add(storeItemReqBodyDTO);
                        }
                        return storeItemReqBodyDTOS;
                    }
                })
                .flatMap(new Function<List<StoreItemReqBodyDTO>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<StoreItemReqBodyDTO> storeItemReqBodyDTOS) throws Exception {
                        return storeApiService
                                .store(storeItemReqBodyDTOS)
                                .map(new ResponseDTOSimpleFunction<List<Long>>());
                    }
                });
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
    public Observable<List<Long>> storePrivate(List<StoreItem<String>> storeItems) {
        return _attachNewVersionInner(storeItems)
                .map(new Function<List<StoreItem<String>>, List<StoreItemReqBodyDTO>>() {
                    @Override
                    public List<StoreItemReqBodyDTO> apply(List<StoreItem<String>> newVersionStoreItems) throws Exception {
                        List<StoreItemReqBodyDTO> storeItemReqBodyDTOS = new ArrayList<>();
                        for (StoreItem<String> storeItem : newVersionStoreItems) {
                            StoreItemReqBodyDTO storeItemReqBodyDTO = new StoreItemReqBodyDTO();
                            storeItemReqBodyDTO.setDid(storeItem.getDid());
                            storeItemReqBodyDTO.setDataType(storeItem.getDataType());
                            storeItemReqBodyDTO.setDataId(storeItem.getDataId());
                            //加密数据
                            String encryptData = TEAUtils.encryptString(storeItem.getData(), getUserDPK(storeItem.getDid()));
                            storeItemReqBodyDTO.setData(encryptData);

                            storeItemReqBodyDTO.setVersion(storeItem.getVersion());
                            storeItemReqBodyDTO.setTimestamp(System.currentTimeMillis());


                            //data sign
                            Map<String, String> dataSignMap = new HashMap<>();
                            dataSignMap.put("data", storeItem.getData());
                            String dataSign = SignUtils.sign(dataSignMap, getUserDPK(storeItemReqBodyDTO.getDid()));
                            storeItemReqBodyDTO.setDataSign(dataSign);

                            storeItemReqBodyDTO.setSign(SignUtils.signByDataPk(storeItemReqBodyDTO, getUserDPK(storeItemReqBodyDTO.getDid())));

                            storeItemReqBodyDTOS.add(storeItemReqBodyDTO);
                        }
                        return storeItemReqBodyDTOS;
                    }
                })
                .flatMap(new Function<List<StoreItemReqBodyDTO>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<StoreItemReqBodyDTO> storeItemReqBodyDTOS) throws Exception {
                        return storeApiService
                                .store(storeItemReqBodyDTOS)
                                .map(new ResponseDTOSimpleFunction<List<Long>>());
                    }
                });
    }

    @Override
    public Observable<StoreItem<String>> getPrivateStore(String did, String dataType, String dataId) {
        return getPublicStore(did, dataType, dataId)
                .map(new Function<StoreItem<String>, StoreItem<String>>() {
                    @Override
                    public StoreItem<String> apply(StoreItem<String> stringStoreItem) throws Exception {
                        //解密
                        String decodeString = TEAUtils.decryptString(stringStoreItem.getData(), getUserDPK(stringStoreItem.getDid()));
                        stringStoreItem.setData(decodeString);
                        return stringStoreItem;
                    }
                });
    }

    @Override
    public Observable<StoreItem<String>> getPublicStore(String did, String dataType, String dataId) {
        long timestamp = System.currentTimeMillis();
        // api sign
        Map<String, String> signMap = new HashMap<>();
        signMap.put("dataId", dataId);
        signMap.put("dataType", dataType);
        signMap.put("did", did);
        signMap.put("timestamp", String.valueOf(timestamp));

        String sign = SignUtils.sign(signMap, getUserDPK(did));
        return storeApiService
                .getStore(did, dataType, dataId, sign, timestamp)
                .map(new ResponseDTOSimpleFunction<>());
    }

    @Override
    public Observable<List<StoreItem<String>>> getPrivateStore(String did, String dataType) {
        return getPrivateStore(did, dataType)
                .map(new Function<List<StoreItem<String>>, List<StoreItem<String>>>() {
                    @Override
                    public List<StoreItem<String>> apply(List<StoreItem<String>> storeItems) throws Exception {
                        for (StoreItem<String> stringStoreItem : storeItems) {
                            //解密
                            String decodeString = TEAUtils.decryptString(stringStoreItem.getData(), getUserDPK(stringStoreItem.getDid()));
                            stringStoreItem.setData(decodeString);
                        }
                        return storeItems;
                    }
                });
    }

    @Override
    public Observable<List<StoreItem<String>>> getPublicStore(String did, String dataType) {
        return Observable
                .fromCallable(new Callable<StoreQueryByTypeReqBodyDTO>() {
                    @Override
                    public StoreQueryByTypeReqBodyDTO call() throws Exception {
                        StoreQueryByTypeReqBodyDTO signBody = new StoreQueryByTypeReqBodyDTO();
                        signBody.setDid(did);
                        signBody.setDataType(dataType);
                        signBody.setTimestamp(System.currentTimeMillis());
                        signBody.setSign(SignUtils.signByDataPk(signBody, getUserDPK(did)));
                        return signBody;
                    }
                })
                .flatMap(new Function<StoreQueryByTypeReqBodyDTO, ObservableSource<List<StoreItem<String>>>>() {
                    @Override
                    public ObservableSource<List<StoreItem<String>>> apply(StoreQueryByTypeReqBodyDTO storeQueryByTypeReqBodyDTO) throws Exception {
                        return storeApiService.getStore(storeQueryByTypeReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<>());
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
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Observable<StoreItem<String>> getStore(String did,
                                                  String dataType,
                                                  String dataId,
                                                  String phone,
                                                  String smsCode) {
        return XXF.getApiService(StoreApiService.class)
                .getStore(did, dataType, dataId, phone, smsCode)
                .map(new ResponseDTOSimpleFunction<>());
    }

    @Override
    public Observable<Long> getStoreVersion(String did, String dataType, String dataId) {
        return getPublicStore(did, dataType, dataId)
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
