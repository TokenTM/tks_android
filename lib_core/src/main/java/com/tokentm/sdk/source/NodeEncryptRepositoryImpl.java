package com.tokentm.sdk.source;

import com.tokentm.sdk.api.EncryptApiService;
import com.tokentm.sdk.common.encrypt.TEAUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.NodeServiceEncryptedItem;
import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;
import com.tokentm.sdk.model.ServiceEncryptItem;
import com.xxf.arch.XXF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NodeEncryptRepositoryImpl implements NodeEncryptService {
    private static final int SPLIT_MIN_COUNT = 3;
    private static final int SPLIT_MAX_COUNT = 6;
    private static volatile NodeEncryptService INSTANCE;

    public static NodeEncryptService getInstance() {
        if (INSTANCE == null) {
            synchronized (NodeEncryptRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NodeEncryptRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 随机分割成3-6份
     *
     * @param str
     * @return
     */
    private List<String> randomSplit(String str) {
        int length = str.length();
        int sliceCount = new Random().nextInt(SPLIT_MAX_COUNT - SPLIT_MIN_COUNT + 1) + SPLIT_MIN_COUNT;
        List<Integer> splitIndexs = new ArrayList<>(Arrays.asList(0, length));
        while (splitIndexs.size() < sliceCount - 1) {
            int index = new Random().nextInt(length);
            if (!splitIndexs.contains(index)) {
                splitIndexs.add(index);
            }
        }
        Collections.sort(splitIndexs);
        List<String> splitStringList = new ArrayList<>();
        for (int i = 0; i < splitIndexs.size() - 1; i++) {
            splitStringList.add(str.substring(splitIndexs.get(i), splitIndexs.get(i + 1)));
        }
        return splitStringList;
    }

    @Override
    public Observable<List<NodeServiceEncryptedItem>> encrypt(String uDid, String data, String phone, String smsCode) {
        //1. 获取到加密service
        return NodeRepositoryImpl
                .getInstance()
                .getNodeService(NodeServiceType.TYPE_ENCRYPT)
                .flatMap(new Function<List<NodeServiceItem>, ObservableSource<List<NodeServiceEncryptedItem>>>() {
                    @Override
                    public ObservableSource<List<NodeServiceEncryptedItem>> apply(List<NodeServiceItem> nodeServiceItems) throws Exception {
                        //用tea 加密 生成更长的字符串
                        String dataEncryptByDid = TEAUtils.encryptString(data, uDid);

                        //随机切割
                        List<String> randomSplit = randomSplit(dataEncryptByDid);
                        List<Observable<NodeServiceEncryptedItem>> encryptObservables = new ArrayList<>();
                        for (String split : randomSplit) {
                            //随机获取一个在线加密服务
                            NodeServiceItem nodeServiceItem = nodeServiceItems.get(new Random().nextInt(nodeServiceItems.size()));
                            NodeServiceItem.NodeFunction nodeFunction = nodeServiceItem.getFuncList().get(0);
                            String serviceDid = nodeServiceItem.getDid();
                            encryptObservables.add(
                                    XXF.getApiService(EncryptApiService.class)
                                            .encrypt(nodeFunction.getUri(), new ServiceEncryptItem(serviceDid, phone, smsCode, split))
                                            .map(new ResponseDTOSimpleFunction<String>())
                                            .map(new Function<String, NodeServiceEncryptedItem>() {
                                                @Override
                                                public NodeServiceEncryptedItem apply(String encryptedData) throws Exception {
                                                    return new NodeServiceEncryptedItem(serviceDid, encryptedData);
                                                }
                                            })
                            );
                        }
                        return Observable.zip(encryptObservables, new Function<Object[], List<NodeServiceEncryptedItem>>() {
                            @Override
                            public List<NodeServiceEncryptedItem> apply(Object[] objects) throws Exception {
                                List<NodeServiceEncryptedItem> result = new ArrayList<>();
                                for (Object obj : objects) {
                                    result.add((NodeServiceEncryptedItem) obj);
                                }
                                return result;
                            }
                        });
                    }
                });
    }

    @Override
    public Observable<String> decrypt(String uDid, String data, String phone, String smsCode) {
        return null;
    }
}
