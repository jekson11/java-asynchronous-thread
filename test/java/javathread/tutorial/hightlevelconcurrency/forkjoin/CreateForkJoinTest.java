package javathread.tutorial.hightlevelconcurrency.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class CreateForkJoinTest {
      /*
ForkJoinPool merupakan turunan dari ExecutorService, jadi cara penggunaannya sama dengan ExecutorService sebelumnya yang sudah kita bahas
Namun agar tujuan dari ForkJoinPool tercapai, baiknya kita menggunakan ForkJoinTask sebagai task yang kita submit ke ForkJoinPool
ForkJoinTask adalah turunan dari Callable, sehingga kita bisa menggunakan method execute() atau submit() untuk mengirim task ke ForkJoinPool

ForkJoinTask adalah abstract class, dan terdapat 2 abstract class turunannya yang bisa kita gunakan agar lebih mudah membuat ForkJoinTask
RecursiveAction, merupakan class yang bisa kita gunakan untuk task yang tidak mengembalikan result seperti Runnable
RecursiveTask, merupakan class yang bisa kita gunakan untuk task yang mengembalikan result seperti Callable
     */
    @Test
    void createForkJoinPoo(){
        ForkJoinPool thread1 = ForkJoinPool.commonPool();//ini mengikuti jumlah cpu pada perangkat kita
        ForkJoinPool thread2 = new ForkJoinPool(5);//ini kalau kita ingin set sendiri thread nya

        ExecutorService executor1 = Executors.newWorkStealingPool();//mengikuti jumlah cpu kita
        ExecutorService executor2 = Executors.newWorkStealingPool(5);//ini kalau kita ingin set sendiri
    }
}
