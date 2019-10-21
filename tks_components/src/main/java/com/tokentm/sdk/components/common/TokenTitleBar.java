package com.tokentm.sdk.components.common;

import android.graphics.drawable.ColorDrawable;

import com.tokentm.sdk.components.R;
import com.xxf.arch.utils.DensityUtil;
import com.xxf.view.databinding.titlebar.TitleBar;

import io.reactivex.functions.Action;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description token 标题栏
 */
public class TokenTitleBar extends TitleBar {

    //R.mipmap.arch_ic_title_back //#00C1CE

    public TokenTitleBar(Action backAction) {
        //默认
        this.setTitleBarBackground(new ColorDrawable(0xFF00C1CE));
        this.setTitleBarHeight(DensityUtil.dip2px(44));
        this.setTitleBarLeftIcon(R.mipmap.arch_ic_title_back, backAction);
    }
}
