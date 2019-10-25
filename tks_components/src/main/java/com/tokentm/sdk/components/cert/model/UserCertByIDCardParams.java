package com.tokentm.sdk.components.cert.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 认证参数
 */
public class UserCertByIDCardParams implements Serializable {
    @Override
    public String toString() {
        return "UserCertByIDCardParams{" +
                "uDid='" + uDid + '\'' +
                ", userName='" + userName + '\'' +
                ", userIDCard='" + userIDCard + '\'' +
                ", tag=" + tag +
                '}';
    }

    public static class Builder {
        private String uDid;
        private String userName;
        private String userIDCard;
        private Serializable tag;

        public Builder(@NonNull String uDid) {
            this.uDid = Objects.requireNonNull(uDid);
        }

        public Builder(UserCertByIDCardParams userCertByIDCardParams) {
            this.uDid = userCertByIDCardParams.uDid;
            this.userName = userCertByIDCardParams.userName;
            this.userIDCard = userCertByIDCardParams.userIDCard;
            this.tag = userCertByIDCardParams.tag;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserIDCard(String userIDCard) {
            this.userIDCard = userIDCard;
            return this;
        }

        public Builder setTag(Serializable tag) {
            this.tag = tag;
            return this;
        }

        public UserCertByIDCardParams build() {
            return new UserCertByIDCardParams(uDid, userName, userIDCard, tag);
        }
    }

    private String uDid;
    private String userName;
    private String userIDCard;
    private Serializable tag;

    private UserCertByIDCardParams(String uDid, String userName, String userIDCard, Serializable tag) {
        this.uDid = uDid;
        this.userName = userName;
        this.userIDCard = userIDCard;
        this.tag = tag;
    }

    public String getuDid() {
        return uDid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserIDCard() {
        return userIDCard;
    }

    public Serializable getTag() {
        return tag;
    }
}
