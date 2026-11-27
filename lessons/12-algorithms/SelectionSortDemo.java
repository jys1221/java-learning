import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：选择排序（Selection Sort）
 * ============================================================
 * 一句话：每一轮从"还没排好的部分"里挑出最小的那个，直接放到这一轮的最前面。
 *
 * 为什么需要它：
 *   冒泡/插入都是靠"相邻交换"挪位置，比较多、交换也多；选择排序反过来 ——
 *   它每轮先把最小元素的下标找出来，整轮最多只做一次交换（最多 n-1 次写操作）。
 *   所以在"交换/写回很贵"的场景（比如元素是大对象、复制成本高）里，它反而有意义。
 *   它和冒泡一样是教学用的 O(n²) 算法，价值在于把"找极值 + 放到前面"的思路讲透。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作          最好       平均       最坏       额外空间   稳定性
 *   选择排序       O(n²)      O(n²)      O(n²)      O(1)       否（不稳定）
 *   说明：无论数组本来有没有序，都要把剩余区间扫一遍找最小值，所以最好也是 O(n²)；
 *         比较次数固定约 n(n-1)/2，但交换次数最多只有 n-1 次。
 *
 * 易错点：
 *   1. 每轮先记录最小值下标 min，而不是一看到更小的就立刻交换 —— 立刻交换会做大量无效写操作
 *   2. 内层循环从 i+1 开始，不要把已排好的前缀再比一遍
 *   3. 找到 min 后要判断 min != i 再交换，自己和自己交换虽然不报错但属于多余写操作
 *   4. 选择排序是不稳定的：跨位置交换会把相等元素的先后顺序打乱（本文件第 4 节用具体数据演示）
 *
 * 与 Java 自带类的对应关系：
 *   JDK 的 Arrays.sort(int[]) 是双轴快排，O(n log n)；选择排序只在"数据量极小、
 *   且每次交换成本很高"时才有讨论价值，工程上几乎不直接使用。
 *
 * 一句话口诀：
 *   每轮扫描找最小，记好下标再交换；比较很多交换少，相等顺序保不住。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 SelectionSortDemo.java
 *   java SelectionSortDemo
 */
public class SelectionSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 基础版：看得见的每一轮 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 与冒泡、插入的实测对比 ==========");
        demoCompareWithBubbleAndInsertion();

        System.out.println();
        System.out.println("========== 3. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 4. 稳定性验证：用具体数据证明它不稳定 ==========");
        demoUnstable();

        System.out.println();
        System.out.println("========== 5. 性能对比：选择排序 vs JDK 的 Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 基础版：把每一轮选出的最小值打印出来
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {5, 1, 4, 2, 8, 3};
        System.out.println("排序前：" + Arrays.toString(arr));

        int n = arr.length;
        // 外层 i：待排区间的左边界；前 i 个位置已经排好
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;   // 先假设待排区间第一个就是最小的
            // 内层 j：从 i+1 起扫描，找比 arr[minIndex] 更小的元素
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;   // 只记录下标，不立刻交换
                }
            }
            // 扫描完才交换：把最小值放到待排区间最前面
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
            System.out.println("第 " + (i + 1) + " 轮后：" + Arrays.toString(arr)
                    + "（arr[" + i + "]=" + arr[i] + " 已归位）");
        }

        check(isSorted(arr), "基础版排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 8, "最小值 1 在最前、最大值 8 在最后");
    }

    // ---------------------------------------------------------
    // 2. 与冒泡、插入的实测对比：同一份数据，三种 O(n²) 排序各跑一遍
    // ---------------------------------------------------------
    private static void demoCompareWithBubbleAndInsertion() {
        final int n = 10000;
        Random random = new Random(7L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        long t1 = System.nanoTime();
        int[] selectionInput = data.clone();
        selectionSort(selectionInput);
        long selectionMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int[] bubbleInput = data.clone();
        bubbleSort(bubbleInput);
        long bubbleMs = (System.nanoTime() - t2) / 1_000_000;

        long t3 = System.nanoTime();
        int[] insertionInput = data.clone();
        insertionSort(insertionInput);
        long insertionMs = (System.nanoTime() - t3) / 1_000_000;

        System.out.println("n = " + n + "：选择 " + selectionMs + " ms，"
                + "冒泡 " + bubbleMs + " ms，插入 " + insertionMs + " ms");
        // 三种排序结果必须一致
        check(Arrays.equals(selectionInput, bubbleInput), "选择与冒泡结果一致");
        check(Arrays.equals(selectionInput, insertionInput), "选择与插入结果一致");
        System.out.println("观察：三者都是 O(n²)，插入通常最省（移动是顺序的、缓存友好）；"
                + "选择交换次数最少，但比较次数一点不少。");
    }

    // ---------------------------------------------------------
    // 3. 对拍：随机数据 + 官方排序当标准答案
    // ---------------------------------------------------------
    private static void demoRandomCheck() {
        Random random = new Random(20261128L);
        int round = 0;

        for (int t = 0; t < 200; t++) {
            int len = random.nextInt(50) + 1;          // 长度 1 ~ 50
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = random.nextInt(200) - 100;    // 含负数
            }

            int[] expected = arr.clone();
            Arrays.sort(expected);

            selectionSort(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 4. 不稳定证明：选择排序会打乱相等元素的先后顺序
    // ---------------------------------------------------------
    private static void demoUnstable() {
        // 两个值都是 2，用标签 a、b 记录出场先后；另有一个 1 要被换到最前面
        Item[] items = {
                new Item(2, "a"), new Item(2, "b"), new Item(1, "c")
        };
        System.out.println("排序前：" + Arrays.toString(items));

        // 选择排序：i=0 时最小值 1 在下标 2，直接和下标 0 跨位置交换
        int n = items.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (items[j].value < items[minIndex].value) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Item temp = items[i];
                items[i] = items[minIndex];
                items[minIndex] = temp;
            }
        }
        System.out.println("排序后：" + Arrays.toString(items));

        // 稳定排序本该得到：1(c)、2(a)、2(b)；选择排序得到：1(c)、2(b)、2(a)
        check(items[0].tag.equals("c"), "值为 1 的 c 被换到最前面");
        check(items[1].tag.equals("b") && items[2].tag.equals("a"),
                "两个值为 2 的元素先后被打乱（b 跑到 a 前面），证明选择排序不稳定");
        check(isSortedByValue(items), "整体仍按 value 升序（不稳定 ≠ 排错，只是相等元素次序变了）");
    }

    // ---------------------------------------------------------
    // 5. 性能：和官方排序放在一起看差距
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 10000;   // O(n²)，取小一点避免拖慢整份文件
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        int[] mine = data.clone();
        long t1 = System.nanoTime();
        selectionSort(mine);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        int[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：选择排序 " + mineMs + " ms，Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(mine, jdk), "选择排序结果与 Arrays.sort 完全一致");
        System.out.println("结论：选择排序比较次数固定 O(n²)，工程上只用于教学和极小数据。");
    }

    // =========================================================
    // 排序实现
    // =========================================================

    /** 选择排序：每轮找最小下标，最多一次交换 */
    static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    /** 优化版冒泡（仅用于第 2 节对比） */
    static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    /** 插入排序（仅用于第 2 节对比） */
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

    /** 带标签的元素，用来观察相等元素的相对顺序 */
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
