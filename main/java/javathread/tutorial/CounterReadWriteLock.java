package javathread.tutorial;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CounterReadWriteLock {
    private Long value = 0L;

    //CARA PEMBUATAN READWRITELOCK
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    public void increment(){
        try {
            //INI NGE LOCK JIKAA ADA YANG INGIN NGE WRITE
            lock.writeLock().lock();
            value++;
        }finally {
            //DI BUKA
            lock.writeLock().unlock();
        }
    }

    public Long getValue(){
        try {
            //INI NGE LOCK JIKA ADA YANG INGIN NGE READ
            lock.readLock().lock();
            return value;
        }finally {
            //DI BUKA
            lock.readLock().unlock();
        }
    }
}
