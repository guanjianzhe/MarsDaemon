package com.marswin89.marsdaemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marswin89.marsdaemon.util.LogUtils;
import com.marswin89.marsdaemon.util.PackageUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final long HANDLE_TIME_INTERVAL = 500L;
    private static long sLastHandleTime;

    public BootCompleteReceiver() {
    }


    public void onReceive(Context context, Intent intent) {
        if (LogUtils.sIsLog) {
            LogUtils.i("Daemon", "BootCompleteReceiver::onReceive-->action:" + intent.getAction());
        }

        String persistentServiceName = DaemonClient.getInstance().getPersistentServiceName();
        if (DaemonClient.getInstance().isDaemonPermitting(context) && isHandle() && !PackageUtils.isServiceRunning(context, persistentServiceName)) {
            LogUtils.i("Daemon", "BootCompleteReceiver::onReceive-->启动被守护的进程");
            PackageUtils.startService(context, persistentServiceName);
        }

    }

    private static boolean isHandle() {
        long time = System.currentTimeMillis();
        if (time - sLastHandleTime > 500L) {
            sLastHandleTime = time;
            return true;
        } else {
            return false;
        }
    }
}

