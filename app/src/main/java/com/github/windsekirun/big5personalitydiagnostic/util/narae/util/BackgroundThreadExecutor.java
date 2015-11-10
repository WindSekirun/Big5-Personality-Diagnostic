package com.github.windsekirun.big5personalitydiagnostic.util.narae.util;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * NaraeAsynchronous
 * Class: BackgroundThreadExecutor
 * Created by WindSekirun on 15. 3. 10..
 */

@SuppressWarnings({"CanBeFinal", "WeakerAccess"})
public class BackgroundThreadExecutor implements BackgroundExecutor {
    private static Map<ExecutorId, Executor> sCachedExecutors = new HashMap<>();
    public String settingTaskType;
    public int settingPoolSize;

    @Override
    public BackgroundExecutor setTaskType(String taskType) {
        settingTaskType = taskType;
        return null;
    }

    @Override
    public BackgroundExecutor setThreadPoolSize(int poolSize) {
        settingPoolSize = poolSize;
        return null;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        getExecutor().execute(command);
    }

    Executor getExecutor() {
        final ExecutorId executorId = new ExecutorId(settingPoolSize, settingTaskType);
        synchronized (BackgroundThreadExecutor.class) {
            Executor executor = sCachedExecutors.get(executorId);
            if (executor == null) {
                executor = Executors.newFixedThreadPool(settingPoolSize);
                sCachedExecutors.put(executorId, executor);
            }
            return executor;
        }
    }
}