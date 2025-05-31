package com.rxjava;

import com.rxjava.schedulers.IOScheduler;
import com.rxjava.schedulers.ComputationScheduler;
import com.rxjava.schedulers.SingleThreadScheduler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SchedulersTest {
    
    @Test
    void testIOScheduler() throws InterruptedException {
        IOScheduler scheduler = new IOScheduler();
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        
        scheduler.execute(() -> {
            executed.set(true);
            latch.countDown();
        });
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertTrue(executed.get());
    }
    
    @Test
    void testComputationScheduler() throws InterruptedException {
        ComputationScheduler scheduler = new ComputationScheduler();
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        
        scheduler.execute(() -> {
            executed.set(true);
            latch.countDown();
        });
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertTrue(executed.get());
    }
    
    @Test
    void testSingleThreadScheduler() throws InterruptedException {
        SingleThreadScheduler scheduler = new SingleThreadScheduler();
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        
        scheduler.execute(() -> {
            executed.set(true);
            latch.countDown();
        });
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertTrue(executed.get());
    }
}