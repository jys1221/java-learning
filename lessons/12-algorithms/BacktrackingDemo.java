import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 知识点：回溯（Backtracking）
 * ============================================================
 * 一句话：在一棵"决策树"上深度优先往下走，每走一步先做选择，
 *        走到叶子就记录答案；回头时把这个选择撤销，再试下一条岔路。
 *
 * 为什么需要它：
 *   排列、组合、子集、棋盘放置（N 皇后）这类问题，本质是"枚举所有可能性"。
 *   层数不固定，硬写 for 循环根本写不出来；回溯用"递归 + 状态撤销"
 *   一套模板处理所有这类问题，并在中途通过剪枝去掉不可能的分支。
 *
 * 复杂度速查（n = 元素个数，k = 组合个数）：
 *   问题          时间量级          栈空间      说明
 *   全排列        O(n · n!)        O(n)       共 n! 个排列，每个拷贝 O(n)
 *   组合 C(n,k)   O(C(n,k) · k)    O(n)       靠 start 参数避免重复组合
 *   子集枚举      O(2^n · n)       O(n)       每个元素选或不选，共 2^n
 *   N 皇后        O(n!) 量级       O(n)       三个标记数组剪枝后远小于暴力
 *
 * 易错点：
 *   1. 【选择与撤销必须对称】递归回来后一定要把 used 改回 false、把路径末尾 remove；
 *      忘了撤销，状态会串到后面的分支，结果全错
 *   2. 去重方式别混用：排列用 used 数组标记"这个元素用过没"；
 *      组合/子集用 start 参数规定"只能从后面选"，否则会出现 [1,2] 和 [2,1] 重复
 *   3. 存答案必须拷贝路径：add(new ArrayList<>(path))；直接存 path 引用，
 *      后面撤销时列表被清空，记录下来的答案也跟着空了
 *   4. N 皇后对角线下标映射：主对角线 i-j 可能为负数，要加 n-1 平移到非负下标；
 *      副对角线 i+j 范围是 0 ~ 2n-2，数组长度要开 2n-1
 *   5. 剪枝是回溯的核心价值：不剪枝就是纯暴力枚举；提前判断"这条分支不可能出答案"，
 *      直接返回，能省下整棵子树
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置排列组合/回溯求解器；路径与答案用 ArrayList 存储，
 *   去重用 HashSet。回溯思想广泛用于正则匹配、数独求解、 constraint 搜索。
 *
 * 一句话口诀：
 *   做选择、往下钻、撤选择；排列标 used，组合传 start；存答案先拷贝；能剪就剪不犹豫。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BacktrackingDemo.java
 *   java BacktrackingDemo
 */
public class BacktrackingDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 全排列：used 数组标记用过的元素 ==========");
        demoPermutation();

        System.out.println();
        System.out.println("========== 2. 组合 C(n,k)：start 起点剪枝，避免重复 ==========");
        demoCombination();

        System.out.println();
        System.out.println("========== 3. N 皇后：列 + 两条对角线三个标记数组 ==========");
        demoNQueens();

        System.out.println();
        System.out.println("========== 4. 子集枚举：每个元素选 / 不选 ==========");
        demoSubsets();

        System.out.println();
        System.out.println("========== 5. 剪枝的意义：剪与不剪访问的节点数对比 ==========");
        demoPruning();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 1. 全排列
    // =========================================================
    private static void demoPermutation() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = permute(nums);

        System.out.println("数组：" + Arrays.toString(nums));
        for (List<Integer> perm : result) {
            System.out.println("  " + perm);
        }

        // 交叉验证：排列个数必须是 3! = 6，且两两不同，每个排列都包含 1、2、3
        check(result.size() == 6, "全排列共 3! = 6 种");
        Set<String> unique = new HashSet<>();
        for (List<Integer> perm : result) {
            check(isPermutationOf(perm, nums), "排列 " + perm + " 包含 1、2、3 各一次");
            unique.add(perm.toString());
        }
        check(unique.size() == 6, "6 个排列两两不同，没有重复");
    }

    /** 全排列入口：准备 used 数组与路径，调用回溯 */
    static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrackPermute(nums, used, new ArrayList<>(), result);
        return result;
    }

    /**
     * 全排列回溯：
     *   选择：从没用过的元素里挑一个，放进路径，标记 used
     *   递归：继续排剩下的位置
     *   撤销：把它从路径拿掉，used 改回 false（对称操作）
     */
    private static void backtrackPermute(int[] nums, boolean[] used,
                                        List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            // 走到叶子：拷贝一份路径存下来（易错点 3）
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue; // 这个元素已经在路径里，跳过
            }
            used[i] = true;            // ① 选择
            path.add(nums[i]);
            backtrackPermute(nums, used, path, result); // ② 递归深入
            path.remove(path.size() - 1); // ③ 撤销
            used[i] = false;
        }
    }

    // =========================================================
    // 2. 组合 C(n, k)
    // =========================================================
    private static void demoCombination() {
        List<List<Integer>> combo = combine(5, 2);
        System.out.println("C(5, 2) 组合：");
        for (List<Integer> c : combo) {
            System.out.println("  " + c);
        }

        // 交叉验证：C(5,2) = 10 个；每个组合长度 2；元素严格递增（无 [2,1] 这种重复形式）
        check(combo.size() == 10, "C(5,2) = 10 个组合");
        Set<String> comboSet = new HashSet<>();
        for (List<Integer> c : combo) {
            check(c.size() == 2, "组合 " + c + " 长度为 2");
            check(isIncreasing(c), "组合 " + c + " 严格递增（没有顺序重复）");
            comboSet.add(c.toString());
        }
        check(comboSet.size() == 10, "10 个组合两两不同");

        List<List<Integer>> combo42 = combine(4, 2);
        check(combo42.size() == 6, "C(4,2) = 6 个组合");
    }

    /** 组合入口：从数字 1 开始选，选满 k 个为止 */
    static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackCombine(1, n, k, new ArrayList<>(), result);
        return result;
    }

    /**
     * 组合回溯：start 是本次可以选的最小数字。
     * 只能从 start 往后挑，保证组合内部递增，不会出现 [1,2] 和 [2,1] 两种重复写法。
     */
    private static void backtrackCombine(int start, int n, int k,
                                        List<Integer> path, List<List<Integer>> result) {
        if (path.size() == k) {
            result.add(new ArrayList<>(path));
            return;
        }
        // 剪枝：剩余可选数字（start ~ n）的个数，凑不齐 k - path.size() 个就不用走了
        int remainNeed = k - path.size();
        if (n - start + 1 < remainNeed) {
            return;
        }
        for (int i = start; i <= n; i++) {
            path.add(i);
            backtrackCombine(i + 1, n, k, path, result); // 下一轮只能从 i+1 开始
            path.remove(path.size() - 1);
        }
    }

    // =========================================================
    // 3. N 皇后
    // =========================================================
    private static void demoNQueens() {
        // n=4 是规模小、能打印棋盘的经典例子
        List<List<String>> solutions4 = solveNQueens(4);
        System.out.println("4 皇后共 " + solutions4.size() + " 个解，打印第一个解：");
        printBoard(solutions4.get(0));

        // n=8 的解数是经典标准答案 92；n=1 只有 1 个解
        int count8 = countNQueens(8);
        int count1 = countNQueens(1);
        System.out.println("8 皇后解数 = " + count8 + "，1 皇后解数 = " + count1);

        check(solutions4.size() == 2, "4 皇后共 2 个解");
        check(count8 == 92, "8 皇后解数 = 92（标准答案）");
        check(count1 == 1, "1 皇后有 1 个解");
    }

    /** 求 n 皇后所有解的棋盘列表（棋盘用字符串数组表示） */
    static List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        int[] placement = new int[n]; // placement[row] = 该行皇后所在列
        boolean[] colUsed = new boolean[n];
        boolean[] mainDiag = new boolean[2 * n - 1]; // 主对角线：row - col + n - 1
        boolean[] antiDiag = new boolean[2 * n - 1]; // 副对角线：row + col
        backtrackQueen(0, n, placement, colUsed, mainDiag, antiDiag, solutions);
        return solutions;
    }

    /** 只数解的个数，复用同一套回溯 */
    static int countNQueens(int n) {
        return solveNQueens(n).size();
    }

    private static void backtrackQueen(int row, int n, int[] placement,
                                       boolean[] colUsed, boolean[] mainDiag,
                                       boolean[] antiDiag, List<List<String>> solutions) {
        if (row == n) {
            solutions.add(buildBoard(placement));
            return;
        }
        for (int col = 0; col < n; col++) {
            int mainIdx = row - col + n - 1; // 平移负下标（易错点 4）
            int antiIdx = row + col;
            if (colUsed[col] || mainDiag[mainIdx] || antiDiag[antiIdx]) {
                continue; // 列冲突、主对角线冲突、副对角线冲突，都不能放
            }
            placement[row] = col;
            colUsed[col] = true;
            mainDiag[mainIdx] = true;
            antiDiag[antiIdx] = true;

            backtrackQueen(row + 1, n, placement, colUsed, mainDiag, antiDiag, solutions);

            // 撤销四个标记
            antiDiag[antiIdx] = false;
            mainDiag[mainIdx] = false;
            colUsed[col] = false;
        }
    }

    /** 把皇后位置数组转成 . / Q 组成的棋盘字符串列表 */
    private static List<String> buildBoard(int[] placement) {
        List<String> board = new ArrayList<>();
        for (int col : placement) {
            char[] row = new char[placement.length];
            Arrays.fill(row, '.');
            row[col] = 'Q';
            board.add(new String(row));
        }
        return board;
    }

    // =========================================================
    // 4. 子集枚举
    // =========================================================
    private static void demoSubsets() {
        int[] nums = {1, 2, 3};
        List<List<Integer>> subsets = subsets(nums);

        System.out.println("数组：" + Arrays.toString(nums));
        for (List<Integer> subset : subsets) {
            System.out.println("  " + subset);
        }

        // n 个元素的子集共 2^n 个，包括空集
        check(subsets.size() == 8, "3 个元素的子集共 2^3 = 8 个（含空集）");
        check(containsSubset(subsets, new ArrayList<>()), "子集列表包含空集");
        check(containsSubset(subsets, Arrays.asList(1, 2, 3)), "子集列表包含全集");
    }

    /** 子集入口：每个元素都有"选 / 不选"两条分支 */
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackSubset(0, nums, new ArrayList<>(), result);
        return result;
    }

    private static void backtrackSubset(int index, int[] nums,
                                       List<Integer> path, List<List<Integer>> result) {
        if (index == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        // 分支一：不选 nums[index]，直接往下走
        backtrackSubset(index + 1, nums, path, result);

        // 分支二：选 nums[index]，加入路径后往下走，回来撤销
        path.add(nums[index]);
        backtrackSubset(index + 1, nums, path, result);
        path.remove(path.size() - 1);
    }

    // =========================================================
    // 5. 剪枝对比：同样是组合，提前剪枝能少走多少节点
    // =========================================================
    private static void demoPruning() {
        // 统计两种写法在 C(8, 3) 上访问的回溯节点数（含内部节点和叶子）
        int nodesWithoutPrune = countCombinationNodes(8, 3, false);
        int nodesWithPrune = countCombinationNodes(8, 3, true);
        System.out.println("C(8,3) 不剪枝访问节点 = " + nodesWithoutPrune
                + "，提前剪枝访问节点 = " + nodesWithPrune);
        check(nodesWithPrune <= nodesWithoutPrune, "剪枝后访问节点数不增加，凑不齐数量的分支被提前砍掉");
        check(nodesWithPrune >= 10, "剪枝不会漏掉答案：叶子组合数仍是 C(8,3)=56 量级的搜索");

        // 再验证剪枝版答案数量和不剪枝版一致（剪枝只砍无效分支，不能砍答案）
        check(combine(8, 3).size() == 56, "C(8,3) 正确组合数 = 56");
    }

    /** 统计组合回溯过程中访问的节点数；withPrune 控制是否启用剩余数量剪枝 */
    private static int countCombinationNodes(int n, int k, boolean withPrune) {
        int[] counter = {0};
        backtrackCount(1, n, k, withPrune, counter);
        return counter[0];
    }

    private static void backtrackCount(int start, int n, int k, boolean withPrune, int[] counter) {
        counter[0]++; // 每进入一个节点记一次
        if (k == 0) {
            return;
        }
        if (withPrune && n - start + 1 < k) {
            return;
        }
        for (int i = start; i <= n; i++) {
            backtrackCount(i + 1, n, k - 1, withPrune, counter);
        }
    }

    // =========================================================
    // 辅助校验方法
    // =========================================================

    /** 判断 list 中的元素是否恰好是 nums 的一个排列（多重集合一致） */
    private static boolean isPermutationOf(List<Integer> list, int[] nums) {
        if (list.size() != nums.length) {
            return false;
        }
        int[] copy = list.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(copy);
        int[] expected = nums.clone();
        Arrays.sort(expected);
        return Arrays.equals(copy, expected);
    }

    /** 判断组合内元素是否严格递增 */
    private static boolean isIncreasing(List<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1) >= list.get(i)) {
                return false;
            }
        }
        return true;
    }

    /** 判断子集列表里是否包含某个子集（按内容比较） */
    private static boolean containsSubset(List<List<Integer>> subsets, List<Integer> target) {
        for (List<Integer> subset : subsets) {
            if (subset.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /** 打印一张棋盘 */
    private static void printBoard(List<String> board) {
        for (String row : board) {
            System.out.println("  " + row);
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
