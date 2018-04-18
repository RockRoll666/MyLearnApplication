package com.exercise.tiger.mylearnapplication.base;

import android.app.Application;
import android.content.Context;

import com.exercise.tiger.mylearnapplication.utils.GlobalConstant;

/**
 * 程序application
 * Created by hzj on 2017/9/28.
 */

public class MyApplication extends Application {
    //TODO
    private static Context context;

    @Override
    public void onCreate() {
        //获取Context
        super.onCreate();
        context = getApplicationContext();
        GlobalConstant.initDeviceInfo(context);
    }

    //返回
    public static Context getContextObject(){
        return context;
    }
}
