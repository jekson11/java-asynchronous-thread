package javathread.tutorial.hightlevelconcurrency.synchronizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class CyclicBarrierTest {
    /*
CyclicBarrier merupakan fitur yang bisa kita gunakan untuk saling menunggu, sampai jumlah thread yang
menunggu terpenuhi
Diawal kita akan tentukan berapa jumlah thread yang men
     */
    @Test
    void testCyclicBarrier() throws InterruptedException {
        //jumlah thread yang kita tunggu itu 5 di sini
        //ini cara pembuatan cyclicBarrier
        CyclicBarrier barrier = new CyclicBarrier(5);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++){
            executor.execute(() -> {
                try {
                    System.out.println("waiting");
                    Thread.sleep(2000L);
                    //disini kita await sampai jumlah nya terpenuhi
                    //kalau sudah terpenuhi baru langsung di eksekusi
                    barrier.await();
                    System.out.println("Done waiting");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
