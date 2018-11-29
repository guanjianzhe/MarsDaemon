package com.marswin89.marsdaemon.demo;

import android.app.Application;
import android.content.Context;

import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;

/**
 * Implementation 2<br/>
 * if you have to extends other Application, use this method.<br/>
 * <p>
 * Created by Mars on 12/24/15.
 */
public class MyApplication2 extends Application {

    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = DaemonClient.getInstance();
        mDaemonClient.setDebugMode();
        mDaemonClient.init(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }


    private DaemonConfigurations createDaemonConfigurations() {
        String PACKAGE_NAME = "com.marswin89.marsdaemon.demo";

        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                PACKAGE_NAME,
                DaemonService.class.getCanonicalName(),
                DaemonReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                PACKAGE_NAME + ":process2",
                AssistService.class.getCanonicalName(),
                AssistReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }
}
