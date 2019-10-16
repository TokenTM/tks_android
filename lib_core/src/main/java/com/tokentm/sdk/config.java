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
        @SerializedName("_tks_user_keystore")
        TYPE_USER_KEY_STORE("_tks_user_keystore"),

        /**
         * 用户备份密钥
         */
        @SerializedName("_tks_user_backup_secretkey")
        TYPE_BACK_UP_SECRETKEY("_tks_user_backup_secretkey");

        private String value;

        BackupType(String value) {
            this.value = value;
        }

        public java.lang.String getValue() {
            return value;
        }
    }

}
