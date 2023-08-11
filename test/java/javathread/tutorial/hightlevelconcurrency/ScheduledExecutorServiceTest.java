package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
ExecutorService memiliki sub child interface bernama ScheduledExecutorService
Fitur tambahan di ScheduledExecutorService adalah, kita bisa melakukan asynchronous task yang terjadwal
Hal ini sangat cocok untuk kasus delayed job (pekerjaan yang butuh ditangguhkan pengerjaannya) dan periodic job
ScheduledExecutorService merupakan fitur yang bisa menggantikan low level penggunaan Timer

Hampir semua method di ScheduledExecutorService mengembalikan data ScheduledFuture
ScheduledFuture sebenarnya mirip dengan Future, yang membedakan, dia adalah turunan dari interface Delayed, yang memiliki method untuk mendapatkan informasi waktu delay

Untuk membuat ScheduledExecutorService kita bisa menggunakan implementasi class ScheduledThreadPoolExecutor
Atau jika ingin mudah, kita bisa gunakan class Executors, terdapat method newSingleThreadScheduledExecutor() dan newScheduledThreadPool(poolSize) untuk membuat ScheduledExecutorService
 */

public class ScheduledExecutorServiceTest {
    @Test
    void testDelayJob() throws InterruptedException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.schedule(() -> {
            System.out.println("Schedule Delay Thread: "+Thread.currentThread().getName());
        },2, TimeUnit.SECONDS);

        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    void testPeriodicJob() throws InterruptedException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        service.scheduleAtFixedRate(() -> {
            System.out.println("Hai from periodic job Thread: "+Thread.currentThread().getName());
        },2,3,TimeUnit.SECONDS);


        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    void testScheduledExecutorServiceWithRunnable() throws InterruptedException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);

        Runnable runnable = () -> {
            System.out.println("Hai from runnable Thread: "+Thread.currentThread().getName());
        };

        //KITA MENGGUNAKAN INI KALAU UDAH TERLANJUT KITA MENGGUNAKAN Runnable sebagai pengirim task nya
        service.scheduleWithFixedDelay(runnable, 2, 3, TimeUnit.SECONDS);

        service.awaitTermination(1, TimeUnit.HOURS);
    }
}
