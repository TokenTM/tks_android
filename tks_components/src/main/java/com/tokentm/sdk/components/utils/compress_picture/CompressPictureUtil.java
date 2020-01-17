package com.tokentm.sdk.components.utils.compress_picture;


import android.annotation.SuppressLint;

import com.xxf.arch.XXF;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 压缩图片
 */
public class CompressPictureUtil {

    /**
     * 压缩图片
     *
     * @param sourceImg 原图
     * @param fileSize  文件大小
     */
    @SuppressLint("CheckResult")
    public static File getCompressPictureWithSize(String sourceImg, long fileSize) throws Exception {
        //如果设置的大小,小于500kB,则固定为500KB
        if (fileSize < 1024 * 500) {
            fileSize = 1024 * 500;
        }
        long sizeTemp = getFileSize(sourceImg);
        if (sizeTemp != -1 && sizeTemp > fileSize) {
            //压缩图片
            File compressPicture = getCompressPicture(sourceImg);
            //进行递归压缩
            getCompressPictureWithSize(compressPicture.getAbsolutePath(), fileSize);
        }
        return new File(sourceImg);
    }

    /**
     * @param sourceImg
     * @return 压缩过的文件
     * @throws Exception
     */
    public static File getCompressPicture(String sourceImg) throws Exception {
        File file = new File(sourceImg);
        XXF.getLogger().d("鲁班压缩前" + file.length());
        File compress = new Engine(new InputStreamProvider() {
            @Override
            public InputStream open() throws IOException {
                return new FileInputStream(new File(sourceImg));
            }

            @Override
            public String getPath() {
                return sourceImg;
            }
        }, new File(sourceImg), false).compress();
        XXF.getLogger().d("鲁班压缩后" + compress.length());
        return compress;
    }


    /**
     * 获取文件大小
     *
     * @return 字节
     * 1MB =  1024*1024字节
     */
    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            XXF.getLogger().d("文件不存在");
            return -1;
        }
        return file.length();
    }
}
