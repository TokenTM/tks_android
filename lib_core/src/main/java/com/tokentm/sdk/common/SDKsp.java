package com.tokentm.sdk.common;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class SDKsp extends SharedPreferenceWrapper {
    static volatile SDKsp INSTANCE;
    private static final String KEY_DPK_FORMAT = "_dpk_%s";

    private SDKsp() {
        super("com.tokentm.sdk.common.SDKsp");
    }

    public static SDKsp getInstance() {
        if (INSTANCE == null) {
            synchronized (SDKsp.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SDKsp();
                }
            }
        }
        return INSTANCE;
    }

    public String _getDPK(String did) {
        return getString(String.format(KEY_DPK_FORMAT, did));
    }

    public void _putDPK(String did, String dpk) {
        putString(String.format(KEY_DPK_FORMAT, did), dpk);
    }
}
