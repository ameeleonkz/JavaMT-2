package com.rxjava;

import com.rxjava.schedulers.IOScheduler;
import com.rxjava.schedulers.ComputationScheduler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SchedulerIntegrationTest {
    
    @Test
    void testSubscribeOn() throws InterruptedException {
        IOScheduler ioScheduler = new IOScheduler();
        AtomicReference<String> threadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        
        Observable<String> observable = Observable.create(observer -> {
            threadName.set(Thread.currentThread().getName());
            observer.onNext("test");
            observer.onComplete();
        });
        
        observable
            .subscribeOn(ioScheduler)
            .subscribe(new Observer<String>() {
                @Override
                public void onNext(String item) {
                    assertEquals("test", item);
                }
                
                @Override
                public void onError(Throwable t) {
                    fail("Unexpected error");
                }
                
                @Override
                public void onComplete() {
                    latch.countDown();
                }
            });
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotEquals("main", threadName.get());
    }
    
    @Test
    void testObserveOn() throws InterruptedException {
        ComputationScheduler computationScheduler = new ComputationScheduler();
        AtomicReference<String> observerThreadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        
        Observable<String> observable = Observable.create(observer -> {
            observer.onNext("test");
            observer.onComplete();
        });
        
        observable
            .observeOn(computationScheduler)
            .subscribe(new Observer<String>() {
                @Override
                public void onNext(String item) {
                    observerThreadName.set(Thread.currentThread().getName());
                    assertEquals("test", item);
                }
                
                @Override
                public void onError(Throwable t) {
                    fail("Unexpected error");
                }
                
                @Override
                public void onComplete() {
                    latch.countDown();
                }
            });
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotEquals("main", observerThreadName.get());
    }
}