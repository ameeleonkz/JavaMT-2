package com.rxjava;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ObservableTest {
    
    private List<String> receivedItems;
    private List<Throwable> receivedErrors;
    private boolean completed;
    
    @BeforeEach
    void setUp() {
        receivedItems = new ArrayList<>();
        receivedErrors = new ArrayList<>();
        completed = false;
    }
    
    @Test
    void testObservableCreate() {
        Observable<String> observable = Observable.create(observer -> {
            observer.onNext("Привет");
            observer.onNext("Мир");
            observer.onComplete();
        });
        
        observable.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                receivedErrors.add(t);
            }
            
            @Override
            public void onComplete() {
                completed = true;
            }
        });
        
        assertEquals(2, receivedItems.size());
        assertEquals("Привет", receivedItems.get(0));
        assertEquals("Мир", receivedItems.get(1));
        assertTrue(completed);
        assertTrue(receivedErrors.isEmpty());
    }
    
    @Test
    void testMapOperator() {
        Observable<Integer> observable = Observable.create(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onComplete();
        });
        
        observable
            .map(x -> x * 2)
            .subscribe(new Observer<Integer>() {
                @Override
                public void onNext(Integer item) {
                    receivedItems.add(item.toString());
                }
                
                @Override
                public void onError(Throwable t) {
                    receivedErrors.add(t);
                }
                
                @Override
                public void onComplete() {
                    completed = true;
                }
            });
        
        assertEquals(3, receivedItems.size());
        assertEquals("2", receivedItems.get(0));
        assertEquals("4", receivedItems.get(1));
        assertEquals("6", receivedItems.get(2));
        assertTrue(completed);
    }
    
    @Test
    void testFilterOperator() {
        Observable<Integer> observable = Observable.create(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onNext(4);
            observer.onComplete();
        });
        
        observable
            .filter(x -> x % 2 == 0)
            .subscribe(new Observer<Integer>() {
                @Override
                public void onNext(Integer item) {
                    receivedItems.add(item.toString());
                }
                
                @Override
                public void onError(Throwable t) {
                    receivedErrors.add(t);
                }
                
                @Override
                public void onComplete() {
                    completed = true;
                }
            });
        
        assertEquals(2, receivedItems.size());
        assertEquals("2", receivedItems.get(0));
        assertEquals("4", receivedItems.get(1));
        assertTrue(completed);
    }
    
    @Test
    void testErrorHandling() {
        Observable<String> observable = Observable.create(observer -> {
            observer.onNext("Данные");
            observer.onError(new RuntimeException("Тестовая ошибка"));
        });
        
        observable.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                receivedErrors.add(t);
            }
            
            @Override
            public void onComplete() {
                completed = true;
            }
        });
        
        assertEquals(1, receivedItems.size());
        assertEquals("Данные", receivedItems.get(0));
        assertEquals(1, receivedErrors.size());
        assertEquals("Тестовая ошибка", receivedErrors.get(0).getMessage());
        assertFalse(completed);
    }
}