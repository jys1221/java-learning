import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：单链表（Singly Linked List）
 * ============================================================
 * 一句话：链表不是一排连续格子，而是一个个节点用 next 指针串起来；
 *        每个节点只知道下一个是谁，所以"头插"很快，"按位置访问"很慢。
 *
 * 为什么需要它：
 *   数组中间插入/删除要整体搬家，代价 O(n)；链表只要改几个指针就能完成中间增删，
 *   也不需要事先知道容量、不要求连续内存。代价是失去下标随机访问，查找只能从头一个个走。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   头部添加 addFirst   O(1)      O(1)      新建节点接上 head 即可
 *   尾部添加 addLast    O(n)      O(n)      单链表要走到最后一个；带 tail 指针才是 O(1)（见双向链表文件）
 *   按值删除 remove     O(n)      O(n)      得先从头找到它在哪
 *   按下标访问 get(i)    O(n)      O(n)      没有下标，只能顺指针一步步走
 *   反转 reverse        O(n)      O(n)      迭代版与递归版都只走一遍
 *
 * 易错点：
 *   1. 删除 head 必须先更新 head；删中间节点要拿"前驱节点"才能改前驱的 next
 *   2. 反转链表时先存好 next 再改 cur.next，否则指针一改，后面的节点就找不到了
 *   3. 递归反转的终止条件是 head==null 或 head.next==null；返回值就是原来的尾节点（新头）
 *   4. 判环用快慢指针即可；另开 visited 集合会额外占 O(n) 空间
 *   5. 找中点：快指针两步、慢指针一步；链表长度奇偶不同，慢指针落点要自己想清楚
 *
 * 与 Java 自带类的对应关系：
 *   java.util.LinkedList 底层其实是"双向链表"（首尾增删都 O(1)），不是本文件这种单链表；
 *   本文件的 MySinglyLinkedList 是最小化单向版本，把节点、指针、反转、判环这些基本功讲透，
 *   并与 JDK 的 LinkedList 逐元素比对结果一致。
 *
 * 一句话口诀：
 *   链表靠指针串起一串节点，头插 O(1) 查找 O(n)；反转先存 next，判环用快慢指针。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 SinglyLinkedListDemo.java
 *   java SinglyLinkedListDemo
 */
public class SinglyLinkedListDemo {

    /** 断言通过计数，最后统一打印，让人一眼看到"到底验了多少条" */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 手写单链表：头插、尾插、按值删除 ==========");
        demoBasicOperations();

        System.out.println();
        System.out.println("========== 2. 反转（迭代 + 递归）、快慢指针找中点、判环 ==========");
        demoReverseMiddleCycle();

        System.out.println();
        System.out.println("========== 3. 合并两个有序链表；与 JDK LinkedList 逐元素比对 ==========");
        demoMergeAndCrossCheck();

        System.out.println();
        System.out.println("========== 4. 实测：链表头插 vs ArrayList 头插的代价 ==========");
        demoInsertCost();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 基本操作
    // ---------------------------------------------------------
    private static void demoBasicOperations() {
        MySinglyLinkedList list = new MySinglyLinkedList();

        // 先尾插三个，再头插一个：头插的元素会排在最前面
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addFirst(5);
        System.out.println("链表内容：" + list.toList());
        check(list.size() == 4, "添加 4 个元素后 size=4");
        check(list.toList().equals(List.of(5, 10, 20, 30)), "顺序应为 [5, 10, 20, 30]");

        // 按值删除中间节点 20：要先找到它的前驱 10，再把 10 的 next 接到 30
        boolean removedMid = list.removeByValue(20);
        System.out.println("删除 20 后：" + list.toList());
        check(removedMid && list.toList().equals(List.of(5, 10, 30)), "删掉中间的 20 后剩 [5, 10, 30]");

        // 删除头节点 5：此时必须先把 head 前移，否则整条链就丢了
        boolean removedHead = list.removeByValue(5);
        System.out.println("删除头节点 5 后：" + list.toList());
        check(removedHead && list.toList().equals(List.of(10, 30)), "删掉头节点后 head 前移，剩 [10, 30]");

        // 删除不存在的值：返回 false，链表不动
        boolean removedMiss = list.removeByValue(999);
        check(!removedMiss && list.size() == 2, "删除不存在的 999 返回 false，元素数不变");
    }

    // ---------------------------------------------------------
    // 2. 反转、中点、判环
    // ---------------------------------------------------------
    private static void demoReverseMiddleCycle() {
        // --- 迭代版反转 ---
        MySinglyLinkedList a = new MySinglyLinkedList();
        for (int v : new int[] { 1, 2, 3, 4, 5 }) {
            a.addLast(v);
        }
        List<Integer> before = new ArrayList<>(a.toList());
        a.reverseIterative();
        System.out.println("迭代反转前：" + before + "  反转后：" + a.toList());

        // 用 JDK 的 Collections.reverse 做交叉验证：反转结果应当与它一致
        List<Integer> expected = new ArrayList<>(before);
        Collections.reverse(expected);
        check(a.toList().equals(expected), "迭代反转结果与 JDK Collections.reverse 一致");

        // --- 递归版反转 ---
        MySinglyLinkedList b = new MySinglyLinkedList();
        for (int v : new int[] { 1, 2, 3, 4, 5 }) {
            b.addLast(v);
        }
        b.reverseRecursive();
        System.out.println("递归反转后：" + b.toList());
        check(b.toList().equals(expected), "递归反转结果也与 JDK 反转一致");

        // --- 快慢指针找中点 ---
        // 奇数长度 5：中点应是 3；偶数长度 4：快指针先到底，慢指针落在第 3 个（1,2,3,4 中的 3）
        MySinglyLinkedList odd = new MySinglyLinkedList();
        for (int v : new int[] { 1, 2, 3, 4, 5 }) {
            odd.addLast(v);
        }
        MySinglyLinkedList even = new MySinglyLinkedList();
        for (int v : new int[] { 1, 2, 3, 4 }) {
            even.addLast(v);
        }
        System.out.println("奇数长度中点：" + odd.middleValue() + "；偶数长度慢指针落点：" + even.middleValue());
        check(odd.middleValue() == 3, "长度 5 的中点是 3");
        check(even.middleValue() == 3, "长度 4 时慢指针落在 3");

        // --- 判环：先造一条无环链，再把尾节点接回中间节点形成环 ---
        check(!odd.hasCycle(), "普通链表没有环");
        MySinglyLinkedList.Node tail = odd.head();
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = odd.head().next; // 尾节点回指第二个节点，形成环
        check(odd.hasCycle(), "尾节点接回中间节点后能被快慢指针判出环");
    }

    // ---------------------------------------------------------
    // 3. 合并有序链表 + 与 JDK 比对
    // ---------------------------------------------------------
    private static void demoMergeAndCrossCheck() {
        // 两条各自有序的链，合并后仍应整体有序
        MySinglyLinkedList left = new MySinglyLinkedList();
        for (int v : new int[] { 1, 3, 5, 7 }) {
            left.addLast(v);
        }
        MySinglyLinkedList right = new MySinglyLinkedList();
        for (int v : new int[] { 2, 4, 6 }) {
            right.addLast(v);
        }
        MySinglyLinkedList merged = MySinglyLinkedList.mergeSorted(left.head(), right.head());
        System.out.println("合并结果：" + merged.toList());
        check(merged.toList().equals(List.of(1, 2, 3, 4, 5, 6, 7)), "两条有序链表合并为 [1..7]");

        // 交叉验证：固定随机种子造一批数据，同时塞进手写链表和 JDK LinkedList，逐元素比对
        Random rnd = new Random(42);
        MySinglyLinkedList mine = new MySinglyLinkedList();
        LinkedList<Integer> jdk = new LinkedList<>();
        for (int i = 0; i < 30; i++) {
            int v = rnd.nextInt(100);
            mine.addLast(v);
            jdk.addLast(v);
        }
        // 两边都删掉三个随机位置附近的值，再比对剩余序列
        mine.removeByValue(mine.toList().get(0));
        jdk.removeFirst();
        mine.removeByValue(mine.toList().get(mine.size() - 1));
        jdk.removeLast();

        List<Integer> mineList = mine.toList();
        check(mineList.size() == jdk.size(), "两边删除后元素个数相同");
        for (int i = 0; i < mineList.size(); i++) {
            check(mineList.get(i).equals(jdk.get(i)), "下标 " + i + " 两边值一致：" + mineList.get(i));
        }
        System.out.println("手写链表与 JDK LinkedList 已逐元素比对完毕。");
    }

    // ---------------------------------------------------------
    // 4. 性能实测：头插代价
    // ---------------------------------------------------------
    private static void demoInsertCost() {
        final int n = 20000;

        // 手写链表头插：只改两个指针，理论上 O(1) 一次
        long t1 = System.nanoTime();
        MySinglyLinkedList linkHead = new MySinglyLinkedList();
        for (int i = 0; i < n; i++) {
            linkHead.addFirst(i);
        }
        long linkMs = (System.nanoTime() - t1) / 1_000_000;

        // ArrayList 头插：每插一个，后面全部后移一格，总量 O(n²)
        long t2 = System.nanoTime();
        ArrayList<Integer> arrayHead = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            arrayHead.add(0, i);
        }
        long arrayMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("链表头插 " + n + " 次耗时：" + linkMs + " ms");
        System.out.println("ArrayList 头插 " + n + " 次耗时：" + arrayMs + " ms");
        check(linkHead.size() == n && arrayHead.size() == n, "两种结构元素个数都对");
        check(linkHead.toList().get(0) == n - 1, "链表头插后第一个元素是最后插入的 n-1");
        System.out.println("结论：头插是链表的主场（O(1)），数组头插要不断搬家（O(n) 每次）。");
    }

    // =========================================================
    // 手写单链表：Node + head 指针 + 基本操作
    // =========================================================
    static class MySinglyLinkedList {

        /** 节点：存一个整数值，以及指向下一个节点的引用 */
        static class Node {
            int val;
            Node next;

            Node(int val) {
                this.val = val;
                this.next = null;
            }
        }

        /** 头指针：永远指向第一个节点；空链表时为 null */
        private Node head;

        /** 元素个数，避免每次都要遍历数一遍 */
        private int size;

        MySinglyLinkedList() {
            head = null;
            size = 0;
        }

        /** 头插：新节点的 next 指向老 head，再把 head 换成新节点 */
        void addFirst(int value) {
            Node node = new Node(value);
            node.next = head;
            head = node;
            size++;
        }

        /** 尾插：单链表没有 tail，必须从头走到最后一个节点再接上 */
        void addLast(int value) {
            Node node = new Node(value);
            if (head == null) {
                head = node;
            } else {
                Node cur = head;
                while (cur.next != null) {
                    cur = cur.next;
                }
                cur.next = node;
            }
            size++;
        }

        /** 删除第一个值等于 value 的节点；删到了返回 true */
        boolean removeByValue(int value) {
            if (head == null) {
                return false;
            }
            // 情况一：删的是头节点，直接把 head 前移
            if (head.val == value) {
                head = head.next;
                size--;
                return true;
            }
            // 情况二：删中间/尾部节点，用 prev 找到待删节点的前驱
            Node prev = head;
            while (prev.next != null) {
                if (prev.next.val == value) {
                    prev.next = prev.next.next; // 前驱跳过待删节点，待删节点自然脱离
                    size--;
                    return true;
                }
                prev = prev.next;
            }
            return false;
        }

        /** 迭代反转：prev 在前、cur 在后，逐个把 next 反向指回 prev */
        Node reverseIterative() {
            Node prev = null;
            Node cur = head;
            while (cur != null) {
                Node next = cur.next; // 关键：先存下一个，否则改完指针就丢了
                cur.next = prev;
                prev = cur;
                cur = next;
            }
            head = prev; // 循环结束 prev 就是原尾节点，即新头
            return head;
        }

        /** 递归反转入口 */
        Node reverseRecursive() {
            head = reverseRec(head);
            return head;
        }

        /** 递归版：先把后面一整段反转好，再把当前节点挂到反转段的尾巴上 */
        private Node reverseRec(Node node) {
            if (node == null || node.next == null) {
                return node; // 终止：空链或只剩一个节点，它就是新头
            }
            Node newHead = reverseRec(node.next); // 后面一段反转后的新头
            node.next.next = node;                // 后继节点反向指回自己
            node.next = null;                     // 自己作为新尾，断开旧的 next
            return newHead;
        }

        /** 快慢指针找中点：快两步、慢一步，快指针到底时慢指针即中点 */
        int middleValue() {
            Node slow = head;
            Node fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow.val;
        }

        /** 快慢指针判环：两指针若能相遇就一定有环，快指针先到 null 则无环 */
        boolean hasCycle() {
            Node slow = head;
            Node fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    return true;
                }
            }
            return false;
        }

        /** 合并两条有序链表（升序），返回新链表 */
        static MySinglyLinkedList mergeSorted(Node a, Node b) {
            Node dummy = new Node(0); // 哑节点，省去"头节点是谁"的特判
            Node tail = dummy;
            while (a != null && b != null) {
                if (a.val <= b.val) {
                    tail.next = a;
                    a = a.next;
                } else {
                    tail.next = b;
                    b = b.next;
                }
                tail = tail.next;
            }
            // 还有剩余的一条直接接到尾巴上，它本身已经有序
            tail.next = (a != null) ? a : b;

            MySinglyLinkedList result = new MySinglyLinkedList();
            result.head = dummy.next;
            int count = 0;
            for (Node cur = result.head; cur != null; cur = cur.next) {
                count++;
            }
            result.size = count;
            return result;
        }

        /** 转成普通 List，方便断言和打印 */
        List<Integer> toList() {
            List<Integer> result = new ArrayList<>();
            for (Node cur = head; cur != null; cur = cur.next) {
                result.add(cur.val);
            }
            return result;
        }

        Node head() {
            return head;
        }

        int size() {
            return size;
        }

        @Override
        public String toString() {
            return toList().toString();
        }
    }

    // =========================================================
    // 断言小工具：条件不成立就抛异常，避免"看起来跑通了其实结果错了"
    // =========================================================
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
