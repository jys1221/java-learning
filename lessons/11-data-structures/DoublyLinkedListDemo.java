import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：双向链表（Doubly Linked List）
 * ============================================================
 * 一句话：每个节点同时握有 prev 和 next 两个指针，既能向前走也能向后走；
 *        再配一个 tail 尾指针，首尾增删都能做到 O(1)。
 *
 * 为什么需要它：
 *   单链表只能往后走，删一个节点还得先找到它的前驱；双向链表从一个节点就能直接摸到
 *   前驱和后继，删除时不用回头找。JDK 的 LinkedList 底层就是双向链表，看懂这份手写版
 *   再看源码会非常顺。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   头部添加 addFirst   O(1)      O(1)      改 head 附近的指针
 *   尾部添加 addLast    O(1)      O(1)      有 tail 指针，不用走到队尾
 *   按值删除 remove     O(n)      O(n)      仍要先从头找到它；找到后改指针是 O(1)
 *   反向遍历            O(n)      O(n)      顺着 tail.prev 一直往前
 *   按位置访问 get(i)    O(n)      O(n)      链表没有下标，只能顺指针走
 *
 * 易错点：
 *   1. 删除节点必须同时改两个方向的指针：前驱的 next 要接上后继，后继的 prev 要接回前驱，
 *      只改一边就会留下"悬空指针"，链表直接断
 *   2. 只有一个节点时删除：head 和 tail 都要置 null，不能只改其中一个
 *   3. 插入时先接好新节点的前后指针，再改老节点的指针，顺序反了容易把链断开
 *   4. 头尾节点的 prev / next 永远是 null，遍历终止要判 null，别顺着 null 再解引用
 *
 * 与 Java 自带类的对应关系：
 *   java.util.LinkedList 内部就是双向链表（Entry 里有 prev/next），首尾增删 O(1)，
 *   还支持 push/pop/offer/peek 等栈和队列的姿势。本文件手写的 MyDoublyLinkedList
 *   是它的最小复刻版，并与 JDK LinkedList 做正反向逐元素比对。
 *
 * 一句话口诀：
 *   双向节点两头牵，prev 回头 next 向前；删除两头都要改，tail 在手尾插 O(1)。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 DoublyLinkedListDemo.java
 *   java DoublyLinkedListDemo
 */
public class DoublyLinkedListDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 手写双向链表：首尾插入、正反遍历、按值删除 ==========");
        demoBasicOperations();

        System.out.println();
        System.out.println("========== 2. 与 JDK LinkedList 正反向逐元素比对 ==========");
        demoCrossCheckWithJdk();

        System.out.println();
        System.out.println("========== 3. 实测：带 tail 的尾插代价 ==========");
        demoTailInsertCost();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 基本操作
    // ---------------------------------------------------------
    private static void demoBasicOperations() {
        MyDoublyLinkedList list = new MyDoublyLinkedList();

        // 尾插三个、头插一个，正反向都应能读出正确顺序
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addFirst(5);
        System.out.println("正向遍历：" + list.toListForward());
        System.out.println("反向遍历：" + list.toListBackward());
        check(list.size() == 4, "添加 4 个元素后 size=4");
        check(list.toListForward().equals(List.of(5, 10, 20, 30)), "正向顺序 [5, 10, 20, 30]");
        check(list.toListBackward().equals(List.of(30, 20, 10, 5)), "反向顺序 [30, 20, 10, 5]");

        // 删除中间节点 20：前驱 10 的 next 接 30，后继 30 的 prev 接 10，两边都要改
        boolean removedMid = list.removeByValue(20);
        System.out.println("删掉中间 20 后，正向：" + list.toListForward() + " 反向：" + list.toListBackward());
        check(removedMid && list.toListForward().equals(List.of(5, 10, 30)), "删中间后正向 [5, 10, 30]");
        check(list.toListBackward().equals(List.of(30, 10, 5)), "删中间后反向 [30, 10, 5]");

        // 删除头节点 5
        boolean removedHead = list.removeByValue(5);
        check(removedHead && list.toListForward().equals(List.of(10, 30)), "删头节点后正向 [10, 30]");

        // 删除尾节点 30
        boolean removedTail = list.removeByValue(30);
        check(removedTail && list.toListForward().equals(List.of(10)), "删尾节点后只剩 [10]");

        // 删掉最后一个节点：head 和 tail 必须同时置 null
        boolean removedLast = list.removeByValue(10);
        check(removedLast && list.isEmpty(), "删掉唯一节点后链表为空，head/tail 均为 null");

        // 删不存在的值：返回 false，空链表不报错
        check(!list.removeByValue(999), "空链表删除不存在的值返回 false");
    }

    // ---------------------------------------------------------
    // 2. 与 JDK LinkedList 比对
    // ---------------------------------------------------------
    private static void demoCrossCheckWithJdk() {
        // 固定随机种子，保证两次跑出的序列完全一致、可复现
        Random rnd = new Random(42);
        MyDoublyLinkedList mine = new MyDoublyLinkedList();
        LinkedList<Integer> jdk = new LinkedList<>();

        for (int i = 0; i < 30; i++) {
            int v = rnd.nextInt(100);
            mine.addLast(v);
            jdk.addLast(v);
        }

        // 正向逐元素比对
        List<Integer> mineForward = mine.toListForward();
        check(mineForward.size() == jdk.size(), "两边元素个数相同");
        for (int i = 0; i < mineForward.size(); i++) {
            check(mineForward.get(i).equals(jdk.get(i)), "正向下标 " + i + " 一致：" + mineForward.get(i));
        }

        // 反向比对：JDK 用 descendingIterator 从尾巴往回走
        List<Integer> jdkBackward = new ArrayList<>();
        Iterator<Integer> desc = jdk.descendingIterator();
        while (desc.hasNext()) {
            jdkBackward.add(desc.next());
        }
        check(mine.toListBackward().equals(jdkBackward), "反向序列与 JDK descendingIterator 一致");

        // 两边都删头、删尾、删一个中间值，再比对剩余序列
        mine.removeByValue(mine.toListForward().get(0));
        jdk.removeFirst();
        mine.removeByValue(mine.toListForward().get(mine.size() - 1));
        jdk.removeLast();
        int middle = mine.toListForward().get(mine.size() / 2);
        mine.removeByValue(middle);
        jdk.remove(Integer.valueOf(middle));
        check(mine.toListForward().equals(new ArrayList<>(jdk)), "删头删尾删中间后两边剩余序列一致");
        System.out.println("手写双向链表与 JDK LinkedList 比对完毕。");
    }

    // ---------------------------------------------------------
    // 3. 尾插实测
    // ---------------------------------------------------------
    private static void demoTailInsertCost() {
        final int n = 20000;

        // 手写双向链表尾插：有 tail 指针，每次只改 tail 附近两个节点
        long t1 = System.nanoTime();
        MyDoublyLinkedList mine = new MyDoublyLinkedList();
        for (int i = 0; i < n; i++) {
            mine.addLast(i);
        }
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        // JDK LinkedList 尾插：底层同样是双向链表，作为对照
        long t2 = System.nanoTime();
        LinkedList<Integer> jdk = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            jdk.addLast(i);
        }
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("手写双向链表尾插 " + n + " 次：" + mineMs + " ms");
        System.out.println("JDK LinkedList 尾插 " + n + " 次：" + jdkMs + " ms");
        check(mine.size() == n && jdk.size() == n, "两边元素个数都是 " + n);
        check(mine.toListForward().equals(new ArrayList<>(jdk)), "尾插结果逐元素一致");
        System.out.println("结论：有 tail 指针时尾插是 O(1)，不需要每次走到队尾。");
    }

    // =========================================================
    // 手写双向链表：Node 同时有 prev / next，外加 head / tail
    // =========================================================
    static class MyDoublyLinkedList {

        /** 节点：前驱 prev、后继 next、存值 val */
        static class Node {
            int val;
            Node prev;
            Node next;

            Node(int val) {
                this.val = val;
                this.prev = null;
                this.next = null;
            }
        }

        private Node head;
        private Node tail;
        private int size;

        MyDoublyLinkedList() {
            head = null;
            tail = null;
            size = 0;
        }

        /** 头插：新节点 next 指向老 head，老 head 的 prev 指向新节点 */
        void addFirst(int value) {
            Node node = new Node(value);
            if (head == null) {
                // 空链表：head 和 tail 都是这个新节点
                head = tail = node;
            } else {
                node.next = head;
                head.prev = node;
                head = node;
            }
            size++;
        }

        /** 尾插：靠 tail 直接接上，O(1) */
        void addLast(int value) {
            Node node = new Node(value);
            if (tail == null) {
                head = tail = node;
            } else {
                node.prev = tail;
                tail.next = node;
                tail = node;
            }
            size++;
        }

        /** 删除第一个值等于 value 的节点；删到了返回 true */
        boolean removeByValue(int value) {
            if (head == null) {
                return false;
            }
            Node cur = head;
            while (cur != null && cur.val != value) {
                cur = cur.next;
            }
            if (cur == null) {
                return false; // 没找到
            }

            // 先处理前驱：有前驱就让它的 next 跨过 cur，否则 cur 就是新 head
            if (cur.prev != null) {
                cur.prev.next = cur.next;
            } else {
                head = cur.next;
            }
            // 再处理后继：有后继就让它的 prev 跨回 cur，否则 cur 就是新 tail
            if (cur.next != null) {
                cur.next.prev = cur.prev;
            } else {
                tail = cur.prev;
            }

            // 断开被删节点自身的引用，帮助 GC（非必须，但好习惯）
            cur.prev = null;
            cur.next = null;
            size--;
            return true;
        }

        /** 正向遍历：从 head 顺着 next 走 */
        List<Integer> toListForward() {
            List<Integer> result = new ArrayList<>();
            for (Node cur = head; cur != null; cur = cur.next) {
                result.add(cur.val);
            }
            return result;
        }

        /** 反向遍历：从 tail 顺着 prev 走 */
        List<Integer> toListBackward() {
            List<Integer> result = new ArrayList<>();
            for (Node cur = tail; cur != null; cur = cur.prev) {
                result.add(cur.val);
            }
            return result;
        }

        int size() {
            return size;
        }

        boolean isEmpty() {
            return size == 0;
        }

        @Override
        public String toString() {
            return toListForward().toString();
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
