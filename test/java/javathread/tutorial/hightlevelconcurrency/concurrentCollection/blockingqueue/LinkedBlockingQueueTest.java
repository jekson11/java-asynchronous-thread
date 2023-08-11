package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class LinkedBlockingQueueTest {
    //LinkedBlockingQueue, implementasi BlockingQueue dengan ukuran bisa berkembang
    //SAMA KAYA ArrayBlockingQueue cuma bedaya ini datanya takterbatas sedangkan array kan kita harus set datanya
    //Tapi haru hati hati saat menggunakan ini karna jika datanya kebanyakan bisah kelebihan memory nanti
    @Test
    void testLinkedBlockingQueue() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 11; i++) {
            service.execute(() -> {
                try {
                    queue.put("Hello from thread: " + Thread.currentThread().getName());
                    Thread.sleep(1000L);
                    System.out.println("Success insert data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        service.execute(() -> {
            while (true){
                try {
                    Thread.sleep(2000L);
                    String data = queue.take();
                    System.out.println("Receive: "+data);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
