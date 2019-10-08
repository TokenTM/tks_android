package com.tokentm.sdk.components.identitypwd;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 基础UI组件工具类
 */
public class ComponentUtils {

    /**
     * 密文
     *
     * @param textView
     */
    public static void showTextForCipher(@NonNull TextView textView) {
        if (textView == null) {
            return;
        }
        textView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if (textView instanceof EditText) {
            setTextSelectEnd((EditText) textView);
        }
    }

    /**
     * 明文
     *
     * @param textView
     */
    public static void showTextForPlain(@NonNull TextView textView) {
        if (textView == null) {
            return;
        }
        textView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        if (textView instanceof EditText) {
            setTextSelectEnd((EditText) textView);
        }
    }

    public static void setTextSelectEnd(@NonNull EditText textView) {
        if (textView == null) {
            return;
        }
        textView.setSelection(textView.getText().length());
    }

    /**
     * 设置文字颜色
     *
     * @param parent
     * @param color
     */
    public static void setChildTextColor(ViewGroup parent, @ColorInt int color) {
        ViewGroup viewGroup = parent;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(color);
            } else if (childAt instanceof ViewGroup) {
                setChildTextColor((ViewGroup) childAt, color);
            }
        }
    }

    /**
     * 设置文字颜色
     *
     * @param parent
     * @param color
     */
    public static void setChildDrawableColor(ViewGroup parent, @ColorInt int color) {
        ViewGroup viewGroup = parent;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ImageView) {
                Drawable drawable = ((ImageView) childAt).getDrawable();
                if (drawable != null) {
                    ((ImageView) childAt).setImageDrawable(tint(drawable, color));
                }
            } else if (childAt instanceof ViewGroup) {
                setChildDrawableColor((ViewGroup) childAt, color);
            }
        }
    }


    /**
     * 重绘Drawable，将drawable颜色着色为color
     *
     * @param drawable
     * @param color
     * @return 重绘后的Drawable
     */
    public static Drawable tint(Drawable drawable, int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        wrappedDrawable.mutate();
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
}
