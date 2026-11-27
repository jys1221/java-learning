import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 知识点：优先队列与堆（Priority Queue & Heap）
 * ============================================================
 * 一句话：堆是一棵"父节点总不大于子节点"的完全二叉树，用数组就能存；
 *        小顶堆每次出堆顶都是当前最小值，大顶堆则是最大值。
 *
 * 为什么需要它：
 *   任务按优先级处理、求 TopK、数据流中位数、Dijkstra 选最近节点，都需要反复
 *   "取最值并删去"。普通数组取最值 O(n)、排序后取最值 O(n log n)；堆把取最值和
 *   调整都压到 O(log n)，是这类问题的核心结构。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   入堆 offer          O(log n)  O(log n)  沿父节点向上 siftUp
 *   出堆 poll           O(log n)  O(log n)  尾节点补到堆顶再向下 siftDown
 *   看堆顶 peek         O(1)      O(1)
 *   整体建堆 buildHeap  O(n)      O(n)      从最后父节点向前批量 siftDown
 *   堆排序（反复出堆）    O(n log n) O(n log n) 每次 O(log n)，共 n 次
 *
 * 易错点：
 *   1. 0 下标数组的父子公式：父=(i-1)/2，左=2i+1，右=2i+2；1 下标数组公式不同，别混
 *   2. siftDown 时要在左右子节点里挑"更小（小顶堆）"的那个交换，只跟左孩子比会错
 *   3. 出堆是"尾节点补到堆顶"再下沉，不是直接删堆顶；否则堆结构就塌了
 *   4. TopK 求最大的 K 个数要用"小顶堆"：堆顶是这 K 个里的最小者，新来更大的就替换它
 *   5. 建堆之所以 O(n) 而不是 O(n log n)：越靠下的节点下沉层数越少，总调整次数是收敛级数
 *
 * 与 Java 自带类的对应关系：
 *   java.util.PriorityQueue 默认是小顶堆；传入 Collections.reverseOrder() 比较器
 *   就变成大顶堆。它底层也是数组 + 二叉堆，与本文件手写 MinHeap 同一套思想。
 *
 * 一句话口诀：
 *   父子下标算得准，上浮下沉调堆身；建堆线性出堆对数，TopK 用小顶守大门。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 PriorityQueueHeapDemo.java
 *   java PriorityQueueHeapDemo
 */
public class PriorityQueueHeapDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 手写小顶堆：siftUp 上浮、siftDown 下沉 ==========");
        demoHeapInsertAndPoll();

        System.out.println();
        System.out.println("========== 2. 批量建堆 O(n)；反复出堆得到升序序列 ==========");
        demoBuildHeapAndHeapSort();

        System.out.println();
        System.out.println("========== 3. TopK 小顶堆；与 JDK PriorityQueue 对照 + 实测 ==========");
        demoTopKAndCrossCheck();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 插入与出堆
    // ---------------------------------------------------------
    private static void demoHeapInsertAndPoll() {
        MinHeap heap = new MinHeap(10);
        int[] seq = { 5, 3, 8, 1, 9, 2 };
        for (int v : seq) {
            heap.offer(v);
        }
        check(heap.size() == 6, "插入 6 个元素后 size=6");
        check(heap.peek() == 1, "小顶堆堆顶是最小值 1");

        // 反复出堆：小顶堆出堆顺序必为升序
        List<Integer> out = new ArrayList<>();
        while (!heap.isEmpty()) {
            out.add(heap.poll());
        }
        System.out.println("出堆顺序：" + out);
        check(out.equals(List.of(1, 2, 3, 5, 8, 9)), "出堆为升序 1,2,3,5,8,9");
        check(heap.isEmpty(), "出空后堆为空");
    }

    // ---------------------------------------------------------
    // 2. 建堆与堆排序
    // ---------------------------------------------------------
    private static void demoBuildHeapAndHeapSort() {
        int[] data = { 4, 10, 3, 5, 1, 2, 7 };
        MinHeap heap = MinHeap.buildHeap(data);

        // 建堆后堆顶应是全局最小值
        check(heap.peek() == 1, "批量建堆后堆顶为最小值 1");

        // 反复出堆即堆排序：得到升序序列
        List<Integer> sorted = new ArrayList<>();
        while (!heap.isEmpty()) {
            sorted.add(heap.poll());
        }
        System.out.println("堆排序结果：" + sorted);
        check(sorted.equals(List.of(1, 2, 3, 4, 5, 7, 10)), "堆排序输出升序序列");

        // 与 JDK Arrays.sort 结果交叉验证
        int[] expected = data.clone();
        java.util.Arrays.sort(expected);
        List<Integer> jdkSorted = new ArrayList<>();
        for (int v : expected) {
            jdkSorted.add(v);
        }
        check(sorted.equals(jdkSorted), "堆排序结果与 JDK Arrays.sort 一致");

        System.out.println("建堆 O(n) 的直观理解：叶子节点不用下沉，越靠下沉降层数越少，");
        System.out.println("总调整次数 ≈ n/2·0 + n/4·1 + n/8·2 + …，是收敛级数，总和线性。");
    }

    // ---------------------------------------------------------
    // 3. TopK + JDK 对照 + 实测
    // ---------------------------------------------------------
    private static void demoTopKAndCrossCheck() {
        int[] nums = { 3, 1, 5, 9, 2, 6, 8, 4, 7 };
        int k = 3;
        List<Integer> topK = topK(nums, k);
        System.out.println("数组：" + java.util.Arrays.toString(nums) + "，最大的 " + k + " 个数（小顶堆内升序）：" + topK);

        // 暴力对照：降序排序后取前 K，再排序比较
        List<Integer> brute = new ArrayList<>();
        for (int v : nums) {
            brute.add(v);
        }
        Collections.sort(brute, Collections.reverseOrder());
        List<Integer> expected = new ArrayList<>(brute.subList(0, k));
        Collections.sort(expected);
        check(topK.equals(expected), "TopK 结果与暴力排序取前 K 一致");
        check(topK.containsAll(List.of(9, 8, 7)), "最大三个数是 9、8、7");

        // JDK PriorityQueue：默认小顶堆，poll 顺序应与手写堆一致
        Random rnd = new Random(42);
        MinHeap mine = new MinHeap(20);
        PriorityQueue<Integer> jdkMin = new PriorityQueue<>();
        for (int i = 0; i < 20; i++) {
            int v = rnd.nextInt(100);
            mine.offer(v);
            jdkMin.offer(v);
        }
        boolean sameOrder = true;
        for (int i = 0; i < 20; i++) {
            if (mine.poll() != jdkMin.poll()) {
                sameOrder = false;
            }
        }
        check(sameOrder, "手写小顶堆与 JDK PriorityQueue（默认小顶）出堆顺序一致");

        // 大顶堆：JDK 传 reverseOrder 比较器，堆顶为最大值
        PriorityQueue<Integer> jdkMax = new PriorityQueue<>(Collections.reverseOrder());
        for (int v : nums) {
            jdkMax.offer(v);
        }
        check(jdkMax.peek() == 9, "传入 reverseOrder 后 JDK 为大顶堆，堆顶是 9");

        // 实测：20000 个元素建堆 + 全部出堆
        final int n = 20000;
        int[] big = rnd.ints(n, 0, 100000).toArray();

        long t1 = System.nanoTime();
        MinHeap benchMine = MinHeap.buildHeap(big);
        while (!benchMine.isEmpty()) {
            benchMine.poll();
        }
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        PriorityQueue<Integer> benchJdk = new PriorityQueue<>();
        for (int v : big) {
            benchJdk.offer(v);
        }
        while (!benchJdk.isEmpty()) {
            benchJdk.poll();
        }
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("手写堆 " + n + " 个元素建堆+出堆耗时：" + mineMs + " ms");
        System.out.println("JDK PriorityQueue " + n + " 个元素入堆+出堆耗时：" + jdkMs + " ms");
    }

    /**
     * TopK：求数组中最大的 K 个数。
     * 用容量 K 的小顶堆：堆顶是 K 个候选里的最小者；遍历到更大的数就替换堆顶再下沉。
     */
    private static List<Integer> topK(int[] nums, int k) {
        MinHeap heap = new MinHeap(k);
        for (int v : nums) {
            if (heap.size() < k) {
                heap.offer(v);
            } else if (v > heap.peek()) {
                // 新值比堆顶大：替换堆顶，再向下下沉恢复堆性质
                heap.data[0] = v;
                heap.siftDown(0);
            }
        }
        List<Integer> out = new ArrayList<>();
        while (!heap.isEmpty()) {
            out.add(heap.poll());
        }
        return out;
    }

    // =========================================================
    // 手写小顶堆：0 下标数组存储的完全二叉树
    // =========================================================
    static class MinHeap {
        private int[] data;
        private int size;

        MinHeap(int capacity) {
            data = new int[capacity];
            size = 0;
        }

        /** 入堆：放到末尾，向上上浮到合适位置 */
        void offer(int value) {
            if (size == data.length) {
                grow();
            }
            data[size] = value;
            siftUp(size);
            size++;
        }

        /** 出堆：堆顶取出，尾节点补到堆顶，再向下下沉 */
        int poll() {
            if (isEmpty()) {
                throw new IllegalStateException("堆为空，不能 poll");
            }
            int top = data[0];
            data[0] = data[size - 1];
            size--;
            siftDown(0);
            return top;
        }

        int peek() {
            if (isEmpty()) {
                throw new IllegalStateException("堆为空");
            }
            return data[0];
        }

        int size() {
            return size;
        }

        boolean isEmpty() {
            return size == 0;
        }

        /** 上浮：若比父节点小就交换，一直走到根或不再小于父节点 */
        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data[i] < data[parent]) {
                    swap(i, parent);
                    i = parent;
                } else {
                    break;
                }
            }
        }

        /** 下沉：在左右孩子里挑更小的交换，直到自己不大于孩子或到叶子 */
        private void siftDown(int i) {
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int smallest = i;
                if (left < size && data[left] < data[smallest]) {
                    smallest = left;
                }
                if (right < size && data[right] < data[smallest]) {
                    smallest = right;
                }
                if (smallest == i) {
                    break;
                }
                swap(i, smallest);
                i = smallest;
            }
        }

        /** 批量建堆：从最后一个父节点向前逐个下沉，整体 O(n) */
        static MinHeap buildHeap(int[] arr) {
            MinHeap heap = new MinHeap(arr.length);
            System.arraycopy(arr, 0, heap.data, 0, arr.length);
            heap.size = arr.length;
            for (int i = (heap.size - 1) / 2; i >= 0; i--) {
                heap.siftDown(i);
            }
            return heap;
        }

        private void swap(int a, int b) {
            int tmp = data[a];
            data[a] = data[b];
            data[b] = tmp;
        }

        private void grow() {
            int[] bigger = new int[data.length * 2];
            System.arraycopy(data, 0, bigger, 0, data.length);
            data = bigger;
        }
    }

    // =========================================================
    // 断言小工具：条件不成立就抛异常
    // =========================================================
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
