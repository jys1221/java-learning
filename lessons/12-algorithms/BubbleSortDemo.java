import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：冒泡排序（Bubble Sort）—— 十大排序的第一课
 * ============================================================
 * 一句话：每一轮都把"还没排好的部分"里最大的那个，一路交换着"冒"到末尾；
 *        冒完一轮，尾部就多一个已经归位的元素。
 *
 * 为什么需要它：
 *   它是所有排序里最直观的一个 —— 只用到"比较 + 相邻交换"这两件事，
 *   没有任何额外空间，也没有复杂的分治。理解了它，才能看懂"为什么插入排序更快"、
 *   "为什么快排要用分治"。它的价值不在性能，而在"看得见的排序过程"。
 *
 * 复杂度速查（n = 元素个数）：
 *   版本            最好      平均      最坏      空间      稳定
 *   基础冒泡        O(n²)     O(n²)     O(n²)     O(1)      是
 *   优化冒泡        O(n)      O(n²)     O(n²)     O(1)      是
 *   说明：最好情况出现在"数组本来就有序"，优化版第一轮发现一次交换都没有，直接收工。
 *
 * 易错点：
 *   1. 内层循环边界写成 j < n，会访问 arr[j+1] 越界 —— 正确边界是 j < n-1-i
 *   2. 交换条件写成 >= 会破坏稳定性（相等的两个元素会被换位置）
 *   3. 优化版的"提前退出"只能在"本轮零交换"时触发，判断标志要每轮重置
 *   4. bubble sort 永远 O(n²) 级别的比较，n = 10 万时基本不能用
 *
 * 与 Java 自带类的对应关系：
 *   JDK 的 Arrays.sort(int[]) 用的是双轴快排（Dual-Pivot Quicksort），
 *   时间复杂度 O(n log n)，比冒泡快几个数量级；
 *   冒泡只在"数据量很小且近乎有序"或"教学演示"时才有意义。
 *
 * 一句话口诀：
 *   相邻比较、逆序就换；一轮冒出一个最大值；没换过说明已经有序，提前收工。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BubbleSortDemo.java
 *   java BubbleSortDemo
 */
public class BubbleSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 基础版：看得见的每一轮 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 优化版：没发生交换就提前收工 ==========");
        demoEarlyExit();

        System.out.println();
        System.out.println("========== 3. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 4. 稳定性验证：相同大小的元素保持原有先后 ==========");
        demoStable();

        System.out.println();
        System.out.println("========== 5. 性能对比：冒泡 vs JDK 的 Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 基础版：把小数组每一轮的结果打印出来
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {5, 1, 4, 2, 8, 3};
        System.out.println("排序前：" + Arrays.toString(arr));

        int n = arr.length;
        // 外层 i：已经排好的元素个数。排好一个，待排区间就少一个
        for (int i = 0; i < n - 1; i++) {
            // 内层 j：在 0 ~ n-2-i 这个"待排区间"里，两两比较相邻元素
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 逆序就交换：这三行是"交换两个变量"的标准写法，需要一个临时变量
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            // 每轮结束，arr[n-1-i] 一定已经是待排区间里的最大值
            System.out.println("第 " + (i + 1) + " 轮后：" + Arrays.toString(arr)
                    + "（arr[" + (n - 1 - i) + "]=" + arr[n - 1 - i] + " 已归位）");
        }

        check(isSorted(arr), "基础版排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 8, "最小值 1 在最前、最大值 8 在最后");
    }

    // ---------------------------------------------------------
    // 2. 优化版：本轮一次交换都没发生，说明已经有序
    // ---------------------------------------------------------
    private static void demoEarlyExit() {
        // 2.1 完全有序的数组：基础版还要跑 n-1 轮，优化版第一轮就退出
        int[] sortedInput = {1, 2, 3, 4, 5, 6, 7, 8};
        int baseCompare = countComparisonsBasic(sortedInput.clone());
        int optCompare = countComparisonsOptimized(sortedInput.clone());
        System.out.println("对已有序的 8 个元素：基础版比较 " + baseCompare + " 次，优化版只比较 "
                + optCompare + " 次");
        check(baseCompare == 28, "基础版固定比较 7+6+5+4+3+2+1 = 28 次");
        check(optCompare == 7, "优化版首轮 7 次比较后直接收工");

        // 2.2 乱序数组：优化版结果必须依然正确
        int[] arr = {5, 2, 9, 1, 7, 3};
        int[] expected = arr.clone();
        Arrays.sort(expected);
        bubbleSortOptimized(arr);
        System.out.println("乱序数组优化版结果：" + Arrays.toString(arr));
        check(Arrays.equals(arr, expected), "优化版对乱序数组结果正确");
    }

    // ---------------------------------------------------------
    // 3. 对拍：随机数据 + 官方排序做参照
    // ---------------------------------------------------------
    private static void demoRandomCheck() {
        Random random = new Random(20261128L);   // 固定种子，任何机器跑出来都一样
        int round = 0;

        for (int t = 0; t < 200; t++) {
            int len = random.nextInt(50) + 1;    // 长度 1 ~ 50
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = random.nextInt(200) - 100;   // 含负数，顺带验证比较运算符没用错
            }

            int[] expected = arr.clone();
            Arrays.sort(expected);               // 官方排序当"标准答案"

            bubbleSortOptimized(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 4. 稳定性：值相同的元素，排序后相对顺序不变
    // ---------------------------------------------------------
    private static void demoStable() {
        // tag 记录"原来是第几个出现的"，用来检查相同 value 的先后关系
        Item[] items = {
                new Item(3, "a"), new Item(1, "b"), new Item(3, "c"),
                new Item(2, "d"), new Item(1, "e"), new Item(3, "f")
        };
        System.out.println("排序前：" + Arrays.toString(items));

        // 只在严格大于时才交换（arr[j].value > arr[j+1].value），相等就不动 → 稳定
        for (int i = 0; i < items.length - 1; i++) {
            for (int j = 0; j < items.length - 1 - i; j++) {
                if (items[j].value > items[j + 1].value) {
                    Item temp = items[j];
                    items[j] = items[j + 1];
                    items[j + 1] = temp;
                }
            }
        }
        System.out.println("排序后：" + Arrays.toString(items));

        // 期望：value=1 的 b、e 顺序不变；value=3 的 a、c、f 顺序不变
        check(items[0].tag.equals("b") && items[1].tag.equals("e"), "value=1 的两个元素保持 b、e 顺序");
        check(items[3].tag.equals("a") && items[4].tag.equals("c") && items[5].tag.equals("f"),
                "value=3 的三个元素保持 a、c、f 顺序");
        check(isSortedByValue(items), "整体按 value 升序");
    }

    // ---------------------------------------------------------
    // 5. 性能：同样是排序，算法不同差距悬殊
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        // 冒泡：O(n²)，约 2 亿次比较
        int[] bubbleInput = data.clone();
        long t1 = System.nanoTime();
        bubbleSortOptimized(bubbleInput);
        long bubbleMs = (System.nanoTime() - t1) / 1_000_000;

        // 官方排序：O(n log n)
        int[] jdkInput = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdkInput);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：冒泡 " + bubbleMs + " ms，" + "Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(bubbleInput, jdkInput), "两种排序结果完全一致（冒泡确实排对了）");
        System.out.println("结论：结果一样，代价天差地别 —— 复杂度不是纸面数字，是真实时间。");
    }

    // =========================================================
    // 三个排序实现
    // =========================================================

    /** 优化版冒泡：本轮零交换 → 已经有序 → 立刻退出 */
    static void bubbleSortOptimized(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;              // 每轮都要重置，这就是易错点 3
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;                            // 一次交换都没有，后面的轮次纯属浪费
            }
        }
    }

    /** 基础版冒泡，同时返回比较次数（用于对比练习） */
    static int countComparisonsBasic(int[] arr) {
        int n = arr.length;
        int count = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                count++;
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return count;
    }

    /** 优化版冒泡，同时返回比较次数 */
    static int countComparisonsOptimized(int[] arr) {
        int n = arr.length;
        int count = 0;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                count++;
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return count;
    }

    // =========================================================
    // 辅助类与工具方法
    // =========================================================

    /** 带标签的元素，标签用来观察"相同值之间的相对顺序是否被打乱" */
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
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedByValue(Item[] items) {
        for (int i = 1; i < items.length; i++) {
            if (items[i - 1].value > items[i].value) {
                return false;
            }
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
