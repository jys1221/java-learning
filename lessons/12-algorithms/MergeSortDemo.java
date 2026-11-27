import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：归并排序（Merge Sort）—— 分治思想的代表作
 * ============================================================
 * 一句话：先把数组对半拆到最小，再把两个已经有序的小段"合并"成一个有序的大段。
 *
 * 为什么需要它：
 *   冒泡 / 选择 / 插入都是 O(n²)，碰到上万数据就吃力；归并是最早稳定达到 O(n log n)
 *   的排序之一，而且是稳定排序。它的代价是需要一块临时数组 O(n) 空间。理解归并也是
 *   理解"分治"（拆开 → 各自解决 → 合并）的最好入口。
 *
 * 复杂度速查（n = 元素个数）：
 *   版本            最好      平均      最坏      额外空间   稳定性
 *   自顶向下        O(n log n) O(n log n) O(n log n)  O(n)      是
 *   自底向上        O(n log n) O(n log n) O(n log n)  O(n)      是
 *   说明：每层合并总共 O(n)，一共 log n 层；临时数组用于归并时暂存两段。
 *
 * 易错点：
 *   1. 合并时两段都要防越界：i 不能超过 mid，j 不能超过 right
 *   2. 相等元素必须优先取左半段（<= 而不是 <），否则会破坏稳定性
 *   3. 合并后剩余元素要分别拷贝完，不能只拷其中一段
 *   4. 递归终止条件是 left >= right（区间只剩 0 或 1 个元素）
 *
 * 与 Java 自带类的对应关系：
 *   对象排序 Arrays.sort(Object[]) 底层是 TimSort（归并 + 插入的混合）；
 *   本文件手写的是纯归并，思路相同但没有 TimSort 的小区间优化与原地合并技巧。
 *
 * 一句话口诀：
 *   一拆两半各自排，再比头部并起来；相等先取左边段，稳是稳来费空间。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 MergeSortDemo.java
 *   java MergeSortDemo
 */
public class MergeSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 分治过程：看得见的拆分与合并 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 3. 稳定性验证：相同大小的元素保持原有先后 ==========");
        demoStable();

        System.out.println();
        System.out.println("========== 4. 自底向上归并：与递归版结果一致 ==========");
        demoBottomUp();

        System.out.println();
        System.out.println("========== 5. 性能对比：归并 vs Arrays.sort ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 小数组：打印每次合并后的结果
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {5, 1, 4, 2, 8, 3, 7, 6};
        System.out.println("排序前：" + Arrays.toString(arr));
        mergeSort(arr);
        System.out.println("排序后：" + Arrays.toString(arr));

        check(isSorted(arr), "归并排序排完后整体有序");
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

            mergeSort(arr);
            if (!Arrays.equals(arr, expected)) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr));
            }
            round++;
        }
        check(round == 200, "200 组随机数组（含负数）全部与 Arrays.sort 结果一致");
    }

    // ---------------------------------------------------------
    // 3. 稳定性：相等元素保持原有先后
    // ---------------------------------------------------------
    private static void demoStable() {
        Item[] items = {
                new Item(3, "a"), new Item(1, "b"), new Item(3, "c"),
                new Item(2, "d"), new Item(1, "e"), new Item(3, "f")
        };
        System.out.println("排序前：" + Arrays.toString(items));
        mergeSortItems(items);
        System.out.println("排序后：" + Arrays.toString(items));

        check(items[0].tag.equals("b") && items[1].tag.equals("e"), "value=1 的两个元素保持 b、e 顺序");
        check(items[3].tag.equals("a") && items[4].tag.equals("c") && items[5].tag.equals("f"),
                "value=3 的三个元素保持 a、c、f 顺序");
        check(isSortedByValue(items), "整体按 value 升序");
    }

    // ---------------------------------------------------------
    // 4. 自底向上：不递归，按宽度 1、2、4、8… 逐层合并
    // ---------------------------------------------------------
    private static void demoBottomUp() {
        Random random = new Random(5L);
        int[] a = new int[30];
        int[] b = new int[30];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(100) - 50;
            b[i] = a[i];
        }
        mergeSort(a);
        mergeSortBottomUp(b);
        System.out.println("自底向上结果：" + Arrays.toString(b));
        check(Arrays.equals(a, b), "自顶向下与自底向上两种归并结果一致");
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

        int[] mine = data.clone();
        long t1 = System.nanoTime();
        mergeSort(mine);
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        int[] jdk = data.clone();
        long t2 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：归并 " + mineMs + " ms，Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(mine, jdk), "归并排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：归并稳定 O(n log n)，但要额外 O(n) 临时空间；单毫秒计时受噪声影响，"
                + "官方排序在工程常数与缓存利用上更优。");
    }

    // =========================================================
    // 排序实现：自顶向下递归
    // =========================================================

    /** 入口：申请一块临时数组，避免递归里反复 new */
    static void mergeSort(int[] arr) {
        if (arr.length <= 1) return;
        int[] temp = new int[arr.length];
        mergeSort(arr, temp, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] temp, int left, int right) {
        if (left >= right) return;                 // 区间只剩 0 或 1 个元素，天然有序
        int mid = left + (right - left) / 2;        // 防溢出的中点写法
        mergeSort(arr, temp, left, mid);           // 排左半段
        mergeSort(arr, temp, mid + 1, right);      // 排右半段
        merge(arr, temp, left, mid, right);        // 合并两段
    }

    /** 把 [left, mid] 与 [mid+1, right] 两个有序段合并回 arr */
    private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
        // 先把待合并区间拷到临时数组，归并过程直接读 temp、写回 arr
        System.arraycopy(arr, left, temp, left, right - left + 1);
        int i = left;       // 左半段指针
        int j = mid + 1;    // 右半段指针
        int k = left;       // 写回位置

        while (i <= mid && j <= right) {
            // 相等时优先取左半段，保证稳定性
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }
        // 左半段还有剩余，依次拷回
        while (i <= mid) arr[k++] = temp[i++];
        // 右半段还有剩余，依次拷回（左半拷完时这段才会生效）
        while (j <= right) arr[k++] = temp[j++];
    }

    // =========================================================
    // 排序实现：自底向上迭代
    // =========================================================

    static void mergeSortBottomUp(int[] arr) {
        int n = arr.length;
        if (n <= 1) return;
        int[] temp = new int[n];
        // width 是当前已合并小段的宽度：1 → 2 → 4 → 8 …
        for (int width = 1; width < n; width *= 2) {
            for (int left = 0; left < n; left += 2 * width) {
                int mid = Math.min(left + width - 1, n - 1);
                int right = Math.min(left + 2 * width - 1, n - 1);
                if (mid < right) {   // 右半段确实存在才合并
                    merge(arr, temp, left, mid, right);
                }
            }
        }
    }

    // =========================================================
    // 带标签元素的归并（稳定性演示）
    // =========================================================

    static void mergeSortItems(Item[] items) {
        if (items.length <= 1) return;
        Item[] temp = new Item[items.length];
        mergeSortItems(items, temp, 0, items.length - 1);
    }

    private static void mergeSortItems(Item[] items, Item[] temp, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortItems(items, temp, left, mid);
        mergeSortItems(items, temp, mid + 1, right);
        mergeItems(items, temp, left, mid, right);
    }

    private static void mergeItems(Item[] items, Item[] temp, int left, int mid, int right) {
        System.arraycopy(items, left, temp, left, right - left + 1);
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (temp[i].value <= temp[j].value) {
                items[k++] = temp[i++];
            } else {
                items[k++] = temp[j++];
            }
        }
        while (i <= mid) items[k++] = temp[i++];
        while (j <= right) items[k++] = temp[j++];
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
