import java.util.ArrayList;
import java.util.Arrays;

/**
 * 知识点：数组与动态数组（也就是 ArrayList 的底层原理）
 * ============================================================
 * 一句话：数组是"一排连续格子，靠下标一步跳到某一格"；动态数组是在数组满了以后
 *        自动换一块更大的连续空间、把老数据整体搬过去，于是"看起来能一直往里塞"。
 *
 * 为什么需要它：
 *   原生数组 new 出来长度就固定死了，而写代码时元素个数往往事先并不知道。
 *   于是需要一个"能自己长大"的数组 —— 这就是 java.util.ArrayList 存在的理由。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   按下标访问 get()     O(1)      O(1)      连续内存 + 地址计算，一步到位
 *   尾部添加 add()       O(1)      O(n)      只有"扩容那一瞬间"是 O(n)，均摊仍是 O(1)
 *   中间插入 add(i,v)    O(n)      O(n)      后面的元素要整体往后挪一格
 *   删除 remove(i)       O(n)      O(n)      后面的元素要整体往前挪一格
 *   按值查找 indexOf()   O(n)      O(n)      没有下标就只能一个个比
 *
 * 易错点：
 *   1. 下标从 0 开始，合法范围是 0 ~ size-1；写 size 就越界（ArrayIndexOutOfBoundsException）
 *   2. remove(int index) 与 remove(Object o) 是两个不同方法：传 int 走的是"按删下标"
 *   3. 扩容不是"原地变大"（内存无法原地延伸），而是"新建数组 + 拷贝 + 换引用"，
 *      所以扩容前拿到的旧数组引用，不会自动变成新数组
 *   4. 扩容倍数取 1.5 倍是折中：倍数太小（如 1.1）就要频繁拷贝，倍数太大（如 2 倍）就会浪费内存
 *
 * 与 Java 自带类的对应关系：
 *   java.util.ArrayList 内部就是 Object[] elementData + int size；
 *   默认容量 10（第一次 add 时才真正分配），扩容规则
 *   newCapacity = oldCapacity + (oldCapacity >> 1)，即 1.5 倍。
 *   本文件里的 MyIntArrayList 就是把这个机制手写了一遍，对照着看最容易懂。
 *
 * 一句话口诀：
 *   数组靠下标 O(1) 直达，增删靠搬家 O(n)；动态数组 = 数组 + 满了就 1.5 倍扩容。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 ArrayDemo.java
 *   java ArrayDemo
 */
public class ArrayDemo {

    /** 断言通过计数，最后统一打印，让人一眼看到"到底验了多少条" */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 原生数组：按下标访问是 O(1) ==========");
        demoStaticArray();

        System.out.println();
        System.out.println("========== 2. 手写动态数组：容量满了自动 1.5 倍扩容 ==========");
        demoMyIntArrayList();

        System.out.println();
        System.out.println("========== 3. 与官方 ArrayList 对照 ==========");
        demoRealArrayList();

        System.out.println();
        System.out.println("========== 4. 实测：头部插入 vs 尾部插入 ==========");
        demoInsertCost();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 原生数组
    // ---------------------------------------------------------
    private static void demoStaticArray() {
        // 声明时就确定了长度 5，之后再也改不了
        int[] scores = new int[5];

        // 按下标赋值：本质是 "首地址 + 下标 * 每个元素的大小"，所以一步到位
        scores[0] = 90;
        scores[1] = 85;
        scores[2] = 78;
        scores[3] = 92;
        scores[4] = 88;
        System.out.println("数组内容：" + Arrays.toString(scores));
        check(scores[3] == 92, "scores[3] 应为 92");

        // 数组自带 length 属性（注意不是方法，没有括号）
        check(scores.length == 5, "length 应为 5");

        // 越界访问会抛异常：这就是"合法下标 0 ~ length-1"的由来
        try {
            int bad = scores[5];
            System.out.println("越界竟然没报错？bad=" + bad);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("越界访问 scores[5] 被拦下：" + e.getClass().getSimpleName());
            check(true, "越界访问会抛 ArrayIndexOutOfBoundsException");
        }

        // 在中间插入一个元素：只能自己手动挪，挪几次就是几次操作
        int[] withInsert = new int[6];
        System.arraycopy(scores, 0, withInsert, 0, 2);      // 0~1 原样拷过去
        withInsert[2] = 99;                                  // 新元素插在下标 2
        System.arraycopy(scores, 2, withInsert, 3, 3);       // 原来的 2~4 整体后移一格
        System.out.println("在下标 2 插入 99 后：" + Arrays.toString(withInsert));
        check(withInsert[2] == 99 && withInsert[3] == 78, "插入后 99 在前、原 scores[2]=78 后移");

        // 删掉下标 0 的元素：后面的全部往前补一格
        int[] afterRemove = new int[4];
        System.arraycopy(withInsert, 1, afterRemove, 0, 4);
        System.out.println("删除下标 0 后：" + Arrays.toString(afterRemove));
        check(afterRemove[0] == 85, "删除下标 0 后首位应是原 scores[1]=85");
    }

    // ---------------------------------------------------------
    // 2. 手写动态数组
    // ---------------------------------------------------------
    private static void demoMyIntArrayList() {
        MyIntArrayList list = new MyIntArrayList();

        // 连续添加 12 个元素，观察容量变化：10 -> 15 -> 22
        for (int i = 1; i <= 12; i++) {
            list.add(i * 10);
        }
        System.out.println("添加 12 个元素后的内容：" + list);
        check(list.size() == 12, "size 应为 12");
        check(list.get(0) == 10 && list.get(11) == 120, "首元素 10、末元素 120");

        // 按下标读取：数组的看家本领
        check(list.get(5) == 60, "get(5) 应为 60");

        // 修改
        list.set(5, 66);
        check(list.get(5) == 66, "set(5, 66) 生效");

        // 中间插入：后面的元素整体后移
        list.add(0, 5);          // 插到最前面
        list.add(3, 33);         // 插到下标 3
        System.out.println("两次中间插入后：" + list);
        check(list.get(0) == 5, "头部插入后 get(0)=5");
        check(list.get(3) == 33, "下标 3 处是 33");
        check(list.size() == 14, "插入两次后 size=14");

        // 删除：后面的元素整体前移
        int removedHead = list.remove(0);
        int removedTail = list.remove(list.size() - 1);
        System.out.println("删掉头部 " + removedHead + "、尾部 " + removedTail + " 后：" + list);
        check(removedHead == 5 && removedTail == 120, "删掉的确实是 5 和 120");
        check(list.size() == 12, "删两个后 size 回到 12");

        // 按值查找：只能从头一个个比
        check(list.indexOf(66) == 6, "indexOf(66) 应为 6（前面插入过两个元素，它被挪到了下标 6）");
        check(list.indexOf(999) == -1, "查不到的值返回 -1");

        // 扩容后的旧数组不会被自动替换 —— 这点必须自己心里清楚
        check(list.capacity() >= 14, "容量已 >= 14（扩容过）");
        System.out.println("当前容量：" + list.capacity() + "（元素数 " + list.size() + "，多出来的空间留给下次 add）");
    }

    // ---------------------------------------------------------
    // 3. 与官方 ArrayList 对照
    // ---------------------------------------------------------
    private static void demoRealArrayList() {
        ArrayList<String> names = new ArrayList<>();
        names.add("张三");
        names.add("李四");
        names.add("王五");
        names.add(1, "插队的赵六");     // 与手写版 add(index, value) 完全对应
        System.out.println("ArrayList 内容：" + names);
        check(names.size() == 4, "ArrayList size=4");
        check(names.get(1).equals("插队的赵六"), "下标 1 是插队的元素");

        // 删除：传 int 删下标，传对象删元素 —— 这就是易错点 2 的真实演示
        names.remove(0);                    // 删下标 0（"张三"）
        names.remove("王五");                // 删这个对象（内部靠 equals 比较）
        System.out.println("两次 remove 之后：" + names);
        check(names.size() == 2, "删掉 2 个后剩 2 个");
        check(names.get(0).equals("插队的赵六"), "删下标 0 后，原下标 1 顶上来");

        // 官方实现扩容也是 1.5 倍：可以自己翻 JDK 源码 ArrayList#grow 验证
        System.out.println("提示：ArrayList.java 的 grow() 里写着 newCapacity = oldCapacity + (oldCapacity >> 1)");
    }

    // ---------------------------------------------------------
    // 4. 实测：插入位置决定代价
    // ---------------------------------------------------------
    private static void demoInsertCost() {
        final int n = 20000;

        // 尾部插入：不用挪元素，只要偶尔扩容
        long t1 = System.nanoTime();
        MyIntArrayList tailList = new MyIntArrayList();
        for (int i = 0; i < n; i++) {
            tailList.add(i);
        }
        long tailMs = (System.nanoTime() - t1) / 1_000_000;

        // 头部插入：每插一个，所有老元素都要后移一格 —— 总量是 n*(n-1)/2 次搬家
        long t2 = System.nanoTime();
        MyIntArrayList headList = new MyIntArrayList();
        for (int i = 0; i < n; i++) {
            headList.add(0, i);
        }
        long headMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("尾部插入 " + n + " 次耗时：" + tailMs + " ms");
        System.out.println("头部插入 " + n + " 次耗时：" + headMs + " ms");
        check(tailList.size() == n && headList.size() == n, "两种插入方式元素个数都对");
        check(headList.get(0) == n - 1, "头部插入后第一个元素是最后插进去的");
        System.out.println("结论：同样是 add，位置不同复杂度不同 —— 尾部均摊 O(1)，头部 O(n)。");
    }

    // =========================================================
    // 手写的动态数组：ArrayList 的迷你版
    // =========================================================
    static class MyIntArrayList {

        /** 真正装数据的格子系统；长度 = 当前容量 */
        private int[] data;

        /** 已经装了几个元素；它总是 <= data.length */
        private int size;

        /** 默认容量，和 JDK 的 DEFAULT_CAPACITY = 10 保持一致 */
        private static final int DEFAULT_CAPACITY = 10;

        MyIntArrayList() {
            data = new int[DEFAULT_CAPACITY];
            size = 0;
        }

        /** 尾部添加：先确保有空间，再放到 size 这个空位上 */
        void add(int value) {
            ensureCapacityForOne();
            data[size] = value;
            size++;
        }

        /** 指定位置插入：index 之后的元素整体后移一格 */
        void add(int index, int value) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("插入下标越界：index=" + index + "，合法范围 0~" + size);
            }
            ensureCapacityForOne();
            // src=index 起的 size-index 个元素，搬到 index+1 起
            System.arraycopy(data, index, data, index + 1, size - index);
            data[index] = value;
            size++;
        }

        /** 读取：数组的 O(1) 优势 */
        int get(int index) {
            checkIndex(index);
            return data[index];
        }

        /** 修改 */
        void set(int index, int value) {
            checkIndex(index);
            data[index] = value;
        }

        /** 删除并返回被删元素：index 之后的元素整体前移一格 */
        int remove(int index) {
            checkIndex(index);
            int old = data[index];
            System.arraycopy(data, index + 1, data, index, size - index - 1);
            size--;
            return old;
        }

        /** 按值查找：找不到返回 -1 */
        int indexOf(int value) {
            for (int i = 0; i < size; i++) {
                if (data[i] == value) {
                    return i;
                }
            }
            return -1;
        }

        int size() {
            return size;
        }

        int capacity() {
            return data.length;
        }

        boolean isEmpty() {
            return size == 0;
        }

        /** 满了吗？满了就扩容 */
        private void ensureCapacityForOne() {
            if (size == data.length) {
                grow();
            }
        }

        /**
         * 扩容 1.5 倍：newCapacity = old + old/2
         * 注意顺序：先建新数组 → 拷贝老数据 → 再换引用，老数组由 GC 回收
         */
        private void grow() {
            int oldCap = data.length;
            int newCap = oldCap + (oldCap >> 1);   // >> 1 就是 /2，但对负数处理更稳
            if (newCap <= 0) {                      // 极端情况（容量 0）兜底
                newCap = 1;
            }
            int[] bigger = new int[newCap];
            System.arraycopy(data, 0, bigger, 0, size);
            data = bigger;
            System.out.println("  [扩容] 容量 " + oldCap + " -> " + newCap + "（老数据已整体拷贝）");
        }

        private void checkIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("下标越界：index=" + index + "，合法范围 0~" + (size - 1));
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(data[i]);
            }
            return sb.append("]").toString();
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
