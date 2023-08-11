package javathread.tutorial.hightlevelconcurrency;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/*
Saat ini sudah sangat populer dengan paradigma concurrency yang bernama reactive programming
Banyak sekali library untuk reactive programming seperti RxJava, Reactor, Akka Stream dan lain-lain
Sejak Java 9, diperkenalkan fitur Reactive Stream di Java
Reactive Stream merupakan standar baru untuk Asynchronous Stream Processing
Detail spesifikasinya terdapat di website http://www.reactive-streams.org/

//FLOW
Dalam Reactive Stream, kita mengenal istilah namanya Flow (aliran data), berbeda dengan yang sebelumnya sudah
kita pelajari tentang Thread, dalam Reactive Stream, yang difokuskan adalah aliran data
Dalam aliran data, artinya ada yang mengirim data dan ada yang menerima data.
Pihak yang mengirim data, kita sebut Publisher, dan pihak yang menerima data, kita sebut Subscriber
Sebuah aliran data, kita sebut namanya Flow
https://docs.oracle.com/en/java/javase/16/docs/api/java.base/java/util/concurrent/Flow.html

Implementasi dari Reactive Stream di Java disediakan dalam bentuk class Flow
Untuk publisher, implementasinya menggunakan interface Flow.Publisher
Dan untuk subscriber, implementasinya menggunakan interface Flow.Subscriber

Saat publisher mengirim data terlalu cepat, maka secara default data akan di buffer
Buffer mirip antrian, dimana secara default buffer di Flow ukurannya sekitar Flow.DEFAULT_BUFFER_SIZE (257),
artinya jika publisher mengirim data terlalu cepat, maka buffer akan menampung data tersebut dahulu sampai
sekitar 256 data, jika buffer sudah penuh, maka publisher harus menunggu sampai data di buffer di ambil oleh subscriber
Jika 256 terlalu besar, kita bisa mengatur data buffer yang kita inginkan

 */
public class ReactiveStreamTest{

    //CARA MENGIMPLEMENTASI Flow.Subscriber kita impl ke class yang kita buat maka nanti kita di suruh
    //mengoverride beberapa method di antaranya
    public static class PrintSubscriber implements Flow.Subscriber<String>{
        //subscription ini untuk menampung datanya y ini juga dari Subscriber tapi kita buat sendiri
        private Flow.Subscription subscription;
        String name;
        Long sleep;
        public PrintSubscriber(String name, Long sleep){
            this.name = name;
            this.sleep = sleep;
        }

        //ini untuk ke tika di panggil kita mau ngapain
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            //di sini kia buat dia menerima 1 data
            this.subscription.request(1);

        }

        //ini akan di ulang ulang terus menerus sampai data yang di kirim daru publisher habis
        @Override
        public void onNext(String item) {
            try {
                Thread.sleep(sleep);
                System.out.println(Thread.currentThread().getName()+": "+name+" : "+item);
                //di sini kita menerima lagi 1 data jadi ini berulang terus menerima 1 data terus menerus
                subscription.request(1);
                //ini untuk jika kita tidak ingin menerima data lagi dari publisher kita menggunakan cancel method
//            subscription.cancel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //ini kalau ada yang error makan akan ke sini larinya
        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        //ini akan jalan kalau semua data yang di alirkan publisher habis
        @Override
        public void onComplete() {
            System.out.println(Thread.currentThread().getName()+": Done");
        }
    }

    @Test
    void testReactiveStream() throws InterruptedException {
        var executor = Executors.newSingleThreadExecutor();
        //untuk membuat publisheer kita tdak perlu membuta object Flow.Publisher cukup meggunakan impl dari class publisher
        //yaitu SubmissionPublisher, publisher ini bisa mengalirkan data ke banya subscriber bukan cuma 1 subscriber saja
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        //membuat object class yang kita buat yang meng implement Flow.Subscriber
        PrintSubscriber subscriber1 = new PrintSubscriber("A", 1000L);
        //ini kita buat subscriber ke dua
        PrintSubscriber subscriber2 = new PrintSubscriber("B", 500L);

        //kita masukkan subscriber nya ke publisher menggunakan method subscriber()
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        //mengirim 100 task ke subscriber
        executor.execute(() -> {
            for (int i = 0; i < 100; i++) {
                publisher.submit("Jekson :"+i);
                System.out.println("send :"+i);
            }
        });

        executor.awaitTermination(1, TimeUnit.DAYS);
    }


    //bagian ini kita mengambil data nya dari file.txt y lebih tepatnya bookdata.txt
    public static class PrintSubscriberFile implements Flow.Subscriber<String>{
        private Flow.Subscription subscription;
        String name;
        Long sleep;
        public PrintSubscriberFile(String name, Long sleep){
            this.name = name;
            this.sleep = sleep;
        }
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1);
            //ini untuk jika kita tidak ingin menerima data lagi dari publisher kita menggunakan cancel method
//            subscription.cancel();
        }

        @Override
        public void onNext(String item) {
            try {
                Thread.sleep(sleep);
                System.out.println(name+" : "+item);
                subscription.request(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("Done Stream");
        }
    }
    @Test
    void TestReactiveStreamSendFile() throws InterruptedException, FileNotFoundException {
        var executor = Executors.newSingleThreadExecutor();
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        //kita mengalirkan data ke dua subscriber
        PrintSubscriberFile subscriber1 = new PrintSubscriberFile("A", 3000L);
        PrintSubscriberFile subscriber2 = new PrintSubscriberFile("B", 2000L);
        publisher.subscribe(subscriber1);
        publisher.subscribe(subscriber2);
        //kita membaca data dari file
        BufferedReader data = new BufferedReader(new FileReader("src/test/bookdata.txt"));
        //di eksekusi secara parallel
        executor.execute(() -> {
            try {
                //ini tempat data nanti kita masukkan
                String line;
                //menggunakan perulangan memasukkan data tiap barisnya ke string
                while ((line = data.readLine()) != null){
                    //di sini kita publisher dia
                    //perlu di ingat data yang kita masukkan ada 1000 sedangkan ReactiveStream ini ada batasannya
                    //yaitu 257 jadi sisa data dari 1000 itu akan menunggu dulu sampai ada yang kosong
                    //baru meraka akan mengalir seperti sungai, jiak publishernya yang lama mengirim data itu tidak masalah
                    //subscribernya akan menunggu data dari publisher nya
                    publisher.submit(line);
                    System.out.println("Add :"+line+": "+Thread.currentThread().getName());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(1, TimeUnit.DAYS);
    }


    //mengatur batasan ReactiveStreanya atau buffer nya namanya
    @Test
    void testBuffer() throws FileNotFoundException, InterruptedException {
        var executor = Executors.newFixedThreadPool(5);
        //ini kita mengatur batas ReactiveStream nya 50 jadi data yang mengalir hanya bisa 50 jika lebih
        //maka harus menunggu dulu sampai data yang lain nyampe ke subscriber baru boleh mengalir data yang menunggu itu
        //ini kita bisa menggunaka ForkJoinPool juga y untuk thread nya contoh Executors.newWorkStealingPool(5);
        var publisher = new SubmissionPublisher<String>(Executors.newFixedThreadPool(5), 50);
        PrintSubscriberFile subscriber = new PrintSubscriberFile("A", 1000L);
        BufferedReader data = new BufferedReader(new FileReader("src/test/bookdata.txt"));

        publisher.subscribe(subscriber);
        executor.execute(() -> {
            try {
                String line;
                while ((line = data.readLine()) != null){
                    publisher.submit(line);
                    System.out.println("Add: "+line+" :"+Thread.currentThread().getName());
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(1, TimeUnit.DAYS);
    }

    /*
Flow memiliki fitur yang bernama Processor yang direpresentasikan dalam interface Processor
Processor singkatnya adalah gabungan antara Publisher dan Subscriber, jadi dia bisa menerima data dari
publisher lain lalu mengirim ke subscriber lain juga dia bisa memodif data tersebut lalu di kirim ke subscriber lain
Processor cocok jika kita ingin memanipulasi data publisher lalu hasilnya dikirim ke subscriber lain
     */
    public static class PrintProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String>{

        private Flow.Subscription subscription;
        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }

        @Override
        public void onNext(String item) {
            try {
                Thread.sleep(500L);
                //memodif datanya
                var data = "Hello "+item;
                //ini cara mengirim data ke subscriber lain
                submit(data);
                //ini kita menerima data
                subscription.request(1);
                System.out.println("Processor: "+item);
                //ini untuk jika kita tidak ingin menerima data lagi dari publisher kita menggunakan cancel method
//            subscription.cancel();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            //kalau Processor harus di close y
            close();
        }
    }

    @Test
    void testProcessor() throws FileNotFoundException, InterruptedException {
        var executor = Executors.newFixedThreadPool(5);
        BufferedReader data = new BufferedReader(new FileReader("src/test/bookdata.txt"));
        //ada publisher mengalirkan data
        var publisher = new SubmissionPublisher<String>();

        //di terima oleh processor atau bisa juga namanya sebagai subscriber
        //lalu processor ini juga mengalirkan data ke subscriber lain
        var processor = new PrintProcessor();
        publisher.subscribe(processor);

        //di terima oleh subscriber
        var subscriber = new PrintSubscriberFile("Subscribe", 500L);
        processor.subscribe(subscriber);

        executor.execute(() -> {
            try {
                String line;
                while ((line = data.readLine()) != null){
                    publisher.submit(line);
                    System.out.println("Add: "+line+" :"+Thread.currentThread().getName());
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

        executor.awaitTermination(1, TimeUnit.DAYS);
    }
}
