package com.tokentm.sdk.source;

import android.text.TextUtils;

import com.tokentm.sdk.common.SDKsp;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface BaseRepo {
    /**
     * 获取数据私钥
     *
     * @param did
     * @return
     */
    default String _getDPK(String did) {
        String dpk = SDKsp.getInstance()._getDPK(did);
        if (TextUtils.isEmpty(dpk)) {
            throw new RuntimeException("dpk is null");
        }
        return dpk;
    }
}
