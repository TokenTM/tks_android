package com.tokentm.sdk.components.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xxf.arch.XXF;
import com.xxf.arch.dialog.XXFAlertDialog;
import com.xxf.arch.widget.progresshud.ProgressHUD;

import java.io.Serializable;

import io.reactivex.functions.BiConsumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BaseAlertDialog<R extends Serializable> extends XXFAlertDialog<R> {
    private TokenProgressHUDImpl tokenProgressHUD;

    protected BaseAlertDialog(@NonNull Context context, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context, dialogConsumer);
    }

    protected BaseAlertDialog(@NonNull Context context, int themeResId, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context, themeResId, dialogConsumer);
    }

    protected BaseAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, @Nullable BiConsumer<DialogInterface, R> dialogConsumer) {
        super(context, cancelable, cancelListener, dialogConsumer);
    }


    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = this.getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //设置window背景透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (dm.widthPixels * 0.72 + 0.5);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(params);
        if (this.getOwnerActivity() != null) {
            tokenProgressHUD = new TokenProgressHUDImpl(this.getOwnerActivity());
        } else if (this.getContext() instanceof ContextWrapper) {
            tokenProgressHUD = new TokenProgressHUDImpl(((ContextWrapper) this.getContext()).getBaseContext());
        } else {
            tokenProgressHUD = new TokenProgressHUDImpl(this.getContext());
        }
    }

    @Override
    public ProgressHUD progressHUD() {
        return tokenProgressHUD;
    }
}
