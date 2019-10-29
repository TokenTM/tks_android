package com.tokentm.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface Config {

    enum BackupType {

        /**
         * 当前用户 个人实名keystore同步(一个用户只有一个)
         */
        @SerializedName("_tks_keystore")
        TYPE_KEY_STORE("_tks_keystore"),
        /**
         * 备份私钥
         */
        @SerializedName("_tks_dpk")
        TYPE_DPK("_tks_dpk");

        private String value;

        BackupType(String value) {
            this.value = value;
        }

        public java.lang.String getValue() {
            return value;
        }
    }

}
