package com.fan.numen.utils;

import android.util.Log;

/**
 * Created by zihao.wang on 17/10/27.
 */

public class Logger {
    private static boolean DEBUG = true;

    private static String TAG = "Numen";

    public static void i(String message) {
        if (DEBUG) {
            Log.i(TAG, message);
        }
    }
}
