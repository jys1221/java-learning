import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：前缀和与差分 —— 多次区间查询 / 多次区间修改的标配工具
 * ============================================================
 * 一句话：前缀和把"求一段区间的和"变成两个前缀值相减；差分把"给一段区间整体加值"变成两个点上的增减。
 *
 * 为什么需要它：
 *   没有前缀和时，每次问 [l,r] 的和都要从头遍历，O(n)；问几百次就是 O(n·q)。
 *   预处理一次前缀和 O(n)，之后每次查询 O(1)。差分则反过来：先记修改，最后一次前缀和还原，
 *   把多次区间加法从 O(n·q) 压到 O(n + q)。
 *
 * 复杂度速查（n = 元素个数，q = 查询/修改次数）：
 *   操作              预处理     单次查询/修改   空间
 *   一维前缀和        O(n)       O(1)           O(n)
 *   二维前缀和        O(n²)      O(1)           O(n²)
 *   差分数组          O(n)       O(1) 记修改    O(n)，最后 O(n) 还原
 *
 * 易错点：
 *   1. 【最大坑：下标错位】pre 数组长度是 n+1，pre[i] 表示"前 i 个元素之和"，
 *      闭区间 [l,r]（0 下标）的和 = pre[r+1] - pre[l]；写成 pre[r]-pre[l] 会漏一格
 *   2. diff[r+1] 当 r 是最后一个下标时 r+1 == n，越界；要判断或允许长度 n+1 的差分数组
 *   3. 区间和累加可能超过 int 范围，求和要用 long；int 溢出会变成负数且不报错
 *   4. 二维前缀和容斥：减去两份重复时要把多减的左上角加回来，公式最后是 +pre[i-1][j-1]
 *   5. 差分做完所有修改后，必须再做一次前缀和才能还原成原数组，直接看 diff 不是答案
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置前缀和工具；Arrays 只提供排序、填充等基础方法。
 *   工程里常用 long[] 存前缀和避免溢出；本文件手写一维 / 二维前缀和与差分。
 *
 * 一句话口诀：
 *   pre 长度 n+1，前 i 个之和；区间和 pre[r+1] 减 pre[l]；差分两头记增减，最后前缀和还原。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 PrefixSumAndDiffDemo.java
 *   java PrefixSumAndDiffDemo
 */
public class PrefixSumAndDiffDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 一维前缀和：pre 长度 n+1，区间和 pre[r+1]-pre[l] ==========");
        int[] arr = {2, 4, 6, 8, 10};
        long[] pre = buildPrefix(arr);
        System.out.println("原数组：" + Arrays.toString(arr));
        System.out.println("前缀和 pre：" + Arrays.toString(pre) + "（pre[i] = 前 i 个元素之和）");

        int l = 1;
        int r = 3;                                 // 闭区间 [1,3]：4+6+8
        long rangeSum = pre[r + 1] - pre[l];
        long bruteSum = bruteRangeSum(arr, l, r);
        System.out.println("区间 [" + l + "," + r + "] 和 = pre[" + (r + 1) + "] - pre[" + l
                + "] = " + rangeSum + "，暴力核算 = " + bruteSum);
        check(pre.length == arr.length + 1, "pre 数组长度为 n+1");
        check(pre[0] == 0, "pre[0] = 0（前 0 个元素之和）");
        check(rangeSum == bruteSum, "区间和公式 pre[r+1]-pre[l] 与暴力一致");

        System.out.println();
        System.out.println("---------- 下标错位反例：错误写法对比 ----------");
        long wrong = pre[r] - pre[l];              // 少算右端一格的典型错法
        System.out.println("错误写法 pre[r]-pre[l] = " + wrong + "（少了 arr[r] = " + arr[r] + "）");
        check(wrong == rangeSum - arr[r], "错误写法恰好漏掉右端元素 arr[r]");

        System.out.println();
        System.out.println("========== 2. 二维前缀和：容斥公式求矩形区间和 ==========");
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        long[][] pre2d = buildPrefix2D(matrix);
        // 矩形：行 [0,1]，列 [1,2] → 2+3+5+6 = 16
        int row1 = 0, row2 = 1, col1 = 1, col2 = 2;
        long rectSum = query2D(pre2d, row1, row2, col1, col2);
        long bruteRect = bruteRectSum(matrix, row1, row2, col1, col2);
        System.out.println("矩形行[" + row1 + "," + row2 + "] 列[" + col1 + "," + col2
                + "] 和 = " + rectSum + "，暴力核算 = " + bruteRect);
        check(rectSum == bruteRect, "二维矩形区间和与暴力一致");

        System.out.println();
        System.out.println("========== 3. 差分数组：区间加记两头，前缀和还原 ==========");
        int n = 5;
        long[] diff = new long[n + 1];              // 多开一格，r+1 落到 n 不越界
        rangeAdd(diff, 1, 3, 3);                   // [1,3] 整体 +3
        rangeAdd(diff, 2, 4, 5);                   // [2,4] 整体 +5
        long[] restored = restore(diff);
        long[] bruteArr = bruteApplyAdds(n);
        System.out.println("差分还原结果：" + Arrays.toString(restored));
        System.out.println("暴力直接加结果：" + Arrays.toString(bruteArr));
        check(Arrays.equals(restored, bruteArr), "差分还原与暴力区间加一致");

        System.out.println();
        System.out.println("========== 4. int 溢出坑：int 得到负数，long 得到正确值 ==========");
        demonstrateOverflow();

        System.out.println();
        System.out.println("========== 5. 规模实测：多次区间查询，前缀和 O(1) vs 暴力 O(n) ==========");
        benchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 一维前缀和：pre[0]=0；pre[i] = arr[0]+…+arr[i-1]（前 i 个元素之和）
    // ---------------------------------------------------------
    private static long[] buildPrefix(int[] arr) {
        int n = arr.length;
        long[] pre = new long[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + arr[i];
        }
        return pre;
    }

    /** 暴力求闭区间 [l,r] 的和，作为对拍参照 */
    private static long bruteRangeSum(int[] arr, int l, int r) {
        long sum = 0;
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum;
    }

    // ---------------------------------------------------------
    // 二维前缀和：pre[i][j] 代表左上角 (0,0) 到 (i-1,j-1) 矩形的和。
    // 容斥：当前格 = 上方 + 左方 - 左上重复 + 自身
    // ---------------------------------------------------------
    private static long[][] buildPrefix2D(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        long[][] pre = new long[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                pre[i][j] = matrix[i - 1][j - 1]
                        + pre[i - 1][j] + pre[i][j - 1] - pre[i - 1][j - 1];
            }
        }
        return pre;
    }

    /** 查询闭矩形 [row1,row2] × [col1,col2]，容斥相减 */
    private static long query2D(long[][] pre, int row1, int row2, int col1, int col2) {
        return pre[row2 + 1][col2 + 1]
                - pre[row1][col2 + 1] - pre[row2 + 1][col1]
                + pre[row1][col1];
    }

    private static long bruteRectSum(int[][] matrix, int row1, int row2, int col1, int col2) {
        long sum = 0;
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    // ---------------------------------------------------------
    // 差分：给 [l,r] 整体加 v，只需 diff[l]+=v、diff[r+1]-=v
    // ---------------------------------------------------------
    private static void rangeAdd(long[] diff, int l, int r, int v) {
        diff[l] += v;
        diff[r + 1] -= v;                          // r+1 可能等于 n，数组已多开一格
    }

    /** 对差分数组做前缀和，还原成最终数组 */
    private static long[] restore(long[] diff) {
        int n = diff.length - 1;
        long[] arr = new long[n];
        arr[0] = diff[0];
        for (int i = 1; i < n; i++) {
            arr[i] = arr[i - 1] + diff[i];
        }
        return arr;
    }

    /** 暴力直接做两次区间加，对拍参照 */
    private static long[] bruteApplyAdds(int n) {
        long[] arr = new long[n];
        for (int i = 1; i <= 3; i++) {
            arr[i] += 3;
        }
        for (int i = 2; i <= 4; i++) {
            arr[i] += 5;
        }
        return arr;
    }

    // ---------------------------------------------------------
    // 4. 溢出演示：同一批数据，int 累加溢出成负数，long 得到正确总和
    // ---------------------------------------------------------
    private static void demonstrateOverflow() {
        final int size = 20000;
        final int value = 1_000_000;                // 单个数 100 万，总和 200 亿，超过 int 上限
        int[] data = new int[size];
        Arrays.fill(data, value);

        int intSum = 0;
        for (int x : data) {
            intSum += x;                            // 溢出：高位被丢弃，结果是负数
        }
        long longSum = 0;
        for (int x : data) {
            longSum += x;
        }
        long expected = (long) size * value;

        System.out.println("int 累加结果：" + intSum + "（负数，已溢出）");
        System.out.println("long 累加结果：" + longSum + "（正确），期望值：" + expected);
        check(intSum < 0, "int 累加确实溢出为负数");
        check(longSum == expected, "long 累加得到正确总和");
    }

    // ---------------------------------------------------------
    // 5. 规模实测：数组 + 大量随机查询，前缀和 vs 暴力
    // ---------------------------------------------------------
    private static void benchmark() {
        final int n = 20000;
        final int q = 20000;
        Random random = new Random(20261128L);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(100);
        }
        long[] pre = buildPrefix(arr);

        // 预先生成查询区间，保证两种算法跑的是同一批问题
        int[][] queries = new int[q][2];
        for (int i = 0; i < q; i++) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);
            queries[i][0] = Math.min(a, b);
            queries[i][1] = Math.max(a, b);
        }

        long t1 = System.nanoTime();
        long preTotal = 0;
        for (int[] query : queries) {
            preTotal += pre[query[1] + 1] - pre[query[0]];
        }
        long preMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        long bruteTotal = 0;
        for (int[] query : queries) {
            bruteTotal += bruteRangeSum(arr, query[0], query[1]);
        }
        long bruteMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "，q = " + q + "：前缀和 " + preMs + " ms，暴力 " + bruteMs + " ms");
        check(preTotal == bruteTotal, "前缀和与暴力在全部查询上总和一致");
    }

    /** 断言辅助：条件不满足就抛异常；通过则计数并打印一条 */
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
