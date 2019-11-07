package com.tokentm.sdk.uidemo;

import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.RequiresApi;

import com.google.gson.JsonParseException;
import com.xxf.arch.http.ResponseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import io.reactivex.annotations.NonNull;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.3.1
 * @Description 转换成读得懂得语言
 * @date createTime：2018/1/15
 */
public class ThrowableConvertUtils {

    /**
     * 将网络异常转换可读懂的语言
     *
     * @param t
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static String convertThrowable2String(@NonNull Throwable t) {
        if (t != null) {
            if (t instanceof ResponseException) {
                ResponseException responseException = (ResponseException) t;
                return String.format("%s:%s", responseException.code, responseException.message);
            } else if (t instanceof retrofit2.HttpException) {
                retrofit2.HttpException httpException = (retrofit2.HttpException) t;
                String combHttpExceptionStr = String.format("%s:%s", httpException.code(), httpException.message());
                return combHttpExceptionStr;
            } else if (t instanceof JsonParseException) {
                return "服务器Json格式错误";
            } else if (t instanceof java.net.UnknownHostException) {
                return "无网络连接";
            } else if (t instanceof NoRouteToHostException) {
                return "服务器路由地址错误";
            } else if (t instanceof ConnectException) {
                return "无网络连接";
            } else if (t instanceof SocketException) {
                return "网络不稳定或服务器繁忙";
            } else if (t instanceof SocketTimeoutException) {
                return "服务器响应超时";
            } else if (t instanceof FileNotFoundException) {
                return "文件权限被拒绝或文件找不到";
            } else if (t instanceof NetworkOnMainThreadException) {
                return "在主线程操作网络";
            } else if (t instanceof SSLHandshakeException) {
                return "网络不稳定,请稍后重试";
            } else if (t instanceof SSLException) {
                //return "服务器拒绝ssl握手";
                return "";
            } else if (t instanceof IOException) {
                return "服务器IO异常";
            } else if (t instanceof NullPointerException) {
                return "服务器返回数据为null";
            } else {
                return t.getMessage();
            }
        }
        return "";
    }
}
