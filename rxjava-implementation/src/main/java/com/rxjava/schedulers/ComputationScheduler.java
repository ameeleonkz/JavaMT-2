package com.rxjava.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ComputationScheduler extends Scheduler {
    private final Executor executor;

    public ComputationScheduler() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }
}