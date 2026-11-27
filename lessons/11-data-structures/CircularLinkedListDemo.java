import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：单向循环链表（Circular Singly Linked List）
 * ============================================================
 * 一句话：普通链表尾节点的 next 是 null；循环链表尾节点的 next 指回 head，
 *        于是整条链首尾相接，遍历一圈后会回到起点。
 *
 * 为什么需要它：
 *   约瑟夫环、轮转调度（时间片轮流给每个任务）、循环播放这类"绕一圈又一圈"的场景，
 *   用循环链表最自然：走到尾巴不用特判接回开头，指针天然就是圆的。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   尾部添加 addLast    O(1)      O(1)      配合 tail 指针，接完再把尾 next 指回 head
 *   遍历一圈            O(n)      O(n)      终止条件是"走回 head"，不是 next==null
 *   按值删除            O(n)      O(n)      先找前驱，再改前驱的 next
 *   约瑟夫环模拟        O(nk)     O(nk)     每出列一个要数 k 步
 *
 * 易错点：
 *   1. 遍历终止条件写错会死循环：普通链表判 cur==null，循环链表必须判 cur==head（回到起点）
 *   2. 空链表和单节点链表要单独处理：单节点时它的 next 必须指向自己
 *   3. 尾插后别忘记把新尾节点的 next 指回 head，否则链就断成普通链表
 *   4. 删除 head 或 tail 时，循环关系要一起维护，不能只改普通节点的 next
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有直接提供"循环链表"类；LinkedList 是普通双向链表。循环链表更多是一种
 *   结构思想，工程里常用 ArrayDeque / 环形数组代替。本文件用手写单向循环链表讲清
 *   回指关系与约瑟夫环，并与 JDK LinkedList 模拟的约瑟夫环结果比对。
 *
 * 一句话口诀：
 *   尾指回头成一圈，遍历回到起点停；数到 k 就出列，绕着圈子一直轮。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 CircularLinkedListDemo.java
 *   java CircularLinkedListDemo
 */
public class CircularLinkedListDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 构造循环链表：尾节点回指 head，遍历一圈回到起点 ==========");
        demoBuildAndTraverse();

        System.out.println();
        System.out.println("========== 2. 约瑟夫环：报数出列，与 JDK LinkedList 模拟比对 ==========");
        demoJosephus();

        System.out.println();
        System.out.println("========== 3. 单向循环 vs 双向循环的取舍 + 遍历实测 ==========");
        demoTradeOffAndBenchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 构造与遍历
    // ---------------------------------------------------------
    private static void demoBuildAndTraverse() {
        MyCircularLinkedList circle = new MyCircularLinkedList();
        for (int v : new int[] { 10, 20, 30, 40 }) {
            circle.addLast(v);
        }
        List<Integer> values = circle.toList();
        System.out.println("遍历一圈：" + values);
        check(circle.size() == 4, "节点个数为 4");
        check(values.equals(List.of(10, 20, 30, 40)), "一圈顺序 [10, 20, 30, 40]");

        // 关键验证：尾节点的 next 必须指回 head，而不是 null
        check(circle.tailNextIsHead(), "尾节点的 next 指回 head（循环关系成立）");

        // 单节点特例：自己指向自己
        MyCircularLinkedList one = new MyCircularLinkedList();
        one.addLast(99);
        check(one.toList().equals(List.of(99)), "单节点链表遍历结果只有自己");
        check(one.tailNextIsHead(), "单节点的 next 也指向自己");
    }

    // ---------------------------------------------------------
    // 2. 约瑟夫环
    // ---------------------------------------------------------
    private static void demoJosephus() {
        // n=7 个人编号 1~7，每数到第 3 个出列
        int n = 7;
        int k = 3;
        List<Integer> mine = MyCircularLinkedList.josephus(n, k);
        System.out.println("手写循环链表出列顺序（含最后幸存者）：" + mine);

        // 用 JDK LinkedList 做第二套独立模拟，交叉验证顺序一致
        List<Integer> jdk = josephusWithJdk(n, k);
        System.out.println("JDK LinkedList 模拟出列顺序：" + jdk);
        check(mine.equals(jdk), "两套实现的出列顺序完全一致");
        check(mine.equals(List.of(3, 6, 2, 7, 5, 1, 4)), "经典结果：3,6,2,7,5,1 出列，最后幸存者 4");
        check(mine.get(mine.size() - 1) == 4, "最后留下的幸存者是 4");

        // 再来一组小例子核对：n=5, k=2
        List<Integer> mine2 = MyCircularLinkedList.josephus(5, 2);
        List<Integer> jdk2 = josephusWithJdk(5, 2);
        System.out.println("n=5,k=2 出列顺序：" + mine2);
        check(mine2.equals(jdk2), "n=5,k=2 两套实现也一致");
    }

    // ---------------------------------------------------------
    // 3. 取舍讲解 + 遍历实测
    // ---------------------------------------------------------
    private static void demoTradeOffAndBenchmark() {
        System.out.println("取舍说明：");
        System.out.println("  · 单向循环：每个节点只存 next，省一半指针；但只能顺着一个方向走。");
        System.out.println("  · 双向循环：prev/next 都有，正反都能走、删除更方便；代价是每个节点多一个指针。");
        System.out.println("  · 工程里若只是轮转调度，环形数组（见 QueueDemo）往往比链表更省内存、缓存更友好。");

        // 固定随机种子造一批数据，实测遍历一圈的耗时
        Random rnd = new Random(42);
        final int n = 20000;
        MyCircularLinkedList circle = new MyCircularLinkedList();
        for (int i = 0; i < n; i++) {
            circle.addLast(rnd.nextInt(1000));
        }
        long t1 = System.nanoTime();
        List<Integer> snapshot = circle.toList();
        long ms = (System.nanoTime() - t1) / 1_000_000;
        System.out.println("遍历 " + n + " 个节点一圈耗时：" + ms + " ms");
        check(snapshot.size() == n, "遍历一圈拿到的节点数为 " + n);
        check(circle.tailNextIsHead(), "大链表尾节点仍指回 head");
    }

    // =========================================================
    // 手写单向循环链表
    // =========================================================
    static class MyCircularLinkedList {

        static class Node {
            int label;
            Node next;

            Node(int label) {
                this.label = label;
                this.next = null;
            }
        }

        private Node head;
        private Node tail;
        private int size;

        MyCircularLinkedList() {
            head = null;
            tail = null;
            size = 0;
        }

        /** 尾插：接到 tail 后面，再把新尾的 next 指回 head */
        void addLast(int label) {
            Node node = new Node(label);
            if (head == null) {
                // 空链表：唯一节点的 next 指向自己
                head = tail = node;
                node.next = node;
            } else {
                tail.next = node;
                node.next = head;
                tail = node;
            }
            size++;
        }

        /** 遍历一圈：do-while 先走一步，再判断是否回到 head，避免一上来就被起点挡住 */
        List<Integer> toList() {
            List<Integer> result = new ArrayList<>();
            if (head == null) {
                return result;
            }
            Node cur = head;
            do {
                result.add(cur.label);
                cur = cur.next;
            } while (cur != head);
            return result;
        }

        /** 校验尾节点的 next 是否指回 head */
        boolean tailNextIsHead() {
            return head != null && tail.next == head;
        }

        int size() {
            return size;
        }

        /**
         * 约瑟夫环：n 个人编号 1..n，从 1 开始报数，数到 k 的出列，直到剩最后一人。
         * 返回值是出列顺序，最后一个元素是幸存者。
         */
        static List<Integer> josephus(int n, int k) {
            MyCircularLinkedList circle = new MyCircularLinkedList();
            for (int i = 1; i <= n; i++) {
                circle.addLast(i);
            }
            List<Integer> order = new ArrayList<>();
            Node cur = circle.head;

            while (circle.size > 1) {
                // 从当前起点数 k-1 步，cur 走到第 k 个，即本轮待删者
                for (int step = 1; step < k; step++) {
                    cur = cur.next;
                }
                order.add(cur.label);

                // 先找到待删者的前驱，才能把前驱的 next 跨过它
                Node prev = circle.head;
                while (prev.next != cur) {
                    prev = prev.next;
                }
                prev.next = cur.next;
                if (cur == circle.head) {
                    circle.head = cur.next;
                }
                if (cur == circle.tail) {
                    circle.tail = prev;
                }
                cur = cur.next; // 下一轮从被删者的下一个人继续报数
                circle.size--;
            }
            order.add(circle.head.label); // 最后剩下的幸存者
            return order;
        }
    }

    /** 用 JDK LinkedList 独立模拟约瑟夫环，作为交叉验证 */
    private static List<Integer> josephusWithJdk(int n, int k) {
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            queue.add(i);
        }
        List<Integer> order = new ArrayList<>();
        int index = 0;
        while (!queue.isEmpty()) {
            // 每轮数 k 步：下标向后挪 k-1，取模绕回开头
            index = (index + k - 1) % queue.size();
            order.add(queue.remove(index));
        }
        return order;
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
