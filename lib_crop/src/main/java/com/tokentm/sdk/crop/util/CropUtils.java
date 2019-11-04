package com.tokentm.sdk.crop.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import com.tokentm.sdk.crop.Crop;
import com.tokentm.sdk.crop.CropActivity;

import java.io.File;

public class CropUtils {
    /**
     * 启动裁剪
     *
     * @param activity       上下文
     * @param sourceFilePath 需要裁剪图片的绝对路径
     * @return //
     */
    public static Intent getUCropLauncher(FragmentActivity activity, String sourceFilePath) {
        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        // String cameraScalePath = outFile.getAbsolutePath();
        // 设置裁切后的sd卡uri
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        Crop crop = Crop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        Crop.Options options = new Crop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(CropActivity.ALL, CropActivity.ALL, CropActivity.ALL);
        //是否隐藏底部容器，默认显示，true-隐藏  false-显示
        options.setHideBottomControls(false);
        //设置toolbar颜色
        options.setToolbarColor(0xFF00C1CE);
        //设置状态栏颜色
        options.setStatusBarColor(0xFF00C1CE);
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //UCrop配置
        crop.withOptions(options);
        // 设置裁剪图片的宽高比，比如16：9，默认是按照图片的宽高比。
        // crop.withAspectRatio(16, 9);
        //设置底部容器是否显示调节裁剪图片宽高比的设置，默认显示。
        crop.useSourceImageAspectRatio();
        return crop.getIntent(activity);
    }
}
