package com.tokentm.sdk.components.identitypwd;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokentm.sdk.components.databinding.ArchBindLayoutTitleBinding;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.utils.DensityUtil;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.view.databinding.titlebar.TitleBar;
import com.xxf.view.utils.StatusBarUtils;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 自带titlebar的activity
 */
public class BaseTitleBarActivity extends XXFActivity {
    private TokenProgressHUDImpl tokenProgressHUD;
    private final TokenTitleBar titleBar = new TokenTitleBar(new Action() {
        @Override
        public void run() throws Exception {
            finish();
        }
    });

    @NonNull
    public final TitleBar getTitleBar() {
        return titleBar;
    }

    protected ArchBindLayoutTitleBinding titleBinding;
    private LinearLayout rootView;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenProgressHUD = new TokenProgressHUDImpl(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int blueColor = 0xFF00C1CE;
            StatusBarUtils.compatStatusBarForM(this, false, blueColor);
        }
        titleBinding = ArchBindLayoutTitleBinding.inflate(getLayoutInflater());
        titleBinding.setTitleBar(titleBar);
        rootView = new LinearLayout(getContext());
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView.addView(titleBinding.getRoot(), 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(49)));
        super.setContentView(rootView);
    }

    /**
     * 移除多余的child
     */
    private final void removeChild() {
        int titleCount = 1;
        int childCount = rootView.getChildCount();
        if (childCount > titleCount) {
            rootView.removeViews(1, childCount - titleCount);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (getTitleBar() != null) {
            getTitleBar().setTitleBarTitle(title);
        }
    }

    @Override
    public final void setContentView(int layoutResID) {
        this.setContentView(LayoutInflater.from(getContext()).inflate(layoutResID, rootView, false));
    }

    @Override
    public final void setContentView(View view) {
        this.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        removeChild();
        rootView.addView(view, params);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (titleBinding != null) {
            titleBinding.unbind();
        }
        if (tokenProgressHUD != null) {
            tokenProgressHUD.detachedContext();
        }
    }

    @Override
    public ProgressHUD progressHUD() {
        return tokenProgressHUD;
    }
}
