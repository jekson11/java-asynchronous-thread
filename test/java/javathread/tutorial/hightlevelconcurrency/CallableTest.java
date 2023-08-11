package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Sebelumnya kita selalu menggunakan Runnable untuk mengirim perintah ke thread, namun pada Runnable, setelah pekerjaan selesai, tidak ada data yang dikembalikan sama sekali, karena method nya return void
Callable mirip dengan Runnable, namun Callable mengembalikan data
Selain itu Callable merupakan generic type, sehingga kita bisa tentukan tipe return data nya

jika kita ingin mengeksekusi callable, kita bisa menggunakan method submit(Callable) pada ExecutorService, method submit(Callable) tersebut akan mengembalikan Future<T>
Future merupakan representasi data yang akan dikembalikan oleh proses asynchronous
Menggunakan Future, kita bisa mengecek apakah pekerjaan Callable sudah selesai atau belum, dan juga mendapat data hasil dari Callable
 */
public class CallableTest {
    @Test
    void testCreteCallable(){
        var executors = Executors.newSingleThreadExecutor();

        //INI TIDAK AKAN DI TUNGGU OLEH Future nya
        Callable callable = () -> {
            Thread.sleep(1000);
            return "Callable thread: "+Thread.currentThread().getName();
        };

        Future submit = executors.submit(callable);
        System.out.println("Callable finished");
    }

    @Test
    void testGetCallable() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executors = Executors.newSingleThreadExecutor();

        Callable callable = () -> {
            Thread.sleep(5000);
            return "Hai from getCallable thread: "+Thread.currentThread().getName();
        };

        Future<String> future = executors.submit(callable);
        //ini bisa juga seperti ini cuma yang di atas lebih jelas aja tipe datanya apa
//        Future future = executors.submit(callable);

        //Mengambil result data, jika belum ada, maka akan menunggu sampai timeou yang kita tentukan
        //di sini kita tentukan selama 2 detik
        String result = future.get(2, TimeUnit.SECONDS);
        System.out.println(result);

        //NGECEK APAKAH NILAI PADA fiture suda ada atau belum
        //kalau belum maka akan di tunggu sampai 1 detik
        while (!future.isDone()){
            System.out.println("Waiting Result");
            Thread.sleep(1000);
        }

        //Mengambil result data, jika belum ada, maka akan menunggu sampai ada
        String result2 = future.get();
        System.out.println(result2);

    }

    @Test
    void testCancelFuture() throws InterruptedException, ExecutionException {
        ExecutorService executors = Executors.newSingleThreadExecutor();

        Callable callable = () -> {
            Thread.sleep(3000);
            return "Hai from CancelCallable thread: "+Thread.currentThread().getName();
        };

        Future<String> future = executors.submit(callable);

        //KALAU SELAMA 2 DETIK CALLABLE NYA MASUH BELUM SELESAI MAKA:
        Thread.sleep(2000);
        //MEMBATALKAN FUTURE
        future.cancel(true);

        //MENGECEK APAKAH FUTURE NYA DI BATALKAN INI RETURN BOOLEAN
        System.out.println(future.isCancelled());
        String result = future.get();
        System.out.println(result);
    }

    /*
ExecutorService memiliki method bernama  invokeAll(Collection<Callable<T>>) untuk mengeksekusi banyak Callable secara sekaligus
Ini cocok ketika ada kasus kita ingin menjalankan proses asynchronous secara parallel sebanyak jumlah thread di threadpool
Hal ini bisa mempercepat proses dibanding kita eksekusi satu persatu
    */
    @Test
    void testCreateManyCallableInvokeAll() throws InterruptedException, ExecutionException {
        ExecutorService executors = Executors.newFixedThreadPool(20);

        List<Callable<String>> callables = IntStream.range(1,11).mapToObj(value -> (Callable<String>) () ->  {
            Thread.sleep(value * 500L);
            return String.valueOf(value);
        }).toList();

        List<Future<String>> futures = executors.invokeAll(callables);

        for (var future : futures){
            System.out.println(future.get());
        }
    }

    @Test
    void testCreateManyCallableInvokeAllLambdaStream() throws InterruptedException, ExecutionException {
        ExecutorService executors = Executors.newFixedThreadPool(4);

        List<Callable<String>> callables = Stream.of("jekson", "luffy", "naruto", "ichigo")
                .map(value -> (Callable<String>) () -> {
                    Thread.sleep(value.length() * 500L);
                    return value+" Thread : "+Thread.currentThread().getName();
                }).toList();

        //LAMBDA VERSION
//        List<Callable<String>> callables1 = Stream.of("jekson", "tambunan", "ganteng").map(value ->
//                (Callable<String>) () -> value).toList();

        List<Future<String>> futures = executors.invokeAll(callables);

        for (var future : futures){
            System.out.println(future.get());
        }
    }

    @Test
    void testCreateManyCallableInvokeAllStreamArray() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executors = Executors.newFixedThreadPool(50);
        String[] arr = {
                "jekson", "luffy", "naruto",
                "ichigo", "goku", "light"
        };
        List<Callable<String>> callables = Arrays.stream(arr).map(value -> (Callable<String>) () -> {
            Thread.sleep(value.length() * 1000L);
            return value;
        }).toList();

        var futures = executors.invokeAll(callables);

        for (var future : futures){
            System.out.println(future.get());
        }
    }

    /*
Kadang ada kasus dimana kita ingin mengeksekusi beberapa proses secara asynchronous, namun ingin mendapatkan hasil yang paling cepat
Hal ini bisa dilakukan dengan menggunakan method invokeAny(Collection<Callable<T>>)
invokeAny() akan mengembalikan result data dari Callable yang paling cepat mengembalikan result
     */
    @Test
    void testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService executors = Executors.newFixedThreadPool(10);

        List<Callable<String>> callable = Stream.of("jekson", "luffy", "light",  "naruto", "ichigo", "goku")
                .map(value -> (Callable<String>) () ->  {
                    Thread.sleep(value.length() * 1000L);
                    return value+" Thread: "+Thread.currentThread().getName();
        }).toList();

        //INI MENGAMBIL TASK YANG DULUAN SIAP PADA THREAD
        String futures = executors.invokeAny(callable);

        System.out.println(futures);
    }
}
