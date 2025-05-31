package com.rxjava.subjects;

import com.rxjava.Observable;
import com.rxjava.Observer;
import com.rxjava.Subscription;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Subject<T> extends Observable<T> implements Observer<T> {
    protected final CopyOnWriteArrayList<Observer<T>> observers = new CopyOnWriteArrayList<>();
    protected boolean completed = false;
    protected Throwable error = null;
    
    @Override
    public Subscription subscribe(Observer<T> observer) {
        if (error != null) {
            observer.onError(error);
            return createEmptySubscription();
        }
        
        if (completed) {
            observer.onComplete();
            return createEmptySubscription();
        }
        
        observers.add(observer);
        
        return new Subscription() {
            private boolean unsubscribed = false;
            
            @Override
            public void unsubscribe() {
                if (!unsubscribed) {
                    unsubscribed = true;
                    observers.remove(observer);
                }
            }
            
            @Override
            public boolean isUnsubscribed() {
                return unsubscribed;
            }
        };
    }
    
    @Override
    public void onNext(T item) {
        if (!completed && error == null) {
            for (Observer<T> observer : observers) {
                observer.onNext(item);
            }
        }
    }
    
    @Override
    public void onError(Throwable t) {
        if (!completed && error == null) {
            error = t;
            for (Observer<T> observer : observers) {
                observer.onError(t);
            }
            observers.clear();
        }
    }
    
    @Override
    public void onComplete() {
        if (!completed && error == null) {
            completed = true;
            for (Observer<T> observer : observers) {
                observer.onComplete();
            }
            observers.clear();
        }
    }
    
    private Subscription createEmptySubscription() {
        return new Subscription() {
            @Override
            public void unsubscribe() {}
            
            @Override
            public boolean isUnsubscribed() {
                return true;
            }
        };
    }
}