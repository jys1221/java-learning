import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 知识点：最小生成树（MST）—— Prim 与 Kruskal
 * ============================================================
 * 一句话：Prim 从一个点出发，每次挑连接"树内/树外"的最小横切边往里扩；
 *        Kruskal 把所有边排序，从小到大挑不形成环的边，用并查集判环。
 *
 * 为什么需要它：
 *   要把若干地点用最低成本连通（修路、铺电缆、集群组网），又不能有多余的环，
 *   这就是生成树问题；在所有生成树里总权值最小的那棵，叫最小生成树。
 *
 * 复杂度速查（n = 点数，m = 边数）：
 *   算法        时间            空间    核心数据结构      适合
 *   Prim（堆）  O((n+m) log n)  O(n)   优先队列          稠密图（边多，邻接表/矩阵都好扩）
 *   Kruskal     O(m log m)      O(m)   边排序 + 并查集   稀疏图（边少，排序代价小）
 *
 * 易错点：
 *   1. Kruskal 选边前必须先排序；一条边两端已经在同一集合，加进去就成环，必须跳过
 *   2. Prim 里"横切边"指一头在树内、一头在树外；树内到树内的旧边不能再选
 *   3. 生成树一定有 n-1 条边；选中不足 n-1 条说明原图不连通（无生成树）
 *   4. 边权相同可能有多棵 MST，总权值一定相同，但具体选哪条边可以不同
 *   5. 并查集不做路径压缩，最坏会退化成 O(n)；合并要按秩/大小，别无脑挂
 *
 * 与 Java 自带类的对应关系：
 *   Prim 用 java.util.PriorityQueue；Kruskal 用 Collections.sort 对边排序；
 *   并查集 JDK 没有内置，本文件手写一个带路径压缩的嵌套类。
 *
 * 一句话口诀：
 *   Prim 围着树找最小横切边；Kruskal 边排序、并查集判环；稠密选 Prim，稀疏选 Kruskal。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 MinimumSpanningTreeDemo.java
 *   java MinimumSpanningTreeDemo
 */
public class MinimumSpanningTreeDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 样例图：Prim 与 Kruskal 各自选出 MST ==========");
        List<WeightedEdge> edges = buildSampleGraph();
        int n = 6;

        List<WeightedEdge> primTree = prim(n, edges);
        List<WeightedEdge> kruskalTree = kruskal(n, edges);

        System.out.println("Prim 选中边：");
        printEdges(primTree);
        System.out.println("Kruskal 选中边：");
        printEdges(kruskalTree);

        int primTotal = sumWeight(primTree);
        int kruskalTotal = sumWeight(kruskalTree);
        System.out.println("Prim 总权值：" + primTotal + "，Kruskal 总权值：" + kruskalTotal);

        check(primTree.size() == n - 1, "Prim 生成树有 n-1 = 5 条边");
        check(kruskalTree.size() == n - 1, "Kruskal 生成树有 n-1 = 5 条边");
        check(primTotal == kruskalTotal, "两种算法总权值相等");
        check(primTotal == 14, "样例 MST 总权值手工核算为 14");

        System.out.println();
        System.out.println("========== 2. 稠密图：随机完全图上两种算法对拍 ==========");
        benchmark(true);

        System.out.println();
        System.out.println("========== 3. 稀疏图：随机少边图上两种算法对拍 + 耗时 ==========");
        benchmark(false);

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 带权无向边
    // ---------------------------------------------------------
    static class WeightedEdge {
        int u;
        int v;
        int w;

        WeightedEdge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public String toString() {
            return u + "—" + v + "(" + w + ")";
        }
    }

    /** 并查集：判一条边两端是否已经连通（加边会不会成环） */
    static class UnionFind {
        int[] parent;
        int[] rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;                  // 初始各自一个集合
            }
        }

        /** 查找根节点，顺带路径压缩：把路上的节点直接挂到根下 */
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        /** 合并两个集合，按秩挂；返回 false 表示本来就在同一集合（会成环） */
        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return false;
            }
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            return true;
        }
    }

    // ---------------------------------------------------------
    // 样例图：0-1(4) 0-2(1) 1-2(2) 1-3(3) 2-3(5) 3-4(2) 4-5(6) 3-5(7)
    // ---------------------------------------------------------
    private static List<WeightedEdge> buildSampleGraph() {
        List<WeightedEdge> edges = new ArrayList<>();
        edges.add(new WeightedEdge(0, 1, 4));
        edges.add(new WeightedEdge(0, 2, 1));
        edges.add(new WeightedEdge(1, 2, 2));
        edges.add(new WeightedEdge(1, 3, 3));
        edges.add(new WeightedEdge(2, 3, 5));
        edges.add(new WeightedEdge(3, 4, 2));
        edges.add(new WeightedEdge(4, 5, 6));
        edges.add(new WeightedEdge(3, 5, 7));
        return edges;
    }

    // ---------------------------------------------------------
    // Prim：从点 0 出发，dist[u] 记录树外点到当前树的最小边权。
    // 每次从堆里取出离树最近的树外点加入树，再用它的出边更新横切边。
    // ---------------------------------------------------------
    private static List<WeightedEdge> prim(int n, List<WeightedEdge> edges) {
        // 邻接表：adj[u] 存 (v, w)
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (WeightedEdge e : edges) {
            adj.get(e.u).add(new int[]{e.v, e.w});
            adj.get(e.v).add(new int[]{e.u, e.w});
        }

        boolean[] inTree = new boolean[n];       // 点是否已经在生成树里
        int[] dist = new int[n];                 // 到树的最小边权
        int[] parent = new int[n];                // 记录是哪条边把它拉进树
        java.util.Arrays.fill(dist, Integer.MAX_VALUE);
        java.util.Arrays.fill(parent, -1);

        List<WeightedEdge> tree = new ArrayList<>();
        dist[0] = 0;
        // 小顶堆：(边权, 点)
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int u = top[1];
            if (inTree[u]) {
                continue;                         // 懒删除：旧条目，已入树
            }
            inTree[u] = true;
            if (parent[u] != -1) {
                tree.add(new WeightedEdge(parent[u], u, dist[u]));
            }
            for (int[] road : adj.get(u)) {
                int v = road[0];
                int w = road[1];
                if (!inTree[v] && w < dist[v]) {
                    dist[v] = w;                 // 更新横切边的最小权
                    parent[v] = u;
                    pq.offer(new int[]{w, v});
                }
            }
        }
        return tree;
    }

    // ---------------------------------------------------------
    // Kruskal：边按权从小到大排；依次尝试加边，两端不连通就收进 MST，
    // 已连通说明加进去成环，跳过。并查集负责判断连通性。
    // ---------------------------------------------------------
    private static List<WeightedEdge> kruskal(int n, List<WeightedEdge> edges) {
        List<WeightedEdge> sorted = new ArrayList<>(edges);
        Collections.sort(sorted, (a, b) -> a.w - b.w);

        UnionFind uf = new UnionFind(n);
        List<WeightedEdge> tree = new ArrayList<>();

        for (WeightedEdge e : sorted) {
            if (uf.union(e.u, e.v)) {
                tree.add(e);                     // 两端不在同一集合，收进 MST
                if (tree.size() == n - 1) {
                    break;                       // 凑够 n-1 条，提前收工
                }
            }
        }
        return tree;
    }

    private static int sumWeight(List<WeightedEdge> tree) {
        int sum = 0;
        for (WeightedEdge e : tree) {
            sum += e.w;
        }
        return sum;
    }

    private static void printEdges(List<WeightedEdge> tree) {
        for (WeightedEdge e : tree) {
            System.out.println("    " + e);
        }
    }

    // ---------------------------------------------------------
    // 规模实测：dense = true 生成接近完全图（稠密），否则只加少量边（稀疏）
    // ---------------------------------------------------------
    private static void benchmark(boolean dense) {
        final int n = 100;
        Random random = new Random(20261128L);
        List<WeightedEdge> edges = new ArrayList<>();
        // 先加一条 0-1-2-…-(n-1) 的骨架边，保证整图连通，MST 才跨全部点
        for (int i = 1; i < n; i++) {
            edges.add(new WeightedEdge(i - 1, i, random.nextInt(50) + 1));
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double prob = dense ? 0.5 : 0.05;
                if (random.nextDouble() < prob) {
                    edges.add(new WeightedEdge(i, j, random.nextInt(50) + 1));
                }
            }
        }

        long t1 = System.nanoTime();
        List<WeightedEdge> p = prim(n, edges);
        long primMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        List<WeightedEdge> k = kruskal(n, edges);
        long kruskalMs = (System.nanoTime() - t2) / 1_000_000;

        String kind = dense ? "稠密" : "稀疏";
        System.out.println(kind + "图 n = " + n + "，边数 = " + edges.size()
                + "：Prim " + primMs + " ms，Kruskal " + kruskalMs + " ms");
        check(sumWeight(p) == sumWeight(k), kind + "图上 Prim 与 Kruskal 总权值一致");
        check(p.size() == k.size(), kind + "图上两棵树边数相同");
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
