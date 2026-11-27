import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * 知识点：二叉搜索树（BST：插入 / 查找 / 删除、中序有序、退化问题）
 * ============================================================
 * 一句话：BST 在二叉树上加一条规矩——左子树所有值 < 根 < 右子树所有值；
 *         于是中序遍历天然就是升序，查找像二分一样一路向左或向右。
 *
 * 为什么需要它：
 *   普通二叉树查找只能 O(n) 逐个翻；BST 靠"大小分边"把每次比较砍掉一半候选，理想情况查找 O(log n)。
 *   TreeSet / TreeMap 的有序能力就建立在 BST（平衡版：红黑树）之上。
 *
 * 复杂度速查（n = 节点个数，h = 树高）：
 *   操作              平均        最坏          说明
 *   查找 contains()   O(log n)    O(n)         平衡时 h=log n；退化成链表时 h=n
 *   插入 insert()     O(log n)    O(n)         沿树高找到空位挂上去
 *   删除 remove()     O(log n)    O(n)         双子节点要找后继（右子树最左）再删
 *   中序遍历          O(n)        O(n)         结果必为升序
 *
 * 易错点：
 *   1. BST 规矩是"整棵左子树都小于根、整棵右子树都大于根"，不只是直接孩子；插错位置就违反性质。
 *   2. 删除双子节点时，不能直接删根节点；要用"后继"（右子树最小值）或"前驱"（左子树最大值）顶替，
 *      再去删掉那个顶替节点，保证中序仍然有序。
 *   3. 按升序或降序依次插入，BST 会退化成一条链表，树高等于 n，查找退回 O(n)——这就是需要 AVL / 红黑树的原因。
 *   4. 本文件不处理重复值：插入已存在的 key 直接忽略（业务里重复值可挂计数字段）。
 *
 * 与 Java 自带类的对应关系：
 *   java.util.TreeSet 底层是 TreeMap，TreeMap 底层是红黑树（自平衡 BST）；
 *   本文件手写的是"裸 BST"，不做旋转平衡，所以有序但可能退化；和 TreeSet 对拍中序结果即可看出一致性。
 *
 * 一句话口诀：
 *   左小右大是 BST，中序遍历必升序；删叶直接摘，单孩接孩子，双子找后继顶替。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BinarySearchTreeDemo.java
 *   java BinarySearchTreeDemo
 */
public class BinarySearchTreeDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 插入与查找：按大小分边 ==========");
        MyBST tree = new MyBST();
        int[] seed = {5, 3, 7, 2, 4, 6, 8};
        for (int v : seed) {
            tree.insert(v);
        }
        List<Integer> in = tree.inorder();
        System.out.println("  中序遍历：" + in);
        check(in.equals(List.of(2, 3, 4, 5, 6, 7, 8)), "BST 中序为严格升序");

        check(tree.contains(6), "查找存在的 6 为 true");
        check(!tree.contains(9), "查找不存在的 9 为 false");
        check(tree.height() == 3, "样例 BST 树高为 3（平衡）");

        System.out.println();
        System.out.println("========== 2. 删除三种情况 ==========");
        // 情况一：叶子节点 2（左右都空）
        tree.remove(2);
        System.out.println("  删除叶子 2 后：" + tree.inorder());
        check(!tree.contains(2) && tree.inorder().equals(List.of(3, 4, 5, 6, 7, 8)), "删除叶子 2 后中序仍升序");

        // 情况二：只有一个孩子的节点 7（右孩子 8，无左孩子）
        tree.remove(7);
        System.out.println("  删除单孩节点 7 后：" + tree.inorder());
        check(!tree.contains(7) && tree.inorder().equals(List.of(3, 4, 5, 6, 8)), "删除单孩节点 7 后 8 顶上");

        // 情况三：两个孩子的节点 5（后继是右子树最左的 6）
        tree.remove(5);
        System.out.println("  删除双子节点 5（后继 6 顶替）后：" + tree.inorder());
        check(!tree.contains(5) && tree.inorder().equals(List.of(3, 4, 6, 8)), "双子节点删除后中序仍升序");

        System.out.println();
        System.out.println("========== 3. 与 TreeSet 对拍：同批数据迭代顺序一致 ==========");
        MyBST other = new MyBST();
        TreeSet<Integer> set = new TreeSet<>();
        int[] batch = {40, 20, 60, 10, 30, 50, 70, 25};
        for (int v : batch) {
            other.insert(v);
            set.add(v);
        }
        List<Integer> myOrder = other.inorder();
        List<Integer> jdkOrder = new ArrayList<>(set);
        System.out.println("  自写 BST：" + myOrder);
        System.out.println("  TreeSet ：" + jdkOrder);
        check(myOrder.equals(jdkOrder), "自写 BST 中序与 TreeSet 迭代顺序完全一致");
        check(other.inorder().equals(jdkOrder.stream().sorted().toList()), "两边结果都等于升序排列");

        System.out.println();
        System.out.println("========== 4. 退化演示：按升序插入会变链表，最坏 O(n) ==========");
        demoDegenerate();

        System.out.println();
        System.out.println("========== 5. 固定种子实测：随机插入（较平衡）vs 顺序插入（退化）==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 手写 BST
    // =========================================================
    static class MyBST {

        static class Node {
            int val;
            Node left;
            Node right;

            Node(int val) {
                this.val = val;
            }
        }

        private Node root;

        void insert(int value) {
            root = insertRec(root, value);
        }

        /** 递归插入：比根小走左，比根大走右；重复值忽略 */
        private Node insertRec(Node node, int value) {
            if (node == null) {
                return new Node(value); // 找到空位，挂新节点
            }
            if (value < node.val) {
                node.left = insertRec(node.left, value);
            } else if (value > node.val) {
                node.right = insertRec(node.right, value);
            }
            // 相等：重复值忽略
            return node;
        }

        boolean contains(int value) {
            Node cur = root;
            while (cur != null) {
                if (value == cur.val) {
                    return true;
                }
                cur = value < cur.val ? cur.left : cur.right;
            }
            return false;
        }

        void remove(int value) {
            root = removeRec(root, value);
        }

        private Node removeRec(Node node, int value) {
            if (node == null) {
                return null;
            }
            if (value < node.val) {
                node.left = removeRec(node.left, value);
                return node;
            }
            if (value > node.val) {
                node.right = removeRec(node.right, value);
                return node;
            }
            // 找到了待删节点，分三种情况
            if (node.left == null) {
                // 情况一/二：左空（叶子或只有右孩子），直接把右孩子接上
                return node.right;
            }
            if (node.right == null) {
                // 情况二：只有左孩子，把左孩子接上
                return node.left;
            }
            // 情况三：两个孩子。取后继 = 右子树最左节点，用它的值顶替，再删掉后继
            Node successor = minNode(node.right);
            node.val = successor.val;
            node.right = removeRec(node.right, successor.val);
            return node;
        }

        /** 找某子树的最左节点（最小值） */
        private Node minNode(Node node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        List<Integer> inorder() {
            List<Integer> out = new ArrayList<>();
            inorderRec(root, out);
            return out;
        }

        private void inorderRec(Node node, List<Integer> out) {
            if (node == null) {
                return;
            }
            inorderRec(node.left, out);
            out.add(node.val);
            inorderRec(node.right, out);
        }

        int height() {
            return heightRec(root);
        }

        private int heightRec(Node node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(heightRec(node.left), heightRec(node.right));
        }
    }

    // ---------------------------------------------------------
    // 退化演示：顺序插入 1..n，树高等于 n，查找最后一个要走 n 步
    // ---------------------------------------------------------
    private static void demoDegenerate() {
        final int n = 20;
        MyBST degenerate = new MyBST();
        for (int i = 1; i <= n; i++) {
            degenerate.insert(i);
        }
        System.out.println("  顺序插入 1.." + n + " 后树高=" + degenerate.height() + "（等于节点数，退化成右斜链）");
        check(degenerate.height() == n, "顺序插入导致树高 = n，退化成链表");
        check(degenerate.contains(n), "退化链表末端元素仍能查到（但要走 n 步）");
    }

    // ---------------------------------------------------------
    // 实测：固定种子，随机打乱插入（较平衡）vs 顺序插入（退化）
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 5000;
        Random rnd = new Random(20261128);

        // 顺序插入：必然退化
        MyBST line = new MyBST();
        long t1 = System.nanoTime();
        for (int i = 0; i < n; i++) {
            line.insert(i);
        }
        long lineInsertMs = (System.nanoTime() - t1) / 1_000_000;

        // 随机打乱后插入：左右分布较均匀，树高接近 log n
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        MyBST balanced = new MyBST();
        long t2 = System.nanoTime();
        for (int v : arr) {
            balanced.insert(v);
        }
        long balInsertMs = (System.nanoTime() - t2) / 1_000_000;

        // 查找末尾元素：退化树要走 n 步，平衡树走 log n 步
        long t3 = System.nanoTime();
        boolean hitLine = line.contains(n - 1);
        long lineGetMs = (System.nanoTime() - t3) / 1_000_000;

        long t4 = System.nanoTime();
        boolean hitBal = balanced.contains(n - 1);
        long balGetMs = (System.nanoTime() - t4) / 1_000_000;

        System.out.println("  顺序插入 " + n + " 条：" + lineInsertMs + " ms，树高=" + line.height());
        System.out.println("  打乱插入 " + n + " 条：" + balInsertMs + " ms，树高=" + balanced.height());
        System.out.println("  查末尾元素：退化树 " + lineGetMs + " ms，平衡树 " + balGetMs + " ms");

        check(hitLine && hitBal, "两种结构都能查到末尾元素");
        check(line.height() == n, "顺序插入树高仍为 n");
        check(balanced.height() < n, "打乱插入树高明显小于 n（接近对数级）");
        System.out.println("  结论：裸 BST 不保证平衡，真实有序集合请用 TreeSet / TreeMap（红黑树）。");
    }

    // =========================================================
    // 断言小工具
    // =========================================================
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
