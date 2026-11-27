import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 知识点：二叉树（四种遍历、高度 / 叶子数 / 节点数、前序+中序重建二叉树）
 * ============================================================
 * 一句话：二叉树是"每个节点最多两个孩子"的层级结构；遍历就是按某种顺序把所有节点访问一遍，
 *         前/中/后序的区别只在于"访问根节点"发生在左右子树之前、之间还是之后。
 *
 * 为什么需要它：
 *   数组和链表都是"一条线"，只能线性遍历；树能表达层级、分支与父子关系（目录、表达式、决策、索引结构）。
 *   二叉树是一切树形结构的基础，理解了它的遍历，后面的 BST、AVL、线段树、堆才看得懂。
 *
 * 复杂度速查（n = 节点个数，h = 树高）：
 *   操作              平均 / 最坏      说明
 *   前/中/后序遍历     O(n)           每个节点恰好访问一次；递归深度为 h，树退化时 h = n
 *   层序遍历          O(n)           用队列一层一层放进去、取出来
 *   求高度 / 叶子数    O(n)           一次 DFS 顺带统计
 *   由前序+中序重建    O(n)           用哈希表记录中序下标可把查找根位置降到 O(1)
 *
 * 易错点：
 *   1. 递归遍历的终止条件是 root == null；忘记判空，遇到叶子的孩子就会空指针。
 *   2. 前/中/后序的命名看的是"根在哪一步"：前=根左右，中=左根右，后=左右根；左右子树的顺序不变。
 *   3. 迭代版中序遍历要"一路向左压栈，走到 null 再弹栈访问，再转向右孩子"，顺序别写反。
 *   4. 重建二叉树时，前序确定根，中序划分左右子树；若前后序不一致或有重复值，重建会出错。
 *   5. 树高 = 最长路径上的节点数（本文件约定空树高度为 0）；叶子是左右孩子都为 null 的节点。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有直接暴露"普通二叉树"；TreeMap / TreeSet 的底层是红黑树（二叉搜索树的平衡版），
 *   PriorityQueue 是完全二叉树堆。本文件手写二叉树与遍历，是这些结构的共同地基。
 *
 * 一句话口诀：
 *   前根左右，中左根右，后左右根；层序用队列，递归看终止，重建靠前序定根、中序分边。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 BinaryTreeDemo.java
 *   java BinaryTreeDemo
 */
public class BinaryTreeDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 搭一棵样例二叉树并打印四种遍历 ==========");
        Node root = buildSampleTree();

        List<Integer> preRec = new ArrayList<>();
        List<Integer> inRec = new ArrayList<>();
        List<Integer> postRec = new ArrayList<>();
        preorderRec(root, preRec);
        inorderRec(root, inRec);
        postorderRec(root, postRec);
        List<Integer> level = levelOrder(root);

        System.out.println("  前序（递归）：" + preRec);
        System.out.println("  中序（递归）：" + inRec);
        System.out.println("  后序（递归）：" + postRec);
        System.out.println("  层序（队列）：" + level);

        System.out.println();
        System.out.println("========== 2. 迭代版遍历与递归版对拍 ==========");
        List<Integer> preIter = preorderIter(root);
        List<Integer> inIter = inorderIter(root);
        List<Integer> postIter = postorderIter(root);
        System.out.println("  前序（迭代）：" + preIter);
        System.out.println("  中序（迭代）：" + inIter);
        System.out.println("  后序（迭代）：" + postIter);

        check(preIter.equals(preRec), "迭代前序与递归前序结果一致");
        check(inIter.equals(inRec), "迭代中序与递归中序结果一致");
        check(postIter.equals(postRec), "迭代后序与递归后序结果一致");

        check(preRec.equals(List.of(1, 2, 4, 5, 3)), "样例前序为 1,2,4,5,3");
        check(inRec.equals(List.of(4, 2, 5, 1, 3)), "样例中序为 4,2,5,1,3");
        check(postRec.equals(List.of(4, 5, 2, 3, 1)), "样例后序为 4,5,2,3,1");
        check(level.equals(List.of(1, 2, 3, 4, 5)), "样例层序为 1,2,3,4,5");

        System.out.println();
        System.out.println("========== 3. 高度、叶子数、节点数 ==========");
        int h = height(root);
        int leaves = leafCount(root);
        int nodes = nodeCount(root);
        System.out.println("  树高=" + h + "，叶子数=" + leaves + "，节点数=" + nodes);
        check(h == 3, "样例树高为 3");
        check(leaves == 3, "样例叶子为 4、5、3 共 3 个");
        check(nodes == 5, "样例节点总数为 5");

        System.out.println();
        System.out.println("========== 4. 由前序 + 中序重建二叉树并验证 ==========");
        Node rebuilt = buildFromPreIn(preRec, inRec);
        List<Integer> rebuiltPre = new ArrayList<>();
        List<Integer> rebuiltIn = new ArrayList<>();
        preorderRec(rebuilt, rebuiltPre);
        inorderRec(rebuilt, rebuiltIn);
        System.out.println("  重建后前序：" + rebuiltPre);
        System.out.println("  重建后中序：" + rebuiltIn);
        check(rebuiltPre.equals(preRec), "重建树的前序与原树一致");
        check(rebuiltIn.equals(inRec), "重建树的中序与原树一致");
        check(nodeCount(rebuilt) == 5 && leafCount(rebuilt) == 3, "重建树节点数、叶子数与原树一致");

        System.out.println();
        System.out.println("========== 5. 固定种子规模实测：随机树统计量两种口径一致 ==========");
        demoRandomTree();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 节点定义
    // =========================================================
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }

    /** 样例树：
     *         1
     *        / \
     *       2   3
     *      / \
     *     4   5
     */
    private static Node buildSampleTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        return root;
    }

    // ---------------------------------------------------------
    // 递归版：前序 / 中序 / 后序
    // ---------------------------------------------------------
    private static void preorderRec(Node root, List<Integer> out) {
        if (root == null) {
            return; // 终止条件：空节点直接返回
        }
        out.add(root.val);      // 根：先访问根
        preorderRec(root.left, out);  // 再走左
        preorderRec(root.right, out); // 再走右
    }

    private static void inorderRec(Node root, List<Integer> out) {
        if (root == null) {
            return;
        }
        inorderRec(root.left, out);   // 先走左
        out.add(root.val);            // 根：左子树回来之后再访问根
        inorderRec(root.right, out);  // 最后走右
    }

    private static void postorderRec(Node root, List<Integer> out) {
        if (root == null) {
            return;
        }
        postorderRec(root.left, out);  // 先走左
        postorderRec(root.right, out); // 再走右
        out.add(root.val);             // 根：左右都走完才访问根
    }

    // ---------------------------------------------------------
    // 层序遍历：队列（FIFO），一层一层从左到右
    // ---------------------------------------------------------
    private static List<Integer> levelOrder(Node root) {
        List<Integer> out = new ArrayList<>();
        if (root == null) {
            return out;
        }
        Deque<Node> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node cur = queue.poll();   // 队首出队
            out.add(cur.val);
            if (cur.left != null) {
                queue.offer(cur.left);  // 左孩子入队
            }
            if (cur.right != null) {
                queue.offer(cur.right); // 右孩子入队
            }
        }
        return out;
    }

    // ---------------------------------------------------------
    // 迭代版：前序（栈）。栈是后进先出，所以先压右、再压左，保证左先出
    // ---------------------------------------------------------
    private static List<Integer> preorderIter(Node root) {
        List<Integer> out = new ArrayList<>();
        if (root == null) {
            return out;
        }
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node cur = stack.pop();
            out.add(cur.val);
            if (cur.right != null) {
                stack.push(cur.right); // 右先压，后出
            }
            if (cur.left != null) {
                stack.push(cur.left);  // 左后压，先出
            }
        }
        return out;
    }

    // ---------------------------------------------------------
    // 迭代版：中序。一路向左压栈，弹栈访问后转向右孩子
    // ---------------------------------------------------------
    private static List<Integer> inorderIter(Node root) {
        List<Integer> out = new ArrayList<>();
        Deque<Node> stack = new ArrayDeque<>();
        Node cur = root;
        while (cur != null || !stack.isEmpty()) {
            // 把当前节点及其左链全部压入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();   // 走到最左，弹栈
            out.add(cur.val);    // 访问根
            cur = cur.right;    // 转向右孩子
        }
        return out;
    }

    // ---------------------------------------------------------
    // 迭代版：后序。双栈法：s1 弹出后压 s2，先压左再压右；最后 s2 弹出即后序
    // ---------------------------------------------------------
    private static List<Integer> postorderIter(Node root) {
        List<Integer> out = new ArrayList<>();
        if (root == null) {
            return out;
        }
        Deque<Node> s1 = new ArrayDeque<>();
        Deque<Node> s2 = new ArrayDeque<>();
        s1.push(root);
        while (!s1.isEmpty()) {
            Node cur = s1.pop();
            s2.push(cur);
            if (cur.left != null) {
                s1.push(cur.left);
            }
            if (cur.right != null) {
                s1.push(cur.right);
            }
        }
        while (!s2.isEmpty()) {
            out.add(s2.pop().val);
        }
        return out;
    }

    // ---------------------------------------------------------
    // 高度：空树 0；否则 1 + 左右子树高度的较大值
    // ---------------------------------------------------------
    private static int height(Node root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(height(root.left), height(root.right));
    }

    // ---------------------------------------------------------
    // 叶子数：左右都为 null 才算叶子
    // ---------------------------------------------------------
    private static int leafCount(Node root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        return leafCount(root.left) + leafCount(root.right);
    }

    // ---------------------------------------------------------
    // 节点数：当前 1 个 + 左子树 + 右子树
    // ---------------------------------------------------------
    private static int nodeCount(Node root) {
        if (root == null) {
            return 0;
        }
        return 1 + nodeCount(root.left) + nodeCount(root.right);
    }

    // ---------------------------------------------------------
    // 前序 + 中序重建：前序首元素是根；在中序里找到根，左边是左子树，右边是右子树
    // ---------------------------------------------------------
    private static Node buildFromPreIn(List<Integer> preorder, List<Integer> inorder) {
        // 用哈希表记录每个值在中序中的下标，避免每次线性查找根位置
        Map<Integer, Integer> inIndex = new HashMap<>();
        for (int i = 0; i < inorder.size(); i++) {
            inIndex.put(inorder.get(i), i);
        }
        return buildRec(preorder, 0, preorder.size() - 1,
                inorder, 0, inorder.size() - 1, inIndex);
    }

    private static Node buildRec(List<Integer> pre, int preL, int preR,
                                 List<Integer> in, int inL, int inR,
                                 Map<Integer, Integer> inIndex) {
        if (preL > preR || inL > inR) {
            return null;
        }
        int rootVal = pre.get(preL);          // 前序首元素即根
        Node root = new Node(rootVal);
        int k = inIndex.get(rootVal);         // 根在中序中的位置
        int leftSize = k - inL;               // 左子树节点个数
        // 左子树：前序 [preL+1, preL+leftSize]，中序 [inL, k-1]
        root.left = buildRec(pre, preL + 1, preL + leftSize, in, inL, k - 1, inIndex);
        // 右子树：前序 [preL+leftSize+1, preR]，中序 [k+1, inR]
        root.right = buildRec(pre, preL + leftSize + 1, preR, in, k + 1, inR, inIndex);
        return root;
    }

    // ---------------------------------------------------------
    // 规模实测：固定种子生成随机二叉树（按随机左/右挂接），统计两种口径一致
    // ---------------------------------------------------------
    private static void demoRandomTree() {
        final int n = 2000;
        Random rnd = new Random(20261128);

        long t1 = System.nanoTime();
        Node randomRoot = null;
        // 逐个随机挂到已有节点的左或右孩子位置（演示用，可能退化）
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }
        randomRoot = nodes[0];
        for (int i = 1; i < n; i++) {
            Node parent;
            // 找一个还有空位的父节点，保证每个新节点都真正挂上去、不覆盖已有孩子
            do {
                parent = nodes[rnd.nextInt(i)];
            } while (parent.left != null && parent.right != null);
            if (parent.left == null) {
                parent.left = nodes[i];
            } else {
                parent.right = nodes[i];
            }
        }
        long buildMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int nodesA = nodeCount(randomRoot);
        int leavesA = leafCount(randomRoot);
        int hA = height(randomRoot);
        long statMs = (System.nanoTime() - t2) / 1_000_000;

        // 用层序遍历再数一遍节点数，作为独立口径交叉验证
        int nodesB = levelOrder(randomRoot).size();

        System.out.println("  随机树节点 " + n + "，构建耗时 " + buildMs + " ms，统计耗时 " + statMs + " ms");
        System.out.println("  递归口径：节点=" + nodesA + "，叶子=" + leavesA + "，树高=" + hA);
        System.out.println("  层序口径节点数=" + nodesB);

        check(nodesA == n, "随机树递归节点数为 " + n);
        check(nodesB == n, "层序遍历数出的节点数与递归口径一致");
        check(leavesA >= 1, "随机树至少有 1 个叶子");
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
