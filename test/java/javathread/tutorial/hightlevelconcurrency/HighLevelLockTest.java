package javathread.tutorial.hightlevelconcurrency;

import javathread.tutorial.CounterLock;
import javathread.tutorial.CounterReadWriteLock;
import org.junit.jupiter.api.Test;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Java menyediakan high level concurrency package untuk melakukan locking atau waiting for conditions
Ini adalah alternatif dari low level synchronized dan manual wait dan notify
Untuk saat ini, sangat disarankan menggunakan package locks dibandingkan menggunakan low level synchronization sebelumnya yang sudah kita bahas
 */
public class HighLevelLockTest {

    @Test
    void testLockInterface() throws InterruptedException {
        /*
        Lock interface merupakan alternatif implementasi dari synchronized method dan synchronized statement
        Untuk melakukan lock, kita bisa gunakan method lock() dan setelah selesai, kita bisa menggunakan method unlock() untuk melepaskan lock
        Implementasi dari interface Lock adalah class ReentrantLock
         */
        CounterLock counterLock = new CounterLock();

        Runnable runnable = () -> {
            for (int i = 0; i < 1_000_000; i++){
                counterLock.increment();
            }
        };

        var thread1 = new Thread(runnable);
        var thread2 = new Thread(runnable);
        var thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println(counterLock.getValue());
    }

    @Test
    void testReadWriteLock() throws InterruptedException {
        /*
        Kadang ada kondisi dimana kita ingin membedakan lock antara operasi update dan operasi get
        Untuk kasus seperti ini, kita bisa membuat dua buah variable Lock
        Namun, di Java disediakan cara yang lebih mudah, yaitu menggunakan interface ReadWriteLock
        ReadWriteLock merupakan lock yang mendukung dua jenis operasi, read dan write
        Implementasi dari interface ReadWriteLock adalah class ReentrantReadWriteLock
         */
        CounterReadWriteLock readWriteLock = new CounterReadWriteLock();

        Runnable runnable = () -> {
            for (int i = 0; i < 1_000_000; i++){
                readWriteLock.increment();
            }
        };

        var thread1 = new Thread(runnable);
        var thread2 = new Thread(runnable);
        var thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println(readWriteLock.getValue());
    }

    String message = null;
    @Test
    void testConditionSignal() throws InterruptedException {
        /*
        Condition merupakan alternatif lain dari monitor method (wait, notify dan notifyAll)
        Pada Java modern saat ini, sangat disarankan menggunakan Condition dibanding monitor method
        Condition memiliki method wait() untuk menunggu, signal() untuk mentrigger satu thread,
        dan signalAll() untuk mentrigger semua thread yang menunggu
        Cara pembuatan Condition, kita bisa menggunakan method newCondition() milik Lock
         */
        Lock lock = new ReentrantLock();
        var conditionLock = lock.newCondition();

        Thread thread1 = new Thread(() -> {
            try {
                lock.lock();
                conditionLock.await();
                System.out.println(message);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(2000L);
                message = "Hai from Thread: "+Thread.currentThread().getName();
                conditionLock.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    @Test
    void testConditionSignalAll() throws InterruptedException {
        /*
Condition merupakan alternatif lain dari monitor method (wait, notify dan notifyAll)
Pada Java modern saat ini, sangat disarankan menggunakan Condition dibanding monitor method
Condition memiliki method wait() untuk menunggu, signal() untuk mentrigger satu thread,
dan signalAll() untuk mentrigger semua thread yang menunggu
Cara pembuatan Condition, kita bisa menggunakan method newCondition() milik Lock
         */
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        var thread1 = new Thread(() -> {
           try  {
               lock.lock();
               while (message == null){
                   condition.await();
               }
               System.out.println("Thread1 "+message);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }finally {
               lock.unlock();
           }
        });

        var thread2 = new Thread(() -> {
           try {
               lock.lock();
               while (message == null){
                   condition.await();
               }
               System.out.println("Thread2 "+message);
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }finally {
               lock.unlock();
           }
        });

        var thread3 = new Thread(() -> {
            try {
                lock.lock();
                while (message == null){
                    condition.await();
                }
                System.out.println("Thread4 "+message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        });

        var thread4 = new Thread(() -> {
            try {
                lock.lock();
                while (message == null){
                    condition.await();
                }
                System.out.println("Thread5 "+message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        });

        var thread5 = new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(2000L);
                message = "Hai from Thread: "+Thread.currentThread().getName();
                condition.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();

    }
}
