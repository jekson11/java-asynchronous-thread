package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.concurrent.*;

public class PriorityBlockingQueueTest {
    //PriorityBlockingQueue, implementasi BlockingQueue dengan otomatis berurut berdasarkan prioritas
    @Test
    void testPriorityQueue() throws InterruptedException {
        //KALAU KITA TIDAK MEMASUKKAN COMPARATORNYA MAKA DEFAULTNYA DATANYA AKAN DARI KECIL KE YANG BESAR
        //SEDANGKAN KALAU KITA MASUKKAN COMPARATOR REVERSEORDER MAKA DATANYA AKAN DARI BESAR KE KECIL
        //KITA BISA MEMASUKKAN INISIALISASU QUEUE NYA
        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(10, Comparator.reverseOrder());
        ExecutorService service = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 10; i++) {
            final var index = i;
            service.execute(() -> {
                try {
                    Thread.sleep(2000L);
                    queue.put(index);
                    System.out.println("Success insert data: ");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        service.execute(() -> {
            while (true){
                try {
                    Thread.sleep(1000L);
                    Integer data = queue.take();
                    System.out.println("Receive: "+data);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
