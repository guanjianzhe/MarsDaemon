package com.marswin89.marsdaemon;

import android.content.Context;
import android.os.Build;

import java.io.File;

public class DaemonConstants {
    public static final String TAG = "daemon";
    public static final String PRIVATE_DIR = "daemon";
    public static final String SEED_DAEMON_FILE_NAME = "seed_daemon";
    public static final int DAEMON_WATCH_INTERVAL = 60;
    public static final int DAEMON_WATCH_INTERVAL_ABOVE_API21 = 120;
    public static final int DAEMON_OTHER_PERSISTENT_PROCESS_TRIGGER = 10;
    public static final int DAEMON_OTHER_PERSISTENT_PROCESS_INTERVAL = 60;
    public static final String KEY_COMMAND_FROM = "marsdaemon.start.from";
    public static final String VALUE_COMMAND_FROM = "marsdaemon";
    public static final String INDICATOR_DIR_NAME = "indicators";
    public static final String INDICATOR_PERSISTENT_FILENAME = "indicator_p";
    public static final String INDICATOR_DAEMON_ASSISTANT_FILENAME = "indicator_d";
    public static final String OBSERVER_PERSISTENT_FILENAME = "observer_p";
    public static final String OBSERVER_DAEMON_ASSISTANT_FILENAME = "observer_d";
    private static String sPersistentLockFilePath;
    private static String sSeedDaemonFilePath;
    public static final String SP_FILENAME = "d_permit";
    public static final String SP_KEY_PERMITTING = "permitted";
    private static final String[] UNSUPPORT_FINGERPRINT = new String[]{"Verizon/heroqltevzw/heroqltevzw:6.0.1/MMB29M/G930VVRU2APB5:user/release-keys", "samsung/zerofltemtr/zerofltemtr:5.0.2/LRX22G/G920T1UVU1AOCH:user/release-keys", "lge/g4stylusc_spr_us/g4stylusc:6.0/MRA58K/153401424f19d:user/release-keys", "samsung/marinelteuc/marinelteatt:5.1.1/LMY47X/G890AUCU3BPB3:user/release-keys", "samsung/j1aceltedx/j1acelte:4.4.4/KTU84P/J110GDXU0AOK2:user/release-keys", "samsung/grandneove3gxx/grandneove3g:4.4.4/KTU84P/I9060IXXU0AOJ1:user/release-keys", "Huawei/H60-L01/hwH60:4.4.2/HDH60-L01/CHNC00B317:user/ota-rel-keys,release-keys"};
    private static final String[] UNHANDLE_CRASH = new String[0];

    public DaemonConstants() {
    }

    public static boolean isNotSupportDaemon() {
        String fingerPrint = Build.FINGERPRINT;
        String[] arr$ = UNSUPPORT_FINGERPRINT;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String item = arr$[i$];
            if (item.equals(fingerPrint)) {
                return true;
            }
        }

        return false;
    }

    public static final String getSeedDaemonFilePath(Context context) {
        if (sSeedDaemonFilePath == null) {
            sSeedDaemonFilePath = context.getDir("daemon", 0).getAbsolutePath() + File.separator + "seed_daemon";
        }

        return sSeedDaemonFilePath;
    }
}
