package com.tokentm.sdk.uidemo;

import android.app.Application;
import android.util.Log;

import com.github.moduth.blockcanary.BlockCanary;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.tools.AppContext;
import com.xxf.arch.XXF;
import com.xxf.arch.core.Logger;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BlockCanary.install(this, new AppContext()).start();
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable.getCause() != null) {
                    Log.d("======>", "error:" + Log.getStackTraceString(throwable.getCause()));
                } else {
                    Log.d("======>", "error:" + Log.getStackTraceString(throwable));
                }
            }
        });
        //初始化
        TokenTmClient.init(this);
        XXF.init(this, new Logger() {
            @Override
            public boolean isLoggable() {
                return true;
            }

            @Override
            public void d(String msg) {
                Log.d("=======>", msg);
            }

            @Override
            public void d(String msg, Throwable throwable) {
                if (throwable.getCause() != null) {
                    Log.d("======>", msg + Log.getStackTraceString(throwable.getCause()));
                } else {
                    Log.d("======>", msg + Log.getStackTraceString(throwable));
                }
            }

            @Override
            public void e(String msg) {

            }

            @Override
            public void e(String msg, Throwable tr) {

            }
        }, new Consumer<Throwable>() {

            @Override
            public void accept(Throwable throwable) throws Exception {
                ToastUtils.showToast(ThrowableConvertUtils.convertThrowable2String((throwable)));
            }
        }, new Function<Throwable, String>() {


            @Override
            public String apply(Throwable throwable) throws Exception {
                return ThrowableConvertUtils.convertThrowable2String(throwable);
            }
        });
    }
}
