package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueTest {
    //SynchronousQueue, implementasi BlockingQueue dimana thread yang menambah data harus menunggu sampai ada thread
    //yang mengambil data, begitu juga kebalikannya
    @Test
    void testSynchronousQueue() throws InterruptedException {
       var queue = new SynchronousQueue<String>();
       var service = Executors.newFixedThreadPool(2);

       //karena ini sistemnya tunggu ada yang mengambil data dari queuenya jadi threadnya juga harus ada lebih dari
        //data yang ada pada queue kalau misalnya pas maka dia akan terus menunggu karena sumua threadnya berada pada put
        for (int i = 0; i < 10; i++) {
            var index = i;
            service.execute(() -> {
                try {
                    //data ini akan di keluarkan ketika ada yang menriknya
                    queue.put("Data"+index);
                    System.out.println("Success insert data :"+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        service.execute(() -> {
            while (true){
                try {
                    //ini yang menarik datanya
                    String data = queue.take();
                    Thread.sleep(1000L);
                    System.out.println("Receive: "+data+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}