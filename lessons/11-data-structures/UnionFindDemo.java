import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * 知识点：并查集（Union-Find / 不相交集合：路径压缩 + 按秩合并 + 连通块计数）
 * ============================================================
 * 一句话：并查集用一棵"父指针树"把若干元素分成若干互不相交的集合；
 *         find 向上找根并顺手把路径压平，union 把两棵树并到一起，快速判断"两个元素是否同属一伙"。
 *
 * 为什么需要它：
 *   判断两人是不是朋友、两个格子是否同属一个岛屿、两条路是否已连通，本质都是"连通性"问题。
 *   用 BFS/DFS 每次都要整片搜索；并查集接近 O(α(n)) （反阿克曼函数，实际可视为常数）地完成合并与查询。
 *
 * 复杂度速查（n = 元素个数）：
 *   操作              平均 / 摊还         说明
 *   find(x)          O(α(n))          路径压缩后树被压平，接近常数
 *   union(a,b)       O(α(n))          先 find 两个根，再按秩/大小挂接
 *   连通块计数        O(1)             维护一个 count，合并成功才减一
 *
 * 易错点：
 *   1. 初始化时 parent[i] = i（每个元素自己是一个集合）；不要全置 0 或 -1。
 *   2. 路径压缩改的是"父指针指向根"，不要在递归里忘了回溯写回 parent[x]。
 *   3. 按秩（高度）或按大小合并：把矮树挂到高树根下，避免树越合越高；只合并不挑方向会退化。
 *   4. union 前必须先 find 到根；直接比较 parent[a]、parent[b] 是错的。
 *   5. 连通块数 = 集合数；只有"两个不同集合合并成功"时 count 才减一，同根合并不减。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置并查集；它是竞赛与图论里的基础工具。典型场景：朋友圈分组、岛屿数量、
 *   Kruskal 最小生成树判环、账号合并、区块链区块归属。本文件手写并与自写 BFS 数出的连通块对拍。
 *
 * 一句话口诀：
 *   父指自己是初始，找根顺手压路径；矮树挂到高树下，一伙看根同不同。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 UnionFindDemo.java
 *   java UnionFindDemo
 */
public class UnionFindDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 初始化、合并与连通判断 ==========");
        demoBasicUnion();

        System.out.println();
        System.out.println("========== 2. 路径压缩：压缩前后查询路径长度对比 ==========");
        demoPathCompression();

        System.out.println();
        System.out.println("========== 3. 连通块计数：与自写 BFS 对拍 ==========");
        demoComponentsVsBfs();

        System.out.println();
        System.out.println("========== 4. 场景说明：朋友圈 / 岛屿 ==========");
        demoScenarios();

        System.out.println();
        System.out.println("========== 5. 固定种子实测：随机合并，UF 与 BFS 连通块一致 ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 手写并查集
    // =========================================================
    static class UnionFind {

        int[] parent;   // parent[i]：父节点下标；根的 parent 指向自己
        int[] rank;     // rank[i]：以 i 为根的树高上界（按秩合并用）
        int count;      // 当前连通块（集合）个数

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // 初始：每个人自成一伙
                rank[i] = 1;
            }
            count = n;
        }

        /** 找 x 所在集合的根，并在回溯时把路径上的节点直接挂到根上（路径压缩） */
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 递归压平：先找到根，再把 x 直接指向根
            }
            return parent[x];
        }

        /** 合并 a、b 所在集合；返回是否发生了真正的合并 */
        boolean union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                return false; // 本就同根，不合并，count 不减
            }
            // 按秩合并：矮树挂到高树根下；高度相同则任选一侧挂，根秩 +1
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
            count--;
            return true;
        }

        /** 判断 a、b 是否同属一个集合 */
        boolean connected(int a, int b) {
            return find(a) == find(b);
        }

        int components() {
            return count;
        }

        /** 统计从 x 走到根要经过几跳（用于演示压缩前后路径长度） */
        int pathLength(int x) {
            int steps = 0;
            while (parent[x] != x) {
                x = parent[x];
                steps++;
            }
            return steps;
        }
    }

    // ---------------------------------------------------------
    // 1. 基础合并演示
    // ---------------------------------------------------------
    private static void demoBasicUnion() {
        UnionFind uf = new UnionFind(6);
        // 边：0-1, 1-2, 3-4, 1-3（最后一条把两大伙并到一起）
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}, {1, 3}};
        for (int[] e : edges) {
            uf.union(e[0], e[1]);
        }
        System.out.println("  合并后连通块数=" + uf.components());
        check(uf.components() == 2, "6 个点经 4 条边后剩 2 个连通块");
        check(uf.connected(0, 4), "0 和 4 已连通");
        check(!uf.connected(2, 5), "2 和 5 不连通");

        // 同根合并不减少连通块
        boolean merged = uf.union(0, 2);
        check(!merged, "同根再次 union 返回 false，连通块数不变");
        check(uf.components() == 2, "重复合并后连通块数仍为 2");
    }

    // ---------------------------------------------------------
    // 2. 路径压缩前后路径长度
    // ---------------------------------------------------------
    private static void demoPathCompression() {
        final int n = 10;
        UnionFind uf = new UnionFind(n);
        // 手动串成一条父指针链：0→1→2→…→9（根是 9），用来观察压缩前后的路径长度。
        // 注意：按秩合并通常会避免出现这种长链，这里是为了直观展示路径压缩的作用。
        for (int i = 0; i < n - 1; i++) {
            uf.parent[i] = i + 1;
        }
        uf.parent[n - 1] = n - 1;

        int before = uf.pathLength(0);
        System.out.println("  压缩前从 0 到根的跳数=" + before);

        // 第一次 find(0)：回溯时把链上节点直接挂到根，完成压缩
        uf.find(0);
        int after = uf.pathLength(0);
        System.out.println("  压缩后从 0 到根的跳数=" + after);

        check(before == n - 1, "压缩前链上路径长度为 n-1");
        check(after == 1, "路径压缩后 0 直接指向根，路径长度为 1");
        check(uf.connected(0, n - 1), "压缩后仍能正确判断连通");
    }

    // ---------------------------------------------------------
    // 3. 连通块计数与 BFS 对拍
    // ---------------------------------------------------------
    private static void demoComponentsVsBfs() {
        final int n = 8;
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}, {4, 5}, {6, 7}};

        // 并查集口径
        UnionFind uf = new UnionFind(n);
        for (int[] e : edges) {
            uf.union(e[0], e[1]);
        }

        // BFS 口径：自建邻接表，逐点扫描 visited 数连通块
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        int bfsCount = countComponentsByBfs(adj, n);

        System.out.println("  并查集连通块数=" + uf.components() + "，BFS 连通块数=" + bfsCount);
        check(uf.components() == bfsCount, "并查集与 BFS 数出的连通块数一致");
    }

    /** 自写 BFS：visited 数组，每个未访问点开一次队列，每开一次算一个连通块 */
    private static int countComponentsByBfs(List<List<Integer>> adj, int n) {
        boolean[] visited = new boolean[n];
        int components = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                components++;
                Deque<Integer> queue = new ArrayDeque<>();
                queue.offer(i);
                visited[i] = true;
                while (!queue.isEmpty()) {
                    int cur = queue.poll();
                    for (int next : adj.get(cur)) {
                        if (!visited[next]) {
                            visited[next] = true;
                            queue.offer(next);
                        }
                    }
                }
            }
        }
        return components;
    }

    // ---------------------------------------------------------
    // 4. 典型场景说明
    // ---------------------------------------------------------
    private static void demoScenarios() {
        System.out.println("  朋友圈：每个人是一个元素，互加好友就 union；最后 find 同一根的人属同一圈子。");
        System.out.println("  岛屿：网格里相邻的陆地格子互相 union；连通块数就是岛屿数量（也可用 DFS/BFS）。");
        System.out.println("  其他：Kruskal 最小生成树用并查集判断两条边是否已连通，避免成环。");
        check(true, "已说明朋友圈 / 岛屿两类典型场景");
    }

    // ---------------------------------------------------------
    // 5. 随机合并实测：UF 与 BFS 连通块始终一致
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 3000;
        Random rnd = new Random(20261128);

        UnionFind uf = new UnionFind(n);
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        long t1 = System.nanoTime();
        int edgeCount = 0;
        for (int i = 0; i < n * 2; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (a != b) {
                uf.union(a, b);
                adj.get(a).add(b);
                adj.get(b).add(a);
                edgeCount++;
            }
        }
        long ufMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int bfsCount = countComponentsByBfs(adj, n);
        long bfsMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("  随机 " + edgeCount + " 条边，UF 耗时 " + ufMs + " ms，BFS 耗时 " + bfsMs + " ms");
        System.out.println("  UF 连通块=" + uf.components() + "，BFS 连通块=" + bfsCount);
        check(uf.components() == bfsCount, "随机图上 UF 与 BFS 连通块一致");
        check(uf.components() >= 1, "连通块数至少为 1");
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
