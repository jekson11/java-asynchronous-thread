package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    @Test
    void testCreateThreadPoll() throws InterruptedException {
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;
        //MENAMPUNG TASK YANG DI KIRIM KE TREAD POOL SETELAH DI TAMPUNG BARU DI EKSEKUSI
        var queue = new ArrayBlockingQueue<Runnable>(100);

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time,queue);

        //MENJALANKAN THREAD POOL BISA SEPERTI INI RECOMMENT INI
        Runnable runnable = () -> {
            try {
                Thread.sleep(2_000);
                System.out.println("Hello from thread pool: "+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        //MAUPUN SEPERTI INI
//        executor.execute(() -> {
//            try {
//                Thread.sleep(2_000);
//                System.out.println("Hello from thread pool: "+Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });

        executor.execute(runnable);

        Thread.sleep(3_000);
    }

    @Test
    void testShutdownThreadPool() throws InterruptedException {
        /*
        Jika kita sudah selesai menggunakan threadpool, dan tidak akan menggunakannya lagi, ada baiknya kita hentikan
        dan matikan ThreadPool nya
         */
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;
        var queue = new ArrayBlockingQueue<Runnable>(1000);

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time, queue);

        for (int i = 0; i < 1_000; i++) {
            var task = i;
            Runnable runnable = () -> {
                System.out.println("Task: "+task+" From thread pool: "+Thread.currentThread().getName());
            };
            executor.execute(runnable);
        }

        executor.shutdown();// untuk menghentikan threadpool, jika ada pekerjaan yang belum dikerjakan, maka akan di ignore
//        executor.shutdownNow();//untuk menghentikan threadpool, namun pekerjaan yang belum dikerjakan akan dikembalikan
//        executor.awaitTermination(1, TimeUnit.DAYS);//menunggu sampai threadpool selesai
    }
    
    @Test
    void testRejectHandlerUseRejectedExecutionHandler() throws InterruptedException {
        /*
        Apa yang terjadi jika queue penuh dan thread juga semua sedang bekerja?
        Maka secara otomatis akan di handle oleh object RejectedExecutionHandler
        Secara default, implementasi rejected handler akan akan mengembalikan exception RejectedExecutionException
        ketika kita submit(Runnable) pada kondisi queue penuh dan thread sedang bekerja semua
        Jika kita ingin mengubahnya, kita bisa membuat RejectedExecutionHandler sendiri
         */
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;
        var queue = new ArrayBlockingQueue<Runnable>(10);

        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("Task :"+r+" is reject");
            }
        };

        var executor = new ThreadPoolExecutor(minThread, maxThread, alive, time, queue, handler);

        for (int i = 0; i < 1000; i++) {
            var task = i;
            Runnable runnable = () -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Task :"+task+" from Thread pool: "+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            executor.execute(runnable);
        }

//        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    void testRejectedHandlerUseLogRejectedExecutionHandler() throws InterruptedException {
        //INI VERSI YANG MENGGUNAKAN CHILD CLASS DARI CLASS YANG KITA GUNAKAN DI test YANG ATAS
        var minThread = 10;
        var maxThread = 100;
        var alive = 1;
        var time = TimeUnit.MINUTES;
        var queue = new ArrayBlockingQueue<Runnable>(1000);
        var handler = new LogRejectedExecutionHandler();

        var executor = new ThreadPoolExecutor(minThread,maxThread,alive,time,queue,handler);

        for (int i = 0; i < 1000; i++) {
            var task = i;
            Runnable runnable = () ->{
                try {
                    Thread.sleep(1000);
                    System.out.println("Task :"+task+" from Thread pool: "+Thread.currentThread().getName());
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            };
            executor.execute(runnable);
        }

        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    public static class LogRejectedExecutionHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("Task :" +r+" is reject");
        }
    }
}
