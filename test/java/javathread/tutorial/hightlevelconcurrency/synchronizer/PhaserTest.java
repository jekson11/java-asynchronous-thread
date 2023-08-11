package javathread.tutorial.hightlevelconcurrency.synchronizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class PhaserTest {
    /*
Phaser merupakan fitur synchronizer yang mirip dengan CyclicBarrier dan CountDownLatch, namun lebih flexible
Sebelumnya, untuk jumlah counter atau thread nya sudah ditentukan di awal, namun pada Phaser, bisa berubah dengan
menggunakan method register() atau bulkRegister(int), dan untuk menurunkan bisa menggunakan method arrive...(),
atau bisa menggunakan await...(int) untuk menunggu sampai jumlah yang register tertentu
     */
    @Test
    void testPhaserCountDownLatch() throws InterruptedException {
        //phaser sebagai countDownLatch ini cara kerjanya mirip seperti class countDown y
        //bedanya untuk nyetel countDownnya kita buat di
        Phaser phaser = new Phaser();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++){
            executor.execute(() -> {
                try {
                    //jadi lebih flexible
                    //kalau kita membuat phaser.bulkRegister(5) ligi ini akan membuat countDown nya tambah menjadi 10 jadi hati2
                    phaser.bulkRegister(5);//sini menggunakan register method pada phaser
//                    phaser.register();//ini untuk jika hanya ingin jumlah countdownnya sesuai task(pekerjaan) yang dikrimkan
                    System.out.println("Start task");
                    Thread.sleep(2000L);
                    System.out.println("End task");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    //ini untuk menurunkan countDown nya
                    phaser.arrive();
                }
            });
        }

        executor.execute(() -> {
            //menunggu sampai countDownnya sisa 0
            phaser.awaitAdvance(0);
            //bari ini bisa jalan
            System.out.println("All task finished");
        });

        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    void testPhaserCyclicBarrier() throws InterruptedException {
        //phaser sebagai cyclicBarrier ini penggunaan nya sama seperti pada cyclicBarrier
        //cyclicBarrier itu menuggu beberapa thread yang sudah kita tentukan sebelumnya
        //kalau jumlah thread yang kita tunggu sudah terpenuhi maka thread nya akan langsung di lepas secara bersamaan
        //dan mengerjakana tugas secara bersamaan
        Phaser phaser = new Phaser();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        //cara menentukan thread yang ingin kita tunggu
        phaser.bulkRegister(5);
//        phaser.register();//kalau ini hanya 1 thread saja
        for (int i = 0; i < 4; i++){
            System.out.println("Waiting");
            Thread.sleep(2000L);
            executor.execute(() -> {
                //ini menurunkan thread dan juga menggu sampai thread lain selesai
                phaser.arriveAndAwaitAdvance();
                System.out.println("Done waiting");
            });
        }

        //kalau kita membuat empat thread dengan perulangan sementara kita set 5 thread agar di lipaskan
        //maka yang akan terjadi dia akan menuggu terus
        //phaser.bulkRegister(5);
//        for (int i = 0; i < 4; i++){
//            System.out.println("Waiting");
//            Thread.sleep(2000L);
//            executor.execute(() -> {
//                //ini menurunkan thread dan juga menggu sampai thread lain selesai
//                phaser.arriveAndAwaitAdvance();
//                System.out.println("Done waiting");
//            });
//        }

        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
