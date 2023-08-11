package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class BlockingDequeueTest {
    /*
BlockingDeque merupakan turunan dari BlockingQueue
BlockingDeque tidak hanya mendukung FIFO (first in first out), tapi juga LIFO (last in last out)
Implementasi BlockingDeque hanyalan LinkedBlockingDeque penggunaan nya sama dengan LinkedBlockingQueue cuma beda
methodnya karena ini mendukung LIFO dan FIFO kalau
     */
    @Test
    void testLinkedBlockingDeQueue() throws InterruptedException {
        var queue = new LinkedBlockingDeque<String>();
        var service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            final var index = i;
            try {
                //memasukkan data ke paling akhir jadi setiap ada data dia akan di masukkan ke paling akhir
                queue.putLast("Data: "+index);
                //memasukkan data ke paling depan jadi setiap ada data dia akan di masukkan ke paling depan
//                queue.putFirst("Data: "+index);
                System.out.println("Success input data: "+index);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        service.execute(() -> {
            while (true){
                try {
                    Thread.sleep(1000L);
                    //mengambil data yang paling terdepan
                    String data = queue.takeFirst();
                    //mengambil data yang paling terbelakang
//                    String data2 = queue.takeLast();
                    System.out.println("Receive: "+data);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
