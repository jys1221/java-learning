import java.util.Random;

/**
 * 知识点：树状数组（Fenwick Tree：lowbit、单点修改、前缀和、区间和、逆序对）
 * ============================================================
 * 一句话：树状数组用一棵"隐含的二叉树"把数组分成若干幂次长度的小块；
 *         单点修改和前缀和都沿 lowbit 上下跳，单次 O(log n)，结构比线段树更轻量、常数更小。
 *
 * 为什么需要它：
 *   前缀和数组查询 O(1) 但单点修改 O(n)；树状数组在"单点改 + 前缀和/区间和"这个最常见组合上，
 *   把两边都做到 O(log n)，代码短、空间省，是求逆序对、单点累计计数的常用工具。
 *
 * 复杂度速查（n = 数组长度）：
 *   操作              平均 / 最坏      说明
 *   单点修改 add       O(log n)       i += lowbit(i) 向上更新覆盖块
 *   前缀和 prefix      O(log n)       i -= lowbit(i) 向下累加小块
 *   区间和 rangeSum    O(log n)       prefix(r) - prefix(l-1)
 *   建树 build         O(n log n)     逐个 add；也可 O(n) 直接构造，本文件用逐个 add
 *
 * 易错点：
 *   1. lowbit(x) = x & -x：取 x 二进制最低位的 1 所代表的值，如 lowbit(6)=2，lowbit(8)=8。
 *   2. 树状数组下标从 1 开始；代码里 0 基数组要 +1 映射，别把下标 0 传进去（会死循环）。
 *   3. 区间和是两个前缀和相减：[l,r] = prefix(r) - prefix(l-1)，l-1 别写成 l。
 *   4. 求逆序对时 BIT 存的是"值出现次数"，不是原数组值；统计比当前值小的已出现个数。
 *   5. 树状数组擅长求和/计数；区间最值、区间赋值、区间加这类它做不了，需要线段树。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置树状数组；它是手写结构。与线段树对比：树状数组代码短、空间小、常数低，
 *   但功能窄（主要是可加可减的统计量）；线段树功能全但代码更重。
 *
 * 一句话口诀：
 *   lowbit 取最低位 1，修改向上跳，前缀向下加；区间和两前缀相减，逆序对靠计数树状数组。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 FenwickTreeDemo.java
 *   java FenwickTreeDemo
 */
public class FenwickTreeDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. lowbit 与单点修改、前缀和 ==========");
        int[] a = {3, 1, 4, 1, 5, 9, 2, 6};
        FenwickTree bit = new FenwickTree(a.length);
        for (int i = 0; i < a.length; i++) {
            bit.add(i + 1, a[i]); // 原数组 0 基 → BIT 1 基
        }

        System.out.println("  数组：" + java.util.Arrays.toString(a));
        System.out.println("  lowbit 演示：lowbit(1)=" + lowbit(1) + "，lowbit(6)=" + lowbit(6) + "，lowbit(8)=" + lowbit(8));
        check(bit.prefix(1) == 3, "前缀和 [1]=3");
        check(bit.prefix(4) == 9, "前缀和 [1..4]=3+1+4+1=9");
        check(bit.rangeSum(2, 5) == 11, "区间和 [2..5]=1+4+1+5=11");

        System.out.println();
        System.out.println("========== 2. 单点修改后前缀和更新 ==========");
        bit.add(3, 5); // 下标 3（原数组下标 2，值 4）加 5 → 9
        check(bit.prefix(3) == 13, "下标 3 加 5 后前缀和 [1..3]=3+1+9=13");
        check(bit.rangeSum(3, 3) == 9, "单点修改后下标 3 的值为 9");

        System.out.println();
        System.out.println("========== 3. 与朴素前缀和对拍 ==========");
        demoCrossCheck();

        System.out.println();
        System.out.println("========== 4. 树状数组求逆序对 ==========");
        demoInversion();

        System.out.println();
        System.out.println("========== 5. 固定种子实测：修改与查询规模 ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 手写树状数组（1 基下标）
    // =========================================================
    static class FenwickTree {

        private final int[] bit;
        private final int n;

        FenwickTree(int n) {
            this.n = n;
            bit = new int[n + 1]; // 下标 0 闲置
        }

        /** lowbit：x 二进制最低位的 1 对应的值 */
        private int lowbit(int x) {
            return x & -x;
        }

        /** 单点修改：下标 i（1 基）增加 delta */
        void add(int i, int delta) {
            while (i <= n) {
                bit[i] += delta;
                i += lowbit(i); // 向上更新所有覆盖 i 的块
            }
        }

        /** 前缀和：1..i 的累加 */
        int prefix(int i) {
            int sum = 0;
            while (i > 0) {
                sum += bit[i];
                i -= lowbit(i); // 向下累加不重叠的小块
            }
            return sum;
        }

        /** 区间和：[l, r]（1 基）= prefix(r) - prefix(l-1) */
        int rangeSum(int l, int r) {
            return prefix(r) - prefix(l - 1);
        }
    }

    /** 独立暴露 lowbit 供演示打印 */
    private static int lowbit(int x) {
        return x & -x;
    }

    // ---------------------------------------------------------
    // 对拍：随机数组上，BIT 前缀和与朴素前缀数组逐一比对
    // ---------------------------------------------------------
    private static void demoCrossCheck() {
        Random rnd = new Random(20261128);
        int n = 30;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextInt(20);
        }

        FenwickTree bit = new FenwickTree(n);
        for (int i = 0; i < n; i++) {
            bit.add(i + 1, a[i]);
        }

        // 朴素前缀和：pre[i] = 前 i 个元素之和
        int[] pre = new int[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + a[i];
        }

        int checked = 0;
        for (int t = 0; t < 30; t++) {
            int l = rnd.nextInt(n) + 1;
            int r = rnd.nextInt(n) + 1;
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int brutePrefix = pre[r];
            int bruteRange = pre[r] - pre[l - 1];
            check(bit.prefix(r) == brutePrefix, "BIT 前缀和 [" + r + "] 与朴素前缀一致");
            check(bit.rangeSum(l, r) == bruteRange, "BIT 区间和 [" + l + "," + r + "] 与朴素一致");
            checked++;
        }
        System.out.println("  共对拍 " + checked + " 组区间");
    }

    // ---------------------------------------------------------
    // 逆序对：从右往左扫，BIT 记值出现次数；比当前值小的已出现个数即逆序贡献
    // ---------------------------------------------------------
    private static void demoInversion() {
        int[] a = {3, 1, 4, 1, 5, 9, 2, 6};
        int max = 0;
        for (int v : a) {
            max = Math.max(max, v);
        }

        FenwickTree bit = new FenwickTree(max);
        long inv = 0;
        // 从右往左：当前元素左边还没入树，右边已入树；统计右边比它小的个数
        for (int i = a.length - 1; i >= 0; i--) {
            int v = a[i];
            inv += bit.prefix(v - 1); // 值 < v 的已出现次数
            bit.add(v, 1);
        }

        // 朴素 O(n²) 对拍
        long brute = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j]) {
                    brute++;
                }
            }
        }
        System.out.println("  数组：" + java.util.Arrays.toString(a));
        System.out.println("  BIT 求逆序对=" + inv + "，朴素 O(n²)=" + brute);
        check(inv == brute, "树状数组逆序对数量与朴素双重循环一致");
        check(inv == 8, "样例逆序对为 8 个");
    }

    // ---------------------------------------------------------
    // 实测：固定种子，随机单点修改 + 前缀查询
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 5000;
        Random rnd = new Random(20261128);
        FenwickTree bit = new FenwickTree(n);
        for (int i = 1; i <= n; i++) {
            bit.add(i, rnd.nextInt(100));
        }

        long t1 = System.nanoTime();
        long total = 0;
        for (int t = 0; t < 5000; t++) {
            if (rnd.nextBoolean()) {
                bit.add(rnd.nextInt(n) + 1, rnd.nextInt(10));
            } else {
                total += bit.prefix(rnd.nextInt(n) + 1);
            }
        }
        long ms = (System.nanoTime() - t1) / 1_000_000;
        System.out.println("  n=" + n + "，5000 次修改/查询耗时 " + ms + " ms，累计前缀和=" + total);
        check(n == 5000, "实测数组长度为 5000");
        check(total >= 0, "累计前缀和为合法数值");
        System.out.println("  取舍：树状数组轻量、常数小，但只适合可加可减的统计量；区间最值/区间加请用线段树。");
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
