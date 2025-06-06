package com.rxjava.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ComputationScheduler implements Scheduler {
    private final Executor executor;

    public ComputationScheduler() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}