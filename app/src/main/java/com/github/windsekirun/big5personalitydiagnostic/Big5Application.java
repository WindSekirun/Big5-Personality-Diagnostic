package com.github.windsekirun.big5personalitydiagnostic;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import me.drakeet.library.CrashWoodpecker;

/**
 * Big5 Personality Diagnostic
 * class: Big5Application
 * Created by WindSekirun on 2015. 11. 10..
 */
public class Big5Application extends Application implements Consts {
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
        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new TypiconsModule())
                .with(new MaterialModule())
                .with(new MeteoconsModule())
                .with(new WeathericonsModule())
                .with(new SimpleLineIconsModule())
                .with(new IoniconsModule());
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        newConfig.orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }
}
