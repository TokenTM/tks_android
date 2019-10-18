package com.tokentm.sdk.demo;

import com.tokentm.sdk.common.SDKsp;
import com.tokentm.sdk.common.SharedPreferenceWrapper;
import com.xxf.arch.XXF;

public class DemoSp extends SharedPreferenceWrapper {
    static volatile DemoSp INSTANCE;

    private DemoSp() {
        super(XXF.getApplication(), "demo_sp");
    }

    public static DemoSp getInstance() {
        if (INSTANCE == null) {
            synchronized (SDKsp.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DemoSp();
                }
            }
        }
        return INSTANCE;
    }
}
