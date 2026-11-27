import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * 知识点：图的遍历 —— BFS（广度优先）与 DFS（深度优先）
 * ============================================================
 * 一句话：BFS 像水面涟漪一圈一圈扩（队列），DFS 像一条路走到黑再回头（递归 / 显式栈）。
 *
 * 为什么需要它：
 *   图是"点 + 边"连成的结构，机器没法一眼看出哪些点相通。遍历是一切图算法的地基：
 *   求连通块、找最短路（无权图）、拓扑排序、判环、最小生成树，都先要有"把图走一遍"的能力。
 *
 * 复杂度速查（n = 点数，m = 边数）：
 *   算法            时间        空间      特点
 *   BFS（邻接表）   O(n + m)   O(n)      按层扩展，天然给出"到起点的最少边数"
 *   DFS 递归版     O(n + m)   O(n)      代码短，但图很深时会递归爆栈
 *   DFS 显式栈版    O(n + m)   O(n)      手动压栈，栈深可控，序列与递归版一致
 *
 * 易错点：
 *   1. 不写 visited 数组：图上有环，会在环里无限来回走，永不退出
 *   2. BFS 距离数组忘了初始化为 -1 / INF：未访问的点会被当成距离 0
 *   3. DFS 递归版在点极多时栈溢出（JVM 默认栈深约几千），深图要换显式栈
 *   4. 邻接表里邻居顺序不同，访问序列会不同；比较序列时必须固定邻居顺序
 *   5. 遍历只从一个起点出发，不连通的点走不到；统计连通分量要循环遍历所有点
 *
 * 与 Java 自带类的对应关系：
 *   BFS 用 java.util.ArrayDeque（官方推荐的队列实现，比旧的 java.util.Stack 快）；
 *   DFS 显式栈也用 ArrayDeque 的 push/pop；JDK 没有内置通用图遍历，需要自己写邻接表。
 *
 * 一句话口诀：
 *   队列一层一层扩，递归一条路走到黑；visited 挡住回头路，距离数组记层数。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 GraphTraversalDemo.java
 *   java GraphTraversalDemo
 */
public class GraphTraversalDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 建图：邻接表 + 一条独立的边（两个连通分量） ==========");
        List<List<Integer>> graph = buildSampleGraph();
        printGraph(graph);

        System.out.println();
        System.out.println("========== 2. BFS：队列层序输出 + 距离数组（无权图最短路） ==========");
        int start = 0;
        List<Integer> bfsOrder = bfs(graph, start);
        System.out.println("BFS 访问序列：" + bfsOrder);

        int[] dist = bfsDistance(graph, start);
        System.out.println("到起点 " + start + " 的最少边数：" + Arrays.toString(dist));

        System.out.println();
        System.out.println("========== 3. DFS：递归版 vs 显式栈版，序列必须一致 ==========");
        List<Integer> dfsRecursive = dfsRecursive(graph, start);
        List<Integer> dfsStack = dfsExplicitStack(graph, start);
        System.out.println("DFS 递归序列：" + dfsRecursive);
        System.out.println("DFS 显式栈序列：" + dfsStack);

        System.out.println();
        System.out.println("========== 4. 连通分量计数：图被分成几块 ==========");
        int components = countComponents(graph);
        System.out.println("连通分量个数：" + components);

        // 样例图上的手工结论统一在这里校验
        runSampleChecks(graph, bfsOrder, dist, dfsRecursive, dfsStack, components);

        System.out.println();
        System.out.println("========== 5. 规模实测：随机大图上 BFS / DFS 耗时对比 ==========");
        benchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 构造样例图：0-1-3-4-5，0-2-3；6-7 是另一个连通分量
    //    邻接表：graph[u] 里存所有和 u 直接相连的点
    // ---------------------------------------------------------
    private static List<List<Integer>> buildSampleGraph() {
        List<List<Integer>> graph = new ArrayList<>();
        // 先给 8 个点各开一个空邻居列表
        for (int i = 0; i < 8; i++) {
            graph.add(new ArrayList<>());
        }
        // 无向边：两边都要加，加完后邻居顺序固定，序列才可复现
        addUndirectedEdge(graph, 0, 1);
        addUndirectedEdge(graph, 0, 2);
        addUndirectedEdge(graph, 1, 3);
        addUndirectedEdge(graph, 2, 3);
        addUndirectedEdge(graph, 3, 4);
        addUndirectedEdge(graph, 4, 5);
        addUndirectedEdge(graph, 6, 7);
        return graph;
    }

    /** 无向图加一条边：u 的邻居加 v，v 的邻居加 u */
    private static void addUndirectedEdge(List<List<Integer>> graph, int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u);
    }

    private static void printGraph(List<List<Integer>> graph) {
        for (int u = 0; u < graph.size(); u++) {
            System.out.println("  点 " + u + " 的邻居：" + graph.get(u));
        }
    }

    // ---------------------------------------------------------
    // 2. BFS：队列里按"先到先扩展"的顺序处理，visited 防止重复入队
    // ---------------------------------------------------------
    private static List<Integer> bfs(List<List<Integer>> graph, int start) {
        int n = graph.size();
        boolean[] visited = new boolean[n];      // 这个点走过没有，环上不会回头
        List<Integer> order = new ArrayList<>(); // 记录访问顺序，便于打印和断言
        Queue<Integer> queue = new ArrayDeque<>();

        visited[start] = true;                   // 起点先标记，否则可能被邻居再次入队
        queue.offer(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();                // 取出队首，它这一层先处理
            order.add(u);
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;           // 入队时就标记，避免同一个点进两次队列
                    queue.offer(v);
                }
            }
        }
        return order;
    }

    /** BFS 同时算距离：dist[u] = 从起点到 u 的最少边数（无权图最短路） */
    private static int[] bfsDistance(List<List<Integer>> graph, int start) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);                   // -1 表示不可达，别留默认 0 造成歧义
        Queue<Integer> queue = new ArrayDeque<>();

        dist[start] = 0;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : graph.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;       // 多走一条边，距离 +1
                    queue.offer(v);
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------
    // 3a. DFS 递归版：走到一个未访问的邻居就立刻钻进去，走不下去再回来
    // ---------------------------------------------------------
    private static List<Integer> dfsRecursive(List<List<Integer>> graph, int start) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        dfsRecursive(graph, start, visited, order);
        return order;
    }

    private static void dfsRecursive(List<List<Integer>> graph, int u,
                                     boolean[] visited, List<Integer> order) {
        visited[u] = true;
        order.add(u);
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                dfsRecursive(graph, v, visited, order); // 钻进去，回来后继续下一个邻居
            }
        }
    }

    // ---------------------------------------------------------
    // 3b. DFS 显式栈版：用栈帧 {节点, 下一个待访问的邻居下标} 模拟递归调用栈。
    //     每一层停在自己的位置上，钻完一个孩子再回来继续下一个邻居，
    //     这样访问顺序和递归版逐条一致（普通"压邻居"写法会因为老节点卡在栈底而错位）。
    // ---------------------------------------------------------
    private static List<Integer> dfsExplicitStack(List<List<Integer>> graph, int start) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        // 栈帧用 int[]{节点编号, 邻居游标}；ArrayDeque 既当栈又能 peek 看栈顶
        ArrayDeque<int[]> stack = new ArrayDeque<>();

        visited[start] = true;
        order.add(start);
        stack.push(new int[]{start, 0});

        while (!stack.isEmpty()) {
            int[] frame = stack.peek();          // 看栈顶，但先不弹出
            int u = frame[0];
            List<Integer> neighbors = graph.get(u);

            if (frame[1] < neighbors.size()) {
                int v = neighbors.get(frame[1]); // 取出当前游标指向的邻居
                frame[1]++;                      // 游标前移，回来时看下一个
                if (!visited[v]) {
                    visited[v] = true;
                    order.add(v);
                    stack.push(new int[]{v, 0}); // 钻进去：压一个新栈帧
                }
            } else {
                stack.pop();                     // 邻居全看完了，这一层退栈
            }
        }
        return order;
    }

    // ---------------------------------------------------------
    // 4. 连通分量：每个没走过的点当一个新起点，能扩到的点都属于同一块
    // ---------------------------------------------------------
    private static int countComponents(List<List<Integer>> graph) {
        int n = graph.size();
        boolean[] visited = new boolean[n];
        int components = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                components++;
                // 每个新起点做一次 BFS，把这一块全部标记掉
                Queue<Integer> queue = new ArrayDeque<>();
                visited[i] = true;
                queue.offer(i);
                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    for (int v : graph.get(u)) {
                        if (!visited[v]) {
                            visited[v] = true;
                            queue.offer(v);
                        }
                    }
                }
            }
        }
        return components;
    }

    // ---------------------------------------------------------
    // 5. 规模实测：随机生成一张连通图，对比 BFS / DFS 耗时
    // ---------------------------------------------------------
    private static void benchmark() {
        final int n = 2000;
        Random random = new Random(20261128L);
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        // 随机加边：每个点连到前面若干随机点，保证大体连通
        for (int i = 1; i < n; i++) {
            int parent = random.nextInt(i);
            addUndirectedEdge(graph, i, parent);
            if (random.nextBoolean()) {
                addUndirectedEdge(graph, i, random.nextInt(i));
            }
        }

        long t1 = System.nanoTime();
        List<Integer> bfs = bfs(graph, 0);
        long bfsMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        List<Integer> dfs = dfsExplicitStack(graph, 0);
        long dfsMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：BFS " + bfsMs + " ms，DFS 显式栈 " + dfsMs + " ms");
        check(bfs.size() == n, "BFS 覆盖全部 " + n + " 个点");
        check(dfs.size() == n, "DFS 覆盖全部 " + n + " 个点");
        // 两种遍历访问到的是同一批点（顺序可以不同，集合必须一致）
        check(bfs.containsAll(dfs) && dfs.containsAll(bfs), "BFS 与 DFS 访问到的点集合相同");
    }

    // =========================================================
    // 断言与自检：样例图上的手工结论
    // =========================================================
    private static void runSampleChecks(List<List<Integer>> graph, List<Integer> bfsOrder,
                                        int[] dist, List<Integer> dfsRecursive,
                                        List<Integer> dfsStack, int components) {
        // 2. BFS 距离：0→1 是 1 条边，0→3 是 2 条边，0→5 是 4 条边，6/7 不可达为 -1
        check(dist[0] == 0, "起点到自己距离为 0");
        check(dist[1] == 1 && dist[2] == 1, "相邻点距离为 1");
        check(dist[3] == 2 && dist[4] == 3 && dist[5] == 4, "沿边递推的最少边数正确");
        check(dist[6] == -1 && dist[7] == -1, "不连通的点距离保持 -1");
        check(bfsOrder.get(0) == 0, "BFS 从起点 0 开始");
        check(bfsOrder.size() == 6, "从 0 出发只能走到第一块的 6 个点");

        // 3. 递归版与显式栈版序列必须逐条一致
        check(dfsRecursive.equals(dfsStack), "DFS 递归版与显式栈版访问序列一致");
        check(dfsRecursive.get(0) == 0, "DFS 同样从起点 0 开始");

        // 4. 整张图共两个连通分量
        check(components == 2, "样例图连通分量个数为 2");

        // 图上存在环（1-3-2-0-1），没有 visited 会死循环；这里反向验证 visited 已生效
        boolean[] visited = new boolean[graph.size()];
        List<Integer> probe = bfs(graph, 0);
        check(probe.size() == 6, "有环图上靠 visited 终止，不会无限走下去");
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
