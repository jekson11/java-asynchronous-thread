package javathread.tutorial;

import org.junit.jupiter.api.Test;

public class ThreadTest {
    @Test
    void testThreadMain(){
        String name = Thread.currentThread().getName();
        System.out.println(name);
    }

    @Test
    void createThreadRunnable() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Proses Program: " + Thread.currentThread().getName());
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        //LAMBDA
        Runnable runnable1 = () -> {
            System.out.println("Proses Program: " + Thread.currentThread().getName());
        };

        Thread thread1 = new Thread(runnable1);
        thread1.start();
        System.out.println("Program Finished");
    }

    @Test
    void testThreadSleep() throws InterruptedException {
        Runnable runnable = () -> {
            try {
                Thread.sleep(1_000L);
                System.out.println("Proses Program:");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        Thread.sleep(1_000);
    }

    @Test
    void testThreadJoin() throws InterruptedException {
        //KALAU JOIN ITU SEMUA THREAD YANG ADA AKAN DI TUNGGU SAMPAI TUGASNYA SELESAI
        //BARULAH PROGRAMNYA AKAN DI TUTUP, JADI JIKA KITA INGIN SEBUAH PROGRAM DI TUTUP
        //KETIKA SEMUA THREADNYA SELESAI DI JALANKAN KITA BISA MENGGUNAKAN JOIN, JADI
        //UNTUK MENUNGGU SEBUAH THREAD SELESAI JANGAN MENGGUNAKAN SLEEP LAGI
        Runnable runnable = () -> {
            try {
                Thread.sleep(1_000);
                System.out.println("Proses Program");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        System.out.println("Waiting Thread");
        thread.join();
        System.out.println("Program Finished");
    }

    @Test
    void testThreadInterrupt() throws InterruptedException {
        /*
        Interrupt merupakan mengirim sinyal ke thread bahwa thread
        Tersebut harus berhenti melakukan pekerjaannya saat ini
         */
        Runnable runnable = () -> {
            //KITA PUNYA PRTULANGAN YANG DI MANA SETIAP PERULANGANNYA KITA BUAT DIA TERTIDUR SELAMA 1 DETIK
            //KARNA INI ADA 10 PERULANGAN MAKAN AKAN MEMAKAN WAKTU 10 DETIK
            for (int i = 0; i < 10; i++) {
                System.out.println("Proses Program: "+i);
                try {
                    Thread.sleep(1_000L);
                } catch (InterruptedException e) {
                    return;//INI HANYA MENGHENTIKAN PROGRAMNYA TIDAK PRINT ERROR
//                    e.printStackTrace();//INI HANYA AKAN PRINT ERRORNYA TIDAK MENGHENTIKAN PROGRAMNYA
//                    throw new RuntimeException(e);//INI KALAU MAU PRINT ERROR DAN MENGHENTIKAN PROGRAMNYA
                }
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        System.out.println("Waiting Program");
        Thread.sleep(5_000L);
        thread.interrupt();//HARUS KITA PANGGIL METHOD interrupt NYA BARULAH PROGRAMNYA MENDENGARKAN
        thread.join();
        System.out.println("Program Finished");
    }

    @Test
    void testThreadInterruptManual() throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 10; i++) {
                //INI MANUALNYA
                //KALAU BIKIN PROGAM SEBENARNYA KITA MENGGUNAKAN INI UNTUK MENGECAK ADA GK  INTERRUPT
                if (Thread.interrupted()){//JIKA INTERUPT SUDAH DI PANGGIL MAKA KONDISI INI AKAN TRUE
                    return;//DAN AKAN MENGHENTIKAN PROGRAM
                }
                System.out.println("Proses Program: "+i);
            }
        };

        var thread = new Thread(runnable);
        thread.start();
        System.out.println("Waiting Program");
        Thread.sleep(5_000L);//JIKA DALAM 5 DETIK PERULANGAN BELUM SELESAI MAKA INTERRUPT DI PANGGIL BARIS 112
        thread.interrupt();
        thread.join();
        System.out.println("Program Finished");
    }

    @Test
    void testThreadSetAndGetName(){
        //KITA BISA JUGA MEMBUAT THREAD SEPERTI INI
        var thread = new Thread(() -> {
            System.out.println("Thread Name: "+Thread.currentThread().getName());
        });
        //KITA UBAH NAMANYA SEBELUM THREADNYA DI MULAI
        thread.setName("Jekson");
        thread.start();
    }

    @Test
    void testStateThread() throws InterruptedException {
        //STATE ITU MERUPAKAN INFORMASI THREADNYA LAGI NGAPAIN
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getState());//INI STATENYA RUNNABLE/SEDANG BERJALAN
            System.out.println("Thread name: "+Thread.currentThread().getName());
        });

        System.out.println(thread.getState());//INI STATENYA NEW/THREADNYA BARU DI BUAT
        thread.start();
        thread.join();
        System.out.println(thread.getState());//INI STATENYA TERMINATED/UDAH SELESAI MENJALANKAN TUGANYA
    }

    //LANJUT DI FOLDER MAIN DAEMON THREAD
}
