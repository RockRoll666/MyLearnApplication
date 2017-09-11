package com.exercise.tiger.mylearnapplication.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * fragmentactivity基类
 * Created by hzj on 2017/9/11.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected abstract void initView();

    /**
     * 获取activity layout
     * @return
     */
    protected abstract int getLayoutID();


    /**
     * 将数据加载到控件中
     */
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        getDataFromIntent(savedInstanceState);
        setContentView(getLayoutID());
        initView();
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.getAppManager().removeActivity(this);
    }

    /**
     * 简化findviewbyid方法
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    /**
     * 简化findeviewbyid方法
     * @param rootView 父view
     * @param id 资源id
     * @param <T> 返回类型
     * @return
     */
    protected <T extends View> T $(View rootView,int id){
        return (T) rootView.findViewById(id);
    }

    /**
     * 从intent中初始化界面数据
     */
    protected abstract void getDataFromIntent(Bundle savedInstanceState);
}
