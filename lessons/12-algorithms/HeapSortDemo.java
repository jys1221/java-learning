import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：堆排序（Heap Sort）—— 用大顶堆原地完成排序
 * ============================================================
 * 一句话：先把数组原地建成大顶堆（堆顶永远是当前最大值），再反复把堆顶换到末尾、缩小堆范围并下沉修复。
 *
 * 为什么需要它：
 *   归并快但要 O(n) 临时空间，快排平均快但最坏会退化；堆排序把这两点都兼顾了 ——
 *   O(n log n) 时间、O(1) 额外空间，且不会出现快排那样的最坏 O(n²)。它也是
 *   TopK、优先队列等问题的底层思路。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作          最好        平均        最坏        额外空间   稳定性
 *   建堆          O(n)        O(n)        O(n)        O(1)       ——
 *   堆排序        O(n log n)  O(n log n)  O(n log n)  O(1)       否（不稳定）
 *   说明：每次取出堆顶 O(log n)，共取 n 次；建堆之所以是 O(n) 而不是 O(n log n)，
 *         是因为越靠下的节点下沉层数越少，分摊后总和线性。
 *
 * 易错点：
 *   1. 建堆要从最后一个非叶子节点（下标 n/2-1）往前下沉，从 0 开始下沉会漏掉下层节点
 *   2. 下沉时左右孩子下标是 2i+1、2i+2，且必须先判断孩子下标 < heapSize 再访问
 *   3. 每次把堆顶换到末尾后，堆范围要减一，末尾已归位的元素不能再参与下沉
 *   4. 堆排序不稳定：堆顶与末尾元素跨距离交换，相等元素可能被打乱次序
 *
 * 与 Java 自带类的对应关系：
 *   java.util.PriorityQueue 是小顶堆（堆顶最小），本文件用大顶堆做升序排序；
 *   Arrays.sort 底层不是堆排序，但堆的思想大量出现在优先队列、TopK 与定时器实现里。
 *
 * 一句话口诀：
 *   原地建成大顶堆，堆顶最大换末尾；缩小堆来再下沉，对数时间常量空间。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 HeapSortDemo.java
 *   java HeapSortDemo
 */
public class HeapSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 建堆与下沉：看得见的每一步 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 3. 稳定性验证：用具体数据证明它不稳定 ==========");
        demoUnstable();

        System.out.println();
        System.out.println("========== 4. 性能对比：堆排序 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 小数组：打印建堆后与每次交换后的结果
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {5, 1, 4, 2, 8, 3, 7, 6};
        System.out.println("排序前：" + Arrays.toString(arr));

        int n = arr.length;
        // 第一步：原地建大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(arr, i, n);
        }
        System.out.println("建成大顶堆：" + Arrays.toString(arr) + "（堆顶 " + arr[0] + " 是最大值）");

        // 第二步：堆顶换到末尾，缩小堆，再下沉修复
        for (int end = n - 1; end > 0; end--) {
            swap(arr, 0, end);
            siftDown(arr, 0, end);
            System.out.println("把最大值换到末尾后：" + Arrays.toString(arr));
        }

        check(isSorted(arr), "堆排序排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 8, "最小值 1 在最前、最大值 8 在最后");
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

            heapSort(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 3. 不稳定证明：堆顶与末尾跨距离交换，相等元素次序被打乱
    // ---------------------------------------------------------
    private static void demoUnstable() {
        // 两个值都是 2，标签 a、b 记录出场先后；另有一个 1
        Item[] items = {
                new Item(2, "a"), new Item(2, "b"), new Item(1, "c")
        };
        System.out.println("排序前：" + Arrays.toString(items));

        int n = items.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDownItems(items, i, n);
        }
        for (int end = n - 1; end > 0; end--) {
            Item temp = items[0];
            items[0] = items[end];
            items[end] = temp;
            siftDownItems(items, 0, end);
        }
        System.out.println("排序后：" + Arrays.toString(items));

        // 稳定排序本该是 1(c)、2(a)、2(b)；堆排序得到 1(c)、2(b)、2(a)
        check(items[0].tag.equals("c"), "值为 1 的 c 被换到最前面");
        check(items[1].tag.equals("b") && items[2].tag.equals("a"),
                "两个值为 2 的元素先后被打乱（b 跑到 a 前面），证明堆排序不稳定");
        check(isSortedByValue(items), "整体仍按 value 升序");
    }

    // ---------------------------------------------------------
    // 4. 性能实测
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 20000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        int[] mine = data.clone();
        long t1 = System.nanoTime();
        heapSort(mine);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        int[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：堆排序 " + mineMs + " ms，Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(mine, jdk), "堆排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：堆排序额外空间 O(1)，但下沉是跳跃式访问、缓存不连续，常数因子通常不如快排。");
    }

    // =========================================================
    // 排序实现
    // =========================================================

    /** 堆排序：原地建堆 + 反复交换堆顶并下沉 */
    static void heapSort(int[] arr) {
        int n = arr.length;
        if (n <= 1) return;
        // 第一步：从最后一个非叶子节点向前逐个下沉，建成大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(arr, i, n);
        }
        // 第二步：堆顶（最大值）换到末尾，堆范围减一，再下沉修复堆
        for (int end = n - 1; end > 0; end--) {
            swap(arr, 0, end);
            siftDown(arr, 0, end);
        }
    }

    /** 下沉：把下标 i 的元素向下调整到合适位置，heapSize 是当前堆的有效长度 */
    private static void siftDown(int[] arr, int i, int heapSize) {
        while (true) {
            int left = 2 * i + 1;    // 左孩子下标
            int right = 2 * i + 2;   // 右孩子下标
            int largest = i;
            if (left < heapSize && arr[left] > arr[largest]) largest = left;
            if (right < heapSize && arr[right] > arr[largest]) largest = right;
            if (largest == i) break;   // 已经比两个孩子都大，下沉到位
            swap(arr, i, largest);
            i = largest;               // 继续向下检查
        }
    }

    // =========================================================
    // 带标签元素的堆排序（稳定性演示）
    // =========================================================

    private static void siftDownItems(Item[] items, int i, int heapSize) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;
            if (left < heapSize && items[left].value > items[largest].value) largest = left;
            if (right < heapSize && items[right].value > items[largest].value) largest = right;
            if (largest == i) break;
            Item temp = items[i];
            items[i] = items[largest];
            items[largest] = temp;
            i = largest;
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

    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
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
