package com.rxjava.schedulers;

import java.util.concurrent.Executor;

public abstract class Scheduler {
    public abstract Executor getExecutor();
    
    public void schedule(Runnable action) {
        getExecutor().execute(action);
    }
}