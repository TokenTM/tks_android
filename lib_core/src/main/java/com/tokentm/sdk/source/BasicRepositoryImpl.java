package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.tokentm.sdk.api.BasicApiService;
import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.http.ResponseDTO;
import com.tokentm.sdk.wallet.SignUtils;
import com.xxf.arch.XXF;

import java.io.InputStream;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BasicRepositoryImpl implements BasicService {
    private static volatile BasicService INSTANCE;

    public static BasicService getInstance() {
        if (INSTANCE == null) {
            synchronized (BasicRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BasicRepositoryImpl();
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
    public Observable<InputStream> getOrgLetterImage(String did, String orgName, String userIdentityCode, String userName) {
        long timestamp = System.currentTimeMillis();
        HashMap<String, String> signMap = new HashMap<>();
        signMap.put("did", did);
        signMap.put("orgName", orgName);
        signMap.put("userIdentityCode", userIdentityCode);
        signMap.put("userName", userName);
        signMap.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.sign(signMap, _getDPK(did));
        return XXF.getApiService(BasicApiService.class)
                .getOrgLetterImage(did, orgName, sign, timestamp, userIdentityCode, userName)
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                });
    }

    @Override
    public Observable<Boolean> sendSmsCode(String phone) {
        return XXF.getApiService(BasicApiService.class)
                .sendSmsCode(phone)
                .map(new Function<ResponseDTO<JsonElement>, Boolean>() {
                    @Override
                    public Boolean apply(ResponseDTO<JsonElement> jsonElementResponseDTO) throws Exception {
                        return true;
                    }
                });
    }
}
