package com.github.windsekirun.big5personalitydiagnostic.util.narae.util;

import java.util.concurrent.Executor;

/**
 * NaraeAsynchronous
 * Class: BackgroundExecutor
 * Created by WindSekirun on 15. 3. 10..
 */
@SuppressWarnings({"unused", "SameReturnValue"})
public interface BackgroundExecutor extends Executor {

    BackgroundExecutor setTaskType(String taskType);

    BackgroundExecutor setThreadPoolSize(int poolSize);
}
