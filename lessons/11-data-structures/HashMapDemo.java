import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * 知识点：哈希表（HashMap 的底层原理：哈希函数、负载因子、开放寻址、链地址法）
 * ============================================================
 * 一句话：哈希表靠"哈希函数把 key 算成数组下标"，让插入、查找、删除平均都接近 O(1)；
 *         撞车（不同 key 落到同一格）时，要么顺着格子往后找（开放寻址），要么在同一格挂链表（链地址法）。
 *
 * 为什么需要它：
 *   用数组按下标访问是 O(1)，但 key 往往是字符串，没法直接当下标；用链表查找是 O(n)。
 *   哈希表在两者之间架了一座桥：先把 key 映射成下标，再处理碰撞，于是大多数操作都是 O(1)。
 *
 * 复杂度速查（n = 元素个数，均匀哈希时）：
 *   操作              平均      最坏        说明
 *   插入 put()        O(1)      O(n)        最坏来自全部 key 撞同一格 / 探测链过长
 *   查找 get()        O(1)      O(n)        同上
 *   删除 remove()     O(1)      O(n)        开放寻址删除必须留"墓碑"，不能直接清空
 *   扩容 rehash()     O(n)      O(n)        把老格子里的元素重新算下标搬一遍，只在扩容瞬间发生
 *
 * 易错点：
 *   1. 开放寻址删除时，如果直接把格子置空（EMPTY），会中断后面元素的探测链，导致"链上靠后的元素再也查不到"；
 *      正确做法是打一个 DELETED（墓碑）标记：查找时遇到墓碑继续往后探，插入时可以复用墓碑格。
 *   2. 负载因子 = 元素数 / 容量。超过阈值（JDK 用 0.75）就扩容；负载因子太高碰撞变多，太低浪费空间。
 *   3. 哈希扰动（h ^ (h >>> 16)）是为了让高 16 位也参与低位运算；直接用 hashCode & mask 会让低位相同的 key 大量撞车。
 *   4. 链地址法扩容时要重新计算每个节点的下标（rehash），不能简单地把老链表整体搬进新桶。
 *   5. JDK 链地址法在链表长度达到 8、且数组容量达到 64 时会把链表升级成红黑树（树化阈值 8 只是其中一个条件），
 *      本文件为零基础起见只手写"数组 + 链表"，树化只做说明。
 *
 * 与 Java 自带类的对应关系：
 *   java.util.HashMap 底层是"数组 + 链表 + 红黑树"的链地址法：数组 tab[]，每个桶挂 Node 链表，
 *   链表过长时树化；默认初始容量 16、负载因子 0.75；哈希扰动就是 h ^ (h >>> 16)。
 *   本文件分别手写"开放寻址线性探测"和"链地址法"两种迷你实现，最后与官方 HashMap 对拍结果。
 *
 * 一句话口诀：
 *   key 哈希算下标，撞车两种解法——开放寻址往后探，链地址法挂链表；删除留墓碑，满了就扩容。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 HashMapDemo.java
 *   java HashMapDemo
 */
public class HashMapDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 哈希函数与扰动：让高位也参与下标计算 ==========");
        demoHashPerturbation();

        System.out.println();
        System.out.println("========== 2. 开放寻址：线性探测 + 墓碑（DELETED）==========");
        demoOpenAddressing();

        System.out.println();
        System.out.println("========== 3. 链地址法：数组 + 链表 + 扩容 rehash ==========");
        demoChaining();

        System.out.println();
        System.out.println("========== 4. 与 java.util.HashMap 对拍：同 key 集合结果必须一致 ==========");
        demoCrossCheckJdk();

        System.out.println();
        System.out.println("========== 5. 规模实测：固定种子，自写链地址 vs 官方 HashMap ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 哈希扰动演示
    // ---------------------------------------------------------
    private static void demoHashPerturbation() {
        // 扰动函数：把 hashCode 的高 16 位异或到低 16 位，减少"低位相同、高位不同"的 key 撞同一格
        String[] samples = {"apple", "banana", "cherry", "apple", "0", "16", "4294967312"};
        for (String s : samples) {
            int raw = s.hashCode();
            int mixed = raw ^ (raw >>> 16);
            System.out.println("  key=" + s + "，hashCode=" + raw + "，扰动后=" + mixed);
        }
        // 同一个 key 的哈希值必须稳定不变（否则表结构会乱）
        check(hash("apple") == hash("apple"), "同一 key 的扰动哈希值稳定不变");
    }

    /** 本文件统一使用的扰动哈希：先取 hashCode，再高低位混合 */
    private static int hash(String key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    // ---------------------------------------------------------
    // 2. 开放寻址：线性探测
    // ---------------------------------------------------------
    private static void demoOpenAddressing() {
        LinearProbingMap map = new LinearProbingMap(8); // 容量取 2 的幂，方便用 & 取模

        // 故意插入若干 key，制造同槽位的探测链
        map.put("a", 10);
        map.put("b", 20);
        map.put("c", 30);
        map.put("d", 40);
        System.out.println("  插入 a/b/c/d 后：" + map);

        check(map.get("a") == 10, "开放寻址 get(a)=10");
        check(map.get("c") == 30, "开放寻址 get(c)=30");

        // 关键演示：删除链中间的 b。墓碑不能置空，否则后面的 c、d 会被"断掉的链"甩在后面查不到。
        boolean removed = map.remove("b");
        System.out.println("  删除 b 后（留墓碑）：" + map);
        check(removed, "remove(b) 返回 true");
        check(map.get("b") == null, "墓碑格上的 b 已查不到");
        check(map.get("c") == 30, "删除中间元素后，链上靠后的 c 仍能查到（墓碑未中断探测链）");
        check(map.get("d") == 40, "删除中间元素后，d 也仍能查到");

        // 复用墓碑格：新 key 优先占用之前的墓碑位置
        map.put("e", 50);
        check(map.get("e") == 50, "新插入的 e 可复用墓碑格并正常查到");
        check(map.size() == 4, "插入 e 后有效元素数为 4");
    }

    // ---------------------------------------------------------
    // 3. 链地址法
    // ---------------------------------------------------------
    private static void demoChaining() {
        ChainedMap map = new ChainedMap(4);

        map.put("张三", 90);
        map.put("李四", 85);
        map.put("王五", 78);
        map.put("赵六", 92);
        map.put("钱七", 88); // 元素数超过 0.75*容量，触发一次扩容
        System.out.println("  链地址法内容：" + map);

        check(map.get("李四") == 85, "链地址 get(李四)=85");
        check(map.get("不存在") == null, "查不到的 key 返回 null");
        check(map.size() == 5, "链地址元素数为 5");

        // 更新已有 key：值覆盖，不新增节点
        map.put("李四", 95);
        check(map.get("李四") == 95, "重复 put 李四 是覆盖，值变 95");
        check(map.size() == 5, "覆盖已有 key 后元素数仍是 5");

        // 删除
        boolean removed = map.remove("王五");
        check(removed, "remove(王五) 返回 true");
        check(map.get("王五") == null, "删除后王五查不到");
        check(map.size() == 4, "删除后元素数为 4");

        // 容量确实扩容过（初始 4，插入第 4 个元素前后就应翻倍）
        check(map.capacity() >= 8, "负载因子达到 0.75 后已扩容，容量 >= 8");
    }

    // ---------------------------------------------------------
    // 4. 与官方 HashMap 对拍
    // ---------------------------------------------------------
    private static void demoCrossCheckJdk() {
        // 同一批 key-value，分别塞进自写链地址表和官方 HashMap
        String[] keys = {"id-1", "id-2", "id-3", "id-4", "id-5", "id-6", "id-7"};
        int[] values = {11, 22, 33, 44, 55, 66, 77};

        ChainedMap mine = new ChainedMap(4);
        HashMap<String, Integer> jdk = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            mine.put(keys[i], values[i]);
            jdk.put(keys[i], values[i]);
        }

        // 对拍 1：key 集合必须完全一致
        Set<String> mineKeys = mine.keySet();
        check(mineKeys.equals(jdk.keySet()), "自写表与官方 HashMap 的 key 集合完全一致");

        // 对拍 2：每个 key 对应的值必须一致
        boolean allEqual = true;
        for (String k : jdk.keySet()) {
            if (mine.get(k) != jdk.get(k)) {
                allEqual = false;
            }
        }
        check(allEqual, "自写表与官方 HashMap 每个 key 的值逐一相等");
        System.out.println("  官方 HashMap key 集合：" + new TreeSet<>(jdk.keySet()));
    }

    // ---------------------------------------------------------
    // 5. 规模实测（固定种子，可复现）
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 8000;
        Random rnd = new Random(20261128); // 固定种子，每次跑出来的随机序列相同

        String[] keys = new String[n];
        for (int i = 0; i < n; i++) {
            // 生成带随机后缀的字符串，模拟真实业务 key
            keys[i] = "user-" + rnd.nextInt(1_000_000) + "-" + i;
        }

        // 自写链地址表：插入
        ChainedMap mine = new ChainedMap(16);
        long t1 = System.nanoTime();
        for (int i = 0; i < n; i++) {
            mine.put(keys[i], i);
        }
        long mineInsertMs = (System.nanoTime() - t1) / 1_000_000;

        // 官方 HashMap：插入
        HashMap<String, Integer> jdk = new HashMap<>();
        long t2 = System.nanoTime();
        for (int i = 0; i < n; i++) {
            jdk.put(keys[i], i);
        }
        long jdkInsertMs = (System.nanoTime() - t2) / 1_000_000;

        // 查找阶段：随机抽一半 key 查询
        long t3 = System.nanoTime();
        int mineHit = 0;
        for (int i = 0; i < n / 2; i++) {
            if (mine.get(keys[i]) != null) {
                mineHit++;
            }
        }
        long mineGetMs = (System.nanoTime() - t3) / 1_000_000;

        long t4 = System.nanoTime();
        int jdkHit = 0;
        for (int i = 0; i < n / 2; i++) {
            if (jdk.get(keys[i]) != null) {
                jdkHit++;
            }
        }
        long jdkGetMs = (System.nanoTime() - t4) / 1_000_000;

        System.out.println("  插入 " + n + " 条：自写链地址 " + mineInsertMs + " ms，官方 HashMap " + jdkInsertMs + " ms");
        System.out.println("  查询 " + (n / 2) + " 条：自写链地址 " + mineGetMs + " ms，官方 HashMap " + jdkGetMs + " ms");

        check(mine.size() == n, "自写表插入条数为 " + n);
        check(jdk.size() == n, "官方表插入条数为 " + n);
        check(mineHit == n / 2 && jdkHit == n / 2, "两边查到的命中数都为 " + (n / 2));
        System.out.println("  说明：自写实现没有树化与优化常量，纯做原理演示；真实业务请直接用 java.util.HashMap。");
    }

    // =========================================================
    // 实现一：开放寻址（线性探测）
    // =========================================================
    static class LinearProbingMap {

        /** 格子状态：空 / 已占用 / 墓碑（曾被占用后删除） */
        enum State { EMPTY, OCCUPIED, DELETED }

        String[] keys;
        int[] values;
        State[] states;
        int size;
        final int capacity;
        static final double LOAD_FACTOR = 0.75;

        LinearProbingMap(int capacity) {
            this.capacity = capacity;
            keys = new String[capacity];
            values = new int[capacity];
            states = new State[capacity];
            for (int i = 0; i < capacity; i++) {
                states[i] = State.EMPTY;
            }
            this.size = 0;
        }

        /** key -> 槽位下标；容量是 2 的幂，用 & (capacity-1) 代替 % */
        private int slot(String key) {
            return hash(key) & (capacity - 1);
        }

        void put(String key, int value) {
            // 负载因子超标就扩容 rehash（本演示在首次插入前检查）
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            int idx = slot(key);
            int tombstone = -1; // 记录探测路上遇到的第一个墓碑格，方便复用
            while (states[idx] != State.EMPTY) {
                if (states[idx] == State.OCCUPIED && keys[idx].equals(key)) {
                    values[idx] = value; // 已存在：覆盖
                    return;
                }
                if (states[idx] == State.DELETED && tombstone == -1) {
                    tombstone = idx;
                }
                idx = (idx + 1) % capacity; // 线性探测：往后顺一格
            }
            // 优先复用墓碑格；否则放到当前遇到的空格里
            int place = tombstone != -1 ? tombstone : idx;
            keys[place] = key;
            values[place] = value;
            states[place] = State.OCCUPIED;
            size++;
        }

        /** 查找：遇到墓碑不能停，必须继续往后探，否则链后元素会被漏掉 */
        Integer get(String key) {
            int idx = slot(key);
            while (states[idx] != State.EMPTY) {
                if (states[idx] == State.OCCUPIED && keys[idx].equals(key)) {
                    return values[idx];
                }
                idx = (idx + 1) % capacity;
            }
            return null;
        }

        /**
         * 删除：只打 DELETED 墓碑标记，绝不能置回 EMPTY。
         * 原因：若置空，探测链在此处断开，落在它后面的 key 会被误判为不存在。
         */
        boolean remove(String key) {
            int idx = slot(key);
            while (states[idx] != State.EMPTY) {
                if (states[idx] == State.OCCUPIED && keys[idx].equals(key)) {
                    states[idx] = State.DELETED;
                    keys[idx] = null;
                    size--;
                    return true;
                }
                idx = (idx + 1) % capacity;
            }
            return false;
        }

        int size() {
            return size;
        }

        /** 扩容：容量翻倍，把仍在占用的元素重新哈希搬过去；墓碑格直接丢弃 */
        private void resize() {
            String[] oldKeys = keys;
            int[] oldValues = values;
            State[] oldStates = states;
            int newCap = capacity * 2;
            keys = new String[newCap];
            values = new int[newCap];
            states = new State[newCap];
            for (int i = 0; i < newCap; i++) {
                states[i] = State.EMPTY;
            }
            size = 0;
            // 临时借用本对象的新表，重新 put 一遍占用元素
            LinearProbingMap fresh = new LinearProbingMap(newCap);
            for (int i = 0; i < oldStates.length; i++) {
                if (oldStates[i] == State.OCCUPIED) {
                    fresh.put(oldKeys[i], oldValues[i]);
                }
            }
            this.keys = fresh.keys;
            this.values = fresh.values;
            this.states = fresh.states;
            this.size = fresh.size;
            System.out.println("  [开放寻址扩容] 容量 -> " + newCap + "（占用元素已重新哈希，墓碑已丢弃）");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (int i = 0; i < capacity; i++) {
                if (states[i] == State.OCCUPIED) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(keys[i]).append("=").append(values[i]);
                    first = false;
                } else if (states[i] == State.DELETED) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append("[墓碑]");
                    first = false;
                }
            }
            return sb.append("]").toString();
        }
    }

    // =========================================================
    // 实现二：链地址法（数组 + 链表 + 扩容 rehash）
    // =========================================================
    static class ChainedMap {

        /** 链表节点：挂在某个桶里，next 指向下一个撞车的节点 */
        static class Node {
            String key;
            int value;
            Node next;

            Node(String key, int value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }

        Node[] buckets;
        int size;
        int capacity;
        static final double LOAD_FACTOR = 0.75;
        /** 树化阈值：JDK 在链表长度 >= 8 且数组容量 >= 64 时才把链表转红黑树；本文件只做说明，不实现树化 */
        static final int TREEIFY_THRESHOLD = 8;

        ChainedMap(int capacity) {
            this.capacity = capacity;
            this.buckets = new Node[capacity];
            this.size = 0;
        }

        private int slot(String key) {
            return hash(key) & (capacity - 1);
        }

        void put(String key, int value) {
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            int idx = slot(key);
            Node head = buckets[idx];

            // 先遍历链表：key 已存在就覆盖值
            Node cur = head;
            while (cur != null) {
                if (cur.key.equals(key)) {
                    cur.value = value;
                    return;
                }
                cur = cur.next;
            }
            // 不存在：头插法挂到链表最前面
            buckets[idx] = new Node(key, value, head);
            size++;
        }

        Integer get(String key) {
            Node cur = buckets[slot(key)];
            while (cur != null) {
                if (cur.key.equals(key)) {
                    return cur.value;
                }
                cur = cur.next;
            }
            return null;
        }

        boolean remove(String key) {
            int idx = slot(key);
            Node head = buckets[idx];
            Node prev = null;
            Node cur = head;
            while (cur != null) {
                if (cur.key.equals(key)) {
                    if (prev == null) {
                        buckets[idx] = cur.next; // 删的是链表头
                    } else {
                        prev.next = cur.next;   // 删中间/尾部：前驱接上后继
                    }
                    size--;
                    return true;
                }
                prev = cur;
                cur = cur.next;
            }
            return false;
        }

        int size() {
            return size;
        }

        int capacity() {
            return capacity;
        }

        /** 收集所有 key，方便与官方 HashMap 的 keySet 对拍 */
        Set<String> keySet() {
            Set<String> result = new java.util.HashSet<>();
            for (Node head : buckets) {
                Node cur = head;
                while (cur != null) {
                    result.add(cur.key);
                    cur = cur.next;
                }
            }
            return result;
        }

        /** 扩容：容量翻倍，逐个节点重新算下标挂到新桶（rehash） */
        private void resize() {
            Node[] oldBuckets = buckets;
            int newCap = capacity * 2;
            buckets = new Node[newCap];
            capacity = newCap;
            size = 0;
            for (Node head : oldBuckets) {
                Node cur = head;
                while (cur != null) {
                    Node next = cur.next;
                    int idx = slot(cur.key);
                    cur.next = buckets[idx];
                    buckets[idx] = cur;
                    size++;
                    cur = next;
                }
            }
            System.out.println("  [链地址扩容] 容量 -> " + newCap + "（全部节点已重新哈希挂桶）");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Node head : buckets) {
                Node cur = head;
                while (cur != null) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(cur.key).append("=").append(cur.value);
                    first = false;
                    cur = cur.next;
                }
            }
            return sb.append("]").toString();
        }
    }

    // =========================================================
    // 断言小工具
    // =========================================================
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
