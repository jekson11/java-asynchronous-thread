package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {
    /*
    Jika kita perhatikan, ThreadPoolExecutor merupakan implementasi dari interface Executor dan ExecutorService
    Jadi saat kita membuat ThreadPoolExecutor sebenarnya kita membuat Executor Service
    Dan sebenarnya pembuatan Threadpool secara manual jarang dilakukan, kecuali pada kasus kita benar-benar butuh melakukan tuning Threadpool
    Namun dalam kebanyakan kasus, kita jarang sekali membuat Threadpool secara manual
    Rata-rata, biasanya untuk mengeksekusi Runnable, biasanya kita akan menggunakan Executor Service

    Karena ExecutorService adalah interface, jadi pembuatan object ExecutorService salah satu nya adalah menggunakan ThreadPoolExecutor
    Namun ada yang lebih mudah, yaitu menggunakan class Executors
    Executor merupakan class utility untuk membantu kita membuat object ExecutorService secara mudah
    Sebenarnya implementasi Executors pun menggunakan ThreadPoolExecutor, hanya saja kita tidak perlu terlalu pusing melakukan pengaturan threadpool secara manual

    Hati-hati ketika membuat ExecutorService menggunakan Executors class
    Karena rata-rata Threadpool yang dibuat memiliki kapasitas queue tidak terbatas
    Artinya jika terlalu banyak Runnable task di dalam queue, maka memori penyimpanan yang akan terpakai akan semakin besar
     */
    @Test
    void testExecutorsNewSingleThreadExecutor() throws InterruptedException {
        //Membuat threadpool dengan jumlah pool min dan max 1 jadi cuma 1 thread yang bekerja
        ExecutorService executors = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 100; i++) {
            executors.execute(() -> {
                try {
                    Thread.sleep(2000);
                    System.out.println("Hai from executors");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executors.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    void testExecutorsNewFixedThread() throws InterruptedException {
        //Membuat threadpool dengan jumlah pool min dan max fix disini kitabuat 10
        ExecutorService executors = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executors.execute(() -> {
                try {
                    Thread.sleep(2000);
                    System.out.println("Hai from executors");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executors.awaitTermination(1, TimeUnit.HOURS);
    }

    @Test
    void testExecutorsNewCachedThreadPool() throws InterruptedException {
        //Membuat threadpool dengan jumlah thread bisa bertambah tidak terhingga
        ExecutorService executors = Executors.newCachedThreadPool();
        //jika kita mengeksekusi task 200 maka akan langsung di selesaikan 200 task nya
        for (int i = 0; i < 300; i++){
            executors.execute(() -> {
                try {
                    Thread.sleep(2000);
                    System.out.println("Hai from executors");
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            });
        }
        executors.awaitTermination(1, TimeUnit.HOURS);
    }
}
