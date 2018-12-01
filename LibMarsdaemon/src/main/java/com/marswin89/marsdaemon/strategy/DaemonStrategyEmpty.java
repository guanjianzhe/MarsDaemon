package com.marswin89.marsdaemon.strategy;

import android.content.Context;

import com.marswin89.marsdaemon.DaemonConfigurations;
import com.marswin89.marsdaemon.IDaemonStrategy;

public class DaemonStrategyEmpty implements IDaemonStrategy {
    @Override
    public boolean onInitialization(Context context) {
        return false;
    }

    @Override
    public void onPersistentCreate(Context context, DaemonConfigurations configs) {

    }

    @Override
    public void onDaemonAssistantCreate(Context context, DaemonConfigurations configs) {

    }

    @Override
    public void onDaemonDead() {

    }
}
