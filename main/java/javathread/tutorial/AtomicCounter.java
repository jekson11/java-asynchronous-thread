package javathread.tutorial;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter {
//    private Long value = 0L;
    private final AtomicLong value = new AtomicLong(0L);
    public void increment(){
        value.incrementAndGet();
    }

    public AtomicLong getValue(){
        return value;
    }
}
