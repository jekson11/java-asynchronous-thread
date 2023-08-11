package javathread.tutorial.hightlevelconcurrency;

import javathread.tutorial.UserService;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocaleTest {
    /*
ThreadLocal merupakan fitur di Java untuk menyimpan data
ThreadLocal memberi kita kemampuan untuk menyimpan data yang hanya bisa digunakan di thread tersebut
Tiap thread akan memiliki data yang berbeda dan tidak saling terhubung antar thread
     */
    @Test
    void testThreadLocale() throws InterruptedException {
        //ini class yang kita buat y jangan pusing
        //cek class nya lalu lihat penjelasannya
        UserService user = new UserService();
        var service = Executors.newFixedThreadPool(100);
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            var index = i;
            service.execute(() -> {
                try {
                    user.setUser("User- "+index);
                    Thread.sleep(1000L+random.nextInt(3000));
                    user.doAction();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        service.awaitTermination(1, TimeUnit.MINUTES);
    }
}
