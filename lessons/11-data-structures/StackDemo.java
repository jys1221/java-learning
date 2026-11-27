import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

/**
 * 知识点：栈（Stack）—— 后进先出 LIFO
 * ============================================================
 * 一句话：栈只在一头（栈顶）进出，最后放进去的最先拿出来，像一摞盘子：
 *        压在最上面的先端走，压在最底下的最后动。
 *
 * 为什么需要它：
 *   括号匹配、表达式求值、方法调用栈、撤销（Undo）、DFS 递归改迭代，本质都是
 *   "按相反顺序倒回去"，这种后进先出的场景用栈最贴切。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作                平均      最坏      说明
 *   入栈 push           O(1)      O(n)      数组版只有扩容那一下 O(n)，均摊 O(1)
 *   出栈 pop            O(1)      O(1)      只动栈顶指针
 *   看栈顶 peek         O(1)      O(1)      不取走
 *   取最小值 min（普通栈）O(n)     O(n)      要扫一遍；最小栈用辅助栈做到 O(1)
 *
 * 易错点：
 *   1. 空栈 pop / peek 会越界，必须先判 isEmpty()
 *   2. 数组栈的栈顶指针 top：空栈是 -1，push 先 ++top 再赋值，pop 先取值再 --top，顺序别反
 *   3. 最小栈的辅助栈：入栈时"小于等于"才压入（等于也要压，否则弹走重复最小值会误删）
 *   4. 出栈时若主栈弹出值等于辅助栈栈顶，辅助栈也要同步弹走
 *
 * 与 Java 自带类的对应关系：
 *   java.util.Stack 是 JDK 早期遗留类（继承 Vector），官方文档明确推荐用
 *   java.util.ArrayDeque 代替它作为栈使用；ArrayDeque 底层是可扩容的循环数组，
 *   栈操作性能更好。本文件手写数组栈 + 链栈，并与 ArrayDeque 做出入栈比对。
 *
 * 一句话口诀：
 *   栈顶一头进和出，后进先出倒着数；括号匹配靠压弹，最小辅助栈记牢。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 StackDemo.java
 *   java StackDemo
 */
public class StackDemo {

    /** 断言通过计数，最后统一打印 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 数组栈与链栈：后进先出 ==========");
        demoArrayAndLinkedStack();

        System.out.println();
        System.out.println("========== 2. 括号匹配校验 ==========");
        demoBracketMatch();

        System.out.println();
        System.out.println("========== 3. 最小栈：辅助栈 O(1) 取最小值 ==========");
        demoMinStack();

        System.out.println();
        System.out.println("========== 4. 与 ArrayDeque 对照 + 出入栈实测 ==========");
        demoCrossCheckAndBenchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 数组栈 + 链栈
    // ---------------------------------------------------------
    private static void demoArrayAndLinkedStack() {
        ArrayStack arrayStack = new ArrayStack();
        int[] seq = { 10, 20, 30, 40 };
        for (int v : seq) {
            arrayStack.push(v);
        }
        System.out.println("数组栈依次压入：10, 20, 30, 40");
        check(arrayStack.size() == 4, "压入 4 个后 size=4");
        check(arrayStack.peek() == 40, "栈顶是最后压入的 40");

        // 出栈顺序应当与入栈相反：40, 30, 20, 10
        int[] out = new int[4];
        for (int i = 0; i < 4; i++) {
            out[i] = arrayStack.pop();
        }
        System.out.println("数组栈出栈顺序：" + java.util.Arrays.toString(out));
        check(java.util.Arrays.equals(out, new int[] { 40, 30, 20, 10 }), "出栈为逆序 40,30,20,10");
        check(arrayStack.isEmpty(), "弹空后栈为空");

        // 链栈行为一致：只动 top 指针
        LinkedStack linkedStack = new LinkedStack();
        for (int v : seq) {
            linkedStack.push(v);
        }
        int[] linkedOut = new int[4];
        for (int i = 0; i < 4; i++) {
            linkedOut[i] = linkedStack.pop();
        }
        System.out.println("链栈出栈顺序：" + java.util.Arrays.toString(linkedOut));
        check(java.util.Arrays.equals(linkedOut, new int[] { 40, 30, 20, 10 }), "链栈同样是逆序出栈");
    }

    // ---------------------------------------------------------
    // 2. 括号匹配
    // ---------------------------------------------------------
    private static void demoBracketMatch() {
        String[] good = { "()", "([])", "{[()]}", "a(b[c]d)e", "" };
        String[] bad = { "(]", "([)]", "(()", "}", "([{}]" };

        for (String s : good) {
            check(isBalanced(s), "合法括号串「" + (s.isEmpty() ? "空串" : s) + "」校验通过");
        }
        for (String s : bad) {
            check(!isBalanced(s), "非法括号串「" + s + "」被拦下");
        }
        System.out.println("括号匹配原理：左括号压栈，右括号弹出配对；栈空还遇右括号、配对不上、最后栈非空，都算错。");
    }

    // ---------------------------------------------------------
    // 3. 最小栈
    // ---------------------------------------------------------
    private static void demoMinStack() {
        MinStack minStack = new MinStack();
        // 压入顺序里最小值有重复：5 出现两次，考验辅助栈同步弹出
        int[] seq = { 5, 3, 8, 3, 9 };
        for (int v : seq) {
            minStack.push(v);
            System.out.println("压入 " + v + "，当前最小值 = " + minStack.min());
        }
        check(minStack.min() == 3, "压完后最小值是 3");

        // 出栈顺序：9, 3, 8, 3, 5；先弹走 9，最小值仍是 3
        minStack.pop();
        check(minStack.min() == 3, "弹走 9 后最小值仍是 3");

        // 弹走上面那个重复的 3，下面那个 3 还在辅助栈里，最小值仍是 3
        minStack.pop();
        check(minStack.min() == 3, "弹走一个 3 后最小值仍是 3");

        // 再弹走 8，最小值还是 3
        minStack.pop();
        check(minStack.min() == 3, "弹走 8 后最小值仍是 3");

        // 最后弹走剩下的 3，辅助栈里只剩 5，最小值回到 5
        minStack.pop();
        check(minStack.min() == 5, "两个 3 都弹走后最小值回到 5");
    }

    // ---------------------------------------------------------
    // 4. 与 ArrayDeque 对照 + 实测
    // ---------------------------------------------------------
    private static void demoCrossCheckAndBenchmark() {
        Random rnd = new Random(42);
        ArrayStack mine = new ArrayStack();
        Deque<Integer> jdk = new ArrayDeque<>();

        // 先压 20 个随机数，两边同步
        for (int i = 0; i < 20; i++) {
            int v = rnd.nextInt(100);
            mine.push(v);
            jdk.push(v);
        }
        // 再各弹 8 个，逐值比对
        for (int i = 0; i < 8; i++) {
            check(mine.pop() == jdk.pop(), "第 " + i + " 次出栈两边值一致");
        }
        check(mine.size() == jdk.size(), "剩余元素个数相同");

        // 实测：20000 次压栈 + 弹栈
        final int n = 20000;
        long t1 = System.nanoTime();
        ArrayStack bench = new ArrayStack();
        for (int i = 0; i < n; i++) {
            bench.push(i);
        }
        while (!bench.isEmpty()) {
            bench.pop();
        }
        long mineMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        Deque<Integer> jdkBench = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            jdkBench.push(i);
        }
        while (!jdkBench.isEmpty()) {
            jdkBench.pop();
        }
        long jdkMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("手写数组栈 " + n + " 次压弹耗时：" + mineMs + " ms");
        System.out.println("ArrayDeque " + n + " 次压弹耗时：" + jdkMs + " ms");
        System.out.println("提示：java.util.Stack 继承 Vector，已属遗留类，工程中请优先用 ArrayDeque。");
    }

    // =========================================================
    // 数组栈：int[] + 栈顶指针 top（空栈 top = -1）
    // =========================================================
    static class ArrayStack {
        private int[] data;
        private int top;

        ArrayStack() {
            data = new int[10];
            top = -1;
        }

        /** 入栈：栈满先扩容，再移动 top 后赋值 */
        void push(int value) {
            if (top == data.length - 1) {
                grow();
            }
            data[++top] = value;
        }

        /** 出栈：先取栈顶值，再把 top 下移一格 */
        int pop() {
            if (isEmpty()) {
                throw new IllegalStateException("空栈不能 pop");
            }
            return data[top--];
        }

        /** 只看栈顶，不取走 */
        int peek() {
            if (isEmpty()) {
                throw new IllegalStateException("空栈不能 peek");
            }
            return data[top];
        }

        boolean isEmpty() {
            return top == -1;
        }

        int size() {
            return top + 1;
        }

        /** 扩容一倍：新建更长数组、整体拷贝、换引用 */
        private void grow() {
            int[] bigger = new int[data.length * 2];
            System.arraycopy(data, 0, bigger, 0, data.length);
            data = bigger;
        }
    }

    // =========================================================
    // 链栈：只保留栈顶节点，push/pop 都在 top 上完成
    // =========================================================
    static class LinkedStack {
        static class Node {
            int val;
            Node next;

            Node(int val) {
                this.val = val;
            }
        }

        private Node top;
        private int size;

        /** 入栈：新节点 next 指向老 top，top 换成新节点 */
        void push(int value) {
            Node node = new Node(value);
            node.next = top;
            top = node;
            size++;
        }

        /** 出栈：取 top 的值，top 后移到下一个节点 */
        int pop() {
            if (isEmpty()) {
                throw new IllegalStateException("空栈不能 pop");
            }
            int value = top.val;
            top = top.next;
            size--;
            return value;
        }

        boolean isEmpty() {
            return top == null;
        }

        int size() {
            return size;
        }
    }

    // =========================================================
    // 最小栈：主栈存全部元素，辅助栈只记"当前最小值"的变化轨迹
    // =========================================================
    static class MinStack {
        private final ArrayStack main = new ArrayStack();
        private final ArrayStack minAux = new ArrayStack();

        /** 入主栈；新值 <= 辅助栈栈顶时也压入辅助栈（等于也要压，便于重复值正确弹出） */
        void push(int value) {
            main.push(value);
            if (minAux.isEmpty() || value <= minAux.peek()) {
                minAux.push(value);
            }
        }

        /** 出主栈；弹出值等于辅助栈栈顶时，辅助栈同步弹出 */
        int pop() {
            int value = main.pop();
            if (value == minAux.peek()) {
                minAux.pop();
            }
            return value;
        }

        /** O(1) 直接取辅助栈栈顶 */
        int min() {
            return minAux.peek();
        }

        boolean isEmpty() {
            return main.isEmpty();
        }
    }

    /** 括号匹配：左压右弹，配对不上就失败 */
    private static boolean isBalanced(String s) {
        ArrayStack stack = new ArrayStack();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    return false; // 栈空还遇右括号，没有左括号可配
                }
                char open = (char) stack.pop();
                if (!matches(open, c)) {
                    return false;
                }
            }
        }
        return stack.isEmpty(); // 最后栈必须空，说明左括号都配完了
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '[' && close == ']')
                || (open == '{' && close == '}');
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
