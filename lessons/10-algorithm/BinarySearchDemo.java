/**
 * 知识点 24：二分查找（Binary Search）
 *
 * 前提：数组必须"已经排好序"，否则结果不可靠。
 *
 * 思路：每次砍掉一半。
 *   取中间值和目标比：
 *     相等 → 找到了
 *     目标更大 → 目标在右半边，left = mid + 1
 *     目标更小 → 目标在左半边，right = mid - 1
 *
 * 为什么快？
 *   100 万条数据，最多比较约 20 次（log2(1000000) ≈ 20），
 *   而顺序查找最坏要比较 100 万次。
 */
public class BinarySearchDemo {

    public static void main(String[] args) {

        // 有序数组（二分查找的前提）
        int[] sorted = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};

        // ---------- 迭代版 ----------
        System.out.println("找 7：" + binarySearch(sorted, 7));        // 下标 3
        System.out.println("找 1：" + binarySearch(sorted, 1));        // 下标 0
        System.out.println("找 19：" + binarySearch(sorted, 19));      // 下标 9
        System.out.println("找 8（不存在）：" + binarySearch(sorted, 8));  // -1

        // ---------- 递归版 ----------
        System.out.println("递归找 13：" + binarySearchRecursive(sorted, 13, 0, sorted.length - 1));

        // ---------- 对比：二分 vs 顺序查找的步数 ----------
        int[] big = new int[1000];
        for (int i = 0; i < big.length; i++) {
            big[i] = i * 2;        // 0, 2, 4, ... 1998
        }

        int target = 1998;
        long t1 = System.nanoTime();
        int idx1 = binarySearch(big, target);
        long cost1 = System.nanoTime() - t1;
        System.out.println("二分查找：" + idx1 + "，耗时 " + cost1 + " 纳秒");

        long t2 = System.nanoTime();
        int idx2 = linearSearch(big, target);
        long cost2 = System.nanoTime() - t2;
        System.out.println("顺序查找：" + idx2 + "，耗时 " + cost2 + " 纳秒");

        // ---------- 易错点提醒 ----------
        // 1. 算 mid 时用 (left + right) / 2 在极端情况下可能溢出 int
        //    标准写法是 left + (right - left) / 2
        // 2. while 条件必须是 left <= right（等于时要再比一次，否则会漏掉最后一个元素）
        // 3. 数组必须有序，且不能有"重复元素"的歧义（重复时找到的是其中某一个）
    }

    /**
     * 迭代版二分查找
     * 返回目标的下标，找不到返回 -1
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {                      // 注意是 <=
            int mid = left + (right - left) / 2;     // 防溢出的中间位置写法
            if (arr[mid] == target) {
                return mid;                          // 找到了
            } else if (arr[mid] < target) {
                left = mid + 1;                      // 目标在右半边
            } else {
                right = mid - 1;                     // 目标在左半边
            }
        }
        return -1;                                   // 循环结束还没找到
    }

    /**
     * 递归版二分查找
     * 每次把问题规模减半，交给递归处理
     */
    public static int binarySearchRecursive(int[] arr, int target, int left, int right) {
        if (left > right) {
            return -1;                    // 终止条件：区间空了，没找到
        }
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return binarySearchRecursive(arr, target, mid + 1, right);   // 右半边
        } else {
            return binarySearchRecursive(arr, target, left, mid - 1);    // 左半边
        }
    }

    /**
     * 顺序查找（用来对比性能）
     */
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
