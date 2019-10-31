package com.tokentm.sdk.source;

import com.tokentm.sdk.api.NodeApiService;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;
import com.xxf.arch.XXF;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NodeRepositoryImpl implements NodeService, BaseRepo {

    private static volatile NodeService INSTANCE;

    public static NodeService getInstance() {
        if (INSTANCE == null) {
            synchronized (NodeRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NodeRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<NodeServiceItem>> getNodeService(NodeServiceType type) {
        return XXF.getApiService(NodeApiService.class)
                .getNodeService(type.getValue())
                .map(new ResponseDTOSimpleFunction<List<NodeServiceItem>>());
    }
}
