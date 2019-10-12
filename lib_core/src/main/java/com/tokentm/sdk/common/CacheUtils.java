package com.tokentm.sdk.common;

import android.content.Context;

import java.io.File;

public class CacheUtils {

    /**
     * 获取
     *
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {
        File tks_sdk = context.getDir(
                "tks_sdk",
                Context.MODE_PRIVATE);
        if (!tks_sdk.exists()) {
            tks_sdk.mkdirs();
        }
        return tks_sdk;
    }
}
