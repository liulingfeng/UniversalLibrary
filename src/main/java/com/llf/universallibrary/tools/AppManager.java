package com.llf.universallibrary.tools;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by llf on 2016/9/29.
 * Activity管理类
 */

public class AppManager {
    private static AppManager sAppManager;
    private static Stack<Activity> sActivities;

    private AppManager(){

    }

    public static AppManager getAppManager(){
        if(sAppManager == null) {
            synchronized (AppManager.class) {
                sAppManager = new AppManager();
                sAppManager.sActivities = new Stack<>();
            }
        }
        return sAppManager;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity){
        sActivities.add(activity);
    }
    /**
     * 获取栈顶的Activity
     */
    public Activity getCurrentActivity(){
        try{
            return sActivities.lastElement();
        }catch (Exception e) {
            return null;
        }
    }
    /**
     * 结束栈顶的Activity
     */
    public void finishActivity() {
        try{
            Activity activity = sActivities.lastElement();
            finishActivity(activity);
        }catch (Exception e){

        }
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            sActivities.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 结束所有的Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = sActivities.size(); i < size; i++) {
            if (null != sActivities.get(i)) {
                sActivities.get(i).finish();
            }
        }
        sActivities.clear();
    }
}
