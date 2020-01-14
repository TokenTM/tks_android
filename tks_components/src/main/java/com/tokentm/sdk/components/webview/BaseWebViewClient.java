package com.tokentm.sdk.components.webview;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 统一web client
 */
public class BaseWebViewClient extends WebViewClient {
    @CallSuper
    @Override
    public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
            }
            CookieSyncManager.createInstance(view.getContext());
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(view, true);
            }
            CookieSyncManager.createInstance(view.getContext());
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.shouldOverrideUrlLoading(view, url);
    }




    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //这里要处理handler.proceed();
        handler.proceed();
        //super.onReceivedSslError(view, handler, error);
    }
}
