package com.github.windsekirun.big5personalitydiagnostic.util.narae;

import palette.twitter.util.narae.util.MainThreadExecutor;

/**
 * NaraeAsynchronous
 * Class: NaraeMain
 * Created by WindSekirun on 15. 6. 22..
 */
@SuppressWarnings("ALL")
public class NaraeMain {
    Runnable runnable;

    public NaraeMain(Runnable runnable) {
        this.runnable = runnable;
    }

    public void execute() {
        MainThreadExecutor mte = new MainThreadExecutor();
        mte.execute(runnable);
    }
}
