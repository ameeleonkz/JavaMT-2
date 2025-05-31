package com.rxjava.subjects;

import com.rxjava.Observer;
import com.rxjava.Observable;
import com.rxjava.Subscription;

import java.util.ArrayList;
import java.util.List;

public class PublishSubject<T> extends Subject<T> {
    private final List<Observer<? super T>> observers = new ArrayList<>();
    private boolean isCompleted = false;
    private Throwable error;

    public static <T> PublishSubject<T> create() {
        return new PublishSubject<>();
    }
    
    private PublishSubject() {
        // Приватный конструктор для принуждения использования фабричного метода
    }
}