/**
 * 知识点 20：线程基础
 *
 * 进程 = 一个正在运行的程序（比如一个 Java 程序）
 * 线程 = 进程里的"执行流"，一个进程可以有很多线程同时干活
 *
 * 为什么要多线程？
 *   比如下载 3 个文件，串行要 3 秒；开 3 个线程并行只需要 1 秒左右。
 *
 * 创建线程的两种方式（现在更推荐第二种 + 线程池）：
 *   1. 继承 Thread，重写 run()
 *   2. 实现 Runnable，交给 Thread 执行
 *
 * 重要区别：start() 才是"启动新线程"；直接调用 run() 只是普通方法调用，还是在主线程跑。
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("主线程名字：" + Thread.currentThread().getName());

        // ---------- 一、继承 Thread ----------
        MyThread t1 = new MyThread("线程A");
        t1.start();          // 真正启动新线程，run() 会在新线程里执行
        // t1.run();         // 错误示范：这只是普通方法调用，不会开新线程

        // ---------- 二、实现 Runnable（更常用，因为不占用继承名额） ----------
        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Runnable 任务执行：" + i);
                    sleep(100);
                }
            }
        };
        Thread t2 = new Thread(task, "线程B");
        t2.start();

        // Lambda 写法更简洁（Runnable 是函数式接口）
        Thread t3 = new Thread(() -> System.out.println("Lambda 线程在跑：" + Thread.currentThread().getName()), "线程C");
        t3.start();

        // ---------- 三、sleep：让当前线程睡一会儿 ----------
        // 单位毫秒。会抛 InterruptedException（受检异常），必须处理
        // 注意：sleep 不会释放锁（这点和 wait 不同，后面学锁时会细说）

        // ---------- 四、join：等另一个线程结束 ----------
        // 主线程在这里等待 t1 执行完，才继续往下走
        t1.join();
        System.out.println("t1 已经结束了，主线程继续");

        // ---------- 五、线程的常用方法 ----------
        Thread current = Thread.currentThread();
        System.out.println("当前线程：" + current.getName() + "，优先级：" + current.getPriority());
        System.out.println("是否存活：" + t2.isAlive());

        // 守护线程：主线程结束时它会被强制结束（比如 GC 线程）
        Thread daemon = new Thread(() -> {
            while (true) {
                sleep(1000);
            }
        }, "守护线程");
        daemon.setDaemon(true);      // 必须在 start 之前设置
        daemon.start();

        // ---------- 六、演示：并发执行确实更快 ----------
        long start = System.currentTimeMillis();

        // 串行：三次 300ms 的任务，约 900ms
        for (int i = 1; i <= 3; i++) {
            sleep(300);
        }
        long serialCost = System.currentTimeMillis() - start;
        System.out.println("串行耗时：" + serialCost + "ms");

        start = System.currentTimeMillis();
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {
            threads[i] = new Thread(() -> sleep(300), "并发" + i);
            threads[i].start();          // 三个线程几乎同时开始睡
        }
        for (Thread t : threads) {
            t.join();                    // 等三个都结束
        }
        long parallelCost = System.currentTimeMillis() - start;
        System.out.println("并行耗时：" + parallelCost + "ms（约为串行的 1/3）");
    }

    /**
     * 封装 sleep，把受检异常在内部处理掉，方便调用
     * 注意：捕获 InterruptedException 后要恢复中断标志，这是规范做法
     */
    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();   // 恢复中断状态，让上层知道被中断过
        }
    }
}

/**
 * 继承 Thread 的方式
 * 缺点：Thread 类的方法很多，继承后就"占用了继承名额"，不能再继承别的类
 */
class MyThread extends Thread {

    private final String tag;

    public MyThread(String tag) {
        this.tag = tag;
        setName(tag);          // 设置线程名字，方便排查问题
    }

    /**
     * run() 是线程真正干活的地方，由 JVM 在新线程里调用
     * 不要手动调用 run()，要调用 start()
     */
    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(tag + " 执行第 " + i + " 次，当前线程：" + Thread.currentThread().getName());
            ThreadDemo.sleep(100);
        }
    }
}
