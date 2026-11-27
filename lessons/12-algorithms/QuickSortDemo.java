import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：快速排序（Quick Sort）—— 工程界最常用的排序之一
 * ============================================================
 * 一句话：选一个基准 pivot，把比它小的甩到左边、比它大的甩到右边，再对左右两段递归重复。
 *
 * 为什么需要它：
 *   归并虽然稳定 O(n log n)，但要 O(n) 额外空间；快排原地分区、常数因子小，
 *   平均 O(n log n)，是大多数语言内置排序的主力思路。理解分区、基准选择和退化场景，
 *   是面试和读源码都绕不开的内容。
 *
 * 复杂度速查（n = 元素个数）：
 *   版本            最好        平均        最坏        额外空间   稳定性
 *   普通快排        O(n log n)  O(n log n)  O(n²)       O(log n)   否（不稳定）
 *   随机化基准      O(n log n)  O(n log n)  概率上极难 O(n²)  O(log n)  否
 *   三路快排        O(n log n)  O(n log n)  O(n²)       O(log n)   否
 *   说明：最坏 O(n²) 出现在每次基准都选到极值（如有序数组 + 固定取端点）；
 *         递归深度平均 O(log n)，最坏 O(n)，会占用调用栈。
 *
 * 易错点：
 *   1. 固定取端点当基准，遇到有序 / 逆序数组会退化成 O(n²) —— 必须随机化基准
 *   2. Lomuto 分区最后要把基准换到分界点 i；Hoare 分区返回的是 j，递归区间是 [lo,j] 与 [j+1,hi]
 *   3. 三路快排把等于基准的元素单独划成中间段，避免大量重复元素时反复比较相等值
 *   4. 快排不稳定：分区交换会把相等元素的先后顺序打乱
 *
 * 与 Java 自带类的对应关系：
 *   Arrays.sort(int[]) 是双轴快排（Dual-Pivot Quicksort），比单轴快排分区更细、
 *   且对小数组插入排序兜底；本文件手写单轴 Lomuto / Hoare / 三路，思路同源但常数因子更大。
 *
 * 一句话口诀：
 *   选个基准分两边，小左大右递归干；随机选枢防退化，重复元素走三路。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 QuickSortDemo.java
 *   java QuickSortDemo
 */
public class QuickSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    /** 固定种子的随机源，用于随机化基准，保证每次运行可复现 */
    private static final Random RANDOM = new Random(20261128L);

    public static void main(String[] args) {

        System.out.println("========== 1. Lomuto 分区：看得见的分界过程 ==========");
        demoLomuto();

        System.out.println();
        System.out.println("========== 2. Hoare 分区：双指针对撞，与 Lomuto 结果对照 ==========");
        demoHoare();

        System.out.println();
        System.out.println("========== 3. 三路快排：大量重复元素也不退化 ==========");
        demoThreeWay();

        System.out.println();
        System.out.println("========== 4. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 5. 性能对比：手写快排 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. Lomuto 分区演示
    // ---------------------------------------------------------
    private static void demoLomuto() {
        int[] arr = {5, 1, 4, 2, 8, 3, 7, 6};
        System.out.println("排序前：" + Arrays.toString(arr));
        quickSortLomuto(arr);
        System.out.println("Lomuto 快排后：" + Arrays.toString(arr));

        check(isSorted(arr), "Lomuto 快排排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 8, "最小值 1 在最前、最大值 8 在最后");
    }

    // ---------------------------------------------------------
    // 2. Hoare 分区演示
    // ---------------------------------------------------------
    private static void demoHoare() {
        int[] a = {5, 1, 4, 2, 8, 3, 7, 6};
        int[] b = a.clone();
        quickSortHoare(a);
        quickSortLomuto(b);
        System.out.println("Hoare 快排后：" + Arrays.toString(a));
        check(Arrays.equals(a, b), "Hoare 与 Lomuto 两种分区结果一致");
        System.out.println("区别：Lomuto 单边扫描、基准最后归位；Hoare 双指针对撞、分界点不一定是基准本身。");
    }

    // ---------------------------------------------------------
    // 3. 三路快排：构造大量重复元素
    // ---------------------------------------------------------
    private static void demoThreeWay() {
        int[] arr = {2, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 2};
        System.out.println("重复元素输入：" + Arrays.toString(arr));
        quickSortThreeWay(arr, 0, arr.length - 1);
        System.out.println("三路快排后：" + Arrays.toString(arr));

        int[] expected = {1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3};
        check(Arrays.equals(arr, expected), "三路快排把重复元素正确聚到各自区间");
        check(isSorted(arr), "三路快排整体有序");
    }

    // ---------------------------------------------------------
    // 4. 对拍：随机数据 + 官方排序当标准答案
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

            int[] copyLomuto = arr.clone();
            quickSortLomuto(copyLomuto);
            int[] copyHoare = arr.clone();
            quickSortHoare(copyHoare);
            int[] copyThree = arr.clone();
            quickSortThreeWay(copyThree, 0, copyThree.length - 1);

            if (!Arrays.equals(copyLomuto, expected)
                    || !Arrays.equals(copyHoare, expected)
                    || !Arrays.equals(copyThree, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）三种快排均与 Arrays.sort 一致");
    }

    // ---------------------------------------------------------
    // 5. 性能实测 + 递归深度观察
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        DepthTracker tracker = new DepthTracker();
        int[] mine = data.clone();
        long t1 = System.nanoTime();
        quickSortLomuto(mine, 0, mine.length - 1, tracker);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        int[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：手写 Lomuto 快排 " + mineMs + " ms，"
                + "Arrays.sort " + jdkMs + " ms，最大递归深度 " + tracker.maxDepth + " 层");
        check(Arrays.equals(mine, jdk), "手写快排结果与 Arrays.sort 完全一致");
        check(tracker.maxDepth <= 4 * (Math.log(n) / Math.log(2)) + 5,
                "递归深度约 O(log n)，没有退化成 O(n) 的链表式递归");
        System.out.println("结论：结果一致；单毫秒级计时受 JIT 与系统调度噪声影响，单次快慢不代表常数因子。"
                + "Arrays.sort 是双轴快排 + 小区间插入兜底 + 调优过的分区，工程常数更优；手写版胜在思路清晰。");
    }

    // =========================================================
    // Lomuto 分区（单轴，基准换到右端）
    // =========================================================

    static void quickSortLomuto(int[] arr) {
        quickSortLomuto(arr, 0, arr.length - 1, null);
    }

    private static void quickSortLomuto(int[] arr, int lo, int hi) {
        quickSortLomuto(arr, lo, hi, null);
    }

    private static void quickSortLomuto(int[] arr, int lo, int hi, DepthTracker tracker) {
        if (lo >= hi) return;
        if (tracker != null) tracker.enter();
        int p = partitionLomuto(arr, lo, hi);
        quickSortLomuto(arr, lo, p - 1, tracker);
        quickSortLomuto(arr, p + 1, hi, tracker);
        if (tracker != null) tracker.leave();
    }

    private static int partitionLomuto(int[] arr, int lo, int hi) {
        // 随机挑一个基准换到 hi，避免有序数组上选到端点而退化
        int pivotIndex = lo + RANDOM.nextInt(hi - lo + 1);
        swap(arr, pivotIndex, hi);
        int pivot = arr[hi];

        int i = lo;   // 小于 pivot 区域的下一个放置位置
        for (int j = lo; j < hi; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, hi);  // 基准归位到分界点 i
        return i;
    }

    // =========================================================
    // Hoare 分区（双指针对撞）
    // =========================================================

    static void quickSortHoare(int[] arr) {
        quickSortHoare(arr, 0, arr.length - 1);
    }

    private static void quickSortHoare(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int p = partitionHoare(arr, lo, hi);
        quickSortHoare(arr, lo, p);
        quickSortHoare(arr, p + 1, hi);
    }

    private static int partitionHoare(int[] arr, int lo, int hi) {
        // 随机基准换到左端
        int pivotIndex = lo + RANDOM.nextInt(hi - lo + 1);
        swap(arr, pivotIndex, lo);
        int pivot = arr[lo];

        int i = lo - 1;
        int j = hi + 1;
        while (true) {
            // i 往右找第一个 >= pivot 的位置
            do { i++; } while (arr[i] < pivot);
            // j 往左找第一个 <= pivot 的位置
            do { j--; } while (arr[j] > pivot);
            if (i >= j) return j;   // 两指针交错，j 就是分界点
            swap(arr, i, j);
        }
    }

    // =========================================================
    // 三路快排（< pivot / == pivot / > pivot 三段）
    // =========================================================

    static void quickSortThreeWay(int[] arr, int lo, int hi) {
        if (lo >= hi) return;

        // 随机基准（只取基准值即可，三路按值划分，不需要把基准换到边界）
        int pivot = arr[lo + RANDOM.nextInt(hi - lo + 1)];
        int lt = lo;       // 小于 pivot 区间的右边界 +1
        int i = lo;        // 正在扫描的元素（从 lo 开始，首元素也要参与划分）
        int gt = hi;       // 大于 pivot 区间的左边界 -1

        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, lt, i);   // 小于段扩容，把小元素换到 lt 位置
                lt++;
                i++;
            } else if (arr[i] > pivot) {
                swap(arr, i, gt);   // 大于段扩容，换过来的元素还要再判断，i 不动
                gt--;
            } else {
                i++;                // 等于 pivot，留在中间段
            }
        }
        // [lo, lt-1] 全 < pivot，[lt, gt] 全 == pivot，[gt+1, hi] 全 > pivot
        quickSortThreeWay(arr, lo, lt - 1);
        quickSortThreeWay(arr, gt + 1, hi);
    }

    // =========================================================
    // 工具方法
    // =========================================================

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /** 递归深度追踪器（观察有没有退化成 O(n) 栈深） */
    static class DepthTracker {
        int current = 0;
        int maxDepth = 0;

        void enter() {
            current++;
            if (current > maxDepth) maxDepth = current;
        }

        void leave() {
            current--;
        }
    }

    private static boolean isSorted(int[] arr) {
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
