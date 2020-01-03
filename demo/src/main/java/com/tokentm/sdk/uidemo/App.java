package com.tokentm.sdk.uidemo;

import android.app.Application;
import android.util.Log;

import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.core.Logger;
import com.xxf.arch.utils.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description application
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //BlockCanary.install(this, new AppContext()).start();
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
        disableAPIDialog();
    }

    /**
     * 反射 禁止弹窗  提示Detected problems with API 弹窗
     */
    private void disableAPIDialog() {
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
