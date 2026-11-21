import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 知识点 21：线程安全与线程池
 *
 * 一、线程安全问题怎么来的？
 *   多个线程同时改同一个共享变量时，可能互相覆盖（"读到旧值 → 计算 → 写回"被打断）。
 *   典型症状：1000 个线程各 +1，最后结果不是 1000。
 *
 * 二、三种解决手段：
 *   1. synchronized：加锁，同一时刻只允许一个线程进入（最常用）
 *   2. Atomic 原子类：底层用 CAS 指令，无锁但保证原子性（计数场景首选）
 *   3. 线程池：不是解决线程安全，而是解决"线程太多、频繁创建销毁"的开销问题
 */
public class ThreadSyncDemo {

    // 共享计数器：不加保护的话，多线程自增会丢数
    private static int unsafeCount = 0;
    private static int safeCount = 0;
    private static final AtomicInteger atomicCount = new AtomicInteger(0);
    // synchronized 用的锁对象：任何对象都能当锁，习惯用一个专用的 final 对象
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws Exception {

        int threads = 10;
        int times = 10000;

        // ---------- 一、不安全版本：结果会小于 10000 ----------
        Thread[] unsafeThreads = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    unsafeCount++;      // 这一步不是原子的：读值、加一、写回，三小步可能被打断
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) {
            t.join();
        }
        System.out.println("不安全自增结果：" + unsafeCount + "（期望 " + (threads * times) + "）");

        // ---------- 二、synchronized 同步代码块 ----------
        Thread[] safeThreads = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    synchronized (LOCK) {        // 同一时刻只有一个线程能进这个块
                        safeCount++;
                    }
                }
            });
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) {
            t.join();
        }
        System.out.println("synchronized 结果：" + safeCount + "（期望 " + (threads * times) + "）");

        // ---------- 三、AtomicInteger：无锁原子类 ----------
        Thread[] atomicThreads = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    atomicCount.incrementAndGet();    // 内部用 CAS 保证原子性，比锁更快
                }
            });
            atomicThreads[i].start();
        }
        for (Thread t : atomicThreads) {
            t.join();
        }
        System.out.println("AtomicInteger 结果：" + atomicCount.get() + "（期望 " + (threads * times) + "）");

        // ---------- 四、线程池：避免频繁创建/销毁线程 ----------
        // 每 new 一个 Thread 都要向操作系统申请资源，用完销毁；线程池就是"复用一批线程"
        ExecutorService pool = Executors.newFixedThreadPool(4);   // 固定 4 个线程（常见配置：CPU 核数）

        // submit 提交任务（Runnable）
        for (int i = 1; i <= 8; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("[池] 任务" + taskId + " 由 " + Thread.currentThread().getName() + " 执行");
                sleep(50);
            });
        }

        // submit 也能提交 Callable（有返回值），返回 Future
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for (int i = 1; i <= 100; i++) {
                    sum += i;
                }
                return sum;
            }
        });
        // get() 会阻塞，直到任务算完拿到结果
        System.out.println("[池] Callable 计算结果：" + future.get());

        // 关闭线程池：不再接收新任务，等已提交的跑完
        pool.shutdown();
        // 最多等 5 秒，还没结束就强制关闭
        if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
        System.out.println("线程池已关闭");

        // ---------- 五、几个必须记住的结论 ----------
        // 1. 能用 Atomic 就不用 synchronized；能用局部变量就不用共享变量
        // 2. synchronized 加锁的范围越小越好，否则多线程变串行，白开线程
        // 3. 生产环境永远不要 new Thread 满天飞，要用线程池
        // 4. ConcurrentHashMap 是线程安全的 Map，多线程共享 Map 时用它
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
