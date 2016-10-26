package com.llf.universallibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

/**
 * Created by llf on 2016/10/26.
 * 基础Application
 */

public class BaseApplication extends Application{
    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = this;
    }

    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    /**
     * 分包
     * 用于解决60K方法数限制
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
