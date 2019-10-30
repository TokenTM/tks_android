package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.tokentm.sdk.api.FileApiService;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.encrypt.TEAUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.tokentm.sdk.wallet.SignUtils;
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
public class FileRepositoryImpl implements FileService {

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
    public Observable<String> upload(String did, File file, String targetDid) {
        return Observable.just(did)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        long timestamp = System.currentTimeMillis();
                        String fileMd5 = TEAUtils.getFileMD5(file);
                        Map<String, String> signMap = new HashMap<>();
                        signMap.put("did", did);
                        signMap.put("fileMd5", fileMd5);
                        signMap.put("targetDid", targetDid);
                        signMap.put("timestamp", String.valueOf(timestamp));
                        String sign = SignUtils.sign(signMap, _getDPK(did));

                        MultipartBody.Part filePart = RequestUtils.createFileBody("file", file);
                        return XXF.getApiService(FileApiService.class)
                                .upload(did, filePart, fileMd5, sign, targetDid, timestamp)
                                .map(new ResponseDTOSimpleFunction<>());
                    }
                });

    }
}
