import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：希尔排序（Shell Sort）—— 插入排序的"分组加速版"
 * ============================================================
 * 一句话：先按一个较大的间隔 gap 把元素分组，组内做插入排序；gap 一步步减半，
 *         最后 gap = 1 时，数组已经大体有序，普通插入排序几乎顺手就能排完。
 *
 * 为什么需要它：
 *   普通插入排序在逆序时要把元素一步步挪很远，效率低；希尔排序先让元素"跨大步"
 *   接近自己的位置，再用小步精修。它是第一批突破 O(n²) 思路的排序之一，
 *   理解它能帮你看懂"先粗排、再细排"的工程思想。
 *
 * 复杂度速查（n = 元素个数）：
 *   项目            最好          平均          最坏          额外空间   稳定性
 *   希尔（gap=n/2）  O(n log n)   约 O(n^1.3)  O(n²)         O(1)       否（不稳定）
 *   说明：复杂度强依赖增量序列；这里用最简单的 gap = n/2 逐步减半，最坏仍可能退化成 O(n²)。
 *
 * 易错点：
 *   1. 分组插入时内层下标跳跃步长是 gap，判断 j >= gap 而不是 j >= 0，防止 j-gap 越界
 *   2. gap 必须最后降到 1；只分组不做 gap=1 的精修，数组不会完全有序
 *   3. 增量序列选得不好（比如总是整除不干净），退化会很明显
 *   4. 希尔排序不稳定：元素按 gap 跨组移动，相等元素可能被交换到彼此后面
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有直接提供希尔排序；Arrays.sort(int[]) 用双轴快排。希尔排序今天主要出现在
 *   教学和历史资料里，工程实现基本被快排 / 归并 / TimSort 取代。
 *
 * 一句话口诀：
 *   大步分组先粗排，间隔减半再细排；间隔为一退回插入，复杂度看增量序列。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 ShellSortDemo.java
 *   java ShellSortDemo
 */
public class ShellSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 分组过程：看得见每一轮 gap ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 3. 稳定性探测：带标签随机数据扫描 ==========");
        demoStabilityProbe();

        System.out.println();
        System.out.println("========== 4. gap=1 即普通插入排序：两种写法结果一致 ==========");
        demoEqualsInsertion();

        System.out.println();
        System.out.println("========== 5. 性能对比：希尔 vs 插入 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 打印每个 gap 结束后的数组
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {8, 5, 2, 9, 1, 6, 3, 7, 4};
        System.out.println("排序前：" + Arrays.toString(arr));

        int n = arr.length;
        // gap 从 n/2 开始，每次减半，直到 0 为止（最后一轮 gap=1）
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // 对间隔为 gap 的各组分别做插入排序
            for (int i = gap; i < n; i++) {
                int key = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > key) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = key;
            }
            System.out.println("gap = " + gap + " 后：" + Arrays.toString(arr));
        }

        check(isSorted(arr), "希尔排序排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 9, "最小值 1 在最前、最大值 9 在最后");
    }

    // ---------------------------------------------------------
    // 2. 对拍：随机数据 + 官方排序当标准答案
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

            shellSort(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 3. 稳定性探测：给相等元素加标签，看相对顺序是否被打乱
    // ---------------------------------------------------------
    private static void demoStabilityProbe() {
        Random random = new Random(99L);
        int unstableCases = 0;
        int totalRounds = 300;

        for (int t = 0; t < totalRounds; t++) {
            int len = random.nextInt(12) + 4;
            Item[] items = new Item[len];
            for (int i = 0; i < len; i++) {
                // 取值域 0~3，故意制造大量相等元素，更容易暴露稳定性问题
                items[i] = new Item(random.nextInt(4), "i" + i);
            }

            // 记录排序前每个值第一次出现的标签顺序
            java.util.Map<Integer, java.util.List<String>> before = captureOrder(items);

            shellSortItems(items);

            java.util.Map<Integer, java.util.List<String>> after = captureOrder(items);
            if (!before.equals(after)) {
                unstableCases++;   // 相等元素次序被打乱，即不稳定的实证
            }
        }

        System.out.println("探测 " + totalRounds + " 组带标签随机数据，其中 " + unstableCases
                + " 组相等元素次序被打乱");
        check(unstableCases > 0, "实测观察到相等元素次序被打乱，用数据证明希尔排序不稳定");
        System.out.println("说明：希尔按 gap 跨组移动元素，相等元素可能越过彼此，因此不是稳定排序。");
    }

    // ---------------------------------------------------------
    // 4. gap=1 这一轮就是普通插入排序，两种实现结果必须一致
    // ---------------------------------------------------------
    private static void demoEqualsInsertion() {
        Random random = new Random(5L);
        int[] a = new int[30];
        int[] b = new int[30];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(100) - 50;
            b[i] = a[i];
        }
        shellSort(a);
        insertionSort(b);
        System.out.println("希尔结果：" + Arrays.toString(a));
        check(Arrays.equals(a, b), "希尔排序与普通插入排序结果一致");
        System.out.println("结论：希尔的最后一轮 gap=1 等价于一次普通插入排序，前面几轮只是在帮它预整理。");
    }

    // ---------------------------------------------------------
    // 5. 性能实测
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        int[] shell = data.clone();
        long t1 = System.nanoTime();
        shellSort(shell);
        long shellMs = (System.nanoTime() - t1) / 1_000_000;

        int[] ins = data.clone();
        long t2 = System.nanoTime();
        insertionSort(ins);
        long insMs = (System.nanoTime() - t2) / 1_000_000;

        int[] jdk = data.clone();
        long t3 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t3) / 1_000_000;

        System.out.println("n = " + n + "：希尔 " + shellMs + " ms，插入 " + insMs + " ms，"
                + "Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(shell, jdk), "希尔排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：希尔比插入快，但增量序列朴素，仍跑不过工程级的 O(n log n) 排序。");
    }

    // =========================================================
    // 排序实现
    // =========================================================

    /** 希尔排序：gap 从 n/2 逐步减半到 1，组内插入排序 */
    static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int key = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > key) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = key;
            }
        }
    }

    /** 带标签元素的希尔排序（用于稳定性探测） */
    static void shellSortItems(Item[] items) {
        int n = items.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Item key = items[i];
                int j = i;
                while (j >= gap && items[j - gap].value > key.value) {
                    items[j] = items[j - gap];
                    j -= gap;
                }
                items[j] = key;
            }
        }
    }

    /** 普通插入排序（对比用） */
    static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // =========================================================
    // 辅助类与工具方法
    // =========================================================

    /** 带标签的元素 */
    static class Item {
        int value;
        String tag;

        Item(int value, String tag) {
            this.value = value;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return value + "(" + tag + ")";
        }
    }

    /** 按 value 分组，记录每组标签出现的先后顺序 */
    private static java.util.Map<Integer, java.util.List<String>> captureOrder(Item[] items) {
        java.util.Map<Integer, java.util.List<String>> map = new java.util.TreeMap<>();
        for (Item it : items) {
            map.computeIfAbsent(it.value, k -> new java.util.ArrayList<>()).add(it.tag);
        }
        return map;
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
