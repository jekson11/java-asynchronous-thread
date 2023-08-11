package javathread.tutorial.hightlevelconcurrency.forkjoin.studycase;

import javathread.tutorial.ConnectionUtil;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class DataUploaderTest {

    //membuat RecursiveAction
    public static class ForkJoinRecursiveAction extends RecursiveAction{
            final private List<String> data;//field
            public ForkJoinRecursiveAction(List<String> data){//constructor
                this.data = data;
            }

        @Override
        protected void compute() {//pengecekan apakah data sudah sesuai yang kita inginkan
            if (data.size() <= 10){
                doCompute(data);
            }else {
                forkCompute();
            }
        }

        private void doCompute(List<String> data) {
                //kalau data sudah kecil ini di panggil dan di kerjakan di sini
            String sql = "INSERT INTO Book(title, author, isBorrow) VALUES(?, ?, ?)";
            try (Connection connection = ConnectionUtil.getDataSource().getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);
            ){
                //memasukkan data ke Array biasa dari arrayList
                for (String datum : data) {
                    String[] rowData = datum.split(",");//tiap datanya di potong dari koma

                    //karna ada tiga field pada database berarti filenya di potong tiga
                    String title = rowData[0].trim();//file indeks ke 0
                    String author = rowData[1].trim();//file indeks ke 1
                    boolean isBorrow = Boolean.parseBoolean(rowData[2].trim());//file indeks ke 2 di konvert ke boolean

                    //kita menggunakan Batch
                    //kita clear parameternya setiap kali perulangan karna statement itu datanya tidak boleh berubah
                    //oleh karna itu kita clear biar bisa berubah
                    statement.clearParameters();
                    //masukkin data ke tiap parameter yang ada
                    statement.setString(1, title);
                    statement.setString(2, author);
                    statement.setBoolean(3, isBorrow);
//                    statement.executeUpdate();
                    //di kumpulkan terlebih dahulu semuanya sampai selesai
                    statement.addBatch();
                }
                //setelah itu di kirim langsung ke database secara sekaligus
                statement.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private void forkCompute() {
                //memecah data menjadi lebih kecil
            List<String> data1 = this.data.subList(0, data.size()/2);
            List<String> data2 = this.data.subList(this.data.size()/2, data.size());

            //memasukkan data ke class ForkJoinRecursiveAction yang kita bikin
            ForkJoinRecursiveAction task1 = new ForkJoinRecursiveAction(data1);
            ForkJoinRecursiveAction task2 = new ForkJoinRecursiveAction(data2);

            //di eksekusi sekali dua ini kita menggunakan Fork Joint Task
            ForkJoinTask.invokeAll(task1, task2);
        }

    }

    public static List<String> readDataFromFile(String urlData){
        List<String> data = new ArrayList<>();
//        pembacaan data
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(urlData))){
            String line;
            //data pada file di baca per baris bukan perkata
            while ((line = bufferedReader.readLine()) != null){
                //tiap baris di masukkan ke array list yang baru
                data.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //di return data ini lah nanti yang di pakai
        return data;
    }

    @Test
    void testDataUpload() throws InterruptedException {
        var pool = ForkJoinPool.commonPool();
        //ini isinya data dari array yang di atas ya yang kita baca dari file
        List<String> task = readDataFromFile("src/test/bookdata.txt");

        //di eksekusi di sini
        pool.execute(new ForkJoinRecursiveAction(task));
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }



    //ini hanya menggunakan Batch dari
    @Test
    void testDataUploaderToDatabase(){
        String sql = "INSERT INTO Book(title, author, isBorrow) VALUES(?, ?, ?)";
        try (Connection connection = ConnectionUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/bookdata.txt"))
        ){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] data = line.split(",");

                String title = data[0].trim();
                String author = data[1].trim();
                boolean isBorrow = Boolean.parseBoolean(data[2].trim());

                statement.clearParameters();
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setBoolean(3, isBorrow);
//                statement.executeUpdate();
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

