package javathread.tutorial.hightlevelconcurrency.forkjoin;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class RecursiveActionTest {
    //pembuatan class RecursiveAction kita buat class baru lalu extend RecursiveAction abstrac
    //ini ibarat Runnable yang tidak memiliki return sedangkan  RecursiveTask ini seperti Callable dia memiliki return
    public static class SimpleForkJoinTaskRecursiveAction extends RecursiveAction {
        final private List<Integer> data;
        //constructor
        public SimpleForkJoinTaskRecursiveAction(List<Integer> data){
            this.data = data;
        }

        @Override
        public void compute() {
            //pengecekan apakah size datanya sudah <= 10
            //method yang di overide ini otomatis di panggil y karna object class ini di eksekusi menggunakan ForkJoinPool
            //padahal kita tidak ada memanggil  method ini
            if (data.size() <= 10){
                doCompute();
            }else {
                forkCompute();
            }
        }

        private void forkCompute() {
            //ini membelah belah fork nya menjadi kecil sesuai yang kita atur pada method compute
            //menggunakan method subList mengambil data dari indeks ke 0 sampe ke indek setengah dari size data
            List<Integer> data1 = this.data.subList(0, data.size()/2);
            //ini memotonga dari indeks setengah size data sampai ke akhir size data
            List<Integer> data2 = this.data.subList(this.data.size()/2, this.data.size());

            //memasukkan ke objec class SimpleForkJoinTaskRecursiveAction yang kita buat
            SimpleForkJoinTaskRecursiveAction task1 = new SimpleForkJoinTaskRecursiveAction(data1);
            SimpleForkJoinTaskRecursiveAction task2 = new SimpleForkJoinTaskRecursiveAction(data2);

            //mengeksekusi kekaligus task nya menggunakan invokeAll
            ForkJoinTask.invokeAll(task1, task2);
        }

        private void doCompute() {
            data.forEach(value -> System.out.println(Thread.currentThread().getName()+" : "+value));
        }
    }

    @Test
    void testForkJoinRecursiveAction() throws InterruptedException {
        //menjalankan menggunakan ForkJoinPool
        //ini parallelism nya sesuai dengan cpu kita jadi threadnya nanti sesuai dengan jumlah cpu kita
        var pool = ForkJoinPool.commonPool();
        //ini jika kita ingin mengatur berapa parallelism nya kalau 3 maka thread yang bekerja itu 3
//        var pool = new ForkJoinPool(3);
        List<Integer> list = IntStream.range(1, 100).boxed().toList();

        //karena pool nya turunan dari ExecutorService jadi operasinya sama saja dengan ExecutorService
        pool.execute(new SimpleForkJoinTaskRecursiveAction(list));

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }
}
