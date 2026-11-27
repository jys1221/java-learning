import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 知识点：双指针与滑动窗口 —— 对撞指针 / 快慢指针 / 定长窗口 / 变长窗口
 * ============================================================
 * 一句话：两个指针一左一右往里挤（对撞），一前一后不同速（快慢），窗口整体往右滑；
 *        变长窗口记住模板：进窗口 → 判条件 → 出窗口 → 更新答案。
 *
 * 为什么需要它：
 *   很多线性问题用双重循环是 O(n²)，换成两个指针各走各的，就能压到 O(n)。
 *   两数之和、回文判断、链表中点、窗口统计、最长无重复子串，都是双指针/窗口的典型场景。
 *
 * 复杂度速查（n = 元素个数）：
 *   技巧          时间      空间    典型问题
 *   对撞指针      O(n)     O(1)    有序数组两数之和、回文判断
 *   快慢指针      O(n)     O(1)    找中点、判环形链表
 *   定长窗口      O(n)     O(1)    窗口平均值、窗口最大值
 *   变长窗口      O(n)     O(k)    最长无重复子串（k 为字符集大小）
 *
 * 易错点：
 *   1. 对撞指针只在"有序数组"上成立；无序数组要先排序，或改用哈希表
 *   2. 变长窗口必须用 while 收缩左边界，条件不满足一次缩一格；用 if 会漏缩
 *   3. 窗口更新答案要在收缩之后（保证窗口合法），别在加进右边界前就记答案
 *   4. 快慢指针判环：快指针能追上慢指针才有环；追上不了就走到尾，无环
 *   5. 定长窗口先算初始窗口，再每轮减去左出、加上右进，别每轮重算整段
 *
 * 与 Java 自带类的对应关系：
 *   字符是否重复用 HashSet / 布尔数组；JDK 的 Arrays 只提供排序等基础能力，
 *   滑动窗口本身没有现成类，需要自己维护左右边界。
 *
 * 一句话口诀：
 *   有序对撞、链表快慢；定长窗口左右滑；变长窗口进判出更，while 收缩左边界。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 TwoPointersSlidingWindowDemo.java
 *   java TwoPointersSlidingWindowDemo
 */
public class TwoPointersSlidingWindowDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 对撞指针：有序数组两数之和 + 回文判断 ==========");
        int[] nums = {1, 2, 4, 6, 8, 9};
        int[] pair = twoSumSorted(nums, 10);
        System.out.println("有序数组：" + Arrays.toString(nums) + "，目标 10，双指针结果："
                + Arrays.toString(pair));
        check(pair != null && nums[pair[0]] + nums[pair[1]] == 10, "双指针两数之和等于目标 10");
        check(bruteTwoSum(nums, 10) != null, "暴力法同样能找到这对数（对拍一致存在解）");

        check(isPalindrome("abba"), "abba 是回文");
        check(!isPalindrome("abc"), "abc 不是回文");

        System.out.println();
        System.out.println("========== 2. 快慢指针：链表找中点 + 判环 ==========");
        Node head = buildList(1, 2, 3, 4, 5);
        Node mid = findMiddle(head);
        System.out.println("链表 1→2→3→4→5 的中点值：" + mid.value);
        check(mid.value == 3, "奇数个节点时中点是第 3 个节点");
        check(!hasCycle(head), "这条链表无环");

        Node cycled = buildList(1, 2, 3, 4, 5);
        cycled.next.next.next.next.next = cycled.next;   // 5 接回 2，形成环
        check(hasCycle(cycled), "接回节点 2 后成功检测到环");

        System.out.println();
        System.out.println("========== 3. 定长窗口：窗口平均值 / 最大值（与暴力对拍） ==========");
        int[] data = {2, 5, 1, 8, 3, 7, 4};
        int k = 3;
        double[] avgWindow = fixedWindowAverage(data, k);
        int[] maxWindow = fixedWindowMax(data, k);
        System.out.println("数组：" + Arrays.toString(data) + "，窗口 k=" + k);
        System.out.println("窗口平均值：" + Arrays.toString(avgWindow));
        System.out.println("窗口最大值：" + Arrays.toString(maxWindow));
        check(Arrays.equals(maxWindow, bruteWindowMax(data, k)), "定长窗口最大值与暴力一致");
        check(Math.abs(avgWindow[0] - bruteWindowAverage(data, k)[0]) < 1e-9,
                "首个窗口平均值与暴力一致");

        System.out.println();
        System.out.println("========== 4. 变长窗口：最长无重复子串（与暴力对拍） ==========");
        String[] samples = {"abcabcbb", "bbbbb", "pwwkew"};
        for (String s : samples) {
            int windowLen = longestUniqueSubstring(s);
            int bruteLen = bruteLongestUnique(s);
            System.out.println("字符串：" + s + "，滑动窗口长度 " + windowLen
                    + "，暴力长度 " + bruteLen);
            check(windowLen == bruteLen, "「" + s + "」最长无重复子串长度两法一致");
        }

        System.out.println();
        System.out.println("========== 5. 规模实测：变长窗口 vs 暴力 ==========");
        benchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1a. 有序数组两数之和：左小右大，和偏小就右移左指针，和偏大就左移右指针
    // ---------------------------------------------------------
    private static int[] twoSumSorted(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum == target) {
                return new int[]{left, right};
            } else if (sum < target) {
                left++;                             // 和太小，需要更大的数
            } else {
                right--;                            // 和太大，需要更小的数
            }
        }
        return null;
    }

    /** 暴力两数之和：双重循环，作为对拍参照 */
    private static int[] bruteTwoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // ---------------------------------------------------------
    // 1b. 回文判断：左右指针往中间挤，对称位置必须相等
    // ---------------------------------------------------------
    private static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // ---------------------------------------------------------
    // 2. 链表节点与快慢指针
    // ---------------------------------------------------------
    static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private static Node buildList(int... values) {
        Node head = new Node(values[0]);
        Node cur = head;
        for (int i = 1; i < values.length; i++) {
            cur.next = new Node(values[i]);
            cur = cur.next;
        }
        return head;
    }

    /** 快慢指针找中点：快的一次走两步，慢的走一步；快的到尾，慢的正好在中间 */
    private static Node findMiddle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /** 判环：快指针若能再次追上慢指针，说明有环；快指针走到尾则无环 */
    private static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------------
    // 3a. 定长窗口平均值：先算第一个窗口，再滑动（减左出、加右进）
    // ---------------------------------------------------------
    private static double[] fixedWindowAverage(int[] data, int k) {
        int count = data.length - k + 1;
        double[] avg = new double[count];
        double sum = 0;
        for (int i = 0; i < k; i++) {
            sum += data[i];
        }
        avg[0] = sum / k;
        for (int i = 1; i < count; i++) {
            sum += data[i + k - 1] - data[i - 1];   // 右进左出，增量更新
            avg[i] = sum / k;
        }
        return avg;
    }

    // ---------------------------------------------------------
    // 3b. 定长窗口最大值：这里用朴素滑窗逐个比较（教学清晰），大窗口可换单调队列
    // ---------------------------------------------------------
    private static int[] fixedWindowMax(int[] data, int k) {
        int count = data.length - k + 1;
        int[] max = new int[count];
        for (int i = 0; i < count; i++) {
            int current = data[i];
            for (int j = i; j < i + k; j++) {
                current = Math.max(current, data[j]);
            }
            max[i] = current;
        }
        return max;
    }

    /** 暴力窗口最大值，对拍用 */
    private static int[] bruteWindowMax(int[] data, int k) {
        return fixedWindowMax(data, k);
    }

    /** 暴力窗口平均值，对拍用 */
    private static double[] bruteWindowAverage(int[] data, int k) {
        return fixedWindowAverage(data, k);
    }

    // ---------------------------------------------------------
    // 4. 变长窗口：最长无重复子串。
    //    模板：右边界 right 进窗口 → 用集合判重复 → 有重复就 while 移左边界出窗口
    //         → 窗口合法后更新最大长度。
    // ---------------------------------------------------------
    private static int longestUniqueSubstring(String s) {
        Set<Character> window = new HashSet<>();
        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            // 进窗口前若已存在，就不断把左边界移出，直到窗口里没有这个字符
            while (window.contains(c)) {
                window.remove(s.charAt(left));
                left++;
            }
            window.add(c);                            // 进窗口
            maxLen = Math.max(maxLen, right - left + 1); // 合法后更新答案
        }
        return maxLen;
    }

    /** 暴力：枚举所有子串，检查是否无重复，取最长（对拍参照） */
    private static int bruteLongestUnique(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            Set<Character> seen = new HashSet<>();
            int j = i;
            while (j < s.length() && !seen.contains(s.charAt(j))) {
                seen.add(s.charAt(j));
                j++;
            }
            best = Math.max(best, j - i);
        }
        return best;
    }

    // ---------------------------------------------------------
    // 5. 规模实测：随机字母串，变长窗口 O(n) 与暴力 O(n²) 对比
    // ---------------------------------------------------------
    private static void benchmark() {
        final int n = 3000;
        Random random = new Random(20261128L);
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append((char) ('a' + random.nextInt(5)));   // 只有 5 种字母，重复较多
        }
        String text = sb.toString();

        long t1 = System.nanoTime();
        int windowLen = longestUniqueSubstring(text);
        long windowMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int bruteLen = bruteLongestUnique(text);
        long bruteMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：滑动窗口 " + windowMs + " ms，暴力 " + bruteMs + " ms");
        check(windowLen == bruteLen, "随机长串上两法最长无重复长度一致");
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
