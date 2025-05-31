package com.rxjava.schedulers;

public interface Scheduler {
    void execute(Runnable task);
}