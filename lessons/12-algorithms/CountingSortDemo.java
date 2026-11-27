import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：计数排序（Counting Sort）—— 不基于比较的线性排序
 * ============================================================
 * 一句话：不两两比较，而是开一个"计数桶"记录每个值出现几次，再按值从小到大依次放回原数组。
 *
 * 为什么需要它：
 *   比较类排序有 O(n log n) 的理论下界；计数排序绕过比较，在"值域小、取值离散"的场景
 *   （比如考试分数、年龄、学号尾数）能做到 O(n + k)。它也是基数排序、桶排序的基础零件。
 *
 * 复杂度速查（n = 元素个数，k = 值域大小）：
 *   版本            时间        额外空间   稳定性       适用条件
 *   基础计数        O(n + k)    O(k)       否（直接回填） 值域小
 *   前缀和稳定版    O(n + k)    O(n + k)   是            值域小、需要保序
 *   说明：k 接近 n 甚至远大于 n 时，计数数组会非常浪费；排序是整数（或可映射成整数）才行。
 *
 * 易错点：
 *   1. 有负数不能直接拿值当下标 —— 要先求最小值 min，用 x - min 做偏移
 *   2. 想要稳定必须"前缀和定位 + 倒序遍历原数组"，正序随意回填会打乱相等元素次序
 *   3. 前缀和 count[i] 表示"<= i 的元素个数"，放元素时下标是 count[idx]-1，放完 count[idx]--
 *   4. 值域跨度很大（如 0 与 10 亿各一个）时，计数数组直接爆内存，不能用计数排序
 *
 * 与 Java 自带类的对应关系：
 *   Arrays.sort 是比较排序，不会在底层调用计数排序；JDK 没有公开的计数排序工具类，
 *   但在字符串排序、基数排序等场景内部会用到"计数 + 前缀和"这套定位思路。
 *
 * 一句话口诀：
 *   数清每个值几次，前缀和定位置；倒序回填保稳定，值域小才占便宜。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 CountingSortDemo.java
 *   java CountingSortDemo
 */
public class CountingSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 基础演示：计数数组与前缀和定位 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 处理负数：偏移技巧 ==========");
        demoNegative();

        System.out.println();
        System.out.println("========== 3. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 4. 稳定性验证：前缀和 + 倒序回填保持原有先后 ==========");
        demoStable();

        System.out.println();
        System.out.println("========== 5. 性能对比：计数排序 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 小数组：打印计数数组与回填结果
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {4, 2, 2, 8, 3, 3, 1};
        System.out.println("排序前：" + Arrays.toString(arr));

        int min = Arrays.stream(arr).min().orElse(0);
        int max = Arrays.stream(arr).max().orElse(0);
        int range = max - min + 1;
        int[] count = new int[range];
        for (int x : arr) count[x - min]++;
        System.out.println("计数数组（下标对应值 " + min + " 起）：" + Arrays.toString(count));

        // 前缀和：count[i] 变成"值 <= i+min 的元素总数"
        for (int i = 1; i < range; i++) count[i] += count[i - 1];

        int[] output = new int[arr.length];
        // 倒序遍历原数组，按前缀和定位 → 稳定
        for (int i = arr.length - 1; i >= 0; i--) {
            int idx = arr[i] - min;
            output[count[idx] - 1] = arr[i];
            count[idx]--;
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
        System.out.println("排序后：" + Arrays.toString(arr));

        check(isSorted(arr), "计数排序排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 8, "最小值 1 在最前、最大值 8 在最后");
    }

    // ---------------------------------------------------------
    // 2. 负数：用最小值做偏移，下标永远非负
    // ---------------------------------------------------------
    private static void demoNegative() {
        int[] arr = {-3, 5, -1, -3, 0, 5, -1};
        int[] expected = arr.clone();
        Arrays.sort(expected);
        countingSort(arr);
        System.out.println("含负数排序后：" + Arrays.toString(arr));
        check(Arrays.equals(arr, expected), "负数经偏移后排序结果与 Arrays.sort 一致");
        check(arr[0] == -3 && arr[arr.length - 1] == 5, "最小负数 -3 在最前、最大值 5 在最后");
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
                arr[i] = random.nextInt(21) - 10;   // 值域 -10 ~ 10，故意收窄以符合计数排序适用条件
            }

            int[] expected = arr.clone();
            Arrays.sort(expected);

            countingSort(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数、窄值域）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 4. 稳定性：相等元素保持原有先后
    // ---------------------------------------------------------
    private static void demoStable() {
        Item[] items = {
                new Item(2, "a"), new Item(1, "b"), new Item(2, "c"),
                new Item(3, "d"), new Item(1, "e"), new Item(2, "f")
        };
        System.out.println("排序前：" + Arrays.toString(items));
        countingSortItems(items);
        System.out.println("排序后：" + Arrays.toString(items));

        check(items[0].tag.equals("b") && items[1].tag.equals("e"), "value=1 的两个元素保持 b、e 顺序");
        check(items[2].tag.equals("a") && items[3].tag.equals("c") && items[4].tag.equals("f"),
                "value=2 的三个元素保持 a、c、f 顺序");
        check(isSortedByValue(items), "整体按 value 升序");
    }

    // ---------------------------------------------------------
    // 5. 性能实测（窄值域，计数排序应明显占优）
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(50);   // 值域 0~49，k=50 很小
        }

        int[] mine = data.clone();
        long t1 = System.nanoTime();
        countingSort(mine);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        int[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "，值域 0~49：计数排序 " + mineMs + " ms，Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(mine, jdk), "计数排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：计数排序是 O(n + k)，不参与两两比较，窄值域下线性优势明显；值域一大就不划算。");
    }

    // =========================================================
    // 排序实现：稳定计数排序（偏移 + 前缀和 + 倒序回填）
    // =========================================================

    static void countingSort(int[] arr) {
        if (arr.length <= 1) return;

        // 1. 求值域最小值、最大值，确定计数数组长度
        int min = arr[0];
        int max = arr[0];
        for (int x : arr) {
            if (x < min) min = x;
            if (x > max) max = x;
        }
        int range = max - min + 1;
        int[] count = new int[range];

        // 2. 统计每个值出现次数（x - min 做偏移，保证下标非负）
        for (int x : arr) count[x - min]++;

        // 3. 前缀和：count[i] 变成"值不超过 i+min 的元素总数"
        for (int i = 1; i < range; i++) count[i] += count[i - 1];

        // 4. 倒序遍历原数组，按前缀和定位到 output，保证相等元素相对顺序不变
        int[] output = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            int idx = arr[i] - min;
            output[count[idx] - 1] = arr[i];
            count[idx]--;
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    // =========================================================
    // 带标签元素的稳定计数排序（稳定性演示）
    // =========================================================

    static void countingSortItems(Item[] items) {
        if (items.length <= 1) return;

        int min = items[0].value;
        int max = items[0].value;
        for (Item it : items) {
            if (it.value < min) min = it.value;
            if (it.value > max) max = it.value;
        }
        int range = max - min + 1;
        int[] count = new int[range];
        for (Item it : items) count[it.value - min]++;
        for (int i = 1; i < range; i++) count[i] += count[i - 1];

        Item[] output = new Item[items.length];
        for (int i = items.length - 1; i >= 0; i--) {
            int idx = items[i].value - min;
            output[count[idx] - 1] = items[i];
            count[idx]--;
        }
        System.arraycopy(output, 0, items, 0, items.length);
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

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) return false;
        }
        return true;
    }

    private static boolean isSortedByValue(Item[] items) {
        for (int i = 1; i < items.length; i++) {
            if (items[i - 1].value > items[i].value) return false;
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
