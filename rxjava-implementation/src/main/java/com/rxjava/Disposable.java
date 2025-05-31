package com.rxjava;

public interface Disposable {
    void dispose();
    boolean isDisposed();
}