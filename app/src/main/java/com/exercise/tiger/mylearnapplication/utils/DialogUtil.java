package com.exercise.tiger.mylearnapplication.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.customview.CustomSelectDialog;

/**
 * 自定义弹框工具类
 * Created by hzj on 2017/9/20.
 */

public class DialogUtil {
    /**
     * 加载中对话框
     *
     * @param context
     * @return
     */
    public static CustomSelectDialog showLoadingDialog(Context context) {

        View loadingView =  LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        CustomSelectDialog dialog = new CustomSelectDialog(context);
        dialog.setCancleAble(false);
        dialog.setAutoDismiss(true);
        dialog.setDismissDelay(15*1000);//加载中如果15S还未获取数据则认为超时，此时自动消失
        dialog.setContentView(loadingView);
        dialog.setmIsShowAnim(true);
        dialog.setShowDismissAnim(true);
        dialog.show();

        return dialog;
    }

}
