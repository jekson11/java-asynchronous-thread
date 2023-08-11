package javathread.tutorial;

import org.junit.jupiter.api.Test;

public class CounterRaceConditionTest {
    /*
    Salah satu problem yang sering sekali terjadi dalam aplikasi concurrent dan parallel adalah race condition
    Race condition merupakan keadaan ketika sebuah data diubah secara berbarengan oleh beberapa thread yang
    menyebabkan hasil akhir yang tidak sesuai dengan yang kita inginkan
     */

    @Test
    void testNoRaceCondition(){
        //INI TIDAK RACE CONDITION KARENA TIDAK BERJALAN SECARA PARALLEL
        Counter counter = new Counter();
        Runnable runnable = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        };

        runnable.run();
        runnable.run();
        runnable.run();

        //INI NILAINYA PASS SESUAI PERULANGAN
        System.out.println(counter.getValue());
    }

    @Test
    void testRaceCondition() throws InterruptedException {
        //INI RACE CONDITION KARENA BERJALAN SECARA PARALLEL
        Counter counter = new Counter();
        Runnable runnable = () -> {
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        };

        var thread1 = new Thread(runnable);
        var thread2 = new Thread(runnable);
        var thread3 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        //INI NILAINYA TIDAK PAS SESUAI DENGAN PERULANGAN KARENA DIA PARALLEL
        System.out.println(counter.getValue());
    }
}
