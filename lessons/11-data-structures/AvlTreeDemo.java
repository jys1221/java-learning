import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * 知识点：AVL 平衡二叉搜索树（平衡因子、四种失衡与四种旋转、插入回溯调平）
 * ============================================================
 * 一句话：AVL 是"自带体检"的 BST：每个节点记左右子树高度差（平衡因子），
 *         一旦失衡就用旋转把高的一侧压下去，保证左右高度差不超过 1，树高始终 O(log n)。
 *
 * 为什么需要它：
 *   裸 BST 按顺序插入会退化成链表，查找 O(n)；AVL 在插入后回溯检查高度，
 *   发现哪边过高就旋转纠正，用一点维护代价换来稳定的对数级查找。
 *
 * 复杂度速查（n = 节点个数）：
 *   操作              平均 / 最坏      说明
 *   插入 insert()     O(log n)       沿树高下探，回溯时更新高度并按需旋转
 *   查找 contains()    O(log n)       严格平衡，树高 <= 1.44 log2(n+2)
 *   删除 remove()      O(log n)       本文件只演示插入调平；删除同样需要回溯旋转
 *   旋转（左/右旋）    O(1)           只改几个指针和高度，不遍历节点
 *
 * 易错点：
 *   1. 平衡因子 = 左子树高 - 右子树高；>1 说明左边过高，<-1 说明右边过高。
 *   2. 四种失衡要先看"哪边高"，再看"新节点落在哪一侧"：
 *      LL 左高且插在左孩子左侧 → 右旋；RR 右高且插在右孩子右侧 → 左旋；
 *      LR 左高但插在左孩子右侧 → 先左旋左孩子、再右旋根；
 *      RL 右高但插在右孩子左侧 → 先右旋右孩子、再左旋根。
 *   3. 旋转后必须先更新被旋转节点的高度，再更新新根的高度（子节点先、父节点后）。
 *   4. 高度约定：空树高度 0，叶子高度 1；平衡因子用高度差，不是节点个数差。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有直接提供 AVL；TreeSet / TreeMap 用的是红黑树（另一种平衡 BST）。
 *   AVL 平衡更严格（高度差 <=1），查找更快；红黑树平衡略松，插入删除旋转更少。
 *   本文件手写 AVL，中序结果与 TreeSet 对拍一致。
 *
 * 一句话口诀：
 *   左高右旋，右高左旋；LR 先左后右，RL 先右后左；转完先算子树高，再算根高。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 AvlTreeDemo.java
 *   java AvlTreeDemo
 */
public class AvlTreeDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 四种失衡与四种旋转 ==========");
        demoFourRotations();

        System.out.println();
        System.out.println("========== 2. 插入后回溯调平：随机数据保持平衡 ==========");
        demoBalancedInsert();

        System.out.println();
        System.out.println("========== 3. 与 TreeSet 对拍中序 ==========");
        demoCrossCheckJdk();

        System.out.println();
        System.out.println("========== 4. 固定种子实测：AVL 树高随 n 对数增长 ==========");
        demoHeightGrowth();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // AVL 节点与实现
    // =========================================================
    static class AvlTree {

        static class Node {
            int val;
            Node left;
            Node right;
            int height; // 以该节点为根的子树高度；空树 0，叶子 1

            Node(int val) {
                this.val = val;
                this.height = 1;
            }
        }

        private Node root;
        /** 是否打印每次调平过程；样例演示开，大规模实测关，避免刷屏 */
        private final boolean verbose;

        AvlTree() {
            this(true);
        }

        AvlTree(boolean verbose) {
            this.verbose = verbose;
        }

        /** 取高度，空节点按 0 处理，避免空指针 */
        private int height(Node node) {
            return node == null ? 0 : node.height;
        }

        /** 平衡因子 = 左高 - 右高 */
        private int balanceFactor(Node node) {
            return height(node.left) - height(node.right);
        }

        void insert(int value) {
            root = insertRec(root, value);
        }

        private Node insertRec(Node node, int value) {
            // 第一步：和普通 BST 一样下探到空位挂节点
            if (node == null) {
                return new Node(value);
            }
            if (value < node.val) {
                node.left = insertRec(node.left, value);
            } else if (value > node.val) {
                node.right = insertRec(node.right, value);
            } else {
                return node; // 重复值忽略
            }

            // 第二步：回溯时更新当前节点高度（子树高度变了，父节点高度也要重算）
            node.height = 1 + Math.max(height(node.left), height(node.right));

            // 第三步：算平衡因子，判断属于哪种失衡，决定旋转方式
            int bf = balanceFactor(node);

            // LL：左边过高，且新节点落在左孩子的左侧 → 右旋
            if (bf > 1 && value < node.left.val) {
                if (verbose) {
                    System.out.println("  [调平] LL 失衡，对 " + node.val + " 做右旋");
                }
                return rightRotate(node);
            }
            // RR：右边过高，且新节点落在右孩子的右侧 → 左旋
            if (bf < -1 && value > node.right.val) {
                if (verbose) {
                    System.out.println("  [调平] RR 失衡，对 " + node.val + " 做左旋");
                }
                return leftRotate(node);
            }
            // LR：左边过高，但新节点落在左孩子的右侧 → 先把左孩子左旋，再把根右旋
            if (bf > 1 && value > node.left.val) {
                if (verbose) {
                    System.out.println("  [调平] LR 失衡，先左旋左孩子 " + node.left.val + "，再右旋根 " + node.val);
                }
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            // RL：右边过高，但新节点落在右孩子的左侧 → 先把右孩子右旋，再把根左旋
            if (bf < -1 && value < node.right.val) {
                if (verbose) {
                    System.out.println("  [调平] RL 失衡，先右旋右孩子 " + node.right.val + "，再左旋根 " + node.val);
                }
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            return node; // 平衡，无需旋转
        }

        /**
         * 右旋（处理 LL）：y 是失衡根，x 是它的左孩子。
         * 把 x 提上来当根，y 降到 x 的右孩子，x 原来的右子树 t2 挂到 y 的左边。
         */
        private Node rightRotate(Node y) {
            Node x = y.left;
            Node t2 = x.right;

            x.right = y;
            y.left = t2;

            // 先更新被压下去的 y，再更新新根 x（子先父后）
            y.height = 1 + Math.max(height(y.left), height(y.right));
            x.height = 1 + Math.max(height(x.left), height(x.right));
            return x;
        }

        /**
         * 左旋（处理 RR）：x 是失衡根，y 是它的右孩子。
         * 把 y 提上来当根，x 降到 y 的左孩子，y 原来的左子树 t2 挂到 x 的右边。
         */
        private Node leftRotate(Node x) {
            Node y = x.right;
            Node t2 = y.left;

            y.left = x;
            x.right = t2;

            x.height = 1 + Math.max(height(x.left), height(x.right));
            y.height = 1 + Math.max(height(y.left), height(y.right));
            return y;
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
            return height(root);
        }
    }

    // ---------------------------------------------------------
    // 1. 逐个构造四种失衡，观察对应旋转
    // ---------------------------------------------------------
    private static void demoFourRotations() {
        // LL：30 → 20 → 10，左孩子左侧过重，右旋后根为 20
        AvlTree ll = new AvlTree();
        ll.insert(30);
        ll.insert(20);
        ll.insert(10);
        System.out.println("  LL 中序：" + ll.inorder());
        check(ll.inorder().equals(List.of(10, 20, 30)), "LL 旋转后中序仍为升序");
        check(ll.height() == 2, "LL 调平后树高为 2");

        // RR：10 → 20 → 30，右孩子右侧过重，左旋后根为 20
        AvlTree rr = new AvlTree();
        rr.insert(10);
        rr.insert(20);
        rr.insert(30);
        System.out.println("  RR 中序：" + rr.inorder());
        check(rr.inorder().equals(List.of(10, 20, 30)), "RR 旋转后中序仍为升序");
        check(rr.height() == 2, "RR 调平后树高为 2");

        // LR：30 → 10 → 20，左孩子右侧过重，先左旋 10 再右旋 30，根为 20
        AvlTree lr = new AvlTree();
        lr.insert(30);
        lr.insert(10);
        lr.insert(20);
        System.out.println("  LR 中序：" + lr.inorder());
        check(lr.inorder().equals(List.of(10, 20, 30)), "LR 旋转后中序仍为升序");
        check(lr.height() == 2, "LR 调平后树高为 2");

        // RL：10 → 30 → 20，右孩子左侧过重，先右旋 30 再左旋 10，根为 20
        AvlTree rl = new AvlTree();
        rl.insert(10);
        rl.insert(30);
        rl.insert(20);
        System.out.println("  RL 中序：" + rl.inorder());
        check(rl.inorder().equals(List.of(10, 20, 30)), "RL 旋转后中序仍为升序");
        check(rl.height() == 2, "RL 调平后树高为 2");
    }

    // ---------------------------------------------------------
    // 2. 平衡插入：连续插入后任意节点左右高度差不超过 1
    // ---------------------------------------------------------
    private static void demoBalancedInsert() {
        AvlTree tree = new AvlTree();
        int[] batch = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
        for (int v : batch) {
            tree.insert(v);
        }
        System.out.println("  中序：" + tree.inorder());
        System.out.println("  树高：" + tree.height());
        check(tree.inorder().equals(List.of(10, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80)), "连续插入后中序升序");
        check(tree.height() <= 4, "AVL 树高很小，未退化");
        check(tree.contains(45) && !tree.contains(99), "查找存在与不存在的值");
    }

    // ---------------------------------------------------------
    // 3. 与 TreeSet 对拍
    // ---------------------------------------------------------
    private static void demoCrossCheckJdk() {
        AvlTree mine = new AvlTree();
        TreeSet<Integer> set = new TreeSet<>();
        int[] batch = {42, 7, 99, 13, 55, 21, 8, 36};
        for (int v : batch) {
            mine.insert(v);
            set.add(v);
        }
        List<Integer> myOrder = mine.inorder();
        List<Integer> jdkOrder = new ArrayList<>(set);
        System.out.println("  AVL  ：" + myOrder);
        System.out.println("  TreeSet：" + jdkOrder);
        check(myOrder.equals(jdkOrder), "AVL 中序与 TreeSet 迭代顺序一致");
    }

    // ---------------------------------------------------------
    // 4. 树高随 n 的增长：固定种子，验证接近对数级而不是线性
    // ---------------------------------------------------------
    private static void demoHeightGrowth() {
        final int n = 5000;
        Random rnd = new Random(20261128);
        // 生成 0..n-1 的不重复数据，再打乱顺序插入，避免重复值被忽略导致节点数对不上
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
        AvlTree tree = new AvlTree(false); // 大规模实测关闭调平日志，避免刷屏

        long t1 = System.nanoTime();
        for (int v : arr) {
            tree.insert(v);
        }
        long insertMs = (System.nanoTime() - t1) / 1_000_000;

        int h = tree.height();
        // log2(5000) 约 12.3，AVL 严格平衡，树高应当在十几到二十几量级
        double log2n = Math.log(n) / Math.log(2);
        System.out.println("  插入 " + n + " 条随机值，耗时 " + insertMs + " ms，树高=" + h + "，log2(n)≈" + String.format("%.1f", log2n));
        check(h <= 25, "AVL 树高在对数级（<=25），没有退化");
        check(tree.inorder().size() == n, "插入节点总数为 " + n);
        System.out.println("  取舍说明：AVL 平衡更严格，查找快；红黑树平衡略松，增删旋转更少，JDK 集合选用红黑树。");
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
