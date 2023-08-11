package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Stream;

public class ParallelStreamTest {
    @Test
    void testParallelStreamListToStream(){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/bookdata.txt"))){
            String next;
            List<String> books = new ArrayList<>();
            while ((next = bufferedReader.readLine()) != null){
                books.add(next);
            }
            Stream<String> stream = books.stream();
            stream.parallel().forEach(book -> System.out.println(Thread.currentThread().getName()+": "+book));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testParallelStreamToList(){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/bookdata.txt"))){
            //bisa begini jadi kita tanpa tunggu harus pindahkan dulu semua data dari file
            //ke list baru masukkan ke list yang baru menggunakan stream seperti di bari bawah
            //kalau ini kita tinggal menggunakan method lines() ini akan mengambil datanya
            //langsung dari file dan di pindahkan se listnya
            List<String> data = bufferedReader.lines().toList();
            data.stream().parallel().forEach(value -> System.out.println(value+" : "+Thread.currentThread().getName()));

            String next;
            List<String> list = new ArrayList<>();
            while ((next = bufferedReader.readLine()) != null){
                list.add(next);
            }

            //ini yang menggunakan parallel stram jadi thread yang di gunakan mengikuti jumlah cpu komputer kita
            list.stream().parallel().forEach(result -> System.out.println(result+" : "+Thread.currentThread().getName()));

            List<String> list2 = list.stream().toList();
            list2.stream().parallel().forEach(value -> System.out.println(value+" : "+Thread.currentThread().getName()));
            //ini yang streamnya tanpa menggunakan parallel jadi hanya menggunakan 1 thread saja
            //untuk mengirim data ke listnya
            list.stream().forEach(result -> System.out.println(result+" : "+Thread.currentThread().getName()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCustomForkJoinPool(){
        //jadi stream yang di atas dia menggunakan semua cpu kita untuk di jadikan thread dan mengeksekusi semua tugas
        //nah di sini kita akan  mengatur berapa cpu yang mau kita gunakan untuk mengerjakan semua tugas
        //dengan cara
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);//untuk mengatur berapa penggunaan cpu nya
        ForkJoinTask<?> submit = forkJoinPool.submit(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/bookdata.txt"))) {
                List<String> stream = bufferedReader.lines().toList();
                stream.stream().parallel().forEach(value -> System.out.println(Thread.currentThread().getName()+" : "+value));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        submit.join();
    }
}
