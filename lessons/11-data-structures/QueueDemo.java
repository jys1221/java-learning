import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：队列（Queue）—— 先进先出 FIFO
 * ============================================================
 * 一句话：队列只在队尾进、队首出，先排队的先办理，像食堂打饭排队：
 *        排在前面的先走，新来的只能站到队尾。
 *
 * 为什么需要它：
 *   任务调度、消息缓冲、BFS 广度优先遍历、生产者-消费者模型，都要求"按到达顺序处理"，
 *   这种先进先出的场景就是队列的主场。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   入队 enqueue        O(1)      O(1)      循环数组动 tail；链队列动 rear 指针
 *   出队 dequeue        O(1)      O(1)      循环数组动 head；链队列动 front 指针
 *   看队首 peek         O(1)      O(1)
 *   判满（循环数组）     O(1)      O(1)      (tail+1) % cap == head
 *
 * 易错点：
 *   1. 循环队列要牺牲一格空间：tail 指向"下一个可写位置"，满 = (tail+1)%cap == head，
 *      否则"队空 head==tail"和"队满 head==tail"两种状态就分不出来
 *   2. 下标绕回必须取模：tail=(tail+1)%cap、head=(head+1)%cap，不能直接 ++
 *   3. 链式队列删到空时，front 和 rear 都要置 null，否则 rear 会指着已脱离的旧节点
 *   4. 队列长度 = (tail - head + cap) % cap，别直接 tail-head（可能为负）
 *
 * 与 Java 自带类的对应关系：
 *   java.util.Queue 是接口，常见实现有 ArrayDeque（循环数组，推荐）和 LinkedList
 *   （链表）；ArrayDeque 既能当队列也能当栈，性能优于老旧的 java.util.Stack。
 *   本文件手写循环数组队列 + 链式队列，并与 JDK 的 ArrayDeque / LinkedList 比对。
 *
 * 一句话口诀：
 *   队尾进、队首出，先进先出守规矩；循环取模绕一圈，牺牲一格判得清。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 QueueDemo.java
 *   java QueueDemo
 */
public class QueueDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 手写循环队列：判空判满、取模绕回 ==========");
        demoCircularQueue();

        System.out.println();
        System.out.println("========== 2. 链式队列：FIFO 先进先出 ==========");
        demoLinkedQueue();

        System.out.println();
        System.out.println("========== 3. 与 JDK ArrayDeque / LinkedList 对照 + 实测 ==========");
        demoCrossCheckAndBenchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 循环数组队列
    // ---------------------------------------------------------
    private static void demoCircularQueue() {
        // 容量 5：按"牺牲一格"规则，实际最多装 4 个元素
        ArrayCircularQueue queue = new ArrayCircularQueue(5);
        check(queue.isEmpty(), "初始队列为空");
        check(!queue.isFull(), "初始队列未满");

        for (int v : new int[] { 10, 20, 30, 40 }) {
            queue.enqueue(v);
        }
        System.out.println("装入 10,20,30,40 后：" + queue.toList());
        check(queue.size() == 4, "容量 5 实际装 4 个（牺牲一格）");
        check(queue.isFull(), "此时队列已满");

        // 再入队应当被拦下
        boolean rejected = false;
        try {
            queue.enqueue(50);
        } catch (IllegalStateException e) {
            rejected = true;
        }
        check(rejected, "队满再入队被拦下");

        // 出队两个，腾出前面的格子；再入队两个，观察下标绕回队首
        check(queue.dequeue() == 10, "先出队 10");
        check(queue.dequeue() == 20, "再出队 20");
        queue.enqueue(50);
        queue.enqueue(60);
        System.out.println("出队两个再补两个后：" + queue.toList());
        check(queue.toList().equals(List.of(30, 40, 50, 60)), "绕回后顺序为 [30, 40, 50, 60]");
        check(queue.peek() == 30, "队首仍是 30");
    }

    // ---------------------------------------------------------
    // 2. 链式队列
    // ---------------------------------------------------------
    private static void demoLinkedQueue() {
        LinkedQueue queue = new LinkedQueue();
        for (int v : new int[] { 1, 2, 3, 4 }) {
            queue.enqueue(v);
        }
        int[] out = new int[4];
        for (int i = 0; i < 4; i++) {
            out[i] = queue.dequeue();
        }
        System.out.println("链式队列出队顺序：" + java.util.Arrays.toString(out));
        check(java.util.Arrays.equals(out, new int[] { 1, 2, 3, 4 }), "链式队列先进先出，出队顺序与入队一致");
        check(queue.isEmpty(), "弹空后链式队列为空");

        // 空队列出队要安全报错
        boolean rejected = false;
        try {
            queue.dequeue();
        } catch (IllegalStateException e) {
            rejected = true;
        }
        check(rejected, "空队列再出队被拦下");
    }

    // ---------------------------------------------------------
    // 3. JDK 对照 + 实测
    // ---------------------------------------------------------
    private static void demoCrossCheckAndBenchmark() {
        Random rnd = new Random(42);
        ArrayCircularQueue mine = new ArrayCircularQueue(21);
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        LinkedList<Integer> linked = new LinkedList<>();

        // 两边同步入队 20 个随机数
        for (int i = 0; i < 20; i++) {
            int v = rnd.nextInt(100);
            mine.enqueue(v);
            deque.offer(v);
            linked.offer(v);
        }
        // 同步出队 12 个，逐值比对
        for (int i = 0; i < 12; i++) {
            int m = mine.dequeue();
            check(m == deque.poll() && m == linked.poll(), "第 " + i + " 次出队三方一致：" + m);
        }
        check(mine.size() == deque.size() && mine.size() == linked.size(), "剩余元素个数三方相同");

        // 实测：20000 次入队 + 出队
        final int n = 20000;
        long t1 = System.nanoTime();
        ArrayCircularQueue benchMine = new ArrayCircularQueue(n + 1);
        for (int i = 0; i < n; i++) {
            benchMine.enqueue(i);
        }
        while (!benchMine.isEmpty()) {
            benchMine.dequeue();
        }
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        ArrayDeque<Integer> benchJdk = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            benchJdk.offer(i);
        }
        while (!benchJdk.isEmpty()) {
            benchJdk.poll();
        }
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("手写循环队列 " + n + " 次入出队耗时：" + mineMs + " ms");
        System.out.println("ArrayDeque " + n + " 次入出队耗时：" + jdkMs + " ms");
        System.out.println("提示：Queue 是接口，推荐实现是 ArrayDeque；LinkedList 也实现了 Queue 接口。");
    }

    // =========================================================
    // 循环数组队列：head 队首、tail 下一个可写位置，取模绕回
    // =========================================================
    static class ArrayCircularQueue {
        private int[] data;
        private int head;
        private int tail;
        private int capacity;

        ArrayCircularQueue(int capacity) {
            this.capacity = capacity;
            data = new int[capacity];
            head = 0;
            tail = 0;
        }

        /** 队满：tail 再走一格就撞上 head（牺牲一格空间换来判满条件） */
        boolean isFull() {
            return (tail + 1) % capacity == head;
        }

        boolean isEmpty() {
            return head == tail;
        }

        /** 入队：写到 tail，再把 tail 取模后移一格 */
        void enqueue(int value) {
            if (isFull()) {
                throw new IllegalStateException("队列已满，不能入队");
            }
            data[tail] = value;
            tail = (tail + 1) % capacity;
        }

        /** 出队：取 head，再把 head 取模后移一格 */
        int dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("队列为空，不能出队");
            }
            int value = data[head];
            head = (head + 1) % capacity;
            return value;
        }

        int peek() {
            if (isEmpty()) {
                throw new IllegalStateException("队列为空");
            }
            return data[head];
        }

        /** 元素个数：tail-head 可能为负，加上容量再取模 */
        int size() {
            return (tail - head + capacity) % capacity;
        }

        /** 按队首到队尾的顺序导出成 List，便于断言和打印 */
        List<Integer> toList() {
            List<Integer> result = new java.util.ArrayList<>();
            int cur = head;
            for (int i = 0; i < size(); i++) {
                result.add(data[cur]);
                cur = (cur + 1) % capacity;
            }
            return result;
        }
    }

    // =========================================================
    // 链式队列：front 队首出队，rear 队尾入队
    // =========================================================
    static class LinkedQueue {
        static class Node {
            int val;
            Node next;

            Node(int val) {
                this.val = val;
            }
        }

        private Node front;
        private Node rear;
        private int size;

        /** 入队：空队列时 front/rear 都指向新节点，否则挂到 rear 后面 */
        void enqueue(int value) {
            Node node = new Node(value);
            if (front == null) {
                front = rear = node;
            } else {
                rear.next = node;
                rear = node;
            }
            size++;
        }

        /** 出队：取 front，front 后移；删空时 rear 也要置 null */
        int dequeue() {
            if (front == null) {
                throw new IllegalStateException("队列为空，不能出队");
            }
            int value = front.val;
            front = front.next;
            if (front == null) {
                rear = null;
            }
            size--;
            return value;
        }

        boolean isEmpty() {
            return front == null;
        }

        int size() {
            return size;
        }
    }

    // =========================================================
    // 断言小工具：条件不成立就抛异常
    // =========================================================
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
