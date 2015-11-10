package com.github.windsekirun.big5personalitydiagnostic;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import me.drakeet.library.CrashWoodpecker;

/**
 * Big5 Personality Diagnostic
 * class: Big5Application
 * Created by WindSekirun on 2015. 11. 10..
 */
public class Big5Application extends Application {
    public static final boolean isDebug = false;

    RefWatcher watcher;

    public static RefWatcher getRefWatcher(Context context) {
        Big5Application application = (Big5Application) context.getApplicationContext();
        return application.watcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug) {
            watcher = LeakCanary.install(this);
            CrashWoodpecker.fly().to(this);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        newConfig.orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }
}
