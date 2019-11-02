package com.tokentm.sdk.common.encrypt;

import java.lang.annotation.ElementType;

/**
 * @author xuanyouwu@163.com
 * @Description 签名赋值字段, 不能随便动！！！
 * 就是用{@link SignField} 签名
 */

@java.lang.annotation.Target({ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface SignTargetField {
    enum SignType {
        //链私钥签名
        CPK_SIGN,
        //数据私钥签名
        DPK_SIGN
    }

    SignType value();
}
