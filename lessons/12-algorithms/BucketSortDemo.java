import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 知识点：桶排序（Bucket Sort）—— 先分桶、桶内再排序
 * ============================================================
 * 一句话：按值域把元素分到若干个桶里，每个桶内部单独排序，最后按桶顺序依次拼回来。
 *
 * 为什么需要它：
 *   当数据在值域上均匀分布时，每个桶里元素很少，桶内排序很快，整体接近 O(n)；
 *   它和计数排序、基数排序同属"分布排序"，是理解"先粗分组、再细处理"思路的一环。
 *
 * 复杂度速查（n = 元素个数，m = 桶数）：
 *   分布情况        平均时间      最坏时间      额外空间   稳定性
 *   均匀分布        O(n + m)      O(n²)         O(n + m)   视桶内排序而定
 *   说明：最坏情况是所有元素挤进同一个桶，桶内排序退化成 O(n²)；
 *         桶数常取 √n 左右的经验值，桶内一般用插入排序（小数组常数小）。
 *
 * 易错点：
 *   1. 元素恰好落在值域上界（如 1.0）时，桶下标可能等于桶数，要 clamp 到最后一桶
 *   2. 所有元素相等或高度集中时，桶排序退化成桶内插入排序，别以为它一定线性
 *   3. 桶内用稳定排序、按原顺序追加，桶排序才稳定；桶内换成快排则不稳定
 *   4. 桶数不是越多越好：桶太多会有建桶和遍历的开销，太少又挤成大桶
 *
 * 与 Java 自带类的对应关系：
 *   Arrays.sort 不使用桶排序；桶排序思路常见于海量均匀数据的分片处理、外部排序与哈希分桶。
 *
 * 一句话口诀：
 *   按值域来分小桶，桶内插入排整齐；均匀分布接近线，挤成一桶退平方。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BucketSortDemo.java
 *   java BucketSortDemo
 */
public class BucketSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 0~1 浮点数演示：看得见每个桶 ==========");
        demoDouble();

        System.out.println();
        System.out.println("========== 2. 整数版本：偏移后映射到桶 ==========");
        demoInt();

        System.out.println();
        System.out.println("========== 3. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 4. 最坏退化：全部挤进同一个桶 ==========");
        demoWorstCase();

        System.out.println();
        System.out.println("========== 5. 性能对比：均匀分布下桶排序 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 0~1 浮点数：打印分桶与桶内排序结果
    // ---------------------------------------------------------
    private static void demoDouble() {
        double[] arr = {0.42, 0.17, 0.89, 0.33, 0.65, 0.24, 0.71, 0.58};
        System.out.println("排序前：" + Arrays.toString(arr));

        int n = arr.length;
        int bucketCount = Math.max(1, (int) Math.sqrt(n));
        List<List<Double>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());

        for (double x : arr) {
            int idx = (int) (x * bucketCount);
            if (idx == bucketCount) idx = bucketCount - 1;   // x 恰为 1.0 的边界保护
            buckets.get(idx).add(x);
        }
        for (int i = 0; i < bucketCount; i++) {
            System.out.println("桶 " + i + "：" + buckets.get(i));
        }

        bucketSortDouble(arr);
        System.out.println("排序后：" + Arrays.toString(arr));

        check(isSortedDouble(arr), "浮点桶排序排完后整体有序");
        check(arr[0] == 0.17 && arr[arr.length - 1] == 0.89, "最小值 0.17 在最前、最大值 0.89 在最后");
    }

    // ---------------------------------------------------------
    // 2. 整数版本：先偏移到非负再映射桶号
    // ---------------------------------------------------------
    private static void demoInt() {
        int[] arr = {17, 3, 25, 8, 12, 30, 5, 20};
        int[] expected = arr.clone();
        Arrays.sort(expected);
        bucketSortInt(arr);
        System.out.println("整数桶排序后：" + Arrays.toString(arr));
        check(Arrays.equals(arr, expected), "整数桶排序结果与 Arrays.sort 一致");
    }

    // ---------------------------------------------------------
    // 3. 对拍：随机数据 + 官方排序当标准答案
    // ---------------------------------------------------------
    private static void demoRandomCheck() {
        Random random = new Random(20261128L);
        int round = 0;

        for (int t = 0; t < 200; t++) {
            int len = random.nextInt(50) + 1;
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = random.nextInt(200) - 100;
            }

            int[] expected = arr.clone();
            Arrays.sort(expected);

            bucketSortInt(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 4. 最坏退化：所有元素映射到同一个桶，桶内退化成插入排序
    // ---------------------------------------------------------
    private static void demoWorstCase() {
        // 全部相等：只会落进同一个桶，桶内插入排序处理 n 个相同元素
        int[] allSame = new int[300];
        Arrays.fill(allSame, 7);
        int[] expected = allSame.clone();
        bucketSortInt(allSame);
        check(Arrays.equals(allSame, expected), "退化到单桶时结果依然正确");

        // 集中在很窄的一段值域：桶间分布极不均匀
        int[] clustered = new int[300];
        Random random = new Random(1L);
        for (int i = 0; i < clustered.length; i++) {
            clustered[i] = random.nextInt(3);   // 只有 0、1、2 三个取值
        }
        int[] clusteredExpected = clustered.clone();
        Arrays.sort(clusteredExpected);
        bucketSortInt(clustered);
        check(Arrays.equals(clustered, clusteredExpected), "窄值域集中分布时桶排序结果仍正确");
        System.out.println("说明：数据高度集中时大部分元素挤进少数桶，桶内排序接近 O(n²)，这就是桶排序的最坏退化。");
    }

    // ---------------------------------------------------------
    // 5. 性能实测（均匀分布）
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextDouble();   // 均匀分布在 [0,1)
        }

        double[] mine = data.clone();
        long t1 = System.nanoTime();
        bucketSortDouble(mine);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        double[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "，均匀 [0,1)：桶排序 " + mineMs + " ms，Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(mine, jdk), "桶排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：均匀分布下桶排序接近线性，但建桶、装箱拆箱有额外常数；分布不均时优势消失。");
    }

    // =========================================================
    // 排序实现
    // =========================================================

    /** 浮点桶排序：值域 [0,1]，桶数取 √n */
    static void bucketSortDouble(double[] arr) {
        int n = arr.length;
        if (n <= 1) return;
        int bucketCount = Math.max(1, (int) Math.sqrt(n));
        List<List<Double>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());

        for (double x : arr) {
            int idx = (int) (x * bucketCount);
            if (idx == bucketCount) idx = bucketCount - 1;
            buckets.get(idx).add(x);
        }

        int k = 0;
        for (List<Double> bucket : buckets) {
            insertionSortDouble(bucket);          // 桶内用插入排序（稳定）
            for (double x : bucket) arr[k++] = x;
        }
    }

    /** 整数桶排序：先求 min/max 做偏移映射，桶内插入排序 */
    static void bucketSortInt(int[] arr) {
        int n = arr.length;
        if (n <= 1) return;

        int min = arr[0];
        int max = arr[0];
        for (int x : arr) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        int range = max - min + 1;
        int bucketCount = Math.max(1, (int) Math.sqrt(n));

        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());

        for (int x : arr) {
            int idx = (int) ((long) (x - min) * bucketCount / range);
            if (idx == bucketCount) idx = bucketCount - 1;   // 上界保护
            buckets.get(idx).add(x);
        }

        int k = 0;
        for (List<Integer> bucket : buckets) {
            insertionSortInt(bucket);
            for (int x : bucket) arr[k++] = x;
        }
    }

    /** 桶内插入排序（Double） */
    private static void insertionSortDouble(List<Double> bucket) {
        for (int i = 1; i < bucket.size(); i++) {
            double key = bucket.get(i);
            int j = i - 1;
            while (j >= 0 && bucket.get(j) > key) {
                bucket.set(j + 1, bucket.get(j));
                j--;
            }
            bucket.set(j + 1, key);
        }
    }

    /** 桶内插入排序（Integer） */
    private static void insertionSortInt(List<Integer> bucket) {
        for (int i = 1; i < bucket.size(); i++) {
            int key = bucket.get(i);
            int j = i - 1;
            while (j >= 0 && bucket.get(j) > key) {
                bucket.set(j + 1, bucket.get(j));
                j--;
            }
            bucket.set(j + 1, key);
        }
    }

    // =========================================================
    // 工具方法
    // =========================================================

    private static boolean isSortedDouble(double[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) return false;
        }
        return true;
    }

    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
