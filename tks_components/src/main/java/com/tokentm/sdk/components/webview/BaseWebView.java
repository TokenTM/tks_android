package com.tokentm.sdk.components.webview;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xxf.arch.utils.ToastUtils;

import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BaseWebView extends WebView {


    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        initCompatible();
    }


    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        try {
            CookieSyncManager.createInstance(getContext());
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        try {
            CookieSyncManager.createInstance(getContext());
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        if (!(client instanceof BaseWebViewClient)) {
            throw new IllegalArgumentException("client must BaseWebViewClient");
        }
        super.setWebViewClient(client);
    }

    /**
     * 解决兼容性
     */
    private void initCompatible() {
        /**
         * 问题：5.0 以后的WebView加载的链接为Https开头，但是链接里面的内容，比如图片为Http链接，这时候，图片就会加载不出来
         * 原因：Android 5.0上Webview默认不允许加载Http与Https混合内容
         * 解决方案如下： 两种都可以
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(getSettings().getMixedContentMode());
        }


        WebSettings webSettings = getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setLoadsImagesAutomatically(true);

        webSettings.setAllowFileAccess(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setSupportZoom(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        // 字体大小不跟随系统变化
        webSettings.setTextZoom(100);


        //默认下载
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    ToastUtils.showToast("未找到相关App来打开");
                }
            }
        });

    }

}
