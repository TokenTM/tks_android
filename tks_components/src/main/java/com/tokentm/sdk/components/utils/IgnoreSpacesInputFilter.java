package com.tokentm.sdk.components.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 忽略空格
 */
public class IgnoreSpacesInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return " ".contentEquals(source) ? "" : null;
    }
}
