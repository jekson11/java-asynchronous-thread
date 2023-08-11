package javathread.tutorial;

import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
    @Test
    void testTimerDelayJob() throws InterruptedException {
        //JADI INTINYA FUNGSINYA INI MENJADWALKAN PROGRAM AGAR BERJALAN PADA SAAT WALTU YANG KITA TENTUTAK
        //DI SINI WAKTU YANG KITA TENTUKAN 2 DETIK
        var task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Delay job");
            }
        };

        var timer = new Timer();
        //KARENA INI DI TEST MAKA PROGRAMNYA AKAN LANGSUNG DI HENTIKA BEGITU SELESAI
        //BELA LAGI KALAU CLASS MAIN INI AKAN BERJALAN TERUS
        timer.schedule(task, 2_000);//MAKA TASK AKAN DI JALANKAN KETIKA 2 DETIK BERLALU

        Thread.sleep(3_000);
    }

    @Test
    void testTimerPeriodicJob() throws InterruptedException {
        var task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Periodic job");
            }
        };

        var timer = new Timer();
        //INI TIDAK AKAN BERHENTI SAMPAI PEROGRAMNYA DI MATIKAN
        //ARTINYA INI task AKAN DI JALANKAN SETELAH 2 DETIK DELAY DAN INI AKAN DI JALANKAN LAGI SETELAH 3 DETIK
        //DI ULANG ULANG BEGITU TERUS 2 DETIK DAN 3 DETIKNYA JALAN BERSAMAAN Y JADI TIDAK SALING MENUNGGU
        timer.schedule(task, 2_000, 3_000);

        Thread.sleep(10_000);
    }
}
