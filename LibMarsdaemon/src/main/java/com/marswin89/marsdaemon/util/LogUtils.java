package com.marswin89.marsdaemon.util;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class LogUtils {
    public static boolean sIsLog = false;
    public static boolean sIsWrite2File = false;
    public static String sTag = "ZH";
    public static final String LOGFILE = Environment.getExternalStorageDirectory().getPath() + "/matt/matt-log.txt";
    private static final int VERBOSE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;
    private static final String[] LOGLEVELS = new String[]{"VERBOSE", "DEBUG  ", "INFO   ", "WARN   ", "ERROR  "};

    public LogUtils() {
    }

    public static void v(String msg) {
        v(sTag, msg);
    }

    public static void d(String msg) {
        d(sTag, msg);
    }

    public static void i(String msg) {
        i(sTag, msg);
    }

    public static void w(String msg) {
        w(sTag, msg);
    }

    public static void e(String msg) {
        e(sTag, msg);
    }

    public static void v(String tag, String msg) {
        if (sIsLog) {
            Log.v(tag, msg);
            writelog(tag, msg, (Throwable) null, 0);
        }

    }

    public static void v(String tag, String msg, Throwable tr) {
        if (sIsLog) {
            Log.v(tag, msg, tr);
            writelog(tag, msg, tr, 0);
        }

    }

    public static void d(String tag, String msg) {
        if (sIsLog) {
            Log.d(tag, msg);
            writelog(tag, msg, (Throwable) null, 1);
        }

    }

    public static void d(String tag, String msg, Throwable tr) {
        if (sIsLog) {
            Log.d(tag, msg, tr);
            writelog(tag, msg, tr, 1);
        }

    }

    public static void i(String tag, String msg) {
        if (sIsLog) {
            Log.i(tag, msg);
            writelog(tag, msg, (Throwable) null, 2);
        }

    }

    public static void i(String tag, String msg, Throwable tr) {
        if (sIsLog) {
            Log.i(tag, msg, tr);
            writelog(tag, msg, tr, 2);
        }

    }

    public static void w(String tag, String msg) {
        if (sIsLog) {
            Log.w(tag, msg);
            writelog(tag, msg, (Throwable) null, 3);
        }

    }

    public static void w(String tag, String msg, Throwable tr) {
        if (sIsLog) {
            Log.w(tag, msg, tr);
            writelog(tag, msg, tr, 3);
        }

    }

    public static void w(String tag, Throwable tr) {
        if (sIsLog) {
            Log.w(tag, tr);
            writelog(tag, "", tr, 3);
        }

    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        if (sIsLog) {
            writelog(tag, "", (Throwable) null, 4);
        }

    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
        if (sIsLog) {
            writelog(tag, "", tr, 4);
        }

    }

    public static void showToast(Context context, CharSequence text, int duration) {
        if (sIsLog) {
            Toast.makeText(context, text, duration).show();
        }

    }

    public static void showToast(Context context, int resId, int duration) {
        if (sIsLog) {
            Toast.makeText(context, resId, duration).show();
        }

    }

    public static String getCurrentStackTraceString() {
        return Log.getStackTraceString(new Throwable());
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    private static synchronized void writelog(String tag, String msg, Throwable tr, int level) {
        if (sIsWrite2File) {
            ;
        }

    }

    private static String getLogString(String tag, String msg, Throwable tr, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append(DateFormat.format("yyyy-MM-dd kk:mm:ss", System.currentTimeMillis()).toString());
        sb.append('\t');
        sb.append(LOGLEVELS[level]);
        sb.append('\t');
        sb.append(tag);
        sb.append('\t');
        sb.append(msg);
        if (tr != null) {
            sb.append("\r\n");
            sb.append(getStackTraceString(tr));
        }

        sb.append("\r\n");
        return sb.toString();
    }
}
