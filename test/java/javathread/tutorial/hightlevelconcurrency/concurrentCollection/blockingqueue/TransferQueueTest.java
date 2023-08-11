package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class TransferQueueTest {
    /*
TransferQueue Merupakan turunan dari BlockingQueue yang membolehkan pengirim data ke queue menunggu sampai data ada yang menerima
Implementasi TransferQueue hanyalah LinkedTransferQueue
     */
    @Test
    void testLinkedTransferQueue() throws InterruptedException {
        var queue = new LinkedTransferQueue<String>();
        var service = Executors.newFixedThreadPool(20);

        //jumlah thread nya harus lebih dari data yang ingin di kirim
        for (int i = 0; i < 10; i++) {
            var index = i;
            service.execute(() -> {
                try {
                    //mengirim data
                    queue.transfer("Data: "+index);
                    System.out.println("Success transfer data: "+index);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        service.execute(() -> {
            while (true){
                try {
                    Thread.sleep(1000L);
                    //menerima data
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
