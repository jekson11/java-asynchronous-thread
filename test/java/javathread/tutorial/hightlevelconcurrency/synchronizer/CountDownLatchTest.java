package javathread.tutorial.hightlevelconcurrency.synchronizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {
    @Test
    void testCountDownLatch() throws InterruptedException {
        /*
CountDownLatch merupakan synchronizer yang digunakan untuk menunggu beberapa proses selesai, cara kerjanya mirip dengan
Semaphore, yang membedakan adalah pada CountDownLatch, counter diawal sudah ditentukan
Setelah proses selesai, kita akan menurunkan counter
Jika counter sudah bernilai 0, maka yang melakukan wait bisa lanjut berjalan
CountDownLatch cocok jika kita misal ingin menunggu beberapa proses yang berjalan secara asynchronous sampai semua proses selesai
         */
        CountDownLatch countDown = new CountDownLatch(5);//kitaa stell 5 countDown nya jadi
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                try {
                    System.out.println("Start task");
                    Thread.sleep(2000L);
                    System.out.println("End task");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    //ini menurunkan countDown
                    countDown.countDown();
                }
            });
        }

        executor.execute(() -> {
            try {
                //di sini karna kita sudah set 5 countDown nya makan dia akan menunggu sampai countDown nya turun dari 5 sampai 0
                //kalau misal pekerjaan yang di kerjakan tidak sampai 5 makan ini akan tetap menunggu jadi tidak akan bisa kalau
                //belum turun lima kali countDown nya
                countDown.await();
                System.out.println("All task finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
