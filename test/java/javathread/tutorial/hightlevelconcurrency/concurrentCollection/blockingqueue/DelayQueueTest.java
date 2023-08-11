package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class DelayQueueTest {
    //DelayQueue, implementasi BlockingQueue untuk tipe data Delayed, dimana data tidak bisa diambil sebelum waktu delay yang telah ditentukan
    @Test
    void testDelayQueue() throws InterruptedException {
        //UNtUK DELAYQUEUE KITA HARUS MENGGUNAKAN TIPE DATANYA ScheduledFuture karena di Delay itu
        //mehodnya rata2 return ScheduleFuture
        var queue = new DelayQueue<ScheduledFuture<String>>();
        //SERVICE NYA JUGA KITA MENGGUNAKAN ScheduledExecutorService turunan dari ExecutorService
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        for (int i = 0; i < 10; i++) {
            //cara penggunaan method nya agak beda dari yang lain karena kita harus
            //memasukkan ScheduledExecutorService di dalamnya
            //ini maksudnya kita buat nilainya "Delay" dan akan di keluarkan secara nilai i dalam perulangan
            //dan hitungan nya itu detik jadi kalau nilai i = 1 berarti 1 detik kalau i = 2 berarti 2 detik
            queue.put(service.schedule(() -> "Delay", i, TimeUnit.SECONDS));
        }

        //KALAU CARA MENGEKSEKUSINYA SAMA SAJA SEPERTI ExecutorService yang biasanya
        service.execute(() -> {
            while (true){
                try {
                    ScheduledFuture<String> data = queue.take();
                    System.out.println("Receive: "+data.get());
                } catch (InterruptedException |ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
