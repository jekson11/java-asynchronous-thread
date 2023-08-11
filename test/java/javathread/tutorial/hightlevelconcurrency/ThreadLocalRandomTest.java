package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreadLocalRandomTest {
    /*
Pada materi-materi sebelumnya, kita sering menggunakan class Random untuk membuat angka random
Saat menggunakan object Random secara parallel, maka di dalam class Random kita akan melakukan sharing variable,
hal ini membuat class Random tidak aman dan juga lambat
Oleh karena itu terdapat class ThreadLocalRandom, ThreadLocalRandom merupakan class yang seperti ThreadLocal,
namun spesial untuk Random, sehingga kita bisa membuat angka
random tanpa khawatir dengan race condition, karena object Random nya akan berbeda tiap thread
     */
    @Test
    void testThreadLocalRandom() throws InterruptedException {
        var service = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100; i++) {
            service.execute(() -> {
                try {
                    Thread.sleep(2000L);
                    //begini cara  oembuatan random thread nya
                    var random = ThreadLocalRandom.current().nextInt();
                    //ini kalau mau membatasi berapa max angka randomnya
//                    var random = ThreadLocalRandom.current().nextInt(500);
                    System.out.println(random);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    void testThreadLocalStream(){
        /*
Fitur lainnya di ThreadLocalRandom adalah, dia memiliki fitur untuk membuat random number secara stream
Hal ini mempermudah kita ketika ingin melakukan random number tanpa harus pusing membuat perulangan secara manual
ada banyak method di ThreadLocalRandom seperti ints(), longs() dan doubles() yang mengembalikan data stream
         */
        var service = Executors.newFixedThreadPool(100);

        service.execute(() -> {
            //cara pembuatannya begini di langsung bisa stream 10 jumlah random nya
            //0 dan 100 itu random nya dari angka 0 sampai 100
            //ini bukan ints (int) doang y masih banyak lagi methodnya
            ThreadLocalRandom.current().ints(10, 0, 100)
                    .forEach(random -> System.out.println(random));
        });

    }
}
