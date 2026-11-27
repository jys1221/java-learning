import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：基数排序（Radix Sort）—— 按位做稳定计数排序
 * ============================================================
 * 一句话：从最低位（个位）开始，每一位单独做一次稳定计数排序；一位位排上去，最高位排完整体有序。
 *
 * 为什么需要它：
 *   计数排序只能处理"值域小"的整数；基数排序把大整数拆成一位一位的小数字（0~9），
 *   每一位都退化成计数排序，于是能排较大的整数。它是 O(d · (n + k)) 的线性排序，
 *   d 是位数、k 是每一位的基数（十进制 k=10）。
 *
 * 复杂度速查（n = 元素个数，d = 最大位数，k = 基数）：
 *   版本        时间            额外空间   稳定性       说明
 *   LSD 低位优先 O(d·(n+k))     O(n+k)     是（依赖每趟稳定） 从个位往高位排
 *   说明：基数排序本身不比较大小；只要每一趟计数排序是稳定的，低位已排好的次序才不会被高位打乱。
 *
 * 易错点：
 *   1. 每一趟按位排序必须稳定，否则高位排序会把同高位元素已有的低位次序冲乱（第 2 节反例）
 *   2. LSD 是低位优先，先排个位再排十位百位；写成 MSD 高位优先则要递归处理每个桶
 *   3. 取第 exp 位数字：(x / exp) % 10；exp 按 1、10、100… 翻倍
 *   4. 负数不能直接按十进制位排：本文件先用最小值做整体偏移变成非负，排完再减回去
 *
 * 与 Java 自带类的对应关系：
 *   Arrays.sort 不使用基数排序；基数排序常见于字符串排序、大批量整数排序与外部排序场景。
 *
 * 一句话口诀：
 *   低位优先逐趟稳，每趟计数按位分；稳定丢了位序乱，偏移先把负数平。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 RadixSortDemo.java
 *   java RadixSortDemo
 */
public class RadixSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. LSD 过程：看得见每一位的排序 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 稳定性是前提：不稳定的按位排序会排错 ==========");
        demoUnstableCounterexample();

        System.out.println();
        System.out.println("========== 3. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 4. 负数处理：偏移后按位排序 ==========");
        demoNegative();

        System.out.println();
        System.out.println("========== 5. 性能对比：基数排序 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 小数组：打印个位、十位、百位各一趟后的结果
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {329, 457, 657, 839, 436, 720, 355};
        System.out.println("排序前：" + Arrays.toString(arr));

        int[] work = arr.clone();
        int max = Arrays.stream(work).max().orElse(0);

        // exp 控制当前处理哪一位：1=个位，10=十位，100=百位……
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingPassByDigit(work, exp, false);
            System.out.println("按 " + digitName(exp) + " 排序后：" + Arrays.toString(work));
        }

        check(isSorted(work), "基数排序排完后整体有序");
        check(work[0] == 329 && work[work.length - 1] == 839, "最小值 329 在最前、最大值 839 在最后");
    }

    // ---------------------------------------------------------
    // 2. 反例：某一趟若不稳定，最终结果会错
    // ---------------------------------------------------------
    private static void demoUnstableCounterexample() {
        int[] stable = {19, 12, 15};
        int[] unstable = stable.clone();

        radixSortStable(stable);
        radixSortUnstable(unstable);

        int[] expected = stable.clone();
        Arrays.sort(expected);

        System.out.println("稳定基数排序：" + Arrays.toString(stable));
        System.out.println("不稳定基数排序：" + Arrays.toString(unstable));
        check(Arrays.equals(stable, expected), "稳定版本结果正确");
        check(!Arrays.equals(unstable, expected),
                "不稳定版本结果错误（同高位元素的低位次序被打乱），证明稳定性是基数排序的正确性前提");
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

            radixSort(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 4. 负数：整体加偏移量变成非负，排完再减回去
    // ---------------------------------------------------------
    private static void demoNegative() {
        int[] arr = {-13, 5, -7, -13, 0, 5, -7};
        int[] expected = arr.clone();
        Arrays.sort(expected);
        radixSort(arr);
        System.out.println("含负数排序后：" + Arrays.toString(arr));
        check(Arrays.equals(arr, expected), "经偏移处理后负数排序结果与 Arrays.sort 一致");
        check(arr[0] == -13 && arr[arr.length - 1] == 5, "最小负数 -13 在最前、最大值 5 在最后");
    }

    // ---------------------------------------------------------
    // 5. 性能实测
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);   // 非负，最多 5 位
        }

        int[] mine = data.clone();
        long t1 = System.nanoTime();
        radixSort(mine);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        int[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：基数排序 " + mineMs + " ms，Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(mine, jdk), "基数排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：基数排序线性但要多趟搬移和额外数组；位数多、值域宽时优势会被常数吃掉。");
    }

    // =========================================================
    // 排序实现
    // =========================================================

    /** 入口：先偏移消去负数，再做 LSD 基数排序，最后还原 */
    static void radixSort(int[] arr) {
        if (arr.length <= 1) return;

        int min = arr[0];
        for (int x : arr) min = Math.min(min, x);

        // 整体减去 min，平移成全非负；偏移不改变元素之间的大小关系
        int[] shifted = new int[arr.length];
        for (int i = 0; i < arr.length; i++) shifted[i] = arr[i] - min;

        radixSortNonNegative(shifted);

        for (int i = 0; i < arr.length; i++) arr[i] = shifted[i] + min;
    }

    /** 对非负整数做 LSD 基数排序 */
    private static void radixSortNonNegative(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingPassByDigit(arr, exp, false);
        }
    }

    /** 按某一位做一趟稳定计数排序；unstable=true 时故意反转桶内次序，制造不稳定反例 */
    private static void countingPassByDigit(int[] arr, int exp, boolean unstable) {
        int[] count = new int[10];
        for (int x : arr) count[(x / exp) % 10]++;

        // 前缀和定位
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];

        int[] output = new int[arr.length];
        if (!unstable) {
            // 稳定：倒序遍历原数组，相等数字保持原来的先后
            for (int i = arr.length - 1; i >= 0; i--) {
                int d = (arr[i] / exp) % 10;
                output[count[d] - 1] = arr[i];
                count[d]--;
            }
        } else {
            // 故意制造不稳定：正序遍历且桶内反序回填，相等数字次序被打乱
            for (int i = 0; i < arr.length; i++) {
                int d = (arr[i] / exp) % 10;
                output[count[d] - 1] = arr[i];
                count[d]--;
            }
        }
        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    /** 稳定版本入口（第 2 节对照用） */
    private static void radixSortStable(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingPassByDigit(arr, exp, false);
        }
    }

    /** 不稳定版本入口（第 2 节反例用） */
    private static void radixSortUnstable(int[] arr) {
        int max = Arrays.stream(arr).max().orElse(0);
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingPassByDigit(arr, exp, true);
        }
    }

    // =========================================================
    // 工具方法
    // =========================================================

    private static String digitName(int exp) {
        if (exp == 1) return "个位";
        if (exp == 10) return "十位";
        if (exp == 100) return "百位";
        return exp + " 位";
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
