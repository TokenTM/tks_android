package com.tokentm.sdk.source;

import com.tokentm.sdk.api.FileApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.common.encrypt.TEAUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.xxf.arch.XXF;
import com.xxf.arch.http.RequestUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 文件仓库
 */
public class FileRepositoryImpl implements FileService, BaseRepo {

    private static volatile FileService INSTANCE;

    public static FileService getInstance() {
        if (INSTANCE == null) {
            synchronized (FileRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FileRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }



    @Override
    public Observable<String> upload(String uDID, String objectDID, File file, String targetDid) {
        return Observable.just(uDID)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        long timestamp = System.currentTimeMillis();
                        String fileMd5 = TEAUtils.getFileMD5(file);
                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("did", objectDID);
                        signMap.put("fileMd5", fileMd5);
                        signMap.put("targetDid", targetDid);
                        signMap.put("timestamp", String.valueOf(timestamp));
                        String sign = SignUtils.sign(signMap, getUserDPK(uDID));

                        MultipartBody.Part filePart = RequestUtils.createFileBody("file", file);
                        return XXF.getApiService(FileApiService.class)
                                .upload(objectDID, filePart, fileMd5, sign, targetDid, timestamp)
                                .map(new ResponseDTOSimpleFunction<>());
                    }
                });
    }
}
