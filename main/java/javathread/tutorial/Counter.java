package javathread.tutorial;

public class Counter {
    private static Long value = 0L;

    public void increment(){
        value++;
    }

    public long getValue(){
        return value;
    }
}
