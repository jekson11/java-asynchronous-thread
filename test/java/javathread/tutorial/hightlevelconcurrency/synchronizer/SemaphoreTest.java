package javathread.tutorial.hightlevelconcurrency.synchronizer;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;

public class SemaphoreTest {
    @Test
    void testSemaphore() throws InterruptedException {
        /*
Semaphore merupakan class yang digunakan untuk manage data counter
Nilai counter bisa naik, namun ada batas maksimal nya, jika batas maksimal nya sudah tercapai, semua thread yang akan
mencoba menaikkan harus menunggu, sampai ada thread lain yang menurunkan nilai counter
Semaphore cocok sekali misal untuk menjaga agar thread berjalan pada maksimal total counter yang sudah kita tentukan
         */
        Semaphore semaphore = new Semaphore(10);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                try {
                    //jika hanya begini dia akan mengikuti semaphore object permits nya
                    // segitulah jumlah pekerjaan yang beleh di eksekusi jika lebih maka harus menuggu dulu
//                    semaphore.acquire();
                    //tapi kalau begini kita yang atur berapa maksimal pekerjaan yang beleh di eksekusi
                    semaphore.acquire(5);//
                    Thread.sleep(2000L);
                    System.out.println("Finish");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally  {
                    //ini untuk melepaskan pekerjaan yang sudah selesai di eksekusi
                    //agar pekerjaan lain bisa masuk
                    semaphore.release();
//                    semaphore.release(5);
                }
            });
        }

        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
