package com.tokentm.sdk.http;

import com.google.gson.annotations.SerializedName;


/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-06-01 16:59
 * <p>
 * 不要轻易修改
 * 不要轻易修改
 */

public class ResponseDTO<T> {
    private static final String FIELD_CODE = "statusCode";
    private static final String FIELD_MESSAGE = "msg";
    private static final String FIELD_RESULT = "data";

    public static int CODE_SUCCESS = 100000;
    public static final int CODE_TOKEN_OVER_TIME = 200004;
    public static int CODE_BODY_NULL = -1;


    @SerializedName(value = FIELD_MESSAGE)
    public String msg;

    @SerializedName(value = FIELD_CODE)
    public int statusCode;

    //注意 空
    @SerializedName(value = FIELD_RESULT)
    public T data;

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "msg='" + msg + '\'' +
                ", statusCode=" + statusCode +
                ", data=" + data +
                '}';
    }

}
