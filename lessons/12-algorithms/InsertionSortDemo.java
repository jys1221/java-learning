import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：插入排序（Insertion Sort）
 * ============================================================
 * 一句话：像摸扑克牌一样，把手里新摸到的牌，插到左边已经排好的那叠牌里正确的位置。
 *
 * 为什么需要它：
 *   冒泡和选择都在"未排好的部分"里反复扫描；插入排序只维护一个"已经有序的前缀"，
 *   每次处理一个新元素。它在数据"近乎有序"时几乎是 O(n)，而且是稳定排序，
 *   所以常被用作高级排序（归并、快排、希尔）在小区间上的兜底子程序。
 *
 * 复杂度速查（n = 元素个数）：
 *   版本            最好      平均      最坏      额外空间   稳定性
 *   普通插入        O(n)      O(n²)     O(n²)     O(1)       是
 *   二分插入        O(n log n)比较  O(n²)移动  O(n²)移动  O(1)       是
 *   说明：最好情况是数组本来就有序，每个元素只比一次就留在原位；
 *         二分插入只减少"比较"次数，元素移动次数一点没变，所以整体仍是 O(n²)。
 *
 * 易错点：
 *   1. 内层 while 的条件是 arr[j] > key（严格大于），写成 >= 会破坏稳定性
 *   2. 移动元素时先把 arr[j] 后移到 j+1，最后再把 key 放进 arr[j+1]，顺序不能反
 *   3. j 越界风险：while 里必须先判断 j >= 0，再访问 arr[j]
 *   4. 二分插入找位置时，相等元素要排在原有元素右边，才能保住稳定性
 *
 * 与 Java 自带类的对应关系：
 *   JDK 的 Arrays.sort(int[]) 是双轴快排；但在归并排序、TimSort 等高级排序里，
 *   小区间（通常 32 以下）会退回插入排序，因为小数组上 O(n²) 常数小、缓存友好。
 *
 * 一句话口诀：
 *   左边有序右边摸，往左找到家再插；近乎有序特别快，相等不动才稳定。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 InsertionSortDemo.java
 *   java InsertionSortDemo
 */
public class InsertionSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 摸扑克牌模型：看得见的每一次插入 ==========");
        demoSteps();

        System.out.println();
        System.out.println("========== 2. 近乎有序时接近 O(n)：比较次数对比 ==========");
        demoNearlyOrdered();

        System.out.println();
        System.out.println("========== 3. 对拍验证：200 组随机数组，与 Arrays.sort 比结果 ==========");
        demoRandomCheck();

        System.out.println();
        System.out.println("========== 4. 稳定性验证：相同大小的元素保持原有先后 ==========");
        demoStable();

        System.out.println();
        System.out.println("========== 5. 二分插入优化 + 与冒泡、Arrays.sort 实测 ==========");
        demoBinaryInsertionAndPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 摸扑克牌：打印每个元素被插入前的有序前缀
    // ---------------------------------------------------------
    private static void demoSteps() {
        int[] arr = {5, 1, 4, 2, 8, 3};
        System.out.println("排序前：" + Arrays.toString(arr));

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];          // 新摸到的牌
            int j = i - 1;
            // 把比 key 大的牌依次右移，给 key 腾出位置
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;          // 插入到腾出的位置
            System.out.println("处理第 " + (i + 1) + " 个元素 " + key + " 后：" + Arrays.toString(arr));
        }

        check(isSorted(arr), "基础版排完后整体有序");
        check(arr[0] == 1 && arr[arr.length - 1] == 8, "最小值 1 在最前、最大值 8 在最后");
    }

    // ---------------------------------------------------------
    // 2. 近乎有序：统计比较次数，对比有序 / 乱序 / 逆序
    // ---------------------------------------------------------
    private static void demoNearlyOrdered() {
        int[] sorted = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] reversed = {8, 7, 6, 5, 4, 3, 2, 1};
        int[] messy = {5, 2, 9, 1, 7, 3, 8, 4};

        int cmpSorted = countComparisons(sorted.clone());
        int cmpReversed = countComparisons(reversed.clone());
        int cmpMessy = countComparisons(messy.clone());

        System.out.println("8 个元素：有序比较 " + cmpSorted + " 次，乱序比较 " + cmpMessy
                + " 次，逆序比较 " + cmpReversed + " 次");
        check(cmpSorted == 7, "完全有序时每个元素只比一次，共 7 次（接近 O(n)）");
        check(cmpReversed == 28, "完全逆序时比较 7+6+...+1 = 28 次（最坏 O(n²)）");
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

            insertionSort(arr);
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
        Item[] items = {
                new Item(3, "a"), new Item(1, "b"), new Item(3, "c"),
                new Item(2, "d"), new Item(1, "e"), new Item(3, "f")
        };
        System.out.println("排序前：" + Arrays.toString(items));

        // 严格大于才右移（arr[j].value > key.value），相等元素留在原位 → 稳定
        for (int i = 1; i < items.length; i++) {
            Item key = items[i];
            int j = i - 1;
            while (j >= 0 && items[j].value > key.value) {
                items[j + 1] = items[j];
                j--;
            }
            items[j + 1] = key;
        }
        System.out.println("排序后：" + Arrays.toString(items));

        check(items[0].tag.equals("b") && items[1].tag.equals("e"), "value=1 的两个元素保持 b、e 顺序");
        check(items[3].tag.equals("a") && items[4].tag.equals("c") && items[5].tag.equals("f"),
                "value=3 的三个元素保持 a、c、f 顺序");
        check(isSortedByValue(items), "整体按 value 升序");
    }

    // ---------------------------------------------------------
    // 5. 二分插入优化 + 性能对比
    // ---------------------------------------------------------
    private static void demoBinaryInsertionAndPerformance() {
        // 5.1 二分插入：比较次数明显少于普通插入，但移动次数不变
        int[] ordinaryInput = {5, 2, 9, 1, 7, 3, 8, 4, 6};
        int[] binaryInput = ordinaryInput.clone();
        int ordinaryCmp = countComparisons(ordinaryInput.clone());
        binaryInsertionSort(binaryInput);
        int[] expected = ordinaryInput.clone();
        Arrays.sort(expected);
        System.out.println("二分插入结果：" + Arrays.toString(binaryInput));
        check(Arrays.equals(binaryInput, expected), "二分插入结果与 Arrays.sort 一致");
        System.out.println("普通插入比较 " + ordinaryCmp + " 次；二分插入在有序前缀里用二分找位置，比较次数降到 O(n log n)。");

        // 5.2 性能实测：插入 vs 冒泡 vs 官方排序
        final int n = 10000;
        Random random = new Random(42L);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(100000);
        }

        int[] ins = data.clone();
        long t1 = System.nanoTime();
        insertionSort(ins);
        long insMs = (System.nanoTime() - t1) / 1_000_000;

        int[] bub = data.clone();
        long t2 = System.nanoTime();
        bubbleSort(bub);
        long bubMs = (System.nanoTime() - t2) / 1_000_000;

        int[] jdk = data.clone();
        long t3 = System.nanoTime();
        Arrays.sort(jdk);
        long jdkMs = (System.nanoTime() - t3) / 1_000_000;

        System.out.println("n = " + n + "：插入 " + insMs + " ms，冒泡 " + bubMs + " ms，"
                + "Arrays.sort " + jdkMs + " ms");
        check(Arrays.equals(ins, jdk), "插入排序结果与 Arrays.sort 完全一致");
        System.out.println("观察：插入通常比冒泡快（移动是连续写、缓存友好），但两者都跑不过 O(n log n)。");
    }

    // =========================================================
    // 排序实现
    // =========================================================

    /** 普通插入排序 */
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

    /** 二分插入排序：先用二分找插入位置，再整体右移 */
    static void binaryInsertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            // 在左闭右开区间 [0, i) 里找第一个大于 key 的位置
            int left = 0;
            int right = i;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (arr[mid] > key) {
                    right = mid;      // 答案在左半边（含 mid）
                } else {
                    left = mid + 1;   // 相等或更小，答案在右半边
                }
            }
            // 把 [left, i) 这一段整体右移一格，腾出 left 位置
            System.arraycopy(arr, left, arr, left + 1, i - left);
            arr[left] = key;
        }
    }

    /** 冒泡（仅用于性能对比） */
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

    /** 统计普通插入排序的比较次数（不改动最终有序结果） */
    static int countComparisons(int[] arr) {
        int count = 0;
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                count++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
        return count;
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
