package com.tokentm.sdk.components.databinding.bindAdapter.view;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.databinding.adapters.ViewBindingAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 绑定点击事件
 */
@SuppressLint("RestrictedApi")
public class ViewBindAdapter extends ViewBindingAdapter {


    @BindingAdapter({"android:onClick"})
    public static void setOnClick(View view, View.OnClickListener clickListener) {
        view.setOnClickListener(clickListener);
        view.setClickable(true);
    }

    @BindingAdapter({"android:onClickListener"})
    public static void setClickListener(View view, View.OnClickListener clickListener) {
        view.setOnClickListener(clickListener);
        view.setClickable(true);
    }

    @BindingAdapter({"android:onClick"})
    public static void setOnClick(View view, final Action action) {
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                if (action != null) {
                    try {
                        action.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params.width != width) {
            params.width = width;
            view.setLayoutParams(params);
        }
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params.height != height) {
            params.height = height;
            view.setLayoutParams(params);
        }
    }

    @BindingAdapter("android:enable")
    public static void setEnable(View view, boolean enable) {
        view.setEnabled(enable);
    }

    @BindingAdapter("android:select")
    public static void setSelected(View view, boolean select) {
        view.setSelected(select);
    }

}
