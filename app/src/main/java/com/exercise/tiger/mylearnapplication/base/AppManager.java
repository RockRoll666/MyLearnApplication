package com.exercise.tiger.mylearnapplication.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 **************************************************************************
 * @ClassName: AppManager
 * @Description: 应用程序Activity管理类：用于Activity管理和应用程序退出
 ***************************************************************************
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public synchronized static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.push(activity);
        Log.d("appstackadd",activityStack.size()+",activityadd:"+activity);
    }

    /**
     * activity执行finish时从栈中移除该activity
     * @param activity
     */
    public void removeActivity(Activity activity){
        if (null != activityStack && activity != null){
            if (activity == activityStack.peek()){
                activityStack.pop();
            }else {
                activityStack.removeElement(activity);
            }
        }
        Log.d("appstackremove",activityStack.size()+",activityaremove:"+activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.peek();
        Log.d("appstackcurrent",activityStack.size()+",activityacurrent:"+activity);
        return activity;
    }

    public Activity getActivity(Class<? extends Activity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.peek();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (null != activityStack){
            while (!activityStack.empty()){
                Activity activity = activityStack.peek();
                activity.finish();
            }
        }
    }

    /**
     * 只留一个栈顶activity
     */
    public void finishAllActivityExceptCurrent() {
        if (null != activityStack){
            while (activityStack.size()>1){
                //循环finish并移除栈顶的前一个activity
                Activity activity = activityStack.elementAt(activityStack.size()-2);
                activity.finish();
            }
        }
    }

    /**
     * 退出所有Activity
     *
     * @return 是否完全退出
     */
    public boolean exitAllActivity() {
        Log.e("AppManager", "应用程序退出exitAllActivity()");
        boolean isFinished = true;
        for (Activity act : activityStack) {
            if (act != null) {
                act.finish();
            }
        }
        for (Activity act : activityStack) {
            if (!act.isFinishing()) {
                isFinished = false;
            }
        }
        return isFinished;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//			ActivityManager activityMgr = (ActivityManager) context
//					.getSystemService(Context.ACTIVITY_SERVICE);
            if (exitAllActivity()) {
                // activityMgr.killBackgroundProcesses(context.getPackageName());
                NotificationManager nManager =((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
                nManager.cancelAll();//取消通知栏所有通知
                System.exit(0);
            }
        } catch (Exception e) {
            Log.e("AppManager", "退出应用程序异常!");
            StackTraceElement[] stacks = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            for (StackTraceElement stack : stacks) {
                sb.append(stack.toString() + "\n");
            }
            Log.e("AppManager", sb.toString());
        }
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public static void setActivityStack(Stack<Activity> activityStack) {
        AppManager.activityStack = activityStack;
    }

    public boolean isAppRunning(){
        if (null == activityStack || activityStack.size() == 0){
            return false;
        }
        return true;
    }

    public boolean hasActivity(Class cla){
        if (null == activityStack || activityStack.size() == 0){
            return false;
        }
        for (Activity a : activityStack) {
            if (a.getClass().equals(cla)){
                return true;
            }
        }
        return false;
    }
}