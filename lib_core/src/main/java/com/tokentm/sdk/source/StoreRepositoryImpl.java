package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.tokentm.sdk.api.StoreApiService;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StoreItemDecrypted;
import com.tokentm.sdk.model.StoreItemEncrypted;
import com.tokentm.sdk.model.StoreItemSigned;
import com.tokentm.sdk.wallet.SignUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.json.JsonUtils;

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
        return Observable
                .fromCallable(new Callable<List<StoreItemSigned>>() {
                    @Override
                    public List<StoreItemSigned> call() throws Exception {
                        //转化成自动签名的json
                        String jsonString = JsonUtils.toJsonString(storeItems);
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

    @Override
    public Observable<List<Long>> storeEncrypt(List<StoreItem<String>> storeItems) {
        return Observable
                .fromCallable(new Callable<List<StoreItemEncrypted>>() {
                    @Override
                    public List<StoreItemEncrypted> call() throws Exception {
                        //json 转化自动签名和加解密的模型
                        String jsonString = JsonUtils.toJsonString(storeItems);
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

}
