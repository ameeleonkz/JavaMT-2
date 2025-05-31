package com.rxjava.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IOScheduler implements Scheduler {
    private final Executor executor;

    public IOScheduler() {
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}