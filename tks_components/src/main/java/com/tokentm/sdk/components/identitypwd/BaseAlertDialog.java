package com.tokentm.sdk.components.identitypwd;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.common.TokenProgressHUDImpl;
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
        super.onCreate(savedInstanceState);
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
