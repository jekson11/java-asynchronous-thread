package javathread.tutorial;

public class ThreadApp {
    public static void main(String[] args) {
        /*
        Secara default, saat sebuah aplikasi Java berjalan, minimal akan ada satu thread yang berjalan
        Dalam aplikasi Java biasa, biasanya kode program kita akan berjalan di dalam thread yang bernama main
        Bahkan di Unit Test pun, memiliki thread sendiri
         */
        String thread = Thread.currentThread().getName();
        System.out.println(thread);

    }

}
