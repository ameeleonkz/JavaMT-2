package com.rxjava.subjects;

import com.rxjava.Observer;
import com.rxjava.Subscription;

public class BehaviorSubject<T> extends Subject<T> {
    private T lastValue;
    private boolean hasValue = false;
    
    public static <T> BehaviorSubject<T> create() {
        return new BehaviorSubject<>();
    }
    
    public static <T> BehaviorSubject<T> createDefault(T defaultValue) {
        BehaviorSubject<T> subject = new BehaviorSubject<>();
        subject.lastValue = defaultValue;
        subject.hasValue = true;
        return subject;
    }
    
    private BehaviorSubject() {
        // Приватный конструктор для принуждения использования фабричного метода
    }
    
    @Override
    public Subscription subscribe(Observer<T> observer) {
        if (error != null) {
            observer.onError(error);
            return createEmptySubscription();
        }
        
        if (completed) {
            if (hasValue) {
                observer.onNext(lastValue);
            }
            observer.onComplete();
            return createEmptySubscription();
        }
        
        observers.add(observer);
        
        // Отправляем последнее значение новому подписчику
        if (hasValue) {
            observer.onNext(lastValue);
        }
        
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
        lastValue = item;
        hasValue = true;
        super.onNext(item);
    }
    
    /**
     * Получить текущее значение (если есть)
     */
    public T getValue() {
        return hasValue ? lastValue : null;
    }
    
    /**
     * Проверить, есть ли текущее значение
     */
    public boolean hasValue() {
        return hasValue;
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