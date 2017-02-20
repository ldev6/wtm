package com.montreal.wtm.utils;

import android.util.Log;


public class LogU {

    public static boolean enableLog = false ;
    public static void v (String tag, String message) {
        if(enableLog) {
            Log.v(tag, message);
        }
    }
    public static void v (String tag, String message, Throwable throwable) {
        if(enableLog) {
            Log.v(tag, message, throwable);
        }
    }

    public static void i (String tag, String message) {
        if(enableLog) {
            Log.i(tag, message);
        }
    }

    public static void i (String tag, String message, Throwable throwable) {
        if(enableLog) {
            Log.i(tag, message, throwable);
        }
    }

    public static void e (String tag, String message) {
        if(enableLog) {
            Log.e(tag, message);
        }
    }

    public static void e (String tag, String message, Throwable throwable) {
        if(enableLog) {
            Log.e(tag, message, throwable);
        }
    }

    public static void d (String tag, String message) {
        if(enableLog) {
            Log.d(tag, message);
        }
    }

    public static void d (String tag, String message, Throwable throwable) {
        if(enableLog) {
            Log.d(tag, message, throwable);
        }
    }
}
