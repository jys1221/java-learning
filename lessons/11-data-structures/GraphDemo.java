import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 知识点：图的两种存储（邻接矩阵 vs 邻接表）
 * ============================================================
 * 一句话：图是"顶点 + 边"的集合；存图有两种主流方式——矩阵用二维数组记"两点之间有没有边"，
 *         表用"每个顶点一个邻居链表"记它连着谁。稠密图适合矩阵，稀疏图适合邻接表。
 *
 * 为什么需要它：
 *   地图导航、社交关系、网络拓扑、任务依赖都建模成图；选对存储直接决定空间占用和查询速度。
 *   两种结构各有取舍，面试和做题时先判断图稠密还是稀疏。
 *
 * 复杂度速查（n = 顶点数，m = 边数）：
 *   结构        空间        判边 hasEdge   求度 degree    遍历邻居
 *   邻接矩阵    O(n²)      O(1) 直接下标  O(n) 数一整行   O(n) 扫一行
 *   邻接表      O(n+m)     O(deg) 扫链表  O(1) 链表长度   O(deg) 走链表
 *
 * 易错点：
 *   1. 无向图的一条边 (u,v) 在矩阵里写对称两个位置；在邻接表里 u 加 v、v 加 u。
 *   2. 矩阵判边快，但 n 很大且边很少时，大量格子是 false，空间被浪费（O(n²)）。
 *   3. 邻接表判边要沿链表找，最坏 O(deg)；不能像矩阵那样 O(1) 直达。
 *   4. 度 = 该顶点的邻居个数；有向图还要再分出入度和出度（本文件只讲无向无权图）。
 *   5. 顶点下标必须在 0~n-1 范围内；加边前要做下标合法性检查。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置图结构；工程里常用 List<List<Integer>> 实现邻接表，用二维数组实现矩阵。
 *   本文件手写两种结构，并让两者在同一张图上对拍"边是否存在、度是多少"。
 *
 * 一句话口诀：
 *   矩阵判边 O(1) 但吃空间，邻接表省空间但找边要链表；稠密用矩阵，稀疏用邻接表。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 GraphDemo.java
 *   java GraphDemo
 */
public class GraphDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 样例图：两种结构分别建图 ==========");
        final int n = 5;
        int[][] edges = {{0, 1}, {0, 2}, {1, 2}, {1, 3}, {2, 4}, {3, 4}};

        AdjacencyMatrix matrix = new AdjacencyMatrix(n);
        AdjacencyList list = new AdjacencyList(n);
        for (int[] e : edges) {
            matrix.addEdge(e[0], e[1]);
            list.addEdge(e[0], e[1]);
        }

        System.out.println("  邻接矩阵：");
        matrix.print();
        System.out.println("  邻接表：");
        list.print();

        System.out.println();
        System.out.println("========== 2. 边存在性与度：两种结构对拍 ==========");
        // 样例图手算度数：0=2, 1=3, 2=3, 3=2, 4=2
        int[] expectedDegree = {2, 3, 3, 2, 2};
        for (int v = 0; v < n; v++) {
            int dm = matrix.degree(v);
            int dl = list.degree(v);
            System.out.println("  顶点 " + v + "：矩阵度=" + dm + "，邻接表度=" + dl);
            check(dm == expectedDegree[v], "顶点 " + v + " 矩阵度为 " + expectedDegree[v]);
            check(dl == dm, "顶点 " + v + " 邻接表度与矩阵一致");
        }

        // 逐对判断边是否存在，两种结构必须一致
        boolean edgeAgree = true;
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (matrix.hasEdge(u, v) != list.hasEdge(u, v)) {
                    edgeAgree = false;
                }
            }
        }
        check(edgeAgree, "所有顶点对：矩阵与邻接表的边存在性一致");
        check(matrix.hasEdge(0, 1) && !matrix.hasEdge(0, 4), "0-1 有边，0-4 无边");

        System.out.println();
        System.out.println("========== 3. 空间/时间取舍对比表 ==========");
        printComparisonTable();

        System.out.println();
        System.out.println("========== 4. 固定种子实测：随机稀疏图两种结构建图 ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 实现一：邻接矩阵
    // =========================================================
    static class AdjacencyMatrix {

        private final boolean[][] adj;
        private final int n;

        AdjacencyMatrix(int n) {
            this.n = n;
            adj = new boolean[n][n]; // 默认 false：初始任意两点无边
        }

        private void checkIndex(int v) {
            if (v < 0 || v >= n) {
                throw new IndexOutOfBoundsException("顶点下标越界：" + v);
            }
        }

        /** 无向图加边：对称两个位置都置 true */
        void addEdge(int u, int v) {
            checkIndex(u);
            checkIndex(v);
            adj[u][v] = true;
            adj[v][u] = true;
        }

        /** 判边：二维数组下标直达，O(1) */
        boolean hasEdge(int u, int v) {
            checkIndex(u);
            checkIndex(v);
            return adj[u][v];
        }

        /** 求度：数一整行有多少个 true，O(n) */
        int degree(int v) {
            checkIndex(v);
            int count = 0;
            for (int u = 0; u < n; u++) {
                if (adj[v][u]) {
                    count++;
                }
            }
            return count;
        }

        void print() {
            for (int u = 0; u < n; u++) {
                StringBuilder sb = new StringBuilder("    ");
                for (int v = 0; v < n; v++) {
                    sb.append(adj[u][v] ? 1 : 0).append(' ');
                }
                System.out.println(sb);
            }
        }
    }

    // =========================================================
    // 实现二：邻接表
    // =========================================================
    static class AdjacencyList {

        private final List<List<Integer>> adj;
        private final int n;

        AdjacencyList(int n) {
            this.n = n;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
        }

        private void checkIndex(int v) {
            if (v < 0 || v >= n) {
                throw new IndexOutOfBoundsException("顶点下标越界：" + v);
            }
        }

        /** 无向图加边：u 的链表加 v，v 的链表加 u */
        void addEdge(int u, int v) {
            checkIndex(u);
            checkIndex(v);
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        /** 判边：沿 u 的邻居链表扫描，O(deg) */
        boolean hasEdge(int u, int v) {
            checkIndex(u);
            checkIndex(v);
            return adj.get(u).contains(v);
        }

        /** 求度：邻居链表长度即度，O(1) */
        int degree(int v) {
            checkIndex(v);
            return adj.get(v).size();
        }

        void print() {
            for (int u = 0; u < n; u++) {
                System.out.println("    " + u + " -> " + adj.get(u));
            }
        }
    }

    // ---------------------------------------------------------
    // 对比表（纯文本对齐）
    // ---------------------------------------------------------
    private static void printComparisonTable() {
        System.out.println("  维度            邻接矩阵            邻接表");
        System.out.println("  ------------    ------------        ------------");
        System.out.println("  空间            O(n²)               O(n+m)");
        System.out.println("  判边            O(1) 下标直达      O(deg) 扫链表");
        System.out.println("  求度            O(n) 数一行         O(1) 链表长度");
        System.out.println("  适用图          稠密图（边多）      稀疏图（边少）");
        System.out.println("  缺点            空边也占格子        找边不能直达");
        check(true, "已列出两种结构的空间/时间取舍对比");
    }

    // ---------------------------------------------------------
    // 实测：固定种子生成稀疏随机图，两种结构建图并对拍度数
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 1000;
        Random rnd = new Random(20261128);

        AdjacencyMatrix matrix = new AdjacencyMatrix(n);
        AdjacencyList list = new AdjacencyList(n);

        long t1 = System.nanoTime();
        int edgeCount = 0;
        for (int i = 0; i < n * 3; i++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u != v && !matrix.hasEdge(u, v)) {
                // 跳过重复边：矩阵天然去重，邻接表也要保持同样的边集合
                matrix.addEdge(u, v);
                list.addEdge(u, v);
                edgeCount++;
            }
        }
        long buildMs = (System.nanoTime() - t1) / 1_000_000;

        // 对拍所有顶点度数
        boolean agree = true;
        for (int v = 0; v < n; v++) {
            if (matrix.degree(v) != list.degree(v)) {
                agree = false;
            }
        }
        System.out.println("  随机 " + edgeCount + " 条边，建图耗时 " + buildMs + " ms");
        check(agree, "随机图上两种结构度数逐一一致");
        check(edgeCount > 0, "确实加入了若干条边");
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
