import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：位运算技巧 —— 与或异或、lowbit、消 1、判 2 的幂、统计 1、找单数、枚举子集
 * ============================================================
 * 一句话：位运算直接操作二进制位，速度极快；记住几个固定套路：x & -x 取最低位 1，
 *        x & (x-1) 消掉最低位 1，异或成对抵消，掩码枚举子集。
 *
 * 为什么需要它：
 *   状态压缩、集合表示、权限标志位、高频计数，用一个 int 的 32 个二进制位就能搞定，
 *   比存布尔数组省空间也快。很多面试题（判 2 的幂、数 1 的个数、找只出现一次的数）都靠位运算。
 *
 * 复杂度速查（x 为一个整数）：
 *   操作                  时间      说明
 *   取最低位 1：x & -x    O(1)      结果是只有最低位那个 1 的数
 *   消最低位 1：x & (x-1) O(1)      把最右边的 1 清成 0
 *   统计 1 的个数          O(k)      k 为 1 的个数；JDK 的 bitCount 是内置优化版
 *   枚举子集              O(2^n)    用掩码 0 ~ (1<<n)-1
 *
 * 易错点：
 *   1. 优先级坑：& 的优先级低于 ==，if (x & 1 == 1) 会按 x & (1==1) 解析，编译报错；
 *      必须写成 if ((x & 1) == 1)，括号不能省
 *   2. >> 是算术右移（补符号位），>>> 是逻辑右移（补 0）；负数右移两者结果天差地别
 *   3. 判断 2 的幂要先排除 0 和负数：x > 0 && (x & (x-1)) == 0
 *   4. 异或找单数：成对出现的数异或抵消为 0；找两个单数要按某一位不同把数组分成两组
 *   5. 左移 1<<n 当 n 接近 31 时会溢出负数，枚举子集前确认 n 很小
 *
 * 与 Java 自带类的对应关系：
 *   Integer.bitCount(int) 是 JDK 内置的统计二进制 1 个数方法（底层用硬件指令/查表优化），
 *   本文件手写 Brian Kernighan 算法与它对拍；Integer.highestOneBit / lowestOneBit 也有现成实现。
 *
 * 一句话口诀：
 *   与留 1、或留 1、异或成对消；lowbit 取最低 1，x&(x-1) 清最低 1；掩码循环枚举子集。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BitManipulationDemo.java
 *   java BitManipulationDemo
 */
public class BitManipulationDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 基础位运算：& | ^ ~ << >> >>> ==========");
        int a = 12;   // 1100
        int b = 10;   // 1010
        System.out.println("a = " + a + "（" + Integer.toBinaryString(a) + "），b = " + b
                + "（" + Integer.toBinaryString(b) + "）");
        System.out.println("a & b = " + (a & b) + "（" + Integer.toBinaryString(a & b) + "）");
        System.out.println("a | b = " + (a | b) + "（" + Integer.toBinaryString(a | b) + "）");
        System.out.println("a ^ b = " + (a ^ b) + "（" + Integer.toBinaryString(a ^ b) + "）");
        System.out.println("a << 1 = " + (a << 1) + "，a >> 1 = " + (a >> 1));
        check((a & b) == 8, "按位与：1100 & 1010 = 1000 = 8");
        check((a | b) == 14, "按位或：1100 | 1010 = 1110 = 14");
        check((a ^ b) == 6, "按位异或：1100 ^ 1010 = 0110 = 6");

        System.out.println();
        System.out.println("========== 2. lowbit 与消最低位 1 ==========");
        int x = 12;                            // 1100
        int lowbit = x & -x;
        int cleared = x & (x - 1);
        System.out.println("x = " + x + "（1100），x & -x = " + lowbit + "，x & (x-1) = " + cleared);
        check(lowbit == 4, "x & -x 取出最低位的 1：4（0100）");
        check(cleared == 8, "x & (x-1) 消掉最低位 1：1100 → 1000 = 8");

        System.out.println();
        System.out.println("========== 3. 判断 2 的幂 + 统计 1 的个数（与 bitCount 对拍） ==========");
        check(isPowerOfTwo(8), "8 是 2 的幂");
        check(!isPowerOfTwo(6), "6 不是 2 的幂");
        check(!isPowerOfTwo(0), "0 不是 2 的幂");

        Random random = new Random(20261128L);
        for (int i = 0; i < 50; i++) {
            int v = random.nextInt(100000);
            check(countOnes(v) == Integer.bitCount(v), "数字 " + v + " 的 1 的个数与 bitCount 一致");
        }
        System.out.println("已对 50 个随机数校验 1 的个数");

        System.out.println();
        System.out.println("========== 4. 异或找出只出现一次的两个数 ==========");
        int[] nums = {1, 2, 1, 3, 2, 5};
        int[] singles = findTwoSingles(nums);
        System.out.println("数组：" + java.util.Arrays.toString(nums) + "，两个单数："
                + java.util.Arrays.toString(singles));
        check((singles[0] == 3 && singles[1] == 5) || (singles[0] == 5 && singles[1] == 3),
                "成功找出只出现一次的 3 和 5");

        System.out.println();
        System.out.println("========== 5. 位掩码枚举子集 ==========");
        String[] elems = {"a", "b", "c"};
        List<List<String>> subsets = enumerateSubsets(elems);
        System.out.println("子集个数：" + subsets.size());
        for (List<String> subset : subsets) {
            System.out.println("  " + subset);
        }
        check(subsets.size() == (1 << elems.length), "n 个元素共有 2^n 个子集");
        check(subsets.get(subsets.size() - 1).size() == elems.length, "最后一个掩码对应全集");

        System.out.println();
        System.out.println("========== 6. 优先级坑与 >> / >>> 对负数的差别 ==========");
        // 错误写法：if (x & 1 == 1) —— & 优先级低于 ==，会被解析成 x & (1==1)，类型不匹配编译报错。
        // 正确写法必须加括号：if ((x & 1) == 1)
        int odd = 7;
        check((odd & 1) == 1, "加括号后正确判断奇偶：7 是奇数");

        int neg = -1;                           // 二进制全 1
        int rightShift = neg >> 1;              // 算术右移：补符号位 1，仍是 -1
        int unsignedShift = neg >>> 1;          // 逻辑右移：补 0，变成最大正整数
        System.out.println("-1 >> 1 = " + rightShift + "（符号位补 1）");
        System.out.println("-1 >>> 1 = " + unsignedShift + "（高位补 0）");
        check(rightShift == -1, ">> 对负数算术右移，结果仍是 -1");
        check(unsignedShift == Integer.MAX_VALUE, ">>> 对负数逻辑右移，结果为 2^31-1");

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 判断是否为 2 的幂：2 的幂二进制只有一个 1，消掉它就变成 0；先排除 0 和负数
    // ---------------------------------------------------------
    private static boolean isPowerOfTwo(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    // ---------------------------------------------------------
    // 统计二进制中 1 的个数：每用 x & (x-1) 消掉一个 1，计数 +1
    // ---------------------------------------------------------
    private static int countOnes(int x) {
        int count = 0;
        while (x != 0) {
            x &= x - 1;                         // 消掉最低位的 1
            count++;
        }
        return count;
    }

    // ---------------------------------------------------------
    // 找两个只出现一次的数：
    // 1. 全部异或，得到两个单数的异或值 xor；
    // 2. 取 xor 最低位的 1 作为分组依据（这一位两个单数不同）；
    // 3. 按这一位把数组分两组，各组内部异或，分别得到一个单数。
    // ---------------------------------------------------------
    private static int[] findTwoSingles(int[] nums) {
        int xor = 0;
        for (int v : nums) {
            xor ^= v;                           // 成对的抵消，剩下两单数之异或
        }
        int diff = xor & -xor;                  // 最低位不同的那一位
        int x = 0;
        int y = 0;
        for (int v : nums) {
            if ((v & diff) == 0) {
                x ^= v;
            } else {
                y ^= v;
            }
        }
        return new int[]{x, y};
    }

    // ---------------------------------------------------------
    // 枚举子集：掩码 mask 从 0 到 2^n-1，第 j 位为 1 表示选入第 j 个元素
    // ---------------------------------------------------------
    private static List<List<String>> enumerateSubsets(String[] elems) {
        List<List<String>> subsets = new ArrayList<>();
        int n = elems.length;
        for (int mask = 0; mask < (1 << n); mask++) {
            List<String> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) {
                    subset.add(elems[j]);
                }
            }
            subsets.add(subset);
        }
        return subsets;
    }

    /** 断言辅助：条件不满足就抛异常；通过则计数并打印一条 */
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
