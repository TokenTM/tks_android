package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.tokentm.sdk.api.EncryptApiService;
import com.tokentm.sdk.common.encrypt.TEAUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.model.NodeServiceEncryptDecryptItem;
import com.tokentm.sdk.model.NodeServiceItem;
import com.tokentm.sdk.model.NodeServiceType;
import com.tokentm.sdk.model.ServiceEncryptDecryptDTO;
import com.xxf.arch.XXF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    public Observable<List<NodeServiceEncryptDecryptItem>> encrypt(String uDid, String data, String phone, String smsCode) {
        //1. 获取到加密service
        return NodeRepositoryImpl
                .getInstance()
                .getNodeService(NodeServiceType.TYPE_ENCRYPT)
                .flatMap(new Function<List<NodeServiceItem>, ObservableSource<List<NodeServiceEncryptDecryptItem>>>() {
                    @Override
                    public ObservableSource<List<NodeServiceEncryptDecryptItem>> apply(List<NodeServiceItem> nodeServiceItems) throws Exception {
                        //用tea 加密 生成更长的字符串
                        String dataEncryptByDid = TEAUtils.encryptString(data, uDid);

                        //随机切割
                        List<String> randomSplit = randomSplit(dataEncryptByDid);
                        List<Observable<NodeServiceEncryptDecryptItem>> encryptObservables = new ArrayList<>();
                        for (int i = 0; i < randomSplit.size(); i++) {
                            final int index = i;
                            String split = randomSplit.get(i);
                            //随机获取一个在线加密服务
                            NodeServiceItem nodeServiceItem = nodeServiceItems.get(new Random().nextInt(nodeServiceItems.size()));
                            NodeServiceItem.NodeFunction nodeFunction = nodeServiceItem.getFuncList().get(0);
                            String serviceDid = nodeServiceItem.getDid();
                            encryptObservables.add(
                                    XXF.getApiService(EncryptApiService.class)
                                            .encrypt(nodeFunction.getUri(), new ServiceEncryptDecryptDTO(uDid, phone, smsCode, split))
                                            .map(new ResponseDTOSimpleFunction<String>())
                                            .map(new Function<String, NodeServiceEncryptDecryptItem>() {
                                                @Override
                                                public NodeServiceEncryptDecryptItem apply(String encryptedData) throws Exception {
                                                    return new NodeServiceEncryptDecryptItem(index, serviceDid, encryptedData);
                                                }
                                            })
                            );
                        }
                        return Observable.zip(encryptObservables, new Function<Object[], List<NodeServiceEncryptDecryptItem>>() {
                            @Override
                            public List<NodeServiceEncryptDecryptItem> apply(Object[] objects) throws Exception {
                                List<NodeServiceEncryptDecryptItem> result = new ArrayList<>();
                                for (Object obj : objects) {
                                    result.add((NodeServiceEncryptDecryptItem) obj);
                                }
                                return result;
                            }
                        });
                    }
                });
    }


    @Override
    public Observable<String> decrypt(String uDid, List<NodeServiceEncryptDecryptItem> data, String phone, String smsCode) {
        return NodeRepositoryImpl
                .getInstance()
                .getNodeService(NodeServiceType.TYPE_DECRYPT)
                .flatMap(new Function<List<NodeServiceItem>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<NodeServiceItem> nodeServiceItems) throws Exception {
                        //排序 按自然顺序
                        Collections.sort(data, new Comparator<NodeServiceEncryptDecryptItem>() {
                            @Override
                            public int compare(NodeServiceEncryptDecryptItem o1, NodeServiceEncryptDecryptItem o2) {
                                return o1.getNo() - o2.getNo();
                            }
                        });
                        List<Observable<String>> decryptObservables = new ArrayList<>();
                        for (NodeServiceEncryptDecryptItem decryptItem : data) {
                            for (NodeServiceItem nodeServiceItem : nodeServiceItems) {
                                String serviceDid = decryptItem.getServiceDid();
                                if (TextUtils.equals(serviceDid, nodeServiceItem.getDid())) {
                                    NodeServiceItem.NodeFunction nodeFunction = nodeServiceItem.getFuncList().get(0);
                                    decryptObservables.add(
                                            XXF.getApiService(EncryptApiService.class)
                                                    .decrypt(nodeFunction.getUri(), new ServiceEncryptDecryptDTO(uDid, phone, smsCode, decryptItem.getValue()))
                                                    .map(new ResponseDTOSimpleFunction<String>())
                                    );
                                }
                            }
                        }
                        return Observable.zip(decryptObservables, new Function<Object[], String>() {
                            @Override
                            public String apply(Object[] objects) throws Exception {
                                StringBuilder sb = new StringBuilder();
                                for (Object obj : objects) {
                                    sb.append((String) obj);
                                }
                                String data = sb.toString();
                                //用tea 解密
                                String dataDecryptByDid = TEAUtils.decryptString(data, uDid);
                                return dataDecryptByDid;
                            }
                        });
                    }
                });
    }

}
