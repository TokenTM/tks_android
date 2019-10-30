package com.tokentm.sdk.common.encrypt;

import com.xxf.arch.XXF;

import org.xxtea.XXTEA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 备份加密
 */
public final class TEAUtils {

    /**
     * md5 32位
     *
     * @param content
     * @return
     */
    public static String md5_32(String content) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(content.getBytes("UTF-8"), 0, content.length());
            String md5Val = new BigInteger(1, m.digest()).toString(16);
            return md5Val;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5(File file) throws Exception {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest m = MessageDigest.getInstance("MD5");
        while ((numRead = fis.read(buffer)) > 0) {
            m.update(buffer, 0, numRead);
        }
        fis.close();
        return new BigInteger(1, m.digest()).toString(16);
    }

    /**
     * md5 16位
     *
     * @param content
     * @return
     */
    public static String md5_16(String content) {
        String md5_32_str = md5_32(content);
        return md5_32_str.substring(8, 24);
    }

    /**
     * 加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encryptString(String content, String key) {
        XXF.getLogger().d(String.format("========>encryptString content:%s key: %s", content, key));
        String result = XXTEA.encryptToBase64String(content, md5_16(key));
        XXF.getLogger().d(String.format("========>encryptString result:%s", result));
        return result;
    }


    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decryptString(String content, String key) {
        XXF.getLogger().d(String.format("========>decryptString content:%s key: %s", content, key));
        String result = XXTEA.decryptBase64StringToString(content, md5_16(key));
        XXF.getLogger().d(String.format("========>decryptString result:%s", result));
        return result;
    }
}
