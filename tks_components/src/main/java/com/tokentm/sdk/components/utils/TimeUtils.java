package com.tokentm.sdk.components.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String formatUtc(long timeMillis) {
        if (timeMillis < 0) {
            return "Ignore time!";
        }
        if (String.valueOf(timeMillis).length() == 10) {
            timeMillis *= 1000;
        }
        //标准格式:yyy-MM-dd HH:mm,不要修改
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss", Locale.CHINA);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String formatYMD(long timeMillis) {
        if (timeMillis < 0) {
            return "Ignore time!";
        }
        //标准格式:yyy-MM-dd HH:mm,不要修改
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

}
