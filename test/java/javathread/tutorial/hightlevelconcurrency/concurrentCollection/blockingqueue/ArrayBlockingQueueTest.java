package javathread.tutorial.hightlevelconcurrency.concurrentCollection.blockingqueue;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;

public class ArrayBlockingQueueTest {
    /*
ArrayBlockingQueue, implementasi BlockingQueue dengan ukuran fix
LinkedBlockingQueue, implementasi BlockingQueue dengan ukuran bisa berkembang
PriorityBlockingQueue, implementasi BlockingQueue dengan otomatis berurut berdasarkan prioritas
DelayQueue, implementasi BlockingQueue untuk tipe data Delayed, dimana data tidak bisa diambil sebelum waktu delay yang telah ditentukan
SynchronousQueue, implementasi BlockingQueue dimana thread yang menambah data harus menunggu sampai ada thread yang mengambil data, begitu juga kebalikannya
     */
    @Test
    void testBlockingQueue() throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(5);
        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 11; i++){
            service.execute(() -> {
                try {
                    //method ini mengisi data kalau datanya tidak ada maka akan di tunggu
                    blockingQueue.put("Hai from thread: "+Thread.currentThread().getName());
                    //3.MAKA AKAN KELUAR INI JADI TASK NYA TINGGAL 4 KARNA KITA SET 5 MAKA AKAN MASUK 1 TASK LAGI
                    //JADI SISA 4 LAGI
                    System.out.println("Success insert data");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        //1.JADI DIA MENGEKSEKUSI 5 TASK DULU 5 NYA LAGI NUNGGU DI put
        service.execute(() -> {
            while (true){
                try {
                    Thread.sleep(1500L);
                    //1.DISAAT DATANYA DI AMBIL
                    //method ini mengambil data kalau datanya tidak ada maka akan di tunggu
                    String data = blockingQueue.take();
                    //4.DAN INI KELUAR
                    System.out.println("Receive: "+data);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //5.MAKA KE DATA SELANJUTNYA
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
