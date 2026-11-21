class MyThread extends Thread{
    @Override
    public void run() {
        for(int i=1;i<=5;i++){
            System.out.println("子线程打印:"+i);
        }
    }
}





public class ThreadTest {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        for(int i=1;i<=5;i++){
            System.out.println("主线程打印:"+i);
        }
    }
}
