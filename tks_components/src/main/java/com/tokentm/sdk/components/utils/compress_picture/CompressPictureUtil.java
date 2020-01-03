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
     * @param sourceImg 原图
     */
    @SuppressLint("CheckResult")
    public static File getCompressPicture(String sourceImg) throws Exception {
        File file = new File(sourceImg);
        XXF.getLogger().d(file.length()+"");
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
        XXF.getLogger().d(compress.length()+"");
        return compress;
    }
}
