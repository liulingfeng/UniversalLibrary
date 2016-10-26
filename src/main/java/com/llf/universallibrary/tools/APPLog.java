package com.llf.universallibrary.tools;

import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;


/**
 * Created by llf on 2016/7/26.
 * log打印工具
 */
public class APPLog {
    private static final String TAG = "刘帅的library";
    public static void init() {
        Logger.init(TAG);
    }

    /**
     * log.i
     * @param msg
     */
    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.i(msg);
        }
    }

    /**
     * log.d
     * @param msg
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.d(msg);
        }
    }

    /**
     * log.w
     * @param msg
     */
    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.w(msg);
        }
    }

    /**
     * log.e
     * @param msg
     */
    public static void e(String msg) {
        Logger.e(msg);
    }
}
