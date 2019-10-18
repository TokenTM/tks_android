package com.tokentm.sdk.components.identitypwd;

import android.databinding.BindingAdapter;
import android.databinding.adapters.ViewBindingAdapter;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 绑定点击事件
 */
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
}
