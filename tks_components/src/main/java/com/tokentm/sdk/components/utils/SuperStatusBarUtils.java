package com.tokentm.sdk.components.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.xxf.view.utils.StatusBarUtils;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class SuperStatusBarUtils extends StatusBarUtils {
    /**
     * 设置状态栏图片背景
     *
     * @param activity
     * @param background
     */
    public static void setTransparent(Activity activity, Drawable background) {
        StatusBarUtils.setTransparent(activity);
        View child = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
        child.setBackground(background);
    }

}
