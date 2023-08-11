package javathread.tutorial.hightlevelconcurrency;

import javathread.tutorial.AtomicCounter;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

public class CounterAtomicTest {
    /*
Java menawarkan sebuah package atomic yang berisikan class-class yang mendukung lock-free dan thread-safe programming pada single variable
Setiap object Atomic class akan mengelola data yang diakses dan di update menggunakan method yang telah disediakan
Atomic class melakukan implementasi Compare-and-Swap untuk mendukung synchronization
Dengan menggunakan Atomic, kita tidak perlu lagi menggunakan synchronized secara manual
     */
    @Test
    void testAtomic() throws InterruptedException {
        //MENGGUNAKAN ATOMIC PACKAGE
        AtomicCounter atomicCounter = new AtomicCounter();
        Runnable runnable = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                atomicCounter.increment();
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

        System.out.println(atomicCounter.getValue());
    }

    @Test
    void testAtomic2() throws InterruptedException, ExecutionException {
        //ini jika menggunakan callable tapi ini tidak baik y karena kita tidak butuh
        //return dara threadnya jadi lebih baik yang atas
        AtomicCounter atomic = new AtomicCounter();
        var executor = Executors.newFixedThreadPool(100);

        List<Callable<Long>> callables = LongStream.range(0, 1_000_000).mapToObj(value -> (Callable<Long>) () -> {
            atomic.increment();
            return value;
        }).toList();

        executor.invokeAll(callables);
        executor.invokeAll(callables);
        executor.invokeAll(callables);
        System.out.println(atomic.getValue());

    }
}
