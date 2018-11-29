package com.marswin89.marsdaemon;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.marswin89.marsdaemon.strategy.DaemonStrategyJobScheduler;
import com.marswin89.marsdaemon.util.LogUtils;
import com.marswin89.marsdaemon.util.PackageUtils;

import java.lang.reflect.Field;

@TargetApi(21)
public class JobSchedulerService extends JobService {
    private boolean mIsHook;

    public JobSchedulerService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.i(DaemonConstants.TAG, "JobSchedulerService::onStartJob-->");
        this.checkStartPersistentService();
        this.hookJobHandler(this);
        this.jobFinished(params, false);
        if (VERSION.SDK_INT >= 24) {
            DaemonStrategyJobScheduler.restartJob();
        }

        this.stopSelf();
        return true;
    }

    private void checkStartPersistentService() {
        String persistentServiceName = DaemonClient.getInstance().getPersistentServiceName();
        if (!PackageUtils.isServiceRunning(this, persistentServiceName)) {
            LogUtils.i(DaemonConstants.TAG, "JobSchedulerService::checkStartPersistentService-->启动被守护进程的服务");
            PackageUtils.startService(this, persistentServiceName);
        }

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.i(DaemonConstants.TAG, "JobSchedulerService::onStopJob-->");
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(DaemonConstants.TAG, "JobSchedulerService::onCreate-->");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i(DaemonConstants.TAG, "JobSchedulerService::onStartCommand-->");
        return super.onStartCommand(intent, flags, startId);
    }

    private void hookJobHandler(Object jobServiceSubObj) {
        if (!this.mIsHook) {
            this.mIsHook = true;

            try {
                Field handlerField = jobServiceSubObj.getClass().getSuperclass().getDeclaredField("mHandler");
                handlerField.setAccessible(true);
                Handler handler = (Handler) handlerField.get(jobServiceSubObj);
                Field callbackField = handler.getClass().getSuperclass().getDeclaredField("mCallback");
                callbackField.setAccessible(true);
                Callback callback = (Callback) callbackField.get(handler);
                callbackField.set(handler, new JobSchedulerService.CallbackWrapper(handler, callback));
            } catch (NoSuchFieldException var6) {
                var6.printStackTrace();
            } catch (IllegalAccessException var7) {
                var7.printStackTrace();
            } catch (Throwable var8) {
                var8.printStackTrace();
            }

        }
    }

    private static class CallbackWrapper implements Callback {
        private Handler mHandler;
        private Callback mCallback;

        CallbackWrapper(Handler handler, Callback callback) {
            this.mHandler = handler;
            this.mCallback = callback;
        }

        public boolean handleMessage(Message msg) {
            if (this.mCallback != null && this.mCallback.handleMessage(msg)) {
                return true;
            } else {
                if (msg != null && LogUtils.sIsLog) {
                    LogUtils.i(DaemonConstants.TAG, "JobSchedulerService::handleMessage-->msg.what=" + msg.what);
                }

                try {
                    this.mHandler.handleMessage(msg);
                } catch (Throwable var3) {
                    var3.printStackTrace();
                }

                return true;
            }
        }
    }
}
