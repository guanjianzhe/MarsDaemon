package com.marswin89.marsdaemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.marswin89.marsdaemon.util.LogUtils;
import com.marswin89.marsdaemon.util.PackageUtils;

/**
 * @author Mars
 */
public class DaemonClient implements IDaemonClient {

    private static DaemonClient sInstance;
    private Context mContext;
    private DaemonConfigurations mConfigurations;

    public DaemonClient() {
    }

    public static DaemonClient getInstance() {
        if (sInstance == null) {
            sInstance = new DaemonClient();
        }
        return sInstance;
    }

    public void init(DaemonConfigurations configurations) {
        this.mConfigurations = configurations;
        if (LogUtils.sIsLog) {
            LogUtils.i(DaemonConstants.TAG, String.format("DaemonClient::init-->{persistent:%s}, {daemon:%s}",
                    configurations.getPersistentConfig().toString(),
                    configurations.getDaemonAssistantConfig().toString()));
        }
    }

    /**
     * 获取被守护的进程服务名称
     *
     * @return
     */
    public String getPersistentServiceName() {
        if (mConfigurations != null && mConfigurations.getPersistentConfig() != null) {
            return mConfigurations.getPersistentConfig().SERVICE_NAME;
        }
        return null;
    }

    @Override
    public void onAttachBaseContext(Context base) {
        mContext = base;

        new Thread() {
            @Override
            public void run() {
                initDaemon(mContext);
            }
        }.start();

        //防止开机广播被杀软禁止
        PackageUtils.setComponentEnable(base, BootCompleteReceiver.class.getName());
    }


    private final String DAEMON_PERMITTING_SP_FILENAME = "d_permit";
    private final String DAEMON_PERMITTING_SP_KEY = "permitted";


    private BufferedReader mBufferedReader;//release later to save time


    /**
     * do some thing about daemon
     *
     * @param base
     */
    private void initDaemon(Context base) {
        if (!isDaemonPermitting(base) || mConfigurations == null) {
            return;
        }
        String processName = getProcessName();
        String packageName = base.getPackageName();

        if (processName.startsWith(mConfigurations.PERSISTENT_CONFIG.PROCESS_NAME)) {
            IDaemonStrategy.Fetcher.fetchStrategy().onPersistentCreate(base, mConfigurations);
        } else if (processName.startsWith(mConfigurations.DAEMON_ASSISTANT_CONFIG.PROCESS_NAME)) {
            IDaemonStrategy.Fetcher.fetchStrategy().onDaemonAssistantCreate(base, mConfigurations);
        } else if (processName.startsWith(packageName)) {
            IDaemonStrategy.Fetcher.fetchStrategy().onInitialization(base);
        }

        releaseIO();
    }

    /**
     * 守护其它常驻进程
     */
    private void daemonOtherPersistentProcesses(Context context) {
//		List<String> otherPersistentServices = mConfigurations.getOtherPersistenServices();
//		if (otherPersistentServices == null || otherPersistentServices.size() <= 0) {
//			return;
//		}
//
//		int alarmId = DaemonProcessAlarm.ALARMID_ACTIVATE_OTHRE_PERSISTENT_PROCESS;
//		long triggerDelay = mConfigurations.getDaemonOtherPersistentProcessTrigger() * 1000;
//		long interval = mConfigurations.getDaemonOtherPersistentProcessInterval() * 1000;
//		DaemonProcessAlarm.getAlarm(context).alarmRepeat(alarmId, triggerDelay, interval, true, new CustomAlarm.OnAlarmListener() {
//
//			@Override
//			public void onAlarm(int alarmId) {
//				List<String> otherPersistentServices = mConfigurations.getOtherPersistenServices();
//				for (String serviceName : otherPersistentServices) {
//					PackageUtils.startServiceIfNotRunning(mContext, serviceName);
//				}
//			}
//		});
    }

	/* spend too much time !! 60+ms
	private String getProcessName(){
		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		int pid = android.os.Process.myPid();
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for (int i = 0; i < infos.size(); i++) {
			RunningAppProcessInfo info = infos.get(i);
			if(pid == info.pid){
				return info.processName;
			}
		}
		return null;
	}
	*/

    private String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            mBufferedReader = new BufferedReader(new FileReader(file));
            return mBufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * release reader IO
     */
    private void releaseIO() {
        if (mBufferedReader != null) {
            try {
                mBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBufferedReader = null;
        }
    }

    public boolean isDaemonPermitting(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DAEMON_PERMITTING_SP_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean(DAEMON_PERMITTING_SP_KEY, true);
    }

    protected boolean setDaemonPermiiting(Context context, boolean isPermitting) {
        SharedPreferences sp = context.getSharedPreferences(DAEMON_PERMITTING_SP_FILENAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(DAEMON_PERMITTING_SP_KEY, isPermitting);
        return editor.commit();
    }

    /**
     * 设置debug模式，打开log
     */
    public void setDebugMode() {
        LogUtils.sIsLog = true;
    }
}
