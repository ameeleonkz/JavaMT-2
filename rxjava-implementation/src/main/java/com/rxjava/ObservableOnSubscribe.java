package com.rxjava;

public interface ObservableOnSubscribe<T> {
    void subscribe(Observer<T> observer) throws Exception;
}