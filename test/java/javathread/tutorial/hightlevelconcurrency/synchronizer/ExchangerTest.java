package javathread.tutorial.hightlevelconcurrency.synchronizer;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangerTest {
    /*
Exchanger merupakan fitur synchronizer untuk melakukan pertukaran data antar thread
Jika data belum tersedia, maka thread yang melakukan pertukaran akan menunggu sampai ada
thread lain yang melakukan pertukaran data
penukaran data ini jika ada tiga thread maka penukaran datanya hanya terjadi pada 2 thead saja
thread yang satunya akan terus menunggu sampai ada thread lain melakukan penukaran data terhadapnya
tetapi kalau jumlah threadnya 4 atau genap maka pertukaran data pada threadnya akan berhasil tapi perlu di ingat
penukaran ini terjadi secara random antara thread yang satu dan yang lainnya kita tidak bisa menentukan suatu thread
harus bertukar data dengan thread yang kita mau
     */
    @Test
    void testExchanger() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger();
//        Exchanger exchanger = new Exchanger<String>(); //penulisannya bisa begini juga ini generic type y
        ExecutorService executor = Executors.newFixedThreadPool(10);

        executor.execute(() -> {
            try {
                System.out.println("1. First");
                Thread.sleep(2000L);
                //ini method untuk menetukan value thread nya
                var value = exchanger.exchange("First");
                System.out.println("1. "+value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.execute(() -> {
            try {
                System.out.println("2. Second");
                Thread.sleep(2000L);
                var value = exchanger.exchange("Second");
                System.out.println("2. "+value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.execute(() -> {
            try {
                System.out.println("3. Three");
                Thread.sleep(2000L);
                var value = exchanger.exchange("Three");
                System.out.println("3. "+value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        executor.execute(() -> {
            try {
                System.out.println("4. Four");
                Thread.sleep(2000L);
                var value = exchanger.exchange("Four");
                System.out.println("4. "+value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
