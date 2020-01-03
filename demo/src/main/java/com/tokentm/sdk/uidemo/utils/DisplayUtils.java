package com.tokentm.sdk.uidemo.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

public class DisplayUtils {
    public DisplayUtils() {
    }

    public static DisplayMetrics getWindowDisplayMetrics(Activity activity) {
        if (activity != null) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric;
        } else {
            return null;
        }
    }

    @RequiresApi(
        api = 13
    )
    public static Point getUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService("window");
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                try {
                    size.x = (Integer)Display.class.getMethod("getRawWidth").invoke(display);
                } catch (InvocationTargetException var5) {
                    var5.printStackTrace();
                }

                size.y = (Integer)Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException var6) {
                var6.printStackTrace();
            } catch (InvocationTargetException var7) {
                var7.printStackTrace();
            } catch (NoSuchMethodException var8) {
                var8.printStackTrace();
            }
        }

        return size;
    }

    @RequiresApi(
        api = 13
    )
    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        } else {
            return appUsableSize.y < realScreenSize.y ? new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y) : new Point();
        }
    }

    public static float getScreenBrightness(Activity activity) {
        return activity != null ? activity.getWindow().getAttributes().screenBrightness : 1.0F;
    }

    public static void setScreenBrightness(Activity activity, float brightness) {
        if (activity != null) {
            Window window = activity.getWindow();
            window.getAttributes().screenBrightness = brightness;
            window.setAttributes(window.getAttributes());
        }

    }

    public static int dip2px(Context context, float dpValue) {
        if (context != null) {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dpValue * scale + 0.5F);
        } else {
            return 0;
        }
    }

    public static float dip2pxWithResId(Context context, int resId) {
        float scale = context.getResources().getDisplayMetrics().density;
        float dpValue = context.getResources().getDimension(resId);
        return dpValue * scale + 0.5F;
    }

    public static float sp2px(Context context, float sp) {
        if (context != null) {
            float scale = context.getResources().getDisplayMetrics().scaledDensity;
            return sp * scale;
        } else {
            return 0.0F;
        }
    }

    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            throw new IllegalArgumentException("content is null");
        } else {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int)(pxValue / scale + 0.5F);
        }
    }

    public static void showSoftPan(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService("input_method");
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.showSoftInput(activity.getCurrentFocus(), 0);
            }
        }

    }

    public static void closeSoftPan(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService("input_method");
            View focus = activity.getCurrentFocus();
            if (focus != null) {
                inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            }
        }

    }

    public static void closeSoftPan(View v) {
        if (v != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)v.getContext().getSystemService("input_method");
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        }

    }

    public static void setInputMethodVisibility(Context context, EditText editText, boolean visible) {
        if (context != null && editText != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService("input_method");
            if (visible) {
                imm.toggleSoftInput(0, 2);
            } else {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 2);
            }

            editText.setTag(visible);
        }
    }

    public boolean isLauncherOnTop(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        } else {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            ArrayList<String> launcherPackageNames = new ArrayList();
            Iterator var4 = context.getPackageManager().queryIntentActivities(intent, 0).iterator();

            while(var4.hasNext()) {
                ResolveInfo info = (ResolveInfo)var4.next();
                launcherPackageNames.add(info.activityInfo.packageName);
            }

            ActivityManager am = (ActivityManager)context.getSystemService("activity");
            Iterator var9 = am.getRunningTasks(1).iterator();

            while(var9.hasNext()) {
                ActivityManager.RunningTaskInfo t = (ActivityManager.RunningTaskInfo)var9.next();
                if (t != null && t.numRunning > 0) {
                    ComponentName componentName = t.baseActivity;
                    if (componentName != null && launcherPackageNames.contains(componentName.getPackageName())) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}