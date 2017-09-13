package com.exercise.tiger.mylearnapplication.utils;

import android.content.Context;
import android.content.Intent;

/**
 * activity启动工具类
 * Created by hzj on 2017/9/13.
 */

public class ActivityUtils {

    public static void startNewActivity(Context from , Class to){
        Intent intent = new Intent(from,to);
        from.startActivity(intent);
    }

    public static void startNewActivity(Context from , Intent intent){
        from.startActivity(intent);
    }
}
