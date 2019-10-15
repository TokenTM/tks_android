package com.tokentm.sdk.source;

import com.tokentm.sdk.api.StoreApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreQueryByTypeReqBodyDTO;
import com.tokentm.sdk.model.StoreQueryReqBodyDTO;
import com.tokentm.sdk.model.StoreReqBodyDTO;
import com.tokentm.sdk.model.DecryptedStoreItem;
import com.tokentm.sdk.wallet.SignUtils;
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
    private String _getDataPrivateKey(String did) {
        //TODO
        return "";
    }

    @Override
    public Observable<Long> store(StoreItem storeItem) {
        return this.store(Arrays.asList(storeItem))
                .map(new Function<List<Long>, Long>() {
                    @Override
                    public Long apply(List<Long> longs) throws Exception {
                        return longs.get(0);
                    }
                });
    }

    @Override
    public Observable<List<Long>> store(List<StoreItem> storeItems) {
        return Observable
                .fromCallable(new Callable<List<StoreReqBodyDTO>>() {
                    @Override
                    public List<StoreReqBodyDTO> call() throws Exception {
                        List<StoreReqBodyDTO> storeReqBodyDTOList = new ArrayList<>();
                        //时间戳
                        long timestamp = System.currentTimeMillis();

                        for (StoreItem storeItem : storeItems) {
                            StoreReqBodyDTO storeReqBodyDTO = new StoreReqBodyDTO();
                            storeReqBodyDTO.setDid(storeItem.getDid());
                            storeReqBodyDTO.setDataId(storeItem.getDataId());
                            storeReqBodyDTO.setDataType(storeItem.getDataType());
                            storeReqBodyDTO.setData(storeItem.getData());
                            storeReqBodyDTO.setVersion(storeItem.getVersion());
                            storeReqBodyDTO.setTimestamp(timestamp);

                            //data sign
                            Map<String, String> dataSignMap = new HashMap<>();
                            dataSignMap.put("data", storeReqBodyDTO.getData());
                            String dataSign = SignUtils.sign(dataSignMap, _getDataPrivateKey(storeReqBodyDTO.getDid()));
                            storeReqBodyDTO.setDataSign(dataSign);


                            // api sign
                            Map<String, String> signMap = new HashMap<>();
                            signMap.put("data", storeReqBodyDTO.getData());
                            signMap.put("dataId", storeReqBodyDTO.getDataId());
                            signMap.put("dataSign", storeReqBodyDTO.getDataSign());
                            signMap.put("dataType", storeReqBodyDTO.getDataType());
                            signMap.put("did", storeReqBodyDTO.getDid());
                            signMap.put("timestamp", String.valueOf(storeReqBodyDTO.getTimestamp()));
                            signMap.put("version", String.valueOf(storeReqBodyDTO.getVersion()));

                            String sign = SignUtils.sign(signMap, _getDataPrivateKey(storeReqBodyDTO.getDid()));
                            storeReqBodyDTO.setSign(sign);

                            // add to list
                            storeReqBodyDTOList.add(storeReqBodyDTO);
                        }
                        return storeReqBodyDTOList;
                    }
                })
                .flatMap(new Function<List<StoreReqBodyDTO>, ObservableSource<List<Long>>>() {
                    @Override
                    public ObservableSource<List<Long>> apply(List<StoreReqBodyDTO> storeReqBodyDTOS) throws Exception {
                        return storeApiService
                                .store(storeReqBodyDTOS)
                                .map(new ResponseDTOSimpleFunction<List<Long>>());
                    }
                });
    }

    @Override
    public Observable<StoreItem> getStore(String did, String dataType, String dataId) {
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

                        String sign = SignUtils.sign(signMap, _getDataPrivateKey(body.getDid()));
                        body.setSign(sign);

                        return body;
                    }
                }).flatMap(new Function<StoreQueryReqBodyDTO, ObservableSource<StoreItem>>() {
                    @Override
                    public ObservableSource<StoreItem> apply(StoreQueryReqBodyDTO bodyDTO) throws Exception {
                        return storeApiService.getStore(bodyDTO)
                                .map(new ResponseDTOSimpleFunction<DecryptedStoreItem>());
                    }
                });
    }

    @Override
    public Observable<List<StoreItem>> getStore(String did, String dataType) {
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

                        String sign = SignUtils.sign(signMap, _getDataPrivateKey(body.getDid()));
                        body.setSign(sign);
                        return body;
                    }
                }).flatMap(new Function<StoreQueryByTypeReqBodyDTO, ObservableSource<List<StoreItem>>>() {
                    @Override
                    public ObservableSource<List<StoreItem>> apply(StoreQueryByTypeReqBodyDTO storeQueryByTypeReqBodyDTO) throws Exception {
                        return storeApiService
                                .getStoreByType(storeQueryByTypeReqBodyDTO)
                                .map(new ResponseDTOSimpleFunction<List<DecryptedStoreItem>>())
                                .map(new Function<List<DecryptedStoreItem>, List<StoreItem>>() {
                                    @Override
                                    public List<StoreItem> apply(List<DecryptedStoreItem> decryptedStoreItems) throws Exception {
                                        return new ArrayList<StoreItem>(decryptedStoreItems);
                                    }
                                });
                    }
                });
    }
}
