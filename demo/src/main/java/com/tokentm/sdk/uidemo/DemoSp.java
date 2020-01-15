package com.tokentm.sdk.uidemo;

import com.tokentm.sdk.common.SharedPreferenceWrapper;
import com.xxf.arch.XXF;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description demo_sp
 */
public class DemoSp extends SharedPreferenceWrapper {


    public static final String SP_KEY_LOGIN = "sp_key_login";

    /**
     * 发货得到的证书id
     */
    public static final String SP_KEY_GOODS_ID = "sp_key_goods_id";
    public static final String SP_KEY_GOODS_NAME = "sp_key_goods_name";
    public static final String SP_KEY_GOODS_NUMBER = "sp_key_goods_number";
    /**
     * 发证得到的证书id
     */
    public static final String SP_KEY_CERTIFICATION_CERTIFICATE_ID = "sp_key_certification_certificate_id";
    public static final String SP_KEY_COMPANY_TX_HASH = "sp_key_company_tx_hash";
    public static final String SP_KEY_COMPANY_DID = "sp_key_company_did";
    public static final String SP_KEY_IDENTITY_DID = "sp_key_identity_did";
    public static final String SP_KEY_IDENTITY_TX_HASH = "sp_key_identity_tx_hash";
    public static final String SP_KEY_CERTIFICATION_CERTIFICATE_CONTENT = "sp_key_certification_certificate_content";
    public static final String SP_KEY_CERTIFICATION_CERTIFICATE_EXTRA_DATA = "sp_key_certification_certificate_extra_data";
    public static final String SP_KEY_TO_DID = "sp_key_to_did";
    public static final String SP_KEY_TX_HASH = "sp_key_tx_hash";

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

    public void login(String phone, String did) {
        putString(SP_KEY_LOGIN, phone);
        putString(phone, did);
    }

    public void logout() {
        putString(SP_KEY_LOGIN, null);
    }

    public String getLoginPhone() {
        return getString(SP_KEY_LOGIN);
    }

    public String getLoginDID() {
        return getString(getLoginPhone());
    }


}
