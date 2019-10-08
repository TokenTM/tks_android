package com.tokentm.sdk.components.identitypwd;


import android.util.SizeF;

import java.util.regex.Pattern;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 用户模块配置常量
 */
public interface UserConfig {
    /**
     * 密码最多20位
     */
    int MAX_LENTH_PWD = 20;

    /**
     * 密码少6位
     */
    int MINI_LENTH_PWD = 6;


    /**
     * 密码正则表达式
     */
    Pattern PATTERN_PWD = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");


    /**
     * 身份密码设置安全问题 数量
     */
    int IDENTITY_PWD_SET_QUESTION_COUNT = 3;

    /**
     * 用户头像大小
     */
    SizeF USER_AVATAR_URL_SIZE = new SizeF(400, 400);
}
