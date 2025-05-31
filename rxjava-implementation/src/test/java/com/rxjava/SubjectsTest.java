package com.rxjava;

import com.rxjava.subjects.PublishSubject;
import com.rxjava.subjects.BehaviorSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class SubjectsTest {
    
    private List<String> receivedItems1;
    private List<String> receivedItems2;
    private boolean completed1;
    private boolean completed2;
    
    @BeforeEach
    void setUp() {
        receivedItems1 = new ArrayList<>();
        receivedItems2 = new ArrayList<>();
        completed1 = false;
        completed2 = false;
    }
    
    @Test
    void testPublishSubject() {
        PublishSubject<String> subject = PublishSubject.create();
        
        // Первый подписчик
        subject.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems1.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                fail("Не ожидалась ошибка");
            }
            
            @Override
            public void onComplete() {
                completed1 = true;
            }
        });
        
        // Отправляем данные
        subject.onNext("Привет");
        
        // Второй подписчик (не получит предыдущие данные)
        subject.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems2.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                fail("Не ожидалась ошибка");
            }
            
            @Override
            public void onComplete() {
                completed2 = true;
            }
        });
        
        subject.onNext("Мир");
        subject.onComplete();
        
        assertEquals(2, receivedItems1.size());
        assertEquals("Привет", receivedItems1.get(0));
        assertEquals("Мир", receivedItems1.get(1));
        
        assertEquals(1, receivedItems2.size());
        assertEquals("Мир", receivedItems2.get(0));
        
        assertTrue(completed1);
        assertTrue(completed2);
    }
    
    @Test
    void testBehaviorSubject() {
        BehaviorSubject<String> subject = BehaviorSubject.create();
        
        // Первый подписчик
        subject.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems1.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                fail("Не ожидалась ошибка");
            }
            
            @Override
            public void onComplete() {
                completed1 = true;
            }
        });
        
        // Отправляем данные
        subject.onNext("Привет");
        
        // Второй подписчик (получит последнее значение)
        subject.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems2.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                fail("Не ожидалась ошибка");
            }
            
            @Override
            public void onComplete() {
                completed2 = true;
            }
        });
        
        subject.onNext("Мир");
        subject.onComplete();
        
        assertEquals(2, receivedItems1.size());
        assertEquals("Привет", receivedItems1.get(0));
        assertEquals("Мир", receivedItems1.get(1));
        
        assertEquals(2, receivedItems2.size());
        assertEquals("Привет", receivedItems2.get(0)); // Получил последнее значение при подписке
        assertEquals("Мир", receivedItems2.get(1));
        
        assertTrue(completed1);
        assertTrue(completed2);
    }
    
    @Test
    void testBehaviorSubjectWithDefault() {
        BehaviorSubject<String> subject = BehaviorSubject.createDefault("По умолчанию");
        
        // Подписчик получит значение по умолчанию
        subject.subscribe(new Observer<String>() {
            @Override
            public void onNext(String item) {
                receivedItems1.add(item);
            }
            
            @Override
            public void onError(Throwable t) {
                fail("Не ожидалась ошибка");
            }
            
            @Override
            public void onComplete() {
                completed1 = true;
            }
        });
        
        assertTrue(subject.hasValue());
        assertEquals("По умолчанию", subject.getValue());
        
        assertEquals(1, receivedItems1.size());
        assertEquals("По умолчанию", receivedItems1.get(0));
        
        subject.onNext("Новое значение");
        subject.onComplete();
        
        assertEquals(2, receivedItems1.size());
        assertEquals("Новое значение", receivedItems1.get(1));
        assertTrue(completed1);
    }
}