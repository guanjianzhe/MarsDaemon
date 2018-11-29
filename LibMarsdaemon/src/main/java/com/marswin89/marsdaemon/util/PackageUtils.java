package com.marswin89.marsdaemon.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.marswin89.marsdaemon.DaemonConstants;

import java.util.Iterator;
import java.util.List;

import static com.marswin89.marsdaemon.DaemonConstants.KEY_COMMAND_FROM;
import static com.marswin89.marsdaemon.DaemonConstants.VALUE_COMMAND_FROM;

/**
 * Utils to prevent component from third-party app forbidding
 *
 * @author Mars
 */
public class PackageUtils {
    public static void setComponentEnable(Context context, String componentClassName) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), componentClassName);

        try {
            pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            pm.setApplicationEnabledSetting(context.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    /**
     * set the component in our package default
     *
     * @param context
     * @param componentClassName
     */
    public static void setComponentDefault(Context context, String componentClassName) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), componentClassName);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }

    /**
     * get the component in our package default
     *
     * @param context
     * @param componentClassName
     */
    public static boolean isComponentDefault(Context context, String componentClassName) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context.getPackageName(), componentClassName);
        return pm.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }

    public static void startService(Context context, String serviceClassName) {
        startService(context, serviceClassName, true);
    }

    public static void startService(Context context, String serviceClassName, boolean isSetFromDaemonSdk) {
        LogUtils.i(DaemonConstants.TAG, "[PackageUtils::startService] serviceClassName:" + serviceClassName);
        if (context != null && serviceClassName != null) {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(context.getPackageName(), serviceClassName);
            intent.setComponent(componentName);
            if (isSetFromDaemonSdk) {
                setFromDaemonSdk(intent);
            }

            startServiceSafely(context, intent);
        }
    }

    public static boolean startServiceIfNotRunning(Context context, String serviceClassName) {
        if (!isServiceRunning(context, serviceClassName)) {
            startService(context, serviceClassName);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isServiceRunning(Context context, String serviceClassName) {
        if (serviceClassName == null) {
            return true;
        } else {
            try {
                String pkgName = context.getPackageName();
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                List<RunningServiceInfo> rsInfos = am.getRunningServices(Integer.MAX_VALUE);
                Iterator i$ = rsInfos.iterator();

                ComponentName service;
                do {
                    if (!i$.hasNext()) {
                        return false;
                    }

                    RunningServiceInfo info = (RunningServiceInfo) i$.next();
                    service = info.service;
                }
                while (service == null || !pkgName.equals(service.getPackageName()) || !serviceClassName.equals(service.getClassName()));

                return true;
            } catch (Exception var8) {
                return true;
            }
        }
    }

    public static void setFromDaemonSdk(Intent intent) {
        if (intent != null) {
            intent.putExtra(KEY_COMMAND_FROM, VALUE_COMMAND_FROM);
        }

    }

    public static boolean isFromDaemonSdk(Intent intent) {
        return VALUE_COMMAND_FROM.equals(intent.getStringExtra(KEY_COMMAND_FROM));
    }

    public static void startServiceSafely(Context context, Intent intent) {
        if (context != null && intent != null) {
            try {
                context.startService(intent);
            } catch (Throwable var3) {
                LogUtils.e(DaemonConstants.TAG, "error-->", var3);
            }

        }
    }
}
