package com.tokentm.sdk.http;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.xxf.arch.http.ResponseException;
import com.xxf.arch.http.converter.gson.GsonConvertInterceptor;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 全局处理转换结果
 */
public class GlobalGsonConvertInterceptor implements GsonConvertInterceptor {
    public GlobalGsonConvertInterceptor() {

    }

    @SuppressLint("WrongConstant")
    @Override
    public <T> T onResponseBodyIntercept(Gson gson, TypeAdapter<T> adapter, ResponseBody value, T convertedBody) throws IOException {
        if (convertedBody != null
                && convertedBody instanceof ResponseDTO) {
            ResponseDTO responseDTO = (ResponseDTO) convertedBody;
            if (responseDTO.statusCode != ResponseDTO.CODE_SUCCESS) {
                throw new ResponseException(responseDTO.statusCode, responseDTO.msg);
            } else if (responseDTO.data == null) {
                throw new ResponseException(ResponseDTO.CODE_BODY_NULL, "返回data字段null");
            }
        }
        return convertedBody;
    }
}
