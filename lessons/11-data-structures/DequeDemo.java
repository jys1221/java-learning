import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * 知识点：双端队列（Deque, Double-Ended Queue）
 * ============================================================
 * 一句话：普通队列只能队尾进、队首出；双端队列两头都能进、两头都能出，
 *        所以它一个结构既能当栈用，也能当队列用。
 *
 * 为什么需要它：
 *   栈和队列本质都是"受限的双端队列"：栈只在一端进出，队列一端进另一端出。
 *   有了统一的双端队列底座，ArrayDeque 才能同时充当栈、队列、单调队列三种角色，
 *   滑动窗口最大值这类问题也靠它高效解决。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   两端添加 addFirst/Last  O(1)  O(1)  只动 head 或 tail 指针
 *   两端删除 removeFirst/Last O(1) O(1)
 *   看两端 peek         O(1)      O(1)
 *   单调队列求窗口最大值  O(n)    O(n)    每个下标最多入队、出队各一次
 *
 * 易错点：
 *   1. 下标绕回都要取模：head 前移是 (head-1+cap)%cap，别写成 head-1 出现负数
 *   2. 判满同样牺牲一格：(tail+1)%cap == head；判空 head==tail
 *   3. 单调队列里存的是"下标"而不是值，靠下标判断是否滑出窗口、再用下标取原值
 *   4. 维护单调递减：队尾 <= 当前值的下标都要弹走，否则队首就不是窗口最大值
 *
 * 与 Java 自带类的对应关系：
 *   java.util.Deque 是接口，推荐实现是 ArrayDeque（循环数组）；LinkedList 也实现了 Deque。
 *   JDK 官方建议：需要栈就用 ArrayDeque 的 push/pop，需要队列就用 offer/poll，
 *   不要再用遗留的 java.util.Stack。本文件手写数组双端队列，并实现单调队列演示。
 *
 * 一句话口诀：
 *   双端两头都开关，能当栈来能当队；单调队列存下标，队首永远是最大。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 DequeDemo.java
 *   java DequeDemo
 */
public class DequeDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 手写数组双端队列：两端增删 ==========");
        demoHandwrittenDeque();

        System.out.println();
        System.out.println("========== 2. ArrayDeque 统一底座：当栈用、当队列用 ==========");
        demoArrayDequeAsStackAndQueue();

        System.out.println();
        System.out.println("========== 3. 单调队列：定长滑动窗口最大值 ==========");
        demoSlidingWindowMaximum();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 手写双端队列
    // ---------------------------------------------------------
    private static void demoHandwrittenDeque() {
        ArrayDequeHandwritten deque = new ArrayDequeHandwritten(8);

        // 先队尾加 20、30，再队头加 10、队头加 5
        deque.addLast(20);
        deque.addLast(30);
        deque.addFirst(10);
        deque.addFirst(5);
        System.out.println("两端插入后：" + deque.toList());
        check(deque.toList().equals(List.of(5, 10, 20, 30)), "顺序为 [5, 10, 20, 30]");
        check(deque.peekFirst() == 5 && deque.peekLast() == 30, "队首 5、队尾 30");

        // 两端各删一个
        check(deque.removeFirst() == 5, "队首删除 5");
        check(deque.removeLast() == 30, "队尾删除 30");
        System.out.println("两端删除后：" + deque.toList());
        check(deque.toList().equals(List.of(10, 20)), "剩 [10, 20]");

        // 再往队头、队尾各补一个，验证下标绕回
        deque.addFirst(1);
        deque.addLast(99);
        System.out.println("补齐后：" + deque.toList());
        check(deque.toList().equals(List.of(1, 10, 20, 99)), "绕回后顺序 [1, 10, 20, 99]");
        check(deque.size() == 4, "元素个数为 4");
    }

    // ---------------------------------------------------------
    // 2. ArrayDeque 一身二用
    // ---------------------------------------------------------
    private static void demoArrayDequeAsStackAndQueue() {
        // 当栈用：push 入栈顶、pop 出栈顶，后进先出
        Deque<Integer> asStack = new ArrayDeque<>();
        asStack.push(1);
        asStack.push(2);
        asStack.push(3);
        List<Integer> stackOut = new ArrayList<>();
        while (!asStack.isEmpty()) {
            stackOut.add(asStack.pop());
        }
        System.out.println("ArrayDeque 当栈，出栈顺序：" + stackOut);
        check(stackOut.equals(List.of(3, 2, 1)), "栈用法后进先出 3,2,1");

        // 当队列用：offer 入队尾、poll 出队首，先进先出
        Deque<Integer> asQueue = new ArrayDeque<>();
        asQueue.offer(1);
        asQueue.offer(2);
        asQueue.offer(3);
        List<Integer> queueOut = new ArrayList<>();
        while (!asQueue.isEmpty()) {
            queueOut.add(asQueue.poll());
        }
        System.out.println("ArrayDeque 当队列，出队顺序：" + queueOut);
        check(queueOut.equals(List.of(1, 2, 3)), "队列用法先进先出 1,2,3");

        // 固定随机种子造数据，手写版与 JDK ArrayDeque 逐值比对
        Random rnd = new Random(42);
        ArrayDequeHandwritten mine = new ArrayDequeHandwritten(31);
        ArrayDeque<Integer> jdk = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            int v = rnd.nextInt(100);
            if (i % 2 == 0) {
                mine.addLast(v);
                jdk.offerLast(v);
            } else {
                mine.addFirst(v);
                jdk.offerFirst(v);
            }
        }
        check(mine.size() == jdk.size(), "两边元素个数相同");
        List<Integer> mineList = mine.toList();
        List<Integer> jdkList = new ArrayList<>(jdk);
        check(mineList.equals(jdkList), "手写双端队列与 JDK ArrayDeque 序列一致");
    }

    // ---------------------------------------------------------
    // 3. 滑动窗口最大值（单调队列）
    // ---------------------------------------------------------
    private static void demoSlidingWindowMaximum() {
        int[] nums = { 1, 3, -1, -3, 5, 3, 6, 7 };
        int k = 3;
        int[] result = maxSlidingWindow(nums, k);
        System.out.println("数组：" + java.util.Arrays.toString(nums) + "，窗口 k=" + k);
        System.out.println("各窗口最大值：" + java.util.Arrays.toString(result));
        check(java.util.Arrays.equals(result, new int[] { 3, 3, 5, 5, 6, 7 }), "窗口最大值结果正确");

        // 暴力法交叉验证：对每个窗口直接扫一遍取最大
        int[] brute = bruteForceMax(nums, k);
        check(java.util.Arrays.equals(result, brute), "单调队列结果与暴力法一致");

        // 规模实测：20000 个元素、窗口大小 100
        final int n = 20000;
        int[] big = new Random(42).ints(n, 0, 1000).toArray();
        long t1 = System.nanoTime();
        int[] bigResult = maxSlidingWindow(big, 100);
        long ms = (System.nanoTime() - t1) / 1_000_000;
        System.out.println("规模 " + n + " 的窗口最大值计算耗时：" + ms + " ms，结果长度 " + bigResult.length);
        check(bigResult.length == n - 100 + 1, "结果长度符合窗口个数");
    }

    /**
     * 单调队列求滑动窗口最大值：
     * 队列里存下标，对应的值从头到尾单调递减；队首就是当前窗口最大值。
     */
    private static int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        List<Integer> out = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            // 1. 队首下标已滑出窗口（<= i-k），移出
            while (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            // 2. 队尾对应值 <= 当前值，不可能再成为最大值，弹走，保持递减
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) {
                dq.pollLast();
            }
            dq.offerLast(i);
            // 3. 窗口形成后（下标 i >= k-1），队首就是本窗口最大值
            if (i >= k - 1) {
                out.add(nums[dq.peekFirst()]);
            }
        }
        return out.stream().mapToInt(Integer::intValue).toArray();
    }

    /** 暴力法：每个窗口直接扫描取最大，用于交叉验证 */
    private static int[] bruteForceMax(int[] nums, int k) {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i + k <= nums.length; i++) {
            int max = nums[i];
            for (int j = i; j < i + k; j++) {
                max = Math.max(max, nums[j]);
            }
            out.add(max);
        }
        return out.stream().mapToInt(Integer::intValue).toArray();
    }

    // =========================================================
    // 手写数组双端队列：head 队首、tail 下一个队尾可写位置
    // =========================================================
    static class ArrayDequeHandwritten {
        private int[] data;
        private int head;
        private int tail;
        private int capacity;

        ArrayDequeHandwritten(int capacity) {
            this.capacity = capacity;
            data = new int[capacity];
            head = 0;
            tail = 0;
        }

        boolean isFull() {
            return (tail + 1) % capacity == head;
        }

        boolean isEmpty() {
            return head == tail;
        }

        /** 队头添加：head 先取模后退一格，再写入 */
        void addFirst(int value) {
            if (isFull()) {
                throw new IllegalStateException("双端队列已满");
            }
            head = (head - 1 + capacity) % capacity;
            data[head] = value;
        }

        /** 队尾添加：写到 tail，tail 取模后移 */
        void addLast(int value) {
            if (isFull()) {
                throw new IllegalStateException("双端队列已满");
            }
            data[tail] = value;
            tail = (tail + 1) % capacity;
        }

        /** 队头删除：取 head，head 后移 */
        int removeFirst() {
            if (isEmpty()) {
                throw new IllegalStateException("双端队列为空");
            }
            int value = data[head];
            head = (head + 1) % capacity;
            return value;
        }

        /** 队尾删除：tail 先取模后退一格，再取值 */
        int removeLast() {
            if (isEmpty()) {
                throw new IllegalStateException("双端队列为空");
            }
            tail = (tail - 1 + capacity) % capacity;
            return data[tail];
        }

        int peekFirst() {
            if (isEmpty()) {
                throw new IllegalStateException("双端队列为空");
            }
            return data[head];
        }

        int peekLast() {
            if (isEmpty()) {
                throw new IllegalStateException("双端队列为空");
            }
            return data[(tail - 1 + capacity) % capacity];
        }

        int size() {
            return (tail - head + capacity) % capacity;
        }

        List<Integer> toList() {
            List<Integer> result = new ArrayList<>();
            int cur = head;
            for (int i = 0; i < size(); i++) {
                result.add(data[cur]);
                cur = (cur + 1) % capacity;
            }
            return result;
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
