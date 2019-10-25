package com.tokentm.sdk.source;

import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface NodeService extends RepoService {

    /**
     * 获取链节点在线服务
     *
     * @param type
     * @return
     */
    Observable<List<NodeServiceItem>> getNodeService(NodeServiceType type);
}
