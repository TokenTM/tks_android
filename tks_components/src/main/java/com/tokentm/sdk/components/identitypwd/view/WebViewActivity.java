package com.tokentm.sdk.components.identitypwd.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityWebviewBinding;
import com.tokentm.sdk.components.webview.BaseWebView;
import com.tokentm.sdk.components.webview.BaseWebViewClient;


/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 展示h5
 */
public class WebViewActivity extends BaseTitleBarActivity {

    private static final String URL = "url";
    private String url;

    public static void launch(@NonNull Context context, @Nullable String url) {
        context.startActivity(getLauncher(context, url));
    }

    public static Intent getLauncher(Context context, String url) {
        return new Intent(context, WebViewActivity.class)
                .putExtra(URL, url);
    }

    private TksComponentsActivityWebviewBinding binding;
    private BaseWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        url = getIntent().getStringExtra(URL);
        mWebView = binding.mWebView;
        mWebView.setWebViewClient(new BaseWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {

                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getTitleBar().setTitleBarTitle(title);
            }
        });
        mWebView.loadUrl(url);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }
}