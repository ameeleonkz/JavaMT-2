package com.rxjava;

public interface Subscription {
    void unsubscribe();
    boolean isUnsubscribed();
}