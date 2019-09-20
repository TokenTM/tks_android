package com.tokentm.cert.demo;

import android.app.Application;
import android.util.Log;

import com.tokentm.cert.CertClient;

import io.reactivex.functions.Consumer;
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
        CertClient.init(this);
    }
}
