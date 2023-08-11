package javathread.tutorial;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterLock {
    private Long value = 0L;

    //Penggunaan Lock
    private Lock lock = new ReentrantLock();

    public void increment(){
        //LOCK DISARANKAN MENGGUNAKAN FINALLY AGAR ERROR ATAU TIDAKNYA PROGRAM LOCKNYA AKAN DI BUKA
        try {
            //MENUTUP AGAR TIDAK ADA THREAD LAIN YANG MASUK
            lock.lock();
            value++;
        }finally {
            //MEMBUKA AGAR THREAD LAIN BISA MASUK SETELAH PROGRAM SELESAI DI KERJAKAN
            lock.unlock();
        }
    }

    public Long getValue(){
        return value;
    }
}
