package com.tokentm.sdk.demo;

import android.app.Application;
import android.util.Log;

import com.tokentm.sdk.TokenTmClient;
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
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("======>", "error:" + Log.getStackTraceString(throwable));
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
            public void d(String msg, Throwable tr) {
                Log.d("=======>", msg + Log.getStackTraceString(tr));
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
