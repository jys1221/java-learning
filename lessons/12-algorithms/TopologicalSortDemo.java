import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * 知识点：拓扑排序 —— Kahn（入度 + 队列）与 DFS 后序逆序版
 * ============================================================
 * 一句话：把有向无环图（DAG）排成一条线，让每条边 u→v 里 u 都排在 v 前面；
 *        Kahn 从入度为 0 的点剥洋葱，DFS 记后序再倒过来。
 *
 * 为什么需要它：
 *   课程先修、编译依赖、任务排期、构建顺序，都是"谁必须先做完"的有向关系。
 *   拓扑排序就是把这种依赖关系排成可执行顺序；排不出来（有环）就说明依赖成了死锁。
 *
 * 复杂度速查（n = 点数，m = 边数）：
 *   算法            时间      空间      特点
 *   Kahn（队列）    O(n + m) O(n)      直观，顺便能数出度变化；结果顺序确定
 *   DFS 后序逆序    O(n + m) O(n)      代码短，天然带递归栈；深图换显式栈
 *
 * 易错点：
 *   1. 结果长度 < n：说明图里有环，剩下的点入度永远不为 0，排不出来
 *   2. 入度表要在加边时同步累加 indeg[v]++，别只建邻接表忘了计入度
 *   3. DFS 版存的是"后序"（孩子都走完才记录），必须 reverse 才是拓扑序
 *   4. 拓扑序不唯一：只要每条边 u 在 v 前，任何排列都合法，不要强求两版输出完全相同
 *   5. 有向图才有拓扑序；无向边不能当成有向依赖，否则会被误判成环
 *
 * 与 Java 自带类的对应关系：
 *   Kahn 用 java.util.ArrayDeque 做队列；JDK 没有内置拓扑排序，需要自己建邻接表和入度表。
 *   构建工具（如 Maven/Gradle）内部做依赖解析，本质就是拓扑排序。
 *
 * 一句话口诀：
 *   入度为 0 先排队，剥完一个减一个；DFS 后序倒着读；排不满就是有环。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 TopologicalSortDemo.java
 *   java TopologicalSortDemo
 */
public class TopologicalSortDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 课程先修 DAG：Kahn 与 DFS 两版排序 ==========");
        int n = 6;
        List<List<Integer>> adj = buildCourseGraph(n);
        List<int[]> edges = listEdges(adj);

        List<Integer> kahnOrder = kahn(adj);
        List<Integer> dfsOrder = dfsTopo(adj);

        System.out.println("Kahn 拓扑序：" + kahnOrder);
        System.out.println("DFS  拓扑序：" + dfsOrder);

        check(kahnOrder.size() == n, "Kahn 排满全部 " + n + " 个点（无环）");
        check(dfsOrder.size() == n, "DFS 排满全部 " + n + " 个点（无环）");
        check(isValidTopo(adj, edges, kahnOrder), "Kahn 序合法：每条边 u→v 中 u 在 v 前");
        check(isValidTopo(adj, edges, dfsOrder), "DFS 序合法：每条边 u→v 中 u 在 v 前");

        System.out.println();
        System.out.println("========== 2. 带环图：排不满 n 个点 → 判定有环 ==========");
        List<List<Integer>> cycleAdj = buildCycleGraph();
        List<Integer> cycleOrder = kahn(cycleAdj);
        System.out.println("环图 Kahn 结果：" + cycleOrder + "，长度 " + cycleOrder.size() + " / 3");
        check(cycleOrder.size() < 3, "结果长度 < n，成功识别存在环");
        check(hasCycle(cycleAdj), "环图的环检测标志为 true");

        System.out.println();
        System.out.println("========== 3. 规模实测：随机 DAG 上两版耗时与合法性 ==========");
        benchmark();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 课程先修图：0→1、0→2、1→3、2→3、3→4、3→5
    // 可以理解为：0 是基础课，1/2 是先修，3 是综合课，4/5 是后续课
    // ---------------------------------------------------------
    private static List<List<Integer>> buildCourseGraph(int n) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        addEdge(adj, 0, 1);
        addEdge(adj, 0, 2);
        addEdge(adj, 1, 3);
        addEdge(adj, 2, 3);
        addEdge(adj, 3, 4);
        addEdge(adj, 3, 5);
        return adj;
    }

    private static void addEdge(List<List<Integer>> adj, int u, int v) {
        adj.get(u).add(v);
    }

    /** 把邻接表拆成边列表，便于合法性校验 */
    private static List<int[]> listEdges(List<List<Integer>> adj) {
        List<int[]> edges = new ArrayList<>();
        for (int u = 0; u < adj.size(); u++) {
            for (int v : adj.get(u)) {
                edges.add(new int[]{u, v});
            }
        }
        return edges;
    }

    // ---------------------------------------------------------
    // Kahn：统计入度，所有入度 0 的点入队；取出一个点，把它邻居的入度减 1，
    // 减到 0 的邻居继续入队。队列为空时若还有点没排到，即有环。
    // ---------------------------------------------------------
    private static List<Integer> kahn(List<List<Integer>> adj) {
        int n = adj.size();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                indeg[v]++;                         // 每条 u→v 都让 v 的入度 +1
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                queue.offer(i);                    // 入度 0：没有先修，可以先排
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                indeg[v]--;                        // u 排掉了，v 的一项先修完成
                if (indeg[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        return order;
    }

    // ---------------------------------------------------------
    // DFS 拓扑：state 0 未访问 / 1 访问中 / 2 已完成；
    // 递归走完所有孩子后把当前点压进后序列表，最后整体 reverse。
    // ---------------------------------------------------------
    private static List<Integer> dfsTopo(List<List<Integer>> adj) {
        int n = adj.size();
        int[] state = new int[n];
        List<Integer> postOrder = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (state[i] == 0) {
                dfsVisit(adj, i, state, postOrder);
            }
        }
        // 后序是"孩子在前、自己在后"，倒过来才是先修在前的拓扑序
        List<Integer> order = new ArrayList<>(postOrder);
        java.util.Collections.reverse(order);
        return order;
    }

    private static void dfsVisit(List<List<Integer>> adj, int u,
                                  int[] state, List<Integer> postOrder) {
        state[u] = 1;                              // 访问中：用于识别回边（环）
        for (int v : adj.get(u)) {
            if (state[v] == 0) {
                dfsVisit(adj, v, state, postOrder);
            }
        }
        state[u] = 2;
        postOrder.add(u);                          // 孩子都走完了，记录自己（后序）
    }

    /** 判断给定序列是否合法拓扑序：每条边 u→v，u 的位置必须在 v 之前 */
    private static boolean isValidTopo(List<List<Integer>> adj, List<int[]> edges,
                                        List<Integer> order) {
        int[] pos = new int[adj.size()];
        Arrays.fill(pos, -1);
        for (int i = 0; i < order.size(); i++) {
            pos[order.get(i)] = i;
        }
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            if (pos[u] == -1 || pos[v] == -1 || pos[u] >= pos[v]) {
                return false;
            }
        }
        return true;
    }

    // ---------------------------------------------------------
    // 带环图：0→1→2→0 形成环，谁都无法成为入度 0 的起点
    // ---------------------------------------------------------
    private static List<List<Integer>> buildCycleGraph() {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            adj.add(new ArrayList<>());
        }
        addEdge(adj, 0, 1);
        addEdge(adj, 1, 2);
        addEdge(adj, 2, 0);
        return adj;
    }

    /** 用 Kahn 结果长度判断是否有环：排不满 n 个点就是有环 */
    private static boolean hasCycle(List<List<Integer>> adj) {
        return kahn(adj).size() < adj.size();
    }

    // ---------------------------------------------------------
    // 规模实测：随机生成一张 DAG（只连 i→j 且 i<j，天然无环），对比两版耗时
    // ---------------------------------------------------------
    private static void benchmark() {
        final int n = 500;
        Random random = new Random(20261128L);
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < 0.05) {
                    addEdge(adj, i, j);           // 只连向前的点，保证无环
                }
            }
        }
        List<int[]> edges = listEdges(adj);

        long t1 = System.nanoTime();
        List<Integer> kahn = kahn(adj);
        long kahnMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        List<Integer> dfs = dfsTopo(adj);
        long dfsMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("n = " + n + "：Kahn " + kahnMs + " ms，DFS " + dfsMs + " ms");
        check(kahn.size() == n, "随机 DAG 的 Kahn 排满全部点");
        check(isValidTopo(adj, edges, kahn), "随机 DAG 的 Kahn 序合法");
        check(isValidTopo(adj, edges, dfs), "随机 DAG 的 DFS 序合法");
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
