package com.tokentm.sdk.uidemo;

import com.tokentm.sdk.common.SharedPreferenceWrapper;
import com.xxf.arch.XXF;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description demo_sp
 */
public class DemoSp extends SharedPreferenceWrapper {


    public static final String SP_KEY_CERTIFICATE_ID = "sp_key_certificate_id";
    public static final String SP_KEY_TX_HASH = "sp_key_tx_hash";
    public static final String SP_KEY_DID = "sp_key_did";

    private static volatile DemoSp INSTANCE;

    private DemoSp() {
        super(XXF.getApplication(), "demo_sp");
    }

    public static DemoSp getInstance() {
        if (INSTANCE == null) {
            synchronized (DemoSp.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoSp();
                }
            }
        }
        return INSTANCE;
    }
}
