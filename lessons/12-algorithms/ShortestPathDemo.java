import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 知识点：最短路径三件套 —— Dijkstra / Bellman-Ford / Floyd
 * ============================================================
 * 一句话：Dijkstra 挑最近的点逐步定案（只认非负权）；Bellman-Ford 反复松弛 n-1 轮（能处理负权、能抓负环）；
 *        Floyd 三重循环把任意两点的最短路一次算全。
 *
 * 为什么需要它：
 *   地图导航、网络路由、任务调度都要回答"从 A 到 B 最少花多少代价"。权值可以是距离、时间、费用；
 *   边权非负用 Dijkstra 最快，出现负权边用 Bellman-Ford，要全两点之间的最短路用 Floyd。
 *
 * 复杂度速查（n = 点数，m = 边数）：
 *   算法            时间            空间      能处理负权   能抓负环   适用
 *   Dijkstra（堆）  O((n+m) log n) O(n)      不能        不能       非负权、单源最短路
 *   Bellman-Ford    O(n·m)         O(n)      能          能         有负权边 / 判负环
 *   Floyd           O(n³)          O(n²)     能（无负环）不能       全源最短路、点很少
 *
 * 易错点：
 *   1. Dijkstra 遇到负权边会出错：贪心"最近点已定案"的前提是后面不可能再更短，负权会打破这个前提
 *   2. Bellman-Ford 必须松弛 n-1 轮；第 n 轮还能松弛，说明存在负环（绕一圈代价变负，最短路无下界）
 *   3. Floyd 三重循环的 k（中间点）必须在最外层，写反了结果是错的
 *   4. 距离数组初始化要用 INF（足够大），对角线为 0；别把不可达点留成 0
 *   5. 负权 ≠ 负环：单条负权边可以有最短路；负环是一条绕回自身、总权为负的环
 *
 * 与 Java 自带类的对应关系：
 *   Dijkstra 用 java.util.PriorityQueue（小顶堆，按距离取最小）；
 *   JDK 没有内置图最短路实现，需要自己建邻接表和边表；PriorityQueue 的懒删除见下方注释。
 *
 * 一句话口诀：
 *   迪杰斯特拉挑最小，负权不能用；贝尔曼松弛 n-1，再来一轮有负环；弗洛伊德中间点 k 在外，任意两点一次算全。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 ShortestPathDemo.java
 *   java ShortestPathDemo
 */
public class ShortestPathDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    /** 不可达的代表值：比任何真实路径都大，但加法时不溢出 */
    private static final int INF = 1_000_000_000;

    public static void main(String[] args) {

        System.out.println("========== 1. 非负权样例图：三种算法结果交叉验证 ==========");
        List<Edge> positiveEdges = buildPositiveGraph();
        int n = 5;

        int[] dijkstraDist = dijkstra(n, positiveEdges, 0);
        int[] bellmanDist = bellmanFord(n, positiveEdges, 0);
        int[][] floyd = floyd(n, positiveEdges);
        int[] floydRow0 = floyd[0];

        System.out.println("Dijkstra 从 0 出发：" + Arrays.toString(dijkstraDist));
        System.out.println("Bellman-Ford 从 0 出发：" + Arrays.toString(bellmanDist));
        System.out.println("Floyd 第 0 行：" + Arrays.toString(floydRow0));

        check(Arrays.equals(dijkstraDist, bellmanDist), "Dijkstra 与 Bellman-Ford 距离一致");
        check(Arrays.equals(bellmanDist, floydRow0), "Bellman-Ford 与 Floyd 第 0 行一致");
        check(dijkstraDist[0] == 0, "起点到自己距离为 0");
        check(dijkstraDist[3] == 4 && dijkstraDist[4] == 7, "样例最短路 0→2→1→3→4 距离为 7");

        System.out.println();
        System.out.println("========== 2. 含负权边（无负环）：Dijkstra 会错，BF 与 Floyd 一致 ==========");
        List<Edge> negativeEdges = buildNegativeEdgeGraph();
        int[] bfNeg = bellmanFord(5, negativeEdges, 0);
        int[][] floydNeg = floyd(5, negativeEdges);
        System.out.println("Bellman-Ford 含负权边：" + Arrays.toString(bfNeg));
        System.out.println("Floyd 含负权边：" + Arrays.toString(floydNeg[0]));
        check(Arrays.equals(bfNeg, floydNeg[0]), "负权图上 BF 与 Floyd 结果一致");
        check(bfNeg[1] == -1, "负权边 2→1(-3) 让点 1 距离被压到 -1");

        System.out.println();
        System.out.println("========== 3. 负环图：第 n 轮仍能松弛 → 判定存在负环 ==========");
        List<Edge> cycleEdges = buildNegativeCycleGraph();
        boolean hasCycle = bellmanFordHasNegativeCycle(5, cycleEdges);
        int[] bfCycle = bellmanFord(5, cycleEdges, 0);
        System.out.println("负环图 BF 距离（已被负环越拉越小，仅作演示）：" + Arrays.toString(bfCycle));
        check(hasCycle, "第 n 轮还能松弛，成功识别负环");

        System.out.println();
        System.out.println("========== 4. 规模实测：随机非负权图上 Dijkstra 与 Floyd 耗时 ==========");
        benchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 边：有向边 u → v，权值 w
    // ---------------------------------------------------------
    static class Edge {
        int u;
        int v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    // ---------------------------------------------------------
    // 1. 非负权图（供三种算法对拍）
    //    0→1(4) 0→2(1) 2→1(2) 1→3(1) 2→3(5) 3→4(3)
    // ---------------------------------------------------------
    private static List<Edge> buildPositiveGraph() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 1));
        edges.add(new Edge(2, 1, 2));
        edges.add(new Edge(1, 3, 1));
        edges.add(new Edge(2, 3, 5));
        edges.add(new Edge(3, 4, 3));
        return edges;
    }

    // ---------------------------------------------------------
    // 2. 含一条负权边但没有负环：0→1(5) 0→2(2) 2→1(-3) 1→3(1) 3→4(2)
    // ---------------------------------------------------------
    private static List<Edge> buildNegativeEdgeGraph() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 5));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(2, 1, -3));
        edges.add(new Edge(1, 3, 1));
        edges.add(new Edge(3, 4, 2));
        return edges;
    }

    // ---------------------------------------------------------
    // 3. 负环图：0→1(1) 1→2(-3) 2→0(1) 成环总权 -1；另加 2→3(4)
    // ---------------------------------------------------------
    private static List<Edge> buildNegativeCycleGraph() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 1));
        edges.add(new Edge(1, 2, -3));
        edges.add(new Edge(2, 0, 1));
        edges.add(new Edge(2, 3, 4));
        return edges;
    }

    // ---------------------------------------------------------
    // Dijkstra：优先队列每次取出当前距离最小的点，松弛它的出边。
    // 懒删除：堆里可能残留旧的、距离更大的条目，弹出来发现 d > dist[u] 就直接跳过。
    // ---------------------------------------------------------
    private static int[] dijkstra(int n, List<Edge> edges, int source) {
        // 邻接表：adj[u] 存 (v, w)
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (Edge e : edges) {
            adj.get(e.u).add(new int[]{e.v, e.w});
        }

        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;

        // 堆元素 int[]{距离, 点}，按距离小顶堆
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, source});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int d = top[0];
            int u = top[1];

            // 懒删除：这是一条过期记录（已经找到更短路径），跳过
            if (d > dist[u]) {
                continue;
            }
            for (int[] road : adj.get(u)) {
                int v = road[0];
                int w = road[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;       // 松弛：找到更短路径就更新
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------
    // Bellman-Ford：对全部边松弛 n-1 轮；每轮都可能把距离再压小。
    // 返回从 source 出发的距离数组。
    // ---------------------------------------------------------
    private static int[] bellmanFord(int n, List<Edge> edges, int source) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;

        // n-1 轮：任意最短路最多经过 n-1 条边
        for (int round = 0; round < n - 1; round++) {
            boolean relaxed = false;
            for (Edge e : edges) {
                if (dist[e.u] != INF && dist[e.u] + e.w < dist[e.v]) {
                    dist[e.v] = dist[e.u] + e.w;
                    relaxed = true;
                }
            }
            if (!relaxed) {
                break;                           // 这一轮没再松弛，提前收敛
            }
        }
        return dist;
    }

    /** 判负环：再走一轮，只要还有边能松弛，就说明存在负环 */
    private static boolean bellmanFordHasNegativeCycle(int n, List<Edge> edges) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[0] = 0;

        for (int round = 0; round < n - 1; round++) {
            for (Edge e : edges) {
                if (dist[e.u] != INF && dist[e.u] + e.w < dist[e.v]) {
                    dist[e.v] = dist[e.u] + e.w;
                }
            }
        }
        // 第 n 轮：仍能松弛 → 负环
        for (Edge e : edges) {
            if (dist[e.u] != INF && dist[e.u] + e.w < dist[e.v]) {
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------------
    // Floyd：任意两点最短路。k 是"允许经过的中间点"，必须在最外层。
    // 原理：先只允许经过点 0，再加入点 1……逐步放宽中转集合。
    // 若把 k 放到内层，等于还没放宽中转集合就用旧数据更新，结果会错。
    // ---------------------------------------------------------
    private static int[][] floyd(int n, List<Edge> edges) {
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;                      // 点到自己距离为 0
        }
        for (Edge e : edges) {
            dist[e.u][e.v] = e.w;
        }

        for (int k = 0; k < n; k++) {           // 中间点：最外层，顺序不能换
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------
    // 4. 规模实测：n 较小，Floyd 是 O(n³)，这里只做量级感受，避免超时
    // ---------------------------------------------------------
    private static void benchmark() {
        final int n = 30;
        Random random = new Random(20261128L);
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < 0.3) {
                    int w = random.nextInt(20) + 1;   // 非负权
                    edges.add(new Edge(i, j, w));
                    edges.add(new Edge(j, i, w));
                }
            }
        }

        long t1 = System.nanoTime();
        int[] d = dijkstra(n, edges, 0);
        long dijkstraMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int[][] f = floyd(n, edges);
        long floydMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：Dijkstra " + dijkstraMs + " ms，Floyd " + floydMs + " ms");
        check(Arrays.equals(d, f[0]), "实测图上 Dijkstra 与 Floyd 第 0 行一致");
        check(d[0] == 0, "实测图起点距离为 0");
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
