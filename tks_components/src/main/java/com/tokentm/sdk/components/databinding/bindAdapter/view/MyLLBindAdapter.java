package com.tokentm.sdk.components.databinding.bindAdapter.view;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.databinding.adapters.LinearLayoutBindingAdapter;
import android.view.View;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 绑定点击事件
 */
@SuppressLint("RestrictedApi")
public class MyLLBindAdapter extends LinearLayoutBindingAdapter {

    @BindingAdapter("android:enable")
    public static void setEnable(View view, boolean enable) {
        view.setEnabled(enable);
    }

    @BindingAdapter("android:select")
    public static void setSelected(View view, boolean select) {
        view.setSelected(select);
    }

}
