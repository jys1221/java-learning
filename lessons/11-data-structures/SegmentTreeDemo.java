import java.util.Random;

/**
 * 知识点：线段树（4n 数组存储、区间和、区间最大值、懒标记区间加 + pushDown）
 * ============================================================
 * 一句话：线段树把数组区间一层层对半切开，每个节点存一段区间的汇总值（和 / 最大值）；
 *         单点修改、区间查询都沿树走，单次 O(log n)；区间批量修改用懒标记先挂在节点上，
 *         等真正要下探时再 pushDown 传给孩子。
 *
 * 为什么需要它：
 *   前缀和能 O(1) 求区间和，但元素一修改就要 O(n) 重建；树状数组擅长单点改 + 前缀和，
 *   但区间最值、区间批量加这类操作不够直接。线段树用一棵树同时支撑区间查询与区间修改，是竞赛/工程常用结构。
 *
 * 复杂度速查（n = 数组长度）：
 *   操作              平均 / 最坏      说明
 *   建树 build         O(n)           每个节点汇总一次
 *   区间和查询         O(log n)       覆盖到的节点直接取，未覆盖下探
 *   区间最大值查询      O(log n)
 *   区间加（懒标记）    O(log n)       标记先挂节点，下探时 pushDown
 *   空间               O(4n)          递归线段树一般开 4 倍长度兜底
 *
 * 易错点：
 *   1. 线段树数组开 4n：递归分治的节点数略大于 2n，开 2n 可能越界，4n 是稳妥上限。
 *   2. 懒标记 lazy 表示"这一整段都还没下发给孩子的增量"；访问孩子前必须先 pushDown，否则孩子的值是旧的。
 *   3. pushDown 时要按左右孩子的区间长度折算区间和：sum 增量 = 增量值 × 孩子区间长度。
 *   4. 区间是闭区间 [l, r]；递归终止要先判断"当前节点区间被查询区间完整覆盖"，再下探。
 *   5. 最大值区间加可以直接 max += v；但区间赋值（覆盖）和区间加的懒标记不能混用，本文件只做区间加。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置线段树；区间统计常自己手写。与树状数组相比：树状数组更轻量、常数小，
 *   但只适合单点改 + 前缀和/区间和；线段树能做区间最值、区间加、区间赋值，适用面更广但代码更重。
 *
 * 一句话口诀：
 *   区间对半切，节点存汇总；修改先挂懒标记，下探之前先下发；查询修改对数级。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 SegmentTreeDemo.java
 *   java SegmentTreeDemo
 */
public class SegmentTreeDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 样例数组：区间和线段树 ==========");
        int[] a = {1, 3, 5, 7, 9, 11};
        SumSegmentTree sumTree = new SumSegmentTree(a);

        System.out.println("  数组：" + java.util.Arrays.toString(a));
        check(sumTree.querySum(0, 2) == 9, "区间和 [0,2]=1+3+5=9");
        check(sumTree.querySum(2, 5) == 32, "区间和 [2,5]=5+7+9+11=32");

        System.out.println();
        System.out.println("========== 2. 懒标记区间加：先挂标记，下探时下发 ==========");
        // 给下标 1~3 整体加 2：3→5，5→7，7→9
        sumTree.rangeAdd(1, 3, 2);
        System.out.println("  对 [1,3] 整体 +2 后，区间和 [1,3]=" + sumTree.querySum(1, 3));
        check(sumTree.querySum(1, 3) == 21, "加 2 后 [1,3]=5+7+9=21");
        check(sumTree.querySum(0, 5) == 42, "加 2 后总和=原 36 + 2×3=42");

        System.out.println();
        System.out.println("========== 3. 区间最大值线段树 ==========");
        MaxSegmentTree maxTree = new MaxSegmentTree(a);
        // 注意：maxTree 是在加 2 之前的原数组上建的；这里查询原数组最值
        check(maxTree.queryMax(1, 4) == 9, "原数组 [1,4] 最大值=max(3,5,7,9)=9");
        check(maxTree.queryMax(0, 5) == 11, "原数组全局最大值=11");

        System.out.println();
        System.out.println("========== 4. 与朴素暴力法对拍 ==========");
        demoCrossCheck();

        System.out.println();
        System.out.println("========== 5. 固定种子实测：随机修改与查询 ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 线段树一：区间和 + 懒标记区间加
    // =========================================================
    static class SumSegmentTree {

        private final int[] tree;  // 节点区间和
        private final int[] lazy;  // 未下发到孩子的区间加增量
        private final int n;

        SumSegmentTree(int[] a) {
            n = a.length;
            tree = new int[4 * n];
            lazy = new int[4 * n];
            build(1, 0, n - 1, a);
        }

        /** 建树：叶子放原数组值，内部节点 = 左右孩子之和 */
        private void build(int node, int l, int r, int[] a) {
            if (l == r) {
                tree[node] = a[l];
                return;
            }
            int mid = (l + r) / 2;
            build(2 * node, l, mid, a);
            build(2 * node + 1, mid + 1, r, a);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }

        /** 把当前节点挂着的懒标记下发给左右孩子 */
        private void pushDown(int node, int l, int r) {
            if (lazy[node] != 0 && l < r) {
                int mid = (l + r) / 2;
                int left = 2 * node;
                int right = 2 * node + 1;
                // 左孩子区间 [l,mid]，长度 mid-l+1；右孩子 [mid+1,r]，长度 r-mid
                tree[left] += lazy[node] * (mid - l + 1);
                lazy[left] += lazy[node];
                tree[right] += lazy[node] * (r - mid);
                lazy[right] += lazy[node];
                lazy[node] = 0; // 已下发，清空本节点标记
            }
        }

        /** 区间加：[ql,qr] 整体加 v */
        void rangeAdd(int ql, int qr, int v) {
            rangeAdd(1, 0, n - 1, ql, qr, v);
        }

        private void rangeAdd(int node, int l, int r, int ql, int qr, int v) {
            if (ql <= l && r <= qr) {
                // 整段覆盖：区间和直接加 v×长度，并把增量挂到懒标记上
                tree[node] += v * (r - l + 1);
                lazy[node] += v;
                return;
            }
            pushDown(node, l, r); // 要下探，先把旧标记下发
            int mid = (l + r) / 2;
            if (ql <= mid) {
                rangeAdd(2 * node, l, mid, ql, qr, v);
            }
            if (qr > mid) {
                rangeAdd(2 * node + 1, mid + 1, r, ql, qr, v);
            }
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }

        /** 区间和查询：[ql,qr] */
        int querySum(int ql, int qr) {
            return querySum(1, 0, n - 1, ql, qr);
        }

        private int querySum(int node, int l, int r, int ql, int qr) {
            if (ql <= l && r <= qr) {
                return tree[node];
            }
            pushDown(node, l, r);
            int mid = (l + r) / 2;
            int sum = 0;
            if (ql <= mid) {
                sum += querySum(2 * node, l, mid, ql, qr);
            }
            if (qr > mid) {
                sum += querySum(2 * node + 1, mid + 1, r, ql, qr);
            }
            return sum;
        }
    }

    // =========================================================
    // 线段树二：区间最大值（本文件不挂懒标记，演示区间最值查询）
    // =========================================================
    static class MaxSegmentTree {

        private final int[] mx;
        private final int n;

        MaxSegmentTree(int[] a) {
            n = a.length;
            mx = new int[4 * n];
            build(1, 0, n - 1, a);
        }

        private void build(int node, int l, int r, int[] a) {
            if (l == r) {
                mx[node] = a[l];
                return;
            }
            int mid = (l + r) / 2;
            build(2 * node, l, mid, a);
            build(2 * node + 1, mid + 1, r, a);
            mx[node] = Math.max(mx[2 * node], mx[2 * node + 1]);
        }

        int queryMax(int ql, int qr) {
            return queryMax(1, 0, n - 1, ql, qr);
        }

        private int queryMax(int node, int l, int r, int ql, int qr) {
            if (ql <= l && r <= qr) {
                return mx[node];
            }
            int mid = (l + r) / 2;
            int best = Integer.MIN_VALUE;
            if (ql <= mid) {
                best = Math.max(best, queryMax(2 * node, l, mid, ql, qr));
            }
            if (qr > mid) {
                best = Math.max(best, queryMax(2 * node + 1, mid + 1, r, ql, qr));
            }
            return best;
        }
    }

    // ---------------------------------------------------------
    // 对拍：随机数组上，线段树结果与朴素前缀和 / 朴素最大值逐一比对
    // ---------------------------------------------------------
    private static void demoCrossCheck() {
        Random rnd = new Random(20261128);
        int n = 30;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextInt(20) - 10;
        }

        SumSegmentTree sumTree = new SumSegmentTree(a);
        MaxSegmentTree maxTree = new MaxSegmentTree(a);

        // 朴素前缀和
        int[] pre = new int[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + a[i];
        }

        int checked = 0;
        for (int t = 0; t < 30; t++) {
            int l = rnd.nextInt(n);
            int r = rnd.nextInt(n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int bruteSum = pre[r + 1] - pre[l];
            int bruteMax = Integer.MIN_VALUE;
            for (int i = l; i <= r; i++) {
                bruteMax = Math.max(bruteMax, a[i]);
            }
            check(sumTree.querySum(l, r) == bruteSum, "线段树区间和 [" + l + "," + r + "] 与前缀和一致");
            check(maxTree.queryMax(l, r) == bruteMax, "线段树区间最大值 [" + l + "," + r + "] 与朴素遍历一致");
            checked++;
        }
        System.out.println("  共对拍 " + checked + " 组区间");
    }

    // ---------------------------------------------------------
    // 实测：固定种子，随机区间加 + 随机查询，统计耗时
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 2000;
        Random rnd = new Random(20261128);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextInt(100);
        }
        SumSegmentTree tree = new SumSegmentTree(a);

        long t1 = System.nanoTime();
        long total = 0;
        for (int t = 0; t < 3000; t++) {
            int l = rnd.nextInt(n);
            int r = rnd.nextInt(n);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (rnd.nextBoolean()) {
                tree.rangeAdd(l, r, rnd.nextInt(10));
            } else {
                total += tree.querySum(l, r);
            }
        }
        long ms = (System.nanoTime() - t1) / 1_000_000;
        System.out.println("  n=" + n + "，3000 次区间加/查询耗时 " + ms + " ms，累计查询和=" + total);
        check(n == 2000, "实测数组长度为 2000");
        check(total >= 0, "累计查询和为合法数值");
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
