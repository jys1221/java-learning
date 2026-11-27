import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：KMP 字符串匹配 —— 前缀函数 next 与线性匹配
 * ============================================================
 * 一句话：先用模式串自己算出"失配时能回退到哪儿"的 next 数组；匹配时主串指针 i 从不回退，
 *        只回退模式串指针 j，整体 O(n + m)。
 *
 * 为什么需要它：
 *   在长文本里找一个子串，暴力法一失配就把主串指针往回挪，最坏 O(n·m)。
 *   KMP 利用"已经匹配过的部分里前后缀相同"的信息，避免重复比对，是字符串算法的基本功。
 *
 * 复杂度速查（n = 文本长度，m = 模式串长度）：
 *   算法        最好      最坏        空间    说明
 *   暴力匹配    O(n·m)   O(n·m)      O(1)    失配就主串回退，简单但慢
 *   KMP         O(n + m) O(n + m)    O(m)    i 不回退，j 沿 next 回退
 *
 * 易错点：
 *   1. next[i] 表示 pattern[0..i] 的"最长相等真前缀与真后缀"长度，不是下标，别混
 *   2. 回退必须用 j = next[j-1]，而不是 next[j]；j-1 才是当前已匹配段的最后位置
 *   3. 匹配成功一次后，j 也要用 next[j-1] 回退，才能继续找重叠的下一处
 *   4. next[0] 固定为 0；算 next 时下标从 1 开始，别从 0 起步越界
 *   5. 模式串比文本长时直接返回空，不要让 j 越界访问
 *
 * 与 Java 自带类的对应关系：
 *   String.indexOf(...) 是 JDK 自带的查找方法；它内部并不是 KMP（早期偏朴素匹配，
 *   新版本有 Boyer-Moore 类的优化思路），本文件只对照"结果是否一致"，不要求算法相同。
 *
 * 一句话口诀：
 *   先求 next 看前后缀，失配回退 j 不回退 i；匹配成功再回退，重叠位置接着找。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 KmpDemo.java
 *   java KmpDemo
 */
public class KmpDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 构造 next 数组：以模式串 abab 为例 ==========");
        String pattern = "abab";
        int[] next = buildNext(pattern);
        System.out.println("模式串：" + pattern);
        System.out.println("next  ：" + java.util.Arrays.toString(next));
        check(next[0] == 0, "next[0] 固定为 0");
        check(next[1] == 0, "ab 没有公共前后缀，next[1]=0");
        check(next[2] == 1, "aba 最长公共前后缀是 a，next[2]=1");
        check(next[3] == 2, "abab 最长公共前后缀是 ab，next[3]=2");

        System.out.println();
        System.out.println("========== 2. 三种方式找匹配：KMP / 暴力 / indexOf 对拍 ==========");
        String text = "abababab";
        List<Integer> kmpResult = kmpSearch(text, pattern);
        List<Integer> bruteResult = bruteSearch(text, pattern);
        List<Integer> jdkResult = indexOfAll(text, pattern);

        System.out.println("文本：" + text + "，模式串：" + pattern);
        System.out.println("KMP   匹配起点：" + kmpResult);
        System.out.println("暴力  匹配起点：" + bruteResult);
        System.out.println("indexOf 起点：" + jdkResult);

        check(kmpResult.equals(bruteResult), "KMP 与暴力匹配结果一致");
        check(kmpResult.equals(jdkResult), "KMP 与 String.indexOf 结果一致");
        check(kmpResult.size() == 3, "abab 在 abababab 中有 3 处重叠匹配");

        System.out.println();
        System.out.println("========== 3. 无匹配与模式串更长的边界情况 ==========");
        check(kmpSearch("hello world", "xyz").isEmpty(), "找不到时返回空列表");
        check(kmpSearch("ab", "abcdef").isEmpty(), "模式串更长时返回空列表");

        System.out.println();
        System.out.println("========== 4. 规模实测：长文本上 KMP 与暴力耗时 ==========");
        benchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 构造前缀函数 next：next[i] = pattern[0..i] 最长相等真前缀/真后缀长度。
    // 回退 j = next[j-1] 的直观解释：当前后缀在 j 处对不上，
    // 就退到"上一段已匹配前缀的最长公共前后缀"长度处继续试，而不是从头再来。
    // ---------------------------------------------------------
    private static int[] buildNext(String pattern) {
        int m = pattern.length();
        int[] next = new int[m];
        next[0] = 0;                             // 单字符没有真前缀，固定 0

        for (int i = 1; i < m; i++) {
            int j = next[i - 1];                 // 上一位已经得到的最长长度
            // 对不上就沿 next 回退，直到能对上或回到 0
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;                             // 对上了，公共长度 +1
            }
            next[i] = j;
        }
        return next;
    }

    // ---------------------------------------------------------
    // KMP 匹配：i 遍历主串不回退；j 是模式串当前已匹配长度，失配沿 next 回退。
    // 返回所有匹配起点下标。
    // ---------------------------------------------------------
    private static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) {
            return result;
        }
        int[] next = buildNext(pattern);

        int j = 0;                               // 模式串已匹配长度
        for (int i = 0; i < n; i++) {
            // 失配：j 沿 next 回退，i 不动
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                result.add(i - m + 1);           // 匹配成功，记录起点
                j = next[j - 1];                 // 回退一格，继续找重叠的下一处
            }
        }
        return result;
    }

    // ---------------------------------------------------------
    // 暴力匹配：双重循环，主串起点 i，逐个字符比对；失配就换下一个起点。
    // 作为"标准答案"和 KMP 对拍。
    // ---------------------------------------------------------
    private static List<Integer> bruteSearch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || m > n) {
            return result;
        }
        for (int i = 0; i <= n - m; i++) {
            int k = 0;
            while (k < m && text.charAt(i + k) == pattern.charAt(k)) {
                k++;
            }
            if (k == m) {
                result.add(i);
            }
        }
        return result;
    }

    /** 用 String.indexOf 循环找出全部起点，作为 JDK 官方结果对照 */
    private static List<Integer> indexOfAll(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int from = 0;
        while (true) {
            int pos = text.indexOf(pattern, from);
            if (pos < 0) {
                break;
            }
            result.add(pos);
            from = pos + 1;                      // 移动一格，允许重叠匹配
        }
        return result;
    }

    // ---------------------------------------------------------
    // 4. 规模实测：随机文本里找一个重复模式，对比两种算法耗时
    // ---------------------------------------------------------
    private static void benchmark() {
        final int n = 20000;
        Random random = new Random(20261128L);
        StringBuilder sb = new StringBuilder(n);
        String alphabet = "ab";
        for (int i = 0; i < n; i++) {
            sb.append(alphabet.charAt(random.nextInt(2)));
        }
        String text = sb.toString();
        String pattern = "ababab";

        long t1 = System.nanoTime();
        List<Integer> kmp = kmpSearch(text, pattern);
        long kmpMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        List<Integer> brute = bruteSearch(text, pattern);
        long bruteMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "，模式串长度 " + pattern.length()
                + "：KMP " + kmpMs + " ms，暴力 " + bruteMs + " ms");
        check(kmp.equals(brute), "长文本上 KMP 与暴力结果一致");
        check(kmp.size() == brute.size(), "两处命中数量相同");
        System.out.println("命中 " + kmp.size() + " 处（随机文本，数值每次由固定种子决定）");
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
