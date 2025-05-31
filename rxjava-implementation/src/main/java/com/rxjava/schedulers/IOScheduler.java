package com.rxjava.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IOScheduler extends Scheduler {
    private final Executor executor;

    public IOScheduler() {
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void schedule(Runnable task) {
        executor.execute(task);
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}