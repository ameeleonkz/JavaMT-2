package com.rxjava.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SingleThreadScheduler implements Scheduler {
    private final Executor executor;
    
    public SingleThreadScheduler() {
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}