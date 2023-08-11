package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;
import java.util.Random;
import java.util.concurrent.*;

public class CompletionServiceTest {
    /*
CompletionService merupakan sebuah interface yang digunakan untuk memisahkan antara bagian yang mengeksekusi asynchronous task dan yang menerima hasil dari task yang sudah selesai
Kadang ada kebutuhan misal kita butuh menjalankan sesuatu secara parallel, lalu ada satu thread yang melakukan eksekusi task dan satu thread menunggu hasil nya
Kita bisa menggunakan CompletionService untuk melakukan itu
Implementasi interface CompletionService adalah class ExecutorCompletionService
     */
    Random random = new Random();
    @Test
    void testCreateCompletionService() throws InterruptedException {
        var executor = Executors.newFixedThreadPool(10);
        //CREATE ExecutorCompletionService
        var service = new ExecutorCompletionService<String>(executor);

        //submit task
        //ini yang mengirim tugas ke service nya yang di atas
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int i = 0; i < 100; i++) {
                final var task = i;
                service.submit(() -> {
                    Thread.sleep(random.nextInt(500));
                    return "Task-"+task;
                });
            }
        });

        //poll task
        //jika tugas yang di kirim keservice sudah ada
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true){
                try {
                    //ini yang akan mengerjakan tugasnya, kalau tugasnya kosong maka akan dibreak while nya
                    Future<String> future = service.poll(5, TimeUnit.SECONDS);
                    if (future == null){
                        break;
                    }else {
                        System.out.println(future.get());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    break;
                }
            }
        });

        executor.awaitTermination(1, TimeUnit.SECONDS);
    }
}
