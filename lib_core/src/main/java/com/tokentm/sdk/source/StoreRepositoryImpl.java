package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokentm.sdk.api.StoreApiService;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreItemDecrypted;
import com.tokentm.sdk.model.StoreItemEncrypted;
import com.tokentm.sdk.model.StoreItemSigned;
import com.tokentm.sdk.model.StoreQueryByTypeReqBodyDTO;
import com.tokentm.sdk.model.StoreQueryReqBodyDTO;
import com.tokentm.sdk.wallet.SignUtils;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.json.JsonUtils;

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
public class StoreRepositoryImpl implements StoreService {
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

    /**
     * 获取数据私钥
     *
     * @param did
     * @return
     */
    private String _getDPK(String did) {
        String dpk = SDKsp.getInstance()._getDPK(did);
        if (TextUtils.isEmpty(dpk)) {
            throw new RuntimeException("dpk is null");
        }
        return dpk;
    }

    @Override
    public <T> Observable<Long> store(StoreItem<T> storeItem) {
        return this.store((List<StoreItem<T>>) Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public <T> Observable<Long> storeEncrypt(StoreItem<T> storeItem) {
        return this.storeEncrypt((List<StoreItem<T>>) Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public <T> Observable<List<Long>> store(List<StoreItem<T>> storeItems) {
        return Observable
                .fromCallable(new Callable<List<StoreItemSigned>>() {
                    @Override
                    public List<StoreItemSigned> call() throws Exception {
                        for (StoreItem storeItem : storeItems) {
                            storeItem.setData(JsonUtils.toJsonString(storeItem.getData()));
                        }
                        //转化成自动签名的json
                        String listJson = JsonUtils.toJsonString(storeItems);
                        List<StoreItemSigned> storeStringItems = JsonUtils.toBeanList(listJson, new TypeToken<List<StoreItemSigned>>() {
                        });
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

    @Override
    public <T> Observable<List<Long>> storeEncrypt(List<StoreItem<T>> storeItems) {
        return Observable
                .fromCallable(new Callable<List<StoreItemEncrypted>>() {
                    @Override
                    public List<StoreItemEncrypted> call() throws Exception {
                        for (StoreItem storeItem : storeItems) {
                            storeItem.setData(JsonUtils.toJsonString(storeItem.getData()));
                        }
                        //转化json
                        String listJson = JsonUtils.toJsonString(storeItems);
                        //json 转化自动签名和加解密的模型
                        List<StoreItemEncrypted> storeItemEncrypteds = JsonUtils.toBeanList(listJson, new TypeToken<List<StoreItemEncrypted>>() {
                        });
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
    public <T extends StoreItem> Observable<T> getStore(Class<T> t,
                                                        String did,
                                                        String dataType,
                                                        String dataId) {
        return Observable
                .fromCallable(new Callable<StoreQueryReqBodyDTO>() {
                    @Override
                    public StoreQueryReqBodyDTO call() throws Exception {
                        long timestamp = System.currentTimeMillis();

                        StoreQueryReqBodyDTO body = new StoreQueryReqBodyDTO();
                        body.setDid(did);
                        body.setDataType(dataType);
                        body.setDataId(dataId);
                        body.setTimestamp(timestamp);

                        // api sign
                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("dataId", body.getDataId());
                        signMap.put("dataType", body.getDataType());
                        signMap.put("did", body.getDid());
                        signMap.put("timestamp", String.valueOf(body.getTimestamp()));

                        String sign = SignUtils.sign(signMap, _getDPK(body.getDid()));
                        body.setSign(sign);

                        return body;
                    }
                })
                .flatMap(new Function<StoreQueryReqBodyDTO, ObservableSource<StoreItem<String>>>() {
                    @Override
                    public ObservableSource<StoreItem<String>> apply(StoreQueryReqBodyDTO bodyDTO) throws Exception {
                        return storeApiService
                                .getStore(bodyDTO)
                                .map(new ResponseDTOSimpleFunction<StoreItem<String>>());
                    }
                })
                .map(new Function<StoreItem<String>, T>() {
                    @Override
                    public T apply(StoreItem<String> stringStoreItem) throws Exception {
                        //范型解析
                        String jsonString = JsonUtils.toJsonString(stringStoreItem);
                        return JsonUtils.toBean(jsonString, t);
                    }
                });
    }

    @Override
    public <T> Observable<StoreItem<T>> getStoreDecrypted(Class<StoreItem<T>> t, String did, String dataType, String dataId) {
        return Observable
                .fromCallable(new Callable<StoreQueryReqBodyDTO>() {
                    @Override
                    public StoreQueryReqBodyDTO call() throws Exception {
                        long timestamp = System.currentTimeMillis();

                        StoreQueryReqBodyDTO body = new StoreQueryReqBodyDTO();
                        body.setDid(did);
                        body.setDataType(dataType);
                        body.setDataId(dataId);
                        body.setTimestamp(timestamp);

                        // api sign
                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("dataId", body.getDataId());
                        signMap.put("dataType", body.getDataType());
                        signMap.put("did", body.getDid());
                        signMap.put("timestamp", String.valueOf(body.getTimestamp()));

                        String sign = SignUtils.sign(signMap, _getDPK(body.getDid()));
                        body.setSign(sign);

                        return body;
                    }
                })
                .flatMap(new Function<StoreQueryReqBodyDTO, ObservableSource<StoreItemDecrypted>>() {

                    @Override
                    public ObservableSource<StoreItemDecrypted> apply(StoreQueryReqBodyDTO bodyDTO) throws Exception {
                        return storeApiService
                                .getStoreDecrypted(bodyDTO)
                                .map(new ResponseDTOSimpleFunction<StoreItemDecrypted>());
                    }
                })
                .map(new Function<StoreItem<String>, StoreItem<T>>() {
                    @Override
                    public StoreItem<T> apply(StoreItem<String> stringStoreItem) throws Exception {
                        //范型解析
                        String jsonString = JsonUtils.toJsonString(stringStoreItem);
                        return JsonUtils.toBean(jsonString, t);
                    }
                });
    }

    @Override
    public <T> Observable<List<StoreItem<T>>> getStore(Class<StoreItem<T>> t, String did, String dataType) {
        return Observable
                .fromCallable(new Callable<StoreQueryByTypeReqBodyDTO>() {
                    @Override
                    public StoreQueryByTypeReqBodyDTO call() throws Exception {
                        long timestamp = System.currentTimeMillis();

                        StoreQueryByTypeReqBodyDTO body = new StoreQueryByTypeReqBodyDTO();
                        body.setDid(did);
                        body.setDataType(dataType);
                        body.setTimestamp(timestamp);


                        // api sign
                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("dataType", body.getDataType());
                        signMap.put("did", body.getDid());
                        signMap.put("timestamp", String.valueOf(body.getTimestamp()));

                        String sign = SignUtils.sign(signMap, _getDPK(body.getDid()));
                        body.setSign(sign);
                        return body;
                    }
                }).flatMap(new Function<StoreQueryByTypeReqBodyDTO, ObservableSource<List<StoreItem<String>>>>() {
                    @Override
                    public ObservableSource<List<StoreItem<String>>> apply(StoreQueryByTypeReqBodyDTO storeQueryByTypeReqBodyDTO) throws Exception {
                        return storeApiService
                                .getStoreByType(storeQueryByTypeReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<List<StoreItem<String>>>());
                    }
                }).map(new Function<List<StoreItem<String>>, List<StoreItem<T>>>() {
                    @Override
                    public List<StoreItem<T>> apply(List<StoreItem<String>> storeItems) throws Exception {
                        //范型解析
                        String jsonString = JsonUtils.toJsonString(storeItems);
                        return JsonUtils.toBeanList(jsonString, t);
                    }
                });
    }

    @Override
    public <T> Observable<List<StoreItem<T>>> getStoreDecrypted(Class<StoreItem<T>> t, String did, String dataType) {
        return Observable
                .fromCallable(new Callable<StoreQueryByTypeReqBodyDTO>() {
                    @Override
                    public StoreQueryByTypeReqBodyDTO call() throws Exception {
                        long timestamp = System.currentTimeMillis();

                        StoreQueryByTypeReqBodyDTO body = new StoreQueryByTypeReqBodyDTO();
                        body.setDid(did);
                        body.setDataType(dataType);
                        body.setTimestamp(timestamp);


                        // api sign
                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("dataType", body.getDataType());
                        signMap.put("did", body.getDid());
                        signMap.put("timestamp", String.valueOf(body.getTimestamp()));

                        String sign = SignUtils.sign(signMap, _getDPK(body.getDid()));
                        body.setSign(sign);
                        return body;
                    }
                }).flatMap(new Function<StoreQueryByTypeReqBodyDTO, ObservableSource<List<StoreItem<String>>>>() {
                    @Override
                    public ObservableSource<List<StoreItem<String>>> apply(StoreQueryByTypeReqBodyDTO storeQueryByTypeReqBodyDTO) throws Exception {
                        return storeApiService
                                .getStoreByTypeDecrypted(storeQueryByTypeReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<List<StoreItemDecrypted>>())
                                .map(new Function<List<StoreItemDecrypted>, List<StoreItem<String>>>() {
                                    @Override
                                    public List<StoreItem<String>> apply(List<StoreItemDecrypted> storeItemDecrypteds) throws Exception {
                                        return new ArrayList<StoreItem<String>>(storeItemDecrypteds);
                                    }
                                });
                    }
                }).map(new Function<List<StoreItem<String>>, List<StoreItem<T>>>() {
                    @Override
                    public List<StoreItem<T>> apply(List<StoreItem<String>> storeItems) throws Exception {
                        //范型解析
                        String jsonString = JsonUtils.toJsonString(storeItems);
                        return JsonUtils.toBeanList(jsonString, t);
                    }
                });
    }

}
