import java.util.Arrays;
import java.util.Random;

/**
 * 知识点：递归与分治（Recursion & Divide and Conquer）
 * ============================================================
 * 一句话：把"一个大问题"拆成"结构相同的小问题"，小问题再拆，
 *        直到碰到不能再拆的出口（终止条件），然后一层层把结果带回来（回归）。
 *
 * 为什么需要它：
 *   很多问题天生就是"自己包含自己"的结构：汉诺塔、二叉树、快速幂、排序分治。
 *   用递归写，代码短、思路清晰；用循环硬写反而要自己维护一个栈，容易错。
 *   分治则是"递归思想 + 合并步骤"的工程化版本：分成两半 → 各算各的 → 合并答案。
 *
 * 复杂度速查（n = 问题规模）：
 *   算法              时间        栈空间       说明
 *   阶乘递归          O(n)        O(n)        每层压一个栈帧，深度 = n
 *   朴素斐波那契      O(2^n)      O(n)        大量重复子问题，指数爆炸
 *   记忆化斐波那契    O(n)        O(n)        每个子问题只算一次
 *   汉诺塔            O(2^n)      O(n)        移动步数 = 2^n - 1
 *   分治求最大值      O(n)        O(log n)    对半切，递归树高度 log n
 *   快速幂            O(log n)    O(log n)    指数按二进制折半下降
 *
 * 易错点：
 *   1. 忘了写终止条件，或终止条件永远到不了 → 一直递归到 JVM 栈溢出 StackOverflowError
 *   2. 递归深度太大（几万层）时，即使算法本身正确也会栈溢出；这时要改成迭代或减小深度
 *   3. 记忆化的占位值不能和合法结果冲突：斐波那契结果可能是 0、1，所以 memo 用 -1 而不是 0
 *   4. 分治切区间时左右两半必须无缝衔接：mid 归左、mid+1 归右，写成 [l,mid] 和 [l,mid] 就会漏元素或重复算
 *   5. 阶乘、斐波那契增长极快，int 很快溢出；n 稍大就要用 long，再大要换 BigInteger
 *
 * 与 Java 自带类的对应关系：
 *   JDK 里 Arrays.sort(int[]) 底层是双轴快排（递归 + 分治），Collections.sort 底层是归并（分治合并）；
 *   递归本身没有现成类，快速幂也没有公开的 JDK 方法，但 BigInteger.modPow 内部就是基于模幂分治实现的。
 *
 * 一句话口诀：
 *   先想出口再写递推；小问题算完带回结果；重复子问题记下来；分治三步走：分、治、合。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 RecursionDivideConquerDemo.java
 *   java RecursionDivideConquerDemo
 */
public class RecursionDivideConquerDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 递归三要素：阶乘 factorial ==========");
        demoFactorial();

        System.out.println();
        System.out.println("========== 2. 斐波那契：朴素递归 vs 记忆化（看调用次数爆炸） ==========");
        demoFibonacci();

        System.out.println();
        System.out.println("========== 3. 汉诺塔：打印移动步骤，验证步数 = 2^n - 1 ==========");
        demoHanoi();

        System.out.println();
        System.out.println("========== 4. 分治求数组最大值 + 快速幂 ==========");
        demoDivideMaxAndFastPow();

        System.out.println();
        System.out.println("========== 5. 递归树与复杂度实测：规模对比 ==========");
        demoComplexity();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 阶乘：用最直白的例子讲清递归三要素
    // ---------------------------------------------------------
    private static void demoFactorial() {
        // 递归三要素对照：
        //   ① 终止条件：n == 0 或 n == 1 时返回 1，不再往下拆
        //   ② 递推关系：fact(n) = n * fact(n - 1)，把 n 交给更小的 n-1
        //   ③ 回归：fact(n-1) 算完带回来，再乘上 n，逐层返回
        System.out.println("5! = " + factorial(5));
        System.out.println("0! = " + factorial(0) + "（数学上 0! 定义为 1，这是出口的边界值）");

        check(factorial(5) == 120, "5! = 120");
        check(factorial(0) == 1, "0! = 1（终止条件边界）");
        check(factorial(6) == 720, "6! = 720，递推链 6×5! 正确");
    }

    /**
     * 阶乘递归实现。
     * 注意：这里返回 int，n 超过 12 左右就会溢出，只是教学演示。
     */
    static int factorial(int n) {
        // ① 终止条件：不能再拆的最小问题
        if (n <= 1) {
            return 1;
        }
        // ② 递推：当前层拿 n 去乘"n-1 的阶乘"，先把 n-1 交给下一层
        // ③ 回归：下一层返回后，这里才执行 n * 结果
        return n * factorial(n - 1);
    }

    // ---------------------------------------------------------
    // 2. 斐波那契：朴素递归为什么指数爆炸
    // ---------------------------------------------------------
    private static void demoFibonacci() {
        final int n = 20;

        // 2.1 朴素递归：每算一个 f(k) 都重新拆成 f(k-1) + f(k-2)，
        // 同一个子问题被反复计算成千上万次，递归树越往下越密。
        naiveCalls = 0;
        long naiveResult = naiveFib(n);
        System.out.println("朴素递归 fib(" + n + ") = " + naiveResult
                + "，函数调用次数 = " + naiveCalls);

        // 2.2 记忆化：算过的子问题记进 memo 数组，下次直接取，不再拆树。
        memoCalls = 0;
        long[] memo = new long[n + 1];
        Arrays.fill(memo, -1); // 易错点 3：占位值 -1，不能用 0（fib(0)=0 是合法结果）
        long memoResult = memoFib(n, memo);
        System.out.println("记忆化  fib(" + n + ") = " + memoResult
                + "，函数调用次数 = " + memoCalls);

        // 2.3 迭代法当"标准答案"，和两种递归结果对拍
        long iterative = iterativeFib(n);
        System.out.println("迭代法  fib(" + n + ") = " + iterative);

        check(naiveResult == iterative, "朴素递归结果与迭代法一致");
        check(memoResult == iterative, "记忆化结果与迭代法一致");
        check(memoCalls < naiveCalls, "记忆化调用次数远少于朴素递归（消灭了重复子问题）");
        // 朴素 fib(n) 的调用次数公式：2 * fib(n+1) - 1，n=20 时为 21891
        check(naiveCalls == 2L * iterativeFib(n + 1) - 1, "朴素递归调用次数符合 2·F(n+1)-1 公式");
    }

    /** 朴素递归的调用计数器（static 字段，每次演示前手动归零） */
    private static long naiveCalls = 0;

    /** 朴素斐波那契：f(n) = f(n-1) + f(n-2)，不做任何记录 */
    static long naiveFib(int n) {
        naiveCalls++; // 每进入一次函数就记一次，用来观察爆炸程度
        if (n <= 1) {
            return n;
        }
        return naiveFib(n - 1) + naiveFib(n - 2);
    }

    /** 记忆化斐波那契：memo[k] != -1 说明算过，直接返回 */
    private static long memoCalls = 0;

    static long memoFib(int n, long[] memo) {
        memoCalls++;
        if (n <= 1) {
            return n;
        }
        if (memo[n] != -1) {
            return memo[n]; // 命中缓存，不再往下拆
        }
        memo[n] = memoFib(n - 1, memo) + memoFib(n - 2, memo);
        return memo[n];
    }

    /** 迭代法：滚动两个变量，O(n) 时间 O(1) 空间，用来当对照标准答案 */
    static long iterativeFib(int n) {
        if (n <= 1) {
            return n;
        }
        long prev = 0, curr = 1;
        for (int i = 2; i <= n; i++) {
            long next = prev + curr;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    // ---------------------------------------------------------
    // 3. 汉诺塔：把一摞圆盘从 A 搬到 C，B 做中转
    // ---------------------------------------------------------
    private static void demoHanoi() {
        final int n = 3;
        StringBuilder log = new StringBuilder();
        int steps = hanoi(n, 'A', 'C', 'B', log);

        System.out.println("圆盘数 n = " + n + "，完整移动步骤：");
        System.out.print(log);
        System.out.println("实际步数 = " + steps + "，理论步数 2^" + n + " - 1 = " + ((1 << n) - 1));

        check(steps == (1 << n) - 1, "汉诺塔 n=3 步数 = 2^3 - 1 = 7");

        // n=4 再验一次，避免"巧合通过"
        StringBuilder log4 = new StringBuilder();
        int steps4 = hanoi(4, 'A', 'C', 'B', log4);
        check(steps4 == 15, "汉诺塔 n=4 步数 = 15");
    }

    /**
     * 汉诺塔递归：
     *   ① 先把上面 n-1 个圆盘，从 A 搬到 B（借助 C）
     *   ② 把最大的第 n 个圆盘，从 A 搬到 C
     *   ③ 再把 n-1 个圆盘，从 B 搬到 C（借助 A）
     *
     * @return 一共移动了多少步
     */
    static int hanoi(int n, char from, char to, char aux, StringBuilder log) {
        if (n == 0) {
            return 0; // 没有圆盘，一步都不用走
        }
        int steps = 0;
        steps += hanoi(n - 1, from, aux, to, log);   // ① 小盘先中转出去
        log.append("  圆盘 ").append(n).append("：").append(from).append(" -> ").append(to).append("\n");
        steps++;                                      // ② 大盘归位，记一步
        steps += hanoi(n - 1, aux, to, from, log);  // ③ 小盘再中转回来
        return steps;
    }

    // ---------------------------------------------------------
    // 4. 分治求最大值 + 快速幂
    // ---------------------------------------------------------
    private static void demoDivideMaxAndFastPow() {
        int[] arr = {17, 3, 92, 41, 6, 58, 24, 73, 11, 50};

        // 4.1 分治求最大值：对半切 → 左右各自求最大 → 取两者较大者
        int divideMax = divideMax(arr, 0, arr.length - 1);
        // 对照：自己遍历一遍取最大（最朴素的标准答案）
        int linearMax = arr[0];
        for (int x : arr) {
            linearMax = Math.max(linearMax, x);
        }
        System.out.println("数组：" + Arrays.toString(arr));
        System.out.println("分治最大值 = " + divideMax + "，遍历最大值 = " + linearMax);

        check(divideMax == linearMax, "分治求最大值与遍历结果一致");
        check(divideMax == 92, "最大值确实是 92");

        // 4.2 快速幂：求 base^exp，指数每次折半，O(log n) 次乘法
        long pow210 = fastPow(2, 10);
        long pow35 = fastPow(3, 5);
        long pow70 = fastPow(7, 0); // 出口：任何数的 0 次方都是 1
        System.out.println("快速幂：2^10 = " + pow210 + "，3^5 = " + pow35 + "，7^0 = " + pow70);

        check(pow210 == 1024, "2^10 = 1024");
        check(pow35 == 243, "3^5 = 243");
        check(pow70 == 1, "任何数的 0 次方 = 1（终止条件边界）");
    }

    /**
     * 分治求区间 [left, right] 的最大值。
     * mid 用 left + (right - left) / 2，防止两个大数相加溢出（下标场景好习惯）。
     */
    static int divideMax(int[] a, int left, int right) {
        // ① 终止条件：区间只剩一个元素，最大值就是它自己
        if (left == right) {
            return a[left];
        }
        // ② 分：从中间切成左右两半，互不重叠
        int mid = left + (right - left) / 2;
        int leftMax = divideMax(a, left, mid);
        int rightMax = divideMax(a, mid + 1, right);
        // ③ 合：两半答案带回来，取较大者
        return Math.max(leftMax, rightMax);
    }

    /**
     * 快速幂：base^exp。
     * 偶数：base^exp = (base^(exp/2))²
     * 奇数：base^exp = (base^(exp/2))² × base
     */
    static long fastPow(long base, long exp) {
        if (exp == 0) {
            return 1; // 终止条件
        }
        long half = fastPow(base, exp / 2);
        long result = half * half; // 先折半相乘
        if (exp % 2 == 1) {
            result *= base; // 指数是奇数，再补一个 base
        }
        return result;
    }

    // ---------------------------------------------------------
    // 5. 复杂度实测：同样规模，朴素递归和分治/记忆化差距多大
    // ---------------------------------------------------------
    private static void demoComplexity() {
        // 5.1 分治求最大值：20000 个随机数，对照遍历法计时
        final int n = 20000;
        Random random = new Random(20261128L); // 固定种子，结果可复现
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = random.nextInt(1_000_000);
        }

        long t1 = System.nanoTime();
        int maxByDivide = divideMax(data, 0, data.length - 1);
        long divideMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int maxByLinear = data[0];
        for (int x : data) {
            maxByLinear = Math.max(maxByLinear, x);
        }
        long linearMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：分治求最大 " + divideMs + " ms，遍历求最大 " + linearMs + " ms");
        System.out.println("（两者都是 O(n)，小数据下常数差异比复杂度更明显，别拿毫秒数当复杂度证据）");
        check(maxByDivide == maxByLinear, "大规模分治最大值与遍历结果一致");

        // 5.2 快速幂 vs 连乘：指数越大，折半的优势越明显
        long t3 = System.nanoTime();
        long fast = fastPow(2, 30);
        long fastMs = (System.nanoTime() - t3) / 1_000_000;

        long t4 = System.nanoTime();
        long plain = 1;
        for (int i = 0; i < 30; i++) {
            plain *= 2;
        }
        long plainMs = (System.nanoTime() - t4) / 1_000_000;

        System.out.println("2^30：快速幂 " + fastMs + " ms，连乘 " + plainMs + " ms，结果 = " + fast);
        check(fast == plain, "快速幂与连乘结果一致");
        check(fast == 1_073_741_824L, "2^30 = 1073741824");
    }

    // ---------------------------------------------------------
    // 断言辅助方法（与样板保持一致）
    // ---------------------------------------------------------
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
