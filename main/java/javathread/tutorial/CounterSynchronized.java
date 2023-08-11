package javathread.tutorial;
/*
Masalah race condition sebelumnya yang terjadi di Java bisa diselesaikan dengan Synchronization
Synchronization merupakan fitur dimana kita memaksa kode program hanya boleh diakses dan dieksekusi oleh satu thread saja
Hal ini menyebabkan thread yang lain yang akan mengakses kode program tersebut harus menunggu thread yang lebih dahulu
mengakses, sehingga proses Synchronization akan lebih lambat, namun proses Synchronization lebih aman karena tidak akan terjadi race condition

Di Java, terdapat dua jenis synchronization, yaitu synchronized method dan synchronized statement

Saat kita menggunakan synchronized method, secara otomatis seluruh method akan ter synchronization
Kadang, misal kita hanya ingin melakukan synchronized pada bagian kode tertentu saja
Untuk melakukan hal tersebut, kita bisa menggunakan synchronized statement
Namun ketika kita menggunakan synchronized statement, kita harus menentukan object intrinsic lock sendiri

*/
public class CounterSynchronized {
    private static Long value = 0L;

    //PEMBUATAN SYNCHRONIZED METHOD
    public synchronized void increment(){
        //KALAU INI SEMUA PROGRAM YANG ADA PADA METHOD DI SYNCHRONIZED
        //JADI TIDAK BISA BERJALAN SECARA PARALLEL
        value++;
    }

    //PEMBUATAN SYNCHRONIZED STATEMENT
    public void increment2(){

        //PROGRAM LAIN MISALNYA .......

        synchronized (this){
            //DENGAN BEGINI CUMIA INI YANG DI SYNCHRONIZE
            //TIDAK SATU BLOCK METHOD INI JADI KALAU ADA PROGRAM
            //LAIN DI LUAR BLOCK SYNCHRONIZED INI INI BISA DI EKSEKUSI SECARA PARALLEL
            value++;
        }

        //PROGRAM LAIN MISALNYA .......
    }

    public long getValue(){
        return value;
    }
}
