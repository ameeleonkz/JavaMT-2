package com.rxjava;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class OperatorsTest {
    
    private List<String> receivedItems;
    private boolean completed;
    
    @BeforeEach
    void setUp() {
        receivedItems = new ArrayList<>();
        completed = false;
    }
    
    @Test
    void testChainedOperators() {
        Observable<Integer> observable = Observable.create(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onNext(4);
            observer.onNext(5);
            observer.onComplete();
        });
        
        observable
            .filter(x -> x % 2 == 0)  // Только четные: 2, 4
            .map(x -> "Число: " + x)   // Преобразуем в строку
            .subscribe(new Observer<String>() {
                @Override
                public void onNext(String item) {
                    receivedItems.add(item);
                }
                
                @Override
                public void onError(Throwable t) {
                    fail("Не ожидалась ошибка: " + t.getMessage());
                }
                
                @Override
                public void onComplete() {
                    completed = true;
                }
            });
        
        assertEquals(2, receivedItems.size());
        assertEquals("Число: 2", receivedItems.get(0));
        assertEquals("Число: 4", receivedItems.get(1));
        assertTrue(completed);
    }
    
    @Test
    void testFlatMapOperator() {
        Observable<Integer> observable = Observable.create(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onComplete();
        });
        
        Observable<String> flatMapped = observable
            .flatMap(x -> Observable.<String>create(innerObserver -> {
                innerObserver.onNext("A" + x);
                innerObserver.onNext("B" + x);
                innerObserver.onComplete();
            }));
        
        flatMapped.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                fail("Не ожидалась ошибка: " + t.getMessage());
            }
            
            @Override
            public void onComplete() {
                completed = true;
            }
        });
        
        assertEquals(4, receivedItems.size());
        assertTrue(receivedItems.contains("A1"));
        assertTrue(receivedItems.contains("B1"));
        assertTrue(receivedItems.contains("A2"));
        assertTrue(receivedItems.contains("B2"));
        assertTrue(completed);
    }
    
    @Test
    void testMapWithDifferentTypes() {
        Observable<Integer> numbers = Observable.create(observer -> {
            observer.onNext(1);
            observer.onNext(2);
            observer.onNext(3);
            observer.onComplete();
        });
        
        numbers
            .map(x -> x.toString())  // Integer -> String
            .map(s -> "Значение: " + s)  // String -> String
            .subscribe(new Observer<String>() {
                @Override
                public void onNext(String item) {
                    receivedItems.add(item);
                }
                
                @Override
                public void onError(Throwable t) {
                    fail("Не ожидалась ошибка: " + t.getMessage());
                }
                
                @Override
                public void onComplete() {
                    completed = true;
                }
            });
        
        assertEquals(3, receivedItems.size());
        assertEquals("Значение: 1", receivedItems.get(0));
        assertEquals("Значение: 2", receivedItems.get(1));
        assertEquals("Значение: 3", receivedItems.get(2));
        assertTrue(completed);
    }
}