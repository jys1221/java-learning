import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：二分查找三套模板（Binary Search Templates）
 * ============================================================
 * 一句话：有序数组上每次砍掉一半；本文件把"精确查找、左边界、右边界、插入位置、二分答案"
 *         五套写法统一在左闭右开区间 [left, right) 下，模板化记忆。
 *
 * 为什么需要它：
 *   普通二分只会"找到就返回"，但工程里更常问"第一个 >= x 的位置""最后一个 <= x 的位置"
 *   "最小的满足条件的值"。这些题都靠二分模板，区间定义（闭还是开）一旦写错就会漏元素或死循环。
 *   这份文件和仓库里已有的 BinarySearchDemo 不重复：那份讲递归/迭代基础，这份专攻三套边界模板。
 *
 * 复杂度速查（n = 元素个数）：
 *   模板              区间约定        返回值                          时间
 *   精确查找          [left, right)   找到返回下标，找不到 -1          O(log n)
 *   lowerBound 左边界 [left, right)   第一个 >= target 的下标          O(log n)
 *   upperBound 右边界 [left, right)   第一个 >  target 的下标          O(log n)
 *   插入位置          同 lowerBound   元素应插入的下标（已存在则在其前） O(log n)
 *   二分答案          答案区间        最小的满足条件 predicate 的值     O(log 答案范围)
 *
 * 易错点：
 *   1. 区间必须前后一致：本文件统一用左闭右开 [left, right)，所以 right 初值是 n（不是 n-1），
 *      收缩时 right = mid（不是 mid-1）；混用闭区间写法是二分最常见的死循环/漏元素原因
 *   2. mid 写成 (left + right) / 2 在两数都接近 int 上限时会溢出，标准写法 left + (right-left)/2
 *   3. lowerBound 用 arr[mid] < target 才右移；upperBound 用 arr[mid] <= target 才右移，差一个等号
 *   4. Arrays.binarySearch 遇到重复元素只返回"其中任意一个"下标，要最左/最右必须自己用 lower/upperBound
 *   5. 二分的前提是数组有序；无序数组上二分结果不可靠
 *
 * 与 Java 自带类的对应关系：
 *   Arrays.binarySearch(int[], key) 即精确查找，找到返回任意一个匹配下标，找不到返回 -(插入点)-1；
 *   JDK 没有直接提供 lowerBound / upperBound，需要按本文件模板手写。
 *
 * 一句话口诀：
 *   左闭右开记心间，mid 防溢写中间；小于右移否则左，左界右界看等号；找不到时回 -1，答案二分找最小。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BinarySearchTemplateDemo.java
 *   java BinarySearchTemplateDemo
 */
public class BinarySearchTemplateDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 模板一：精确查找（找不到返回 -1） ==========");
        demoExactSearch();

        System.out.println();
        System.out.println("========== 2. 模板二：lowerBound 左边界 / upperBound 右边界 ==========");
        demoBoundTemplates();

        System.out.println();
        System.out.println("========== 3. 插入位置 + 与 Arrays.binarySearch 对照 ==========");
        demoInsertionAndJdk();

        System.out.println();
        System.out.println("========== 4. 模板五：二分答案（最小的满足条件的值） ==========");
        demoBinaryAnswer();

        System.out.println();
        System.out.println("========== 5. 对拍验证：200 组随机有序数组 + 性能实测 ==========");
        demoRandomCheckAndPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 精确查找
    // ---------------------------------------------------------
    private static void demoExactSearch() {
        int[] arr = {1, 3, 5, 7, 9, 11, 13, 15};
        System.out.println("有序数组：" + Arrays.toString(arr));

        check(exactSearch(arr, 7) == 3, "找 7，返回下标 3");
        check(exactSearch(arr, 1) == 0, "找首元素 1，返回下标 0");
        check(exactSearch(arr, 15) == 7, "找尾元素 15，返回下标 7");
        check(exactSearch(arr, 8) == -1, "找不存在的 8，返回 -1（语义：未找到）");
    }

    // ---------------------------------------------------------
    // 2. 左边界 / 右边界模板
    // ---------------------------------------------------------
    private static void demoBoundTemplates() {
        // 重复元素集中在中间，方便观察"最左"和"最右"
        int[] arr = {1, 2, 2, 2, 2, 3, 5};
        System.out.println("有序数组：" + Arrays.toString(arr));

        int left = lowerBound(arr, 2);    // 第一个 >= 2
        int right = upperBound(arr, 2);   // 第一个 > 2
        System.out.println("target=2：lowerBound=" + left + "，upperBound=" + right
                + "，等于 2 的区间是 [" + left + ", " + right + ")");

        check(left == 1, "lowerBound 返回最左的 2（下标 1）");
        check(right == 5, "upperBound 返回最右 2 的下一位（下标 5）");
        check(lowerBound(arr, 4) == 6, "不存在的 4，lowerBound 指到 5 所在下标 6");
        check(upperBound(arr, 5) == 7, "upperBound 超过末尾时返回数组长度 7");
        System.out.println("循环不变量：区间 [left, right) 内始终包含答案；arr[mid] < target 时答案在右半，"
                + "left=mid+1；否则答案在左半（含 mid），right=mid。");
    }

    // ---------------------------------------------------------
    // 3. 插入位置 + 与 JDK 对照
    // ---------------------------------------------------------
    private static void demoInsertionAndJdk() {
        int[] arr = {1, 2, 2, 2, 3, 5};
        System.out.println("有序数组：" + Arrays.toString(arr));

        // 插入位置就是 lowerBound：把 target 放进去仍保持有序的下标
        int insertPos = lowerBound(arr, 4);
        System.out.println("4 的插入位置：" + insertPos + "（插到下标 " + insertPos + " 处，前面 3 后面 5）");
        check(insertPos == 5, "lowerBound 给出正确插入位置");

        // JDK 对照：重复元素时 Arrays.binarySearch 返回任意一个匹配下标
        int jdkResult = Arrays.binarySearch(arr, 2);
        int myLeft = lowerBound(arr, 2);
        System.out.println("target=2：Arrays.binarySearch 返回 " + jdkResult
                + "（重复元素中任意一个），lowerBound 返回最左 " + myLeft);
        check(myLeft == 1, "lowerBound 稳定返回最左下标 1");
        check(arr[jdkResult] == 2, "Arrays.binarySearch 返回的下标确实值为 2（但不一定是最左）");

        // 找不到时 JDK 返回 -(插入点)-1，可反推插入点
        int jdkMissing = Arrays.binarySearch(arr, 4);
        int derivedInsert = -(jdkMissing + 1);
        System.out.println("找 4：Arrays.binarySearch 返回 " + jdkMissing
                + "，反推插入点 " + derivedInsert);
        check(derivedInsert == lowerBound(arr, 4), "JDK 找不到时的返回值与 lowerBound 插入点一致");
    }

    // ---------------------------------------------------------
    // 4. 二分答案：求最小的 x 使 x² >= n（向上取整的平方根）
    // ---------------------------------------------------------
    private static void demoBinaryAnswer() {
        // 单调条件：x 越大，x² >= n 越容易成立；答案是满足条件的最小值
        check(minSqrtCeil(9) == 3, "n=9，最小满足 x²>=9 的 x 是 3");
        check(minSqrtCeil(10) == 4, "n=10，最小满足 x²>=10 的 x 是 4（3²=9 不够）");
        check(minSqrtCeil(1) == 1, "n=1，答案是 1");
        System.out.println("二分答案要点：先把问题写成一个单调的判定条件 predicate(x)，"
                + "再在答案区间上二分找最小成立点；本示例 predicate 为 x*x >= n。");
    }

    // ---------------------------------------------------------
    // 5. 对拍 + 性能实测
    // ---------------------------------------------------------
    private static void demoRandomCheckAndPerformance() {
        Random random = new Random(20261128L);
        int round = 0;

        for (int t = 0; t < 200; t++) {
            int len = random.nextInt(50) + 1;
            int[] arr = new int[len];
            for (int i = 0; i < len; i++) {
                arr[i] = random.nextInt(20);   // 值域窄，制造重复元素
            }
            Arrays.sort(arr);

            int target = random.nextInt(22) - 1;

            // 线性扫描当标准答案
            int expectedExact = linearIndex(arr, target);
            int expectedLower = linearLowerBound(arr, target);
            int expectedUpper = linearUpperBound(arr, target);

            // 精确查找遇到重复元素返回任意一个匹配下标，所以只校验"找到且值对 / 未找到"语义，不比下标
            int myExact = exactSearch(arr, target);
            boolean exactOk = (expectedExact == -1)
                    ? (myExact == -1)
                    : (myExact >= 0 && arr[myExact] == target);

            if (!exactOk
                    || lowerBound(arr, target) != expectedLower
                    || upperBound(arr, target) != expectedUpper) {
                throw new AssertionError("第 " + (t + 1) + " 组对拍失败：" + Arrays.toString(arr)
                        + " target=" + target);
            }
            round++;
        }
        check(round == 200, "200 组随机有序数组，精确/左界/右界均与线性扫描一致");

        // 性能实测：二分 vs 线性查找
        final int n = 20000;
        int[] big = new int[n];
        for (int i = 0; i < n; i++) big[i] = i * 2;   // 0,2,4,…,39998
        int target = 39998;

        long t1 = System.nanoTime();
        int bs = exactSearch(big, target);
        long bsCost = System.nanoTime() - t1;

        long t2 = System.nanoTime();
        int ls = linearIndex(big, target);
        long lsCost = System.nanoTime() - t2;

        System.out.println("n = " + n + "：二分返回 " + bs + "，耗时 " + bsCost + " 纳秒；"
                + "线性返回 " + ls + "，耗时 " + lsCost + " 纳秒");
        check(bs == ls && bs == n - 1, "二分与线性查找结果一致，都定位到末元素");
        System.out.println("观察：二分 O(log n) 约 15 次比较，线性最坏 O(n)；数据越大差距越悬殊。");
    }

    // =========================================================
    // 五套模板实现（区间统一为左闭右开 [left, right)）
    // =========================================================

    /** 模板一：精确查找，找到返回下标，找不到返回 -1 */
    static int exactSearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length;            // 右开：right 初始化为 n
        while (left < right) {
            int mid = left + (right - left) / 2;   // 防溢出写法
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;            // 答案在右半 [mid+1, right)
            } else {
                right = mid;               // 答案在左半 [left, mid)
            }
        }
        return -1;
    }

    /** 模板二：lowerBound，返回第一个 >= target 的下标；若全小于 target，返回 n */
    static int lowerBound(int[] arr, int target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                left = mid + 1;            // 严格小于，答案不可能在 mid 及左边
            } else {
                right = mid;               // >= target，答案可能就是 mid
            }
        }
        return left;
    }

    /** 模板三：upperBound，返回第一个 > target 的下标；若全 <= target，返回 n */
    static int upperBound(int[] arr, int target) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                left = mid + 1;            // 相等也归到左边，右界才能越过所有相等元素
            } else {
                right = mid;
            }
        }
        return left;
    }

    /** 模板四：查找插入位置，等价于 lowerBound */
    static int insertionPosition(int[] arr, int target) {
        return lowerBound(arr, target);
    }

    /** 模板五：二分答案，求最小的 x 满足 x*x >= n */
    static int minSqrtCeil(int n) {
        long left = 0;
        long right = n;                    // 答案区间 [0, n]
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (mid * mid >= n) {
                right = mid;               // 条件成立，尝试更小的答案
            } else {
                left = mid + 1;            // 条件不成立，答案必须更大
            }
        }
        return (int) left;
    }

    // =========================================================
    // 线性扫描标准答案（对拍用）
    // =========================================================

    private static int linearIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    private static int linearLowerBound(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= target) return i;
        }
        return arr.length;
    }

    private static int linearUpperBound(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > target) return i;
        }
        return arr.length;
    }

    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
