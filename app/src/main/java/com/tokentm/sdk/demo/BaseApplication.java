package com.tokentm.sdk.demo;

import android.app.Application;
import android.util.Log;

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
        TokenTmClient.init(this);
    }
}
