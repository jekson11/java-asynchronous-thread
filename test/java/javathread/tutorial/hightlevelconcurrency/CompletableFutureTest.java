package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
/*
Pada Java 8, terdapat sebuah class baru bernama CompletableFuture, ini merupakan implementasi Future yang bisa kita set datanya secara manual
CompletableFuture sangat cocok ketika kita misal perlu membuat future secara manual, sehingga kita tidak memerlukan Callable
Untuk memberi value terhadap CompletableFuture secara manual, kita bisa menggunakan method complete(value) atau completeExceptionally(error) untuk error
 */

public class CompletableFutureTest {

    Random random = new Random();
    ExecutorService execution = Executors.newFixedThreadPool(10);
    //MISAL KITA PUNYA METHOD YANG MEMBUAT CompletableFuture
    public Future<String> getValue(){
        //CARA PEMBUATAN CompletableFuture
        CompletableFuture<String> future = new CompletableFuture<>();

        //DI DALAMNYA KITA MASUKIN execution.execute tidak invoke atau submit
        execution.execute(() -> {
            try {
                Thread.sleep(2000L);
                //kita gunakan method complete untuk mengisi nilainya
                //jika ini tidak di isi maka saat dia di panggil ini akan menunggu terus sampai nilainya ada
                future.complete("Success");
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    };
    //memanggil method yang di atas
    @Test
    void testCreateCompletableFuture() throws ExecutionException, InterruptedException {
        //seperti ini cara menggeluarkan nilainya
        Future<String> future = getValue();
        System.out.println(future. get());
    }

    //method ini menggunakan completableFuture juga pada parameternya
    public void execute(CompletableFuture<String> future, String value){
        execution.execute(() -> {
            try {
                Thread.sleep(random.nextInt(500) + 100L);
                //nilai dari completable nya di isi dari value yang di parameter
                future.complete(value);
            } catch (InterruptedException e) {
                //completableFuture bisa di jadikan error juga
                future.completeExceptionally(e);
            }
        });
    }

    //ini memanggil method yang di atas
    public Future<String> getFastest(){
        //nilai methodnya di masukkan dalam completableFuture
        CompletableFuture<String> future = new CompletableFuture<>();

        //lalu di panggil 4 kali methodnya jadi konsepnya
        //siapa yang paling cepat threadnya selesai itu yang akan menjadi nilainya
        //kita menggunakan angka random itu sebagai penyaingnya
        execute(future, "Thread first");
        execute(future, "Thread second");
        execute(future, "Thread third");
        execute(future, "Thread fourth");

        //mengeluarkan nilai yang paling cepat
        return future;
    }

    @Test
    void testFastest() throws ExecutionException, InterruptedException {
        //di panggil di sini
        System.out.println(getFastest().get());
    }

    /*
CompletableFuture merupakan turunan dari interface CompletionStage
CompletionStage merupakan fitur dimana kita bisa menambahkan asynchronous computation, tanpa harus menunggu dulu data dari Future nya ada
CompletionStage sangat mirip dengan operation di Java Stream, hanya saja tidak sekomplit di Java Stream
     */
    //kita buat method yang returnya CompletableFuture
    public CompletableFuture<String> getFuture(){
        CompletableFuture<String> future = new CompletableFuture<>();

        //ini seperti biasa
        execution.execute(() -> {
            try {
                Thread.sleep(2000L);
                //nilai futurenya kita set ini
                future.complete("Jekson Luffy Ichigo Naruto Goku");
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    //memanggil method di atas
    @Test
    void testCompletionStage() throws ExecutionException, InterruptedException {
        //penggunaanya jadi seperti java stream
        //nilai method di masukkan ke sini
        Future<String> future = getFuture();

        //lalu kita membuat perubahan di sini pada nilainya kita buata menjadi array
        CompletableFuture<String[]> future1 = getFuture()
                //di ubah menjadi upperCase menggunakan method thenApply untuk melakukan seperti ini
                //kalau di java streamkan kita menggunakan map
                .thenApply(value -> value.toUpperCase())
                //kita putong nilainya berdasarkan spasi
                .thenApply(value -> value.split(" "));

        System.out.println(future.get());
        //ini yang sudah menjadi array
        System.out.println(Arrays.toString(future1.get()));
        //cara ke dua untuk mengeluarkan nilai yang array
        String[] arr = future1.get();
        for (var value : arr){
            System.out.println(value);
        }
    }
}
