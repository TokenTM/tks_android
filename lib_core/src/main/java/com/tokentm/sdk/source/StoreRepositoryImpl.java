package com.tokentm.sdk.source;

import com.tokentm.sdk.api.StoreApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.model.StorePostBodyDTO;
import com.xxf.arch.http.XXFHttp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

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


    @Override
    public Observable<Long> store(String did, String dataType, String data, long version) {
        long timestamp = System.currentTimeMillis();

        Map<String, String> signMap = new HashMap<>();
        signMap.put("timestamp", String.valueOf(timestamp));
        // signMap.put("chainAddress", walletResult.getCredentials().getAddress());

        StorePostBodyDTO storePostBodyDTO = new StorePostBodyDTO();
        storePostBodyDTO.setDid(did);
        storePostBodyDTO.setDataType(dataType);
        storePostBodyDTO.setData(data);
        storePostBodyDTO.setVersion(version);
        // storePostBodyDTO.setDataSign(SignUtils.);
        //  storePostBodyDTO.setSign();

        storePostBodyDTO.setTimestamp(timestamp);
        return storeApiService
                .store(storePostBodyDTO)
                .map(new ResponseDTOSimpleFunction<Long>());
    }

    @Override
    public Observable<StoreItem> getStore(String did, String dataType, String dataId) {
        return null;
    }

    @Override
    public Observable<List<StoreItem>> getStore(String did) {
        return null;
    }
}
