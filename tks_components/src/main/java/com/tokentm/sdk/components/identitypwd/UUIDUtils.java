package com.tokentm.sdk.components.identitypwd;

import java.util.UUID;

public class UUIDUtils {
    /**
     * 生成32位随机uuid
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
