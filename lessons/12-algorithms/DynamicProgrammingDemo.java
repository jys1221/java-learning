import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 知识点：动态规划（Dynamic Programming，DP）
 * ============================================================
 * 一句话：把大问题拆成带状态的小问题，把每个小问题的答案记进一张表（dp 表），
 *        后面的状态由前面的状态推出来，避免像朴素递归那样重复算成千上万遍。
 *
 * 为什么需要它：
 *   递归 + 记忆化是 DP 的雏形，但真正的 DP 强调"先定义状态，再写转移"。
 *   背包、最长子序列、编辑距离、股票买卖这些面试高频题，暴力枚举要么指数爆炸，
 *   要么根本写不出来；DP 用一张表把复杂度压到 O(n²) 甚至 O(n log n)。
 *
 * 复杂度速查（n = 元素个数，W = 背包容量）：
 *   问题              时间          空间          说明
 *   0/1 背包（二维）   O(n·W)       O(n·W)        最容易理解，表完整可打印
 *   0/1 背包（一维）   O(n·W)       O(W)          倒序遍历，滚动压缩空间
 *   完全背包（一维）   O(n·W)       O(W)          正序遍历，同物可重复取
 *   LIS（O(n²)）      O(n²)        O(n)          双重循环，好理解
 *   LIS（贪心+二分）  O(n log n)   O(n)          tails 数组 + 二分找位置
 *   编辑距离          O(m·n)       O(m·n)        m、n 两个字符串长度
 *   股票系列          O(n)          O(1)~O(n)     每天只维护几个状态值
 *
 * 易错点：
 *   1. 【0/1 背包一维数组必须倒序】容量从 W 往 w[i] 遍历：正序时 dp[c-w[i]]
 *      已经被本轮更新过，同一件物品会被再用一次，0/1 背包就错成了完全背包
 *   2. 【初始化边界】背包求最大价值，dp[0][c]=0、dp[i][0]=0；求最小代价
 *      （编辑距离、凑钱最少）时，不可达状态要初始化成 INF（如 Integer.MAX_VALUE），
 *      初始化成 0 会让最小值被错误地保留成 0
 *   3. 【int 溢出】价值、利润累加得多了 int 会溢出；先估算量级，必要时换 long；
 *      编辑距离 INF 相加也要防溢出（用 INF/2 或先判空）
 *   4. 状态定义没写清就动手转移：先回答"dp[i][j] 到底表示什么含义"，
 *      含义错了，转移方程再工整也是错的
 *   5. 遍历顺序搞反：0/1 倒序、完全背包正序；求"排列数"和"组合数"外层循环对象也不同
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置背包 / LIS / 编辑距离求解器；排序底座是 Arrays.sort，
 *   二分查找是 Arrays.binarySearch。编辑距离的思想被用在文本 diff、
 *   git 版本对比、拼写检查（如 Levenshtein 距离）里。
 *
 * 一句话口诀：
 *   状态定义先写清，转移方程照着推；初始化划边界，遍历顺序别搞反；表打出来，一步一步对。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 DynamicProgrammingDemo.java
 *   java DynamicProgrammingDemo
 */
public class DynamicProgrammingDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 0/1 背包：二维 dp 表 + 一维倒序，两种结果必须一致 ==========");
        demoKnapsack01();

        System.out.println();
        System.out.println("========== 2. 完全背包：一维正序，同一件物品可以反复取 ==========");
        demoCompleteKnapsack();

        System.out.println();
        System.out.println("========== 3. 最长上升子序列 LIS：O(n²) 与 O(n log n) 等长 ==========");
        demoLis();

        System.out.println();
        System.out.println("========== 4. 编辑距离：打印完整 dp 表 ==========");
        demoEditDistance();

        System.out.println();
        System.out.println("========== 5. 股票系列：一次买卖 / 无限交易 / 含冷冻期 ==========");
        demoStock();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 1. 0/1 背包
    // =========================================================
    private static void demoKnapsack01() {
        // 物品：重量 w[i]，价值 v[i]；背包容量 8
        int[] weight = {2, 3, 4, 5};
        int[] value = {3, 4, 5, 6};
        int capacity = 8;

        // 1.1 二维 dp：dp[i][c] 表示"前 i 件物品、容量 c 时的最大价值"
        int[][] dp2d = knapsack2D(weight, value, capacity);
        System.out.println("二维 dp 表（行=前 i 件物品，列=容量 c）：");
        printIntMatrix(dp2d);
        int result2d = dp2d[weight.length][capacity];

        // 1.2 一维 dp：倒序遍历容量，把二维表压成一行
        int result1d = knapsack1D(weight, value, capacity);

        // 1.3 枚举全部 2^n 子集当标准答案
        int brute = bruteKnapsack01(weight, value, capacity);

        System.out.println("二维结果 = " + result2d + "，一维结果 = " + result1d + "，枚举最优 = " + brute);
        check(result2d == 10, "二维 0/1 背包最优价值 10（B+D：3+5 重量，4+6 价值）");
        check(result1d == result2d, "一维倒序与二维结果一致");
        check(brute == result2d, "枚举子集与 DP 结果一致");
    }

    /**
     * 二维 0/1 背包。
     * 转移方程：
     *   不选第 i 件：dp[i][c] = dp[i-1][c]
     *   选第 i 件：  dp[i][c] = dp[i-1][c-w[i-1]] + v[i-1]（前提 c >= w[i-1]）
     *   两者取大。
     */
    static int[][] knapsack2D(int[] weight, int[] value, int capacity) {
        int n = weight.length;
        int[][] dp = new int[n + 1][capacity + 1];
        // 第 0 行（一件物品都没有）价值全 0，Java 数组默认值就是 0，这里显式说明边界
        for (int i = 1; i <= n; i++) {
            for (int c = 0; c <= capacity; c++) {
                dp[i][c] = dp[i - 1][c]; // 先默认不选
                if (c >= weight[i - 1]) {
                    dp[i][c] = Math.max(dp[i][c],
                            dp[i - 1][c - weight[i - 1]] + value[i - 1]);
                }
            }
        }
        return dp;
    }

    /**
     * 一维 0/1 背包（滚动数组）。
     * 关键：容量 c 必须从大往小遍历。这样 dp[c-w[i]] 读到的还是"上一轮物品"
     * 的旧值，保证第 i 件物品只被考虑一次；正序就会重复取用，变成完全背包。
     */
    static int knapsack1D(int[] weight, int[] value, int capacity) {
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weight.length; i++) {
            for (int c = capacity; c >= weight[i]; c--) { // 倒序，易错点 1
                dp[c] = Math.max(dp[c], dp[c - weight[i]] + value[i]);
            }
        }
        return dp[capacity];
    }

    /** 枚举对照：2^n 个子集，重量不超容量取最大价值 */
    static int bruteKnapsack01(int[] weight, int[] value, int capacity) {
        int n = weight.length;
        int best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            int w = 0, v = 0;
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    w += weight[i];
                    v += value[i];
                }
            }
            if (w <= capacity) {
                best = Math.max(best, v);
            }
        }
        return best;
    }

    // =========================================================
    // 2. 完全背包（每件物品可以取任意多次）
    // =========================================================
    private static void demoCompleteKnapsack() {
        // 物品重量、价值；容量 7。完全背包允许同一件物品反复拿。
        int[] weight = {2, 3, 4};
        int[] value = {3, 4, 5};
        int capacity = 7;

        int dpResult = completeKnapsack(weight, value, capacity);
        int brute = bruteCompleteKnapsack(weight, value, capacity);
        System.out.println("完全背包：物品 (2,3)(3,4)(4,5)，容量 " + capacity);
        System.out.println("DP 正序结果 = " + dpResult + "，递归枚举对照 = " + brute);

        check(dpResult == 10, "完全背包最优价值 10（两件 w=2 + 一件 w=3：重量 7，价值 3+3+4）");
        check(dpResult == brute, "完全背包 DP 与递归枚举一致");
    }

    /**
     * 完全背包一维：容量 c 从小到大（正序）遍历。
     * 正序时 dp[c-w[i]] 已经包含本轮物品，同一件物品可以被再次取用 —— 这正是完全背包要的。
     */
    static int completeKnapsack(int[] weight, int[] value, int capacity) {
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < weight.length; i++) {
            for (int c = weight[i]; c <= capacity; c++) { // 正序，易错点 5
                dp[c] = Math.max(dp[c], dp[c - weight[i]] + value[i]);
            }
        }
        return dp[capacity];
    }

    /** 递归枚举对照：容量还够就再拿一件任意物品，取最大价值（小容量，跑得动） */
    static int bruteCompleteKnapsack(int[] weight, int[] value, int capacity) {
        if (capacity == 0) {
            return 0;
        }
        int best = 0;
        for (int i = 0; i < weight.length; i++) {
            if (weight[i] <= capacity) {
                best = Math.max(best, value[i] + bruteCompleteKnapsack(weight, value, capacity - weight[i]));
            }
        }
        return best;
    }

    // =========================================================
    // 3. 最长上升子序列 LIS
    // =========================================================
    private static void demoLis() {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};

        // 3.1 O(n²)：dp[i] 表示以 nums[i] 结尾的最长上升子序列长度
        int lisN2 = lisQuadratic(nums);

        // 3.2 O(n log n)：tails[k] 记录长度为 k+1 的子序列最小结尾值
        int lisNLogN = lisGreedyBinary(nums);

        System.out.println("数组：" + Arrays.toString(nums));
        System.out.println("O(n²) 结果 = " + lisN2 + "，贪心+二分结果 = " + lisNLogN);

        check(lisN2 == 4, "LIS 长度为 4（如 2,3,7,101 或 2,3,7,18）");
        check(lisNLogN == lisN2, "两种 LIS 解法长度一致");
    }

    /** O(n²) 版 LIS：双重循环，前面所有更小的元素都可以接到 i 前面 */
    static int lisQuadratic(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // 每个元素自己至少是长度 1 的子序列
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        int best = 0;
        for (int x : dp) {
            best = Math.max(best, x);
        }
        return best;
    }

    /**
     * 贪心 + 二分 O(n log n) 版 LIS。
     * tails 是一个严格递增数组：tails[k] 表示长度 k+1 的上升子序列，结尾值最小能是多少。
     * 新来一个 x：二分找到 tails 中第一个 >= x 的位置替换；找不到就追加到末尾。
     * 注意：tails 本身不一定是真实子序列，它的长度才是 LIS 长度。
     */
    static int lisGreedyBinary(int[] nums) {
        List<Integer> tails = new ArrayList<>();
        for (int x : nums) {
            int pos = lowerBound(tails, x); // 第一个 >= x 的下标
            if (pos == tails.size()) {
                tails.add(x);
            } else {
                tails.set(pos, x);
            }
            System.out.println("  处理 " + x + " 后 tails = " + tails);
        }
        return tails.size();
    }

    /** 二分找第一个 >= target 的位置（lowerBound），找不到返回 list.size() */
    static int lowerBound(List<Integer> list, int target) {
        int left = 0, right = list.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // =========================================================
    // 4. 编辑距离（Levenshtein Distance）
    // =========================================================
    private static void demoEditDistance() {
        // 经典例子：horse → ros，答案 3
        String word1 = "horse";
        String word2 = "ros";
        int dist1 = editDistance(word1, word2, true);
        System.out.println("\"" + word1 + "\" → \"" + word2 + "\" 编辑距离 = " + dist1);

        // 第二个例子：intention → execution，答案 5
        String word3 = "intention";
        String word4 = "execution";
        int dist2 = editDistance(word3, word4, false);
        System.out.println("\"" + word3 + "\" → \"" + word4 + "\" 编辑距离 = " + dist2);

        check(dist1 == 3, "horse → ros 编辑距离为 3");
        check(dist2 == 5, "intention → execution 编辑距离为 5");
        check(editDistance("abc", "abc", false) == 0, "两个相同字符串编辑距离为 0");
        check(editDistance("", "abc", false) == 3, "空串 → abc 需要插入 3 次");
    }

    /**
     * 编辑距离 dp[i][j]：把 word1 前 i 个字符变成 word2 前 j 个字符的最少操作数。
     * 三种操作：删 word1 一个字符（dp[i-1][j]+1）、插一个字符（dp[i][j-1]+1）、
     * 替换（dp[i-1][j-1]+1）；字符相同时直接继承 dp[i-1][j-1]。
     */
    static int editDistance(String word1, String word2, boolean printTable) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        // 边界：一个串为空，操作数就是另一个串的长度（逐个插入或删除）
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // 字符相同，不用操作
                } else {
                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int replace = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(Math.min(delete, insert), replace);
                }
            }
        }

        if (printTable) {
            System.out.println("编辑距离 dp 表（行=word1 前缀，列=word2 前缀）：");
            printEditTable(word1, word2, dp);
        }
        return dp[m][n];
    }

    // =========================================================
    // 5. 股票系列
    // =========================================================
    private static void demoStock() {
        // 5.1 只能买卖一次：找一个最低价买入、之后最高价卖出
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        int once = maxProfitOneTransaction(prices1);
        System.out.println("一次买卖 " + Arrays.toString(prices1) + "：最大利润 = " + once);
        check(once == 5, "一次买卖最大利润 5（1 买 6 卖）");

        // 5.2 可以无限次交易：只要后一天比前一天涨，就把这段差价吃掉
        int[] prices2 = {7, 1, 5, 3, 6, 4};
        int infinite = maxProfitInfinite(prices2);
        System.out.println("无限交易 " + Arrays.toString(prices2) + "：最大利润 = " + infinite);
        check(infinite == 7, "无限交易最大利润 7（4+3 两段上涨）");

        // 5.3 含冷冻期：卖出后第二天不能买；三个状态滚动
        int[] prices3 = {1, 2, 3, 0, 2};
        int cooldown = maxProfitWithCooldown(prices3);
        System.out.println("含冷冻期 " + Arrays.toString(prices3) + "：最大利润 = " + cooldown);
        check(cooldown == 3, "含冷冻期最大利润 3（1 买 2 卖，0 买 2 卖）");
    }

    /** 一次买卖：维护历史最低价，每天算"今天卖能赚多少" */
    static int maxProfitOneTransaction(int[] prices) {
        int minPrice = prices[0];
        int profit = 0;
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            profit = Math.max(profit, price - minPrice);
        }
        return profit;
    }

    /** 无限交易：累加所有正的日差价（不持有过夜也不影响，等价于吃到每段上涨） */
    static int maxProfitInfinite(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        return profit;
    }

    /**
     * 含冷冻期：三个状态
     *   hold   —— 当天结束持有股票
     *   sold   —— 当天刚卖出（次日进入冷冻期）
     *   free   —— 当天结束不持有，且不在冷冻期
     */
    static int maxProfitWithCooldown(int[] prices) {
        int hold = -prices[0]; // 第一天买入
        int sold = 0;
        int free = 0;
        for (int i = 1; i < prices.length; i++) {
            int prevHold = hold;
            int prevSold = sold;
            int prevFree = free;
            // 持有：之前就持有，或从 free 状态今天买入
            hold = Math.max(prevHold, prevFree - prices[i]);
            // 今天卖出：只能从持有状态卖
            sold = prevHold + prices[i];
            // 空闲：之前就空闲，或昨天刚卖出、今天冷冻期结束
            free = Math.max(prevFree, prevSold);
        }
        return Math.max(hold, Math.max(sold, free));
    }

    // =========================================================
    // 打印辅助
    // =========================================================

    /** 打印二维 int 表，每个数字占 4 格宽，便于对齐看 dp 过程 */
    private static void printIntMatrix(int[][] dp) {
        for (int[] row : dp) {
            StringBuilder sb = new StringBuilder("  ");
            for (int x : row) {
                sb.append(String.format("%4d", x));
            }
            System.out.println(sb);
        }
    }

    /** 打印编辑距离表，表头标出 word2 的每个字符，首列标出 word1 的每个字符 */
    private static void printEditTable(String word1, String word2, int[][] dp) {
        StringBuilder header = new StringBuilder("      ");
        for (int j = 0; j < word2.length(); j++) {
            header.append(String.format("%4s", word2.charAt(j)));
        }
        System.out.println(header);
        for (int i = 0; i < dp.length; i++) {
            StringBuilder sb = new StringBuilder("  ");
            sb.append(i == 0 ? " " : word1.charAt(i - 1));
            for (int j = 0; j < dp[i].length; j++) {
                sb.append(String.format("%4d", dp[i][j]));
            }
            System.out.println(sb);
        }
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
