package com.github.windsekirun.big5personalitydiagnostic.util.narae.util;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * NaraeAsynchronous
 * Class: MainThreadExecutor
 * Created by WindSekirun on 15. 3. 10..
 */
public class MainThreadExecutor implements Executor {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable runnable) {
        mHandler.post(runnable);
    }
}
