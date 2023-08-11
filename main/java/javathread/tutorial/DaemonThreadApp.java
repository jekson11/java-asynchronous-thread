package javathread.tutorial;
/*
Secara default, saat kita membuat thread, thread tersebut disebut user thread
Java (bukan JUnit) secara default akan selalu menunggu semua user thread selesai sebelum program berhenti
Jika kita mengubah thread menjadi daemon thread, menggunakan setDaemon(true), maka secara otomatis thread tersebut menjadi daemon thread
Daemon thread tidak akan ditunggu jika memang program Java akan berhenti
Namun jika kita menghentikan program Java menggunakan System.exit(), maka user thread pun akan otomatis terhenti

 */
public class DaemonThreadApp {
    public static void main(String[] args) {
        var thread = new Thread( () ->
        {
            try {
                Thread.sleep(2_000L);
                System.out.println("Hello Daemon");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
