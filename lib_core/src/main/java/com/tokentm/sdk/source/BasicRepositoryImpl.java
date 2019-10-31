package com.tokentm.sdk.source;

import com.tokentm.sdk.api.BasicApiService;
import com.tokentm.sdk.common.encrypt.SignUtils;
import com.tokentm.sdk.http.ResponseDTOSimpleFunction;
import com.xxf.arch.XXF;
import com.xxf.arch.http.ResponseException;

import java.io.InputStream;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BasicRepositoryImpl implements BasicService, BaseRepo {
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


    @Override
    public Observable<InputStream> getOrgLetterImage(String did, String orgName, String userIdentityCode, String userName) {
        long timestamp = System.currentTimeMillis();
        HashMap<String, String> signMap = new HashMap<>();
        signMap.put("did", did);
        signMap.put("orgName", orgName);
        signMap.put("userIdentityCode", userIdentityCode);
        signMap.put("userName", userName);
        signMap.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.sign(signMap, getUserDPK(did));
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
                .map(new ResponseDTOSimpleFunction<Boolean>())
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean sended) throws Exception {
                        if (!sended) {
                            throw new ResponseException(502, "服务器发送验证码失败!");
                        }
                        return sended;
                    }
                });
    }
}
