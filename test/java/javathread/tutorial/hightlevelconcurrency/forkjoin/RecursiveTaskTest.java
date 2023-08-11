package javathread.tutorial.hightlevelconcurrency.forkjoin;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class RecursiveTaskTest {
    //pembuatan RecursiveTask kita extends RecursiveTask penggunaan nya hampir sama saja seperti
    //RecursiveAction cuma bedanya ini ibarat Callable yang memiliki return sedangkan
    //RecursiveTask ini seperti Runnable dia tidak memiliki return
    public static class SimpleForkJoinTaskRecursiveTask extends RecursiveTask<Long> {

        final private List<Integer> data;

        public SimpleForkJoinTaskRecursiveTask(List<Integer> data) {
            this.data = data;
        }

        @Override
        protected Long compute() {
            if (data.size() <= 10){
                return doCompute();
            }else {
                return forkCompute();
            }
        }

        private Long forkCompute() {
            var data1 = this.data.subList(0, this.data.size()/2);
            var data2 = this.data.subList(this.data.size()/2, this.data.size());

            SimpleForkJoinTaskRecursiveTask task1 = new SimpleForkJoinTaskRecursiveTask(data1);
            SimpleForkJoinTaskRecursiveTask task2 = new SimpleForkJoinTaskRecursiveTask(data2);

            ForkJoinTask.invokeAll(task1, task2);

            //Karena callable
            return task1.join() + task2.join();
        }

        private Long doCompute() {
            return data.stream().mapToLong(value -> value).peek(value -> {
                System.out.println(Thread.currentThread().getName()+" : "+value);
            }).sum();
        }
    }

    @Test
    void testRecursiveTask() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        List<Integer> data = IntStream.range(1, 1000).boxed().toList();

        var task = new SimpleForkJoinTaskRecursiveTask(data);
        ForkJoinTask<Long> submit = forkJoinPool.submit(task);

        Long aLong = submit.get();
        System.out.println(aLong);
    }
}
