package com.tokentm.sdk.components.common;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.xxf.arch.fragment.XXFFragment;
import com.xxf.arch.widget.progresshud.ProgressHUD;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BaseFragment extends XXFFragment {
    private TokenProgressHUDImpl tokenProgressHUD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenProgressHUD = new TokenProgressHUDImpl(getContext());
    }


    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tokenProgressHUD != null) {
            tokenProgressHUD.detachedContext();
        }
    }

    @Override
    public ProgressHUD progressHUD() {
        return tokenProgressHUD;
    }
}
