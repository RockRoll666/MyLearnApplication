package com.exercise.tiger.mylearnapplication.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * create by hzj on 2018/4/18
 **/
public final class GlobalConstant {

    private static int DEVICE_WIDTH;
    private static int DEVICE_HEIGHT;
    private static float DEVICE_DENSITY;

    public static void initDeviceInfo(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        DEVICE_WIDTH = metrics.widthPixels;
        DEVICE_HEIGHT = metrics.heightPixels;
        DEVICE_DENSITY = metrics.density;
    }

    public static int getDeviceWidth() {
        return DEVICE_WIDTH;
    }

    public static int getDeviceHeight() {
        return DEVICE_HEIGHT;
    }

    public static float getDeviceDensity() {
        return DEVICE_DENSITY;
    }
}
