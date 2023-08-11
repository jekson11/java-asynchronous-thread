package javathread.tutorial;

public class Balance {
    private Long value = 0L;

    public Balance(Long value){
        this.value = value;
    }

    public void setValue(Long value){
        this.value = value;
    }

    public Long getValue(){
        return this.value;
    }

    //INI CONTOH DEAD LOCK
//    public static void transfer(Balance from, Balance to, Long value) throws InterruptedException {
//        synchronized (from){
//            Thread.sleep(500);
//            from.setValue(from.getValue() - value);
//            synchronized (to){
//                Thread.sleep(500);
//                to.setValue(to.getValue() + value);
//            }
//        }
//    }

    public static void transfer(Balance from, Balance to, Long value) throws InterruptedException {
        synchronized (from){
            Thread.sleep(500);
            from.setValue(from.getValue() - value);
        }
        synchronized (to){
            Thread.sleep(500);
            to.setValue(to.getValue() + value);
        }
    }
}
