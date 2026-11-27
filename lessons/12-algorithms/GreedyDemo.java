import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 知识点：贪心算法（Greedy）
 * ============================================================
 * 一句话：每一步都选"当下看起来最好"的那个选择，不回头、不后悔，
 *        指望一路局部最优拼出全局最优。
 *
 * 为什么需要它：
 *   贪心思路最朴素、代码最短、通常最快（往往省掉 DP 的嵌套循环）。
 *   但它有严格前提：问题必须具备"贪心选择性质"（局部最优能推出全局最优）
 *   和"最优子结构"。前提不满足时，贪心得到的就是错误答案 —— 本文件第 4 节
 *   用一个具体背包反例把这件事当场算给你看。
 *
 * 复杂度速查（n = 元素个数）：
 *   问题              贪心时间        说明
 *   找零钱            O(k)           k = 币值种数；只对规范币值最优
 *   区间调度          O(n log n)     排序占大头，扫描 O(n)
 *   跳跃游戏          O(n)           维护一个能跳到的最远距离
 *   0/1 背包          贪心不可靠     按性价比贪心会错，必须 DP 或枚举
 *
 * 易错点：
 *   1. 不判断问题能不能贪心就直接上贪心：0/1 背包、任务调度带权重等场景贪心会给出次优甚至错误答案
 *   2. 区间调度按"左端点"排序是经典错误；正确做法是按"右端点"升序，越早结束越留出空间
 *   3. 找零钱贪心只对规范币值成立（人民币、美分这种）；币值设计不规范时，贪心得不出最少张数
 *   4. 比较器写反（降序/升序）会让贪心选择的方向完全相反，结果看似合理其实错
 *   5. 跳跃游戏要维护"全程能到达的最远下标"，不能只看当前这一步能不能跳
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有现成的"贪心求解器"；排序底座是 Arrays.sort / List.sort，
 *   按权重逐个挑选可以用 java.util.PriorityQueue（堆）。
 *   注意：JDK 自带的 Knapsack 相关工具并不存在，背包问题要自己写 DP。
 *
 * 一句话口诀：
 *   先证可贪心，再挑当下最优；区间先比右端点；背包贪心会翻车，老老实实 DP。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 GreedyDemo.java
 *   java GreedyDemo
 */
public class GreedyDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 找零钱：规范币值贪心成立，非规范币值贪心翻车 ==========");
        demoCoinChange();

        System.out.println();
        System.out.println("========== 2. 区间调度：按右端点排序，挑最多不重叠区间 ==========");
        demoIntervalScheduling();

        System.out.println();
        System.out.println("========== 3. 跳跃游戏：维护能到达的最远距离 ==========");
        demoJumpGame();

        System.out.println();
        System.out.println("========== 4. 贪心失效反例：0/1 背包按性价比贪心是错的 ==========");
        demoKnapsackGreedyFail();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 找零钱
    // ---------------------------------------------------------
    private static void demoCoinChange() {
        // 1.1 规范币值（类似人民币：1 / 5 / 10 / 25）：每次拿最大面额，贪心最优。
        int[] canonical = {25, 10, 5, 1};
        int amount1 = 37;
        int greedy1 = greedyCoinChange(canonical, amount1);
        int optimal1 = bruteMinCoins(canonical, amount1);
        System.out.println("规范币值 " + Arrays.toString(canonical) + "，找 " + amount1
                + " 元：贪心 " + greedy1 + " 张，枚举最优 " + optimal1 + " 张");
        check(greedy1 == 4, "规范币值 37 元贪心用 4 张（25+10+1+1）");
        check(greedy1 == optimal1, "规范币值下贪心结果与枚举最优一致");

        // 1.2 非规范币值 {1, 3, 4}，amount = 6：
        //   贪心：先拿 4，剩下 2 只能两个 1 → 4+1+1 = 3 张
        //   最优：3+3 = 2 张。贪心错了。
        int[] tricky = {4, 3, 1};
        int amount2 = 6;
        int greedy2 = greedyCoinChange(tricky, amount2);
        int optimal2 = bruteMinCoins(tricky, amount2);
        System.out.println("非规范币值 " + Arrays.toString(tricky) + "，找 " + amount2
                + " 元：贪心 " + greedy2 + " 张，枚举最优 " + optimal2 + " 张");
        check(greedy2 == 3, "非规范币值贪心拿到 3 张（4+1+1）");
        check(optimal2 == 2, "枚举最优是 2 张（3+3）");
        check(greedy2 > optimal2, "贪心张数多于最优 → 贪心在这套币值上失效");
    }

    /**
     * 贪心找零钱：从大面额往小面额贪心，能拿几张拿几张。
     * 输入 coins 约定为降序。
     */
    static int greedyCoinChange(int[] coins, int amount) {
        int remaining = amount;
        int count = 0;
        for (int coin : coins) {
            while (remaining >= coin) {
                remaining -= coin;
                count++;
            }
        }
        return count;
    }

    /**
     * 对照标准答案：DP 求最少硬币数（完全背包思路），用来检验贪心对不对。
     * dp[a] 表示凑出 a 元最少要几张硬币，不可能凑出时给一个很大的值。
     */
    static int bruteMinCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int coin : coins) {
                if (coin <= a) {
                    dp[a] = Math.min(dp[a], dp[a - coin] + 1);
                }
            }
        }
        return dp[amount];
    }

    // ---------------------------------------------------------
    // 2. 区间调度：最多选出多少个互不重叠的区间
    // ---------------------------------------------------------
    private static void demoIntervalScheduling() {
        // 每个区间是 [start, end]，从第 0 天开始不早于 0
        int[][] intervals = {
                {1, 3}, {2, 4}, {3, 5}, {0, 6},
                {4, 7}, {5, 9}, {6, 10}, {8, 11}
        };

        int greedyCount = greedyMaxNonOverlap(intervals);
        int bruteCount = bruteMaxNonOverlap(intervals);
        System.out.println("区间列表：");
        for (int[] iv : intervals) {
            System.out.println("  [" + iv[0] + ", " + iv[1] + "]");
        }
        System.out.println("贪心选出最多不重叠区间 = " + greedyCount + "，枚举最优 = " + bruteCount);

        check(greedyCount == 3, "贪心选出 3 个互不重叠区间");
        check(greedyCount == bruteCount, "区间调度贪心结果与枚举最优一致");
    }

    /**
     * 区间调度贪心：
     *   ① 按右端点升序排序 —— 越早结束的区间，给后面留的空间越多
     *   ② 扫描：只要当前区间起点 >= 上一个选中区间的终点，就选它
     * 两端点相接（如 [1,3] 和 [3,5]）视为不重叠，所以用 >= 而不是 >。
     */
    static int greedyMaxNonOverlap(int[][] intervals) {
        // 先按右端点排序；Comparator 比较的是每个区间的第二个元素
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));

        int count = 0;
        int lastEnd = -1; // 上一个选中区间的终点，-1 表示还没选过
        for (int[] iv : intervals) {
            if (iv[0] >= lastEnd) {
                count++;
                lastEnd = iv[1];
            }
        }
        return count;
    }

    /**
     * 枚举对照：n 个区间一共 2^n 个子集，逐个检查是否互不重叠，取最大个数。
     * 这里 n = 8，256 个子集，跑得很快，用来给贪心做对拍。
     */
    static int bruteMaxNonOverlap(int[][] intervals) {
        int n = intervals.length;
        int best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            List<int[]> selected = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    selected.add(intervals[i]);
                }
            }
            selected.sort(Comparator.comparingInt(a -> a[1]));
            boolean ok = true;
            int lastEnd = -1;
            for (int[] iv : selected) {
                if (iv[0] < lastEnd) {
                    ok = false;
                    break;
                }
                lastEnd = iv[1];
            }
            if (ok) {
                best = Math.max(best, selected.size());
            }
        }
        return best;
    }

    // ---------------------------------------------------------
    // 3. 跳跃游戏：nums[i] 表示在 i 位置最多能跳几步
    // ---------------------------------------------------------
    private static void demoJumpGame() {
        // 能到达终点的例子
        int[] can = {2, 3, 1, 1, 4};
        // 到不了终点的例子：下标 1 最多跳 1 步，卡在 1、2 两个位置
        int[] cannot = {3, 2, 1, 0, 4};

        boolean greedyCan = greedyCanReach(can);
        boolean dpCan = dpCanReach(can);
        boolean greedyNo = greedyCanReach(cannot);
        boolean dpNo = dpCanReach(cannot);

        System.out.println(Arrays.toString(can) + "：贪心 " + greedyCan + "，DP " + dpCan);
        System.out.println(Arrays.toString(cannot) + "：贪心 " + greedyNo + "，DP " + dpNo);

        check(greedyCan, "例子 1 可以到达终点");
        check(greedyCan == dpCan, "可达例子：贪心与 DP 结论一致");
        check(!greedyNo, "例子 2 到不了终点");
        check(greedyNo == dpNo, "不可达例子：贪心与 DP 结论一致");
    }

    /**
     * 贪心：维护一个"全程能到达的最远下标" furthest。
     * 走到 i 时，如果 i > furthest，说明这个位置根本到不了，直接 false。
     */
    static boolean greedyCanReach(int[] nums) {
        int furthest = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > furthest) {
                return false; // 当前位置都到不了，后面更不可能
            }
            furthest = Math.max(furthest, i + nums[i]);
            if (furthest >= nums.length - 1) {
                return true; // 已经能到终点，提前收工
            }
        }
        return true;
    }

    /** DP 对照：reach[i] 表示能不能到达下标 i */
    static boolean dpCanReach(int[] nums) {
        int n = nums.length;
        boolean[] reach = new boolean[n];
        reach[0] = true;
        for (int i = 0; i < n; i++) {
            if (!reach[i]) {
                continue;
            }
            for (int step = 1; step <= nums[i] && i + step < n; step++) {
                reach[i + step] = true;
            }
        }
        return reach[n - 1];
    }

    // ---------------------------------------------------------
    // 4. 0/1 背包：贪心按性价比排序为什么是错的
    // ---------------------------------------------------------
    private static void demoKnapsackGreedyFail() {
        // 4 件物品：(重量 w, 价值 v)，背包容量 8
        //   A: w=2, v=3，性价比 1.5
        //   B: w=3, v=4，性价比 1.33
        //   C: w=4, v=5，性价比 1.25
        //   D: w=5, v=6，性价比 1.20
        int[] weight = {2, 3, 4, 5};
        int[] value = {3, 4, 5, 6};
        int capacity = 8;

        int greedyValue = greedyKnapsack(weight, value, capacity);
        int dpValue = dpKnapsack(weight, value, capacity);
        int bruteValue = bruteKnapsack(weight, value, capacity);

        System.out.println("物品：A(2,3) B(3,4) C(4,5) D(5,6)，容量 " + capacity);
        System.out.println("按性价比贪心：选 A、B（重量 5，价值 7），剩余 3 装不下任何一件 → 价值 " + greedyValue);
        System.out.println("DP 正解：选 B、D（重量 3+5=8，价值 4+6=10）→ 价值 " + dpValue);
        System.out.println("枚举全部 16 种子集对照 → 价值 " + bruteValue);

        check(greedyValue == 7, "贪心按性价比得到价值 7");
        check(dpValue == 10, "DP 正解价值 10");
        check(bruteValue == 10, "枚举最优也是 10，证明 DP 正确");
        check(greedyValue < dpValue, "贪心价值 7 < 正解 10：0/1 背包按性价比贪心确实失效");
    }

    /**
     * 贪心背包：按单位重量价值（性价比）从高到低装，装不下就跳过。
     * 注意：0/1 背包物品不能拆开，这种"先挑性价比高的"策略在很多数据上都会错。
     */
    static int greedyKnapsack(int[] weight, int[] value, int capacity) {
        Integer[] order = new Integer[weight.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }
        // 按性价比 value/weight 降序；用 double 比较即可，教学数据不会有精度问题
        Arrays.sort(order, (a, b) -> Double.compare(
                (double) value[b] / weight[b], (double) value[a] / weight[a]));

        int remaining = capacity;
        int totalValue = 0;
        for (int idx : order) {
            if (weight[idx] <= remaining) {
                remaining -= weight[idx];
                totalValue += value[idx];
            }
        }
        return totalValue;
    }

    /** DP 正解：一维 0/1 背包（倒序遍历容量，细节在 DynamicProgrammingDemo 里展开） */
    static int dpKnapsack(int[] weight, int[] value, int capacity) {
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weight.length; i++) {
            for (int c = capacity; c >= weight[i]; c--) {
                dp[c] = Math.max(dp[c], dp[c - weight[i]] + value[i]);
            }
        }
        return dp[capacity];
    }

    /** 枚举对照：2^n 个子集，重量不超容量取最大价值 */
    static int bruteKnapsack(int[] weight, int[] value, int capacity) {
        int n = weight.length;
        int best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            int totalWeight = 0;
            int totalValue = 0;
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    totalWeight += weight[i];
                    totalValue += value[i];
                }
            }
            if (totalWeight <= capacity) {
                best = Math.max(best, totalValue);
            }
        }
        return best;
    }

    // ---------------------------------------------------------
    // 断言辅助方法
    // ---------------------------------------------------------
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
