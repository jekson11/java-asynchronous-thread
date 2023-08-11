package javathread.tutorial.hightlevelconcurrency.concurrentCollection;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentMapTest {
    /*
ConcurrentMap merupakan turunan dari Map yang thread safe, dan cocok jika memang diakses oleh lebih dari satu thread
Tidak ada hal yang spesial dari ConcurrentMap, semua operasi method nya sama seperti Map, yang membedakan adalah pada ConcurrentMap, dijamin thread safe
Implementasi dari interface ConcurrentMap adalah class ConcurrentHashMap
     */
    @Test
    void testConcurrentMap() throws InterruptedException {
        var countDown = new CountDownLatch(100);
        var map = new ConcurrentHashMap<Integer, String>();
        var service = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100; i++) {
            var index = i;
            service.execute(() -> {
                try {
                    Thread.sleep(1000L);
                    map.putIfAbsent(index, "Data: "+index);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    countDown.countDown();
                }
            });
        }

        service.execute(() -> {
            try {
                countDown.await();
                map.forEach((key, value) -> System.out.println("Key: "+key+" Value: "+value));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        service.awaitTermination(1, TimeUnit.MINUTES);
    }

    /*
Pada kasus tertentu, kadang kita tetap butuh menggunakan Java Collection, namun butuh menggunakan multiple thread
Untuk kasus seperti itu, disarankan merubah Java Collection menjadi synchronized menggunakan helper method Collections.synchronized…(collection)
     */
    @Test
    void testConvertFromCollectionToCollectionMultipleThread(){
        List<String> list = List.of("Jekson", "Tambunan", "November");
        //DENGAN BEGINI ARRAY NYA JADI BISA THREAD SAFE/ AMA SAAT DI AKSES BANYAK THREAD SEKALIGUS
        //UNTUK PENGGUNAANNYA SAMA SAJA SEPERTI Lis biasanya
        List<String> list1 = Collections.synchronizedList(list);
    }
}
