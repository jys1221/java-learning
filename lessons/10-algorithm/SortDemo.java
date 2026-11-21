import java.util.Arrays;
import java.util.Random;

/**
 * 知识点 23：常见排序算法
 *
 * 本篇包含三种最经典的排序，都是"面试必问 + 理解算法思维必学"：
 *   冒泡排序   —— 每轮把最大的"冒"到末尾，最容易理解，但最慢
 *   插入排序   —— 像打扑克整理手牌，对近乎有序的数据很快
 *   快速排序   —— 选一个基准，把小的放左边、大的放右边，再递归处理两边，平均最快
 *
 * 复杂度对比（n 为数据量）：
 *   冒泡/插入：平均 O(n²)，数据量大就卡
 *   快排：平均 O(n log n)，最坏 O(n²)（基准选得极差时）
 */
public class SortDemo {

    public static void main(String[] args) {

        int[] data = {5, 2, 9, 1, 7, 3, 8, 6, 4};

        // ---------- 冒泡排序 ----------
        int[] bubble = data.clone();      // clone：复制一份，避免影响原数组
        bubbleSort(bubble);
        System.out.println("冒泡排序结果：" + Arrays.toString(bubble));

        // ---------- 插入排序 ----------
        int[] insert = data.clone();
        insertionSort(insert);
        System.out.println("插入排序结果：" + Arrays.toString(insert));

        // ---------- 快速排序 ----------
        int[] quick = data.clone();
        quickSort(quick, 0, quick.length - 1);
        System.out.println("快速排序结果：" + Arrays.toString(quick));

        // ---------- 验证：随机大数组，比较耗时 ----------
        Random random = new Random(42);     // 固定种子，结果可复现
        int[] big = new int[20000];
        for (int i = 0; i < big.length; i++) {
            big[i] = random.nextInt(100000);
        }

        int[] b1 = big.clone();
        long t1 = System.currentTimeMillis();
        bubbleSort(b1);
        System.out.println("冒泡排序 2 万条耗时：" + (System.currentTimeMillis() - t1) + "ms");

        int[] b2 = big.clone();
        long t2 = System.currentTimeMillis();
        insertionSort(b2);
        System.out.println("插入排序 2 万条耗时：" + (System.currentTimeMillis() - t2) + "ms");

        int[] b3 = big.clone();
        long t3 = System.currentTimeMillis();
        quickSort(b3, 0, b3.length - 1);
        System.out.println("快速排序 2 万条耗时：" + (System.currentTimeMillis() - t3) + "ms");

        // 校验三种算法结果一致（排序是确定的，结果必须相同）
        System.out.println("三种结果一致吗：" + (Arrays.equals(b1, b2) && Arrays.equals(b2, b3)));
    }

    /**
     * 冒泡排序
     *
     * 思路：相邻两个比较，大的往后换。一轮下来最大的跑到最后。
     * n 个数需要 n-1 轮，每轮比较次数递减（因为末尾已经排好了）。
     *
     * 优化：如果某一轮一次交换都没发生，说明已经有序，可以提前结束。
     */
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;              // 本轮是否发生过交换
            for (int j = 0; j < n - 1 - i; j++) { // 已排好的尾部不用再比
                if (arr[j] > arr[j + 1]) {
                    // 交换相邻两个
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;                            // 已经有序，提前收工
            }
        }
    }

    /**
     * 插入排序
     *
     * 思路：把数组看成"已排序区 + 未排序区"。
     * 每次取未排序区的第一个，在已排序区里从后往前找位置插进去。
     * 类比：打扑克摸牌，把新牌插到手里合适的位置。
     */
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int current = arr[i];        // 要插入的牌
            int j = i - 1;               // 从已排序区的最后一个开始比
            // 比 current 大的都往后挪一格，给 current 腾位置
            while (j >= 0 && arr[j] > current) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = current;        // 找到位置放进去
        }
    }

    /**
     * 快速排序（递归实现）
     *
     * 思路：
     *   1. 选一个基准值 pivot
     *   2. 把小于 pivot 的放左边，大于的放右边（分区）
     *   3. 对左右两段分别递归重复
     *
     * 本实现用"挖坑填数"法，选最左元素为基准。
     */
    public static void quickSort(int[] arr, int left, int right) {
        // 递归终止：区间内元素少于 2 个
        if (left >= right) {
            return;
        }
        int i = left;
        int j = right;
        int pivot = arr[left];        // 基准值（先挖走 left 位置的数，形成"坑"）

        while (i < j) {
            // 从右往左找第一个比基准小的，填到左边的坑里
            while (i < j && arr[j] >= pivot) {
                j--;
            }
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }
            // 从左往右找第一个比基准大的，填到右边的坑里
            while (i < j && arr[i] <= pivot) {
                i++;
            }
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }
        // 循环结束时 i == j，这个位置就是基准的最终位置
        arr[i] = pivot;

        // 递归处理左右两段（不含基准本身）
        quickSort(arr, left, i - 1);
        quickSort(arr, i + 1, right);
    }
}
