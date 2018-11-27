package com.marswin89.marsdaemon.strategy;

import android.annotation.TargetApi;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build.VERSION;

import com.marswin89.marsdaemon.DaemonConfigurations;
import com.marswin89.marsdaemon.IDaemonStrategy;
import com.marswin89.marsdaemon.JobSchedulerService;
import com.marswin89.marsdaemon.util.LogUtils;

public class DaemonStrategyJobScheduler implements IDaemonStrategy {

    private Context mContext;
    private DaemonConfigurations mConfigs;
    private JobScheduler mJobScheduler;
    private static DaemonStrategyJobScheduler sInstance;

    public DaemonStrategyJobScheduler() {
        sInstance = this;
    }

    @Override
    public boolean onInitialization(Context context) {
        mContext = context;
        return true;
    }

    @Override
    public void onPersistentCreate(Context context, DaemonConfigurations configs) {
        this.mContext = context;
        this.mConfigs = configs;
        if (VERSION.SDK_INT >= 21) {
            (new Thread("daemon") {
                public void run() {
                    DaemonStrategyJobScheduler.this.startJob();
                }
            }).start();
        }
    }

    public static void restartJob() {
        if (sInstance != null) {
            sInstance.startJob();
        }

    }

    @TargetApi(21)
    private void startJob() {
        if (this.mContext != null && this.mConfigs != null) {
            if (this.mJobScheduler == null) {
                this.mJobScheduler = (JobScheduler) this.mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            }

            int jobId = this.getClass().hashCode();
            this.mJobScheduler.cancel(jobId);
            Builder builder = new Builder(jobId, new ComponentName(this.mContext.getPackageName(),
                    JobSchedulerService.class.getCanonicalName()));
            if (VERSION.SDK_INT >= 24) {
                builder.setMinimumLatency((long) (this.mConfigs.getDaemonWatchIntervalAboveAPI21() * 1000));
            } else {
                builder.setPeriodic((long) (this.mConfigs.getDaemonWatchIntervalAboveAPI21() * 1000));
            }

            try {
                if (this.mJobScheduler.schedule(builder.build()) <= 0) {
//                    LogUtils.w("Daemon", "DaemonStrategyJobScheduler::startJob-->failed!!!");
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        } else {
//            LogUtils.w("Daemon", "[DaemonStrategyJobScheduler#startJob] ", new IllegalArgumentException("context is null!"));
        }
    }


    @Override
    public void onDaemonAssistantCreate(Context context, DaemonConfigurations configs) {
        this.mContext = context;
    }

    @Override
    public void onDaemonDead() {

    }

    @TargetApi(21)
    public void cancelDaemon(Context context) {
        LogUtils.i("Daemon", "DaemonStrategyJobScheduler::cancelDaemon-->");
        if (this.mJobScheduler == null) {
            this.mJobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        }

        int jobId = this.getClass().hashCode();
        this.mJobScheduler.cancel(jobId);
    }
}
