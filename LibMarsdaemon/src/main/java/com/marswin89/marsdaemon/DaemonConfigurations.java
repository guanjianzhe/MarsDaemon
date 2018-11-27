package com.marswin89.marsdaemon;

import android.content.Context;

/**
 * the configurations of Daemon SDK, contains two process configuration.
 *
 * @author Mars
 */
public class DaemonConfigurations {

    /**
     * 主常驻进程配置
     */
    public final DaemonConfiguration PERSISTENT_CONFIG;
    /**
     * 辅助进程配置
     */
    public final DaemonConfiguration DAEMON_ASSISTANT_CONFIG;
    public final DaemonListener LISTENER;
    /**
     * daemon监控时间间隔(android版本21以上)，单位s
     */
    private int mDaemonWatchIntervalAboveAPI21 = DaemonConstants.DAEMON_WATCH_INTERVAL_ABOVE_API21;

    public DaemonConfigurations(DaemonConfiguration persistentConfig, DaemonConfiguration daemonAssistantConfig) {
        this.PERSISTENT_CONFIG = persistentConfig;
        this.DAEMON_ASSISTANT_CONFIG = daemonAssistantConfig;
        this.LISTENER = null;
    }

    public DaemonConfigurations(DaemonConfiguration persistentConfig, DaemonConfiguration daemonAssistantConfig, DaemonListener listener) {
        this.PERSISTENT_CONFIG = persistentConfig;
        this.DAEMON_ASSISTANT_CONFIG = daemonAssistantConfig;
        this.LISTENER = listener;
    }

    public int getDaemonWatchIntervalAboveAPI21() {
        return mDaemonWatchIntervalAboveAPI21;
    }

    /**
     * 设置daemon监控时间间隔(android版本21以上)，单位s。默认120s
     * @param interval
     */
    public void setDaemonWatchIntervalAboveAPI21(int interval) {
        if (interval <= 0) {
            interval = 1;
        }
        mDaemonWatchIntervalAboveAPI21 = interval;
    }

    /**
     * the configuration of a daemon process, contains process name, service name and receiver name if Android 6.0
     *
     * @author guoyang
     */
    public static class DaemonConfiguration {

        public final String PROCESS_NAME;
        public final String SERVICE_NAME;
        public final String RECEIVER_NAME;

        public DaemonConfiguration(String processName, String serviceName, String receiverName) {
            this.PROCESS_NAME = processName;
            this.SERVICE_NAME = serviceName;
            this.RECEIVER_NAME = receiverName;
        }

        public String toString() {
            return String.format("[DaemonConfiguration] mProcessName:%s, mServiceName:%s, mReceiverName:%s",
                    this.PROCESS_NAME, this.SERVICE_NAME, this.RECEIVER_NAME);
        }
    }

    /**
     * listener of daemon for external
     *
     * @author Mars
     */
    public interface DaemonListener {
        void onPersistentStart(Context context);

        void onDaemonAssistantStart(Context context);

        void onWatchDaemonDaed();
    }

    public DaemonConfiguration getPersistentConfig() {
        return PERSISTENT_CONFIG;
    }

    public DaemonConfiguration getDaemonAssistantConfig() {
        return DAEMON_ASSISTANT_CONFIG;
    }
}
