package javathread.tutorial;

public class UserService {
    /*
ThreadLocal merupakan fitur di Java untuk menyimpan data
ThreadLocal memberi kita kemampuan untuk menyimpan data yang hanya bisa digunakan di thread tersebut
Tiap thread akan memiliki data yang berbeda dan tidak saling terhubung antar thread
     */
    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    //jika kita menggunakan string biasasetiap thread nilainya sama
//    String threadLocal;
    public void setUser(String user){
//        this.threadLocal = user;
        //untuk mengatur data thread
        threadLocal.set(user);
    }

    public void doAction(){
        //ini untuk mengeluarkan data pada thread tersebut
        String data = threadLocal.get();
        System.out.println(data+" Do action");
//        System.out.println(threadLocal+" Do action");
    }
}
