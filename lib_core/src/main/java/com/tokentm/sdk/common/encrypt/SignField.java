package com.tokentm.sdk.common.encrypt;

import java.lang.annotation.ElementType;

/**
 * @author xuanyouwu@163.com
 * @Description 字段参与签名注解, 不能随便动！！！
 * 不能随便动！！！
 * 不能随便动！！！
 * 不能随便动！！！
 * 不能随便动！！！
 * 仅支持基本类型和String
 */
@java.lang.annotation.Target({ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface SignField {

    /**
     * 链私钥进行签名,⚠️ 默认【不参与签名】
     *
     * @return
     */
    boolean chainPKSign() default false;

    /**
     * 数据私钥进行签名,⚠️ 默认【不参与签名】
     *
     * @return
     */
    boolean dataPKSign() default false;

    /**
     * 是否忽略值为null的字段进行签名,⚠️ 默认【忽略】
     *
     * @return
     */
    boolean ignoreNullValue() default true;
}