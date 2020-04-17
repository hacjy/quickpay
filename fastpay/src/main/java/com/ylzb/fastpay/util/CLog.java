package com.ylzb.fastpay.util;

import android.util.Log;

import com.ylzb.fastpay.BuildConfig;

/**
 * 控制输出
 */

public class CLog {
    //默认debug模式
    public static final boolean isLogDebug = BuildConfig.DEBUG;

    public static void v(String TAG, String msg) {
        if (isLogDebug) {
            Log.v(TAG, msg);
        }
    }

    public static void e(String TAG, String msg){
        if (isLogDebug){
            Log.e(TAG,msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (isLogDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (isLogDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (isLogDebug) {
            Log.i(TAG, msg);
        }
    }
}

