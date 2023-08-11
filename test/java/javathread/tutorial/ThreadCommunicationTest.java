package javathread.tutorial;

import org.junit.jupiter.api.Test;

import javax.imageio.plugins.tiff.TIFFImageReadParam;

public class ThreadCommunicationTest {
    /*
    Dalam multithreaded programming, kadang sudah biasa ketika sebuah thread perlu menunggu thread lain menyelesaikan
    tugas tertentu, baru thread tersebut melakukan tugasnya
    Sayangnya tidak ada cara otomatis komunikasi antar thread secara langsung
    Oleh karena itu, programmer harus melakukannya secara manual untuk komunikasi antar thread
     */
    private String message = null;
    @Test
    void testCommunicationManualUseWhileLoop() throws InterruptedException {
        /*
        Menggunakan loop untuk menunggu sangat tidak direkomendasikan, alasannya buang-buang resource
        CPU dan juga jika terjadi interrupt, loop akan terus berjalan tanpa henti
         */
        var thread1 = new Thread(() -> {
            while (message == null){
                //WAITING
            }
            System.out.println(message);
        });

        var thread2 = new Thread(() -> {
            message = "Hello bro";
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    void testCommunicationWaitNotify() throws InterruptedException {
        //MENGGUNAKAN WAIT DAN NOTIFY INI YANG DI SARANKAN JIKA KITA INGIN
        //MEMBUAT SUATU THREAD DI EKSEKUSI SETELAH THREAD YNG DI TUNGGU SELASAI

        //BUAT AJA OBJECT APAPUN MAU ITU STRING INT APAPUN TERSERAH KARENA KITA HANAY MEMBUTUHKAN
        //OBJECTNYA AJA BUKAN ISINYA
        Object object = new Object();//BUAT AJA OBJECT APAPUN MAU ITU STRING INT APAPUN TERSERAH
        var thread1 = new Thread(() -> {
            synchronized (object){//KITA LOCK OBJECT TERSEBUT
                try {
                    //KITA BUAT THREAD YANG MENGLOCK OBJECT INI MENUNGGU SAMPAI ADA SINYAL YANG MENYURUH DIA UNTUK JALAN
                    object.wait();
                    System.out.println(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var thread2 = new Thread(() -> {
            synchronized(object){
                message = "Hello bro";
                object.notify();//INI DIA PRINTAH YANG AKAN MENYURUH THREAD TERSEBUT BERJALAN KEMBALI
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    void testCommunicationThreadWaitAndNotifyAll() throws InterruptedException {
        //NOTIFYALL ITU UNTUK MEMBERI SINYAL KEPADA SEMUA THREAD, BUKAN HANYA KEPADA SATU THREAD SAYA
        //SEPERTI NOTIFY JADI JIKA ADA LEBIH DARI SATU THREAD YANG MENUNGGU THREAD YANG SAMA UNTUK MEMBERTI
        //SINYAKNYA AGAR MEREKA LANJUT BERHERAK KITA BISA GUNAKAN NOTIFYALL
        Object object = new Object();
        var thread1 = new Thread(() -> {
            synchronized(object){
                try {
                    object.wait();
                    System.out.println(message);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        });

        var thread3 = new Thread(() -> {
            synchronized (object){
                try {
                    object.wait();
                    System.out.println(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        var thread2 = new Thread(() -> {
            synchronized(object){
                message = "Halo Indonesia";
                object.notifyAll();
            }
        });

        //HARUS MEREKA DULUAN KARENA MEREKA YANG SEDANG MENUNGGU
        thread1.start();
        thread3.start();

        thread2.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }
}
