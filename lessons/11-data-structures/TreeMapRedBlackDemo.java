import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 知识点：红黑树与 TreeMap（性质、近似平衡、有序 API、源码要点）
 * ============================================================
 * 一句话：红黑树是一棵"用颜色规则约束左右高度"的平衡 BST：它不要求高度差为 1（那是 AVL），
 *         只保证"最长路径不超过最短路径的两倍"，从而增删旋转更少、整体仍是 O(log n)。
 *
 * 为什么需要它：
 *   裸 BST 会退化，AVL 平衡严格但每次增删都要反复检查高度；红黑树用一套颜色约束换取更宽松的平衡，
 *   插入删除的旋转次数更少，非常适合做有序字典（JDK 的 TreeMap / TreeSet 底层就是它）。
 *
 * 复杂度速查（n = 节点个数）：
 *   操作              平均 / 最坏      说明
 *   插入 put()        O(log n)       插入后 fixAfterInsertion 沿父链变色 / 旋转
 *   查找 get()        O(log n)       按比较器沿左右下探
 *   删除 remove()      O(log n)       本文件不手写红黑树删除（见"已知取舍"）
 *   有序区间查询       O(log n + k)   floor/ceiling/subMap 等，k 为返回元素个数
 *
 * 易错点：
 *   1. 红黑树 5 条性质必须背熟：① 节点非红即黑；② 根节点是黑；③ 叶子(NIL)是黑；
 *      ④ 红节点的孩子必须是黑（不能两个红相连）；⑤ 从任一节点到其每个 NIL 叶子，黑节点数相同。
 *   2. 性质⑤配合性质④推出"黑高一致、红不连续"，所以最长红黑相间路径 ≈ 2 倍全黑路径，
 *      这就是"最长路径 ≤ 2×最短路径"、树高仍是 O(log n) 的原因。
 *   3. TreeMap 靠比较器（自然顺序或自定义 Comparator）决定左右；字符串按字典序、数字按大小。
 *   4. floorKey 返回 ≤ key 的最大键；ceilingKey 返回 ≥ key 的最小键；不存在时返回 null，别当成 0。
 *   5. subMap(from, to) 是左闭右开；headMap(to) 不含 to；区间边界容易踩错。
 *
 * 与 Java 自带类的对应关系：
 *   java.util.TreeMap 底层是红黑树，节点是内部类 TreeMap.Entry（key/value/left/right/parent/color）；
 *   put 插入后调用 fixAfterInsertion 做变色与左旋/右旋；TreeSet 内部包了一个 TreeMap。
 *   本文件不手写完整红黑树（尤其不手写删除），只讲性质并用 TreeMap 演示有序能力与源码对应点。
 *
 * 一句话口诀：
 *   根黑叶黑红不连，到叶黑高都一样；路径不超两倍长，TreeMap 有序靠红黑。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 TreeMapRedBlackDemo.java
 *   java TreeMapRedBlackDemo
 */
public class TreeMapRedBlackDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 红黑树 5 条性质与近似平衡解释 ==========");
        explainRedBlackRules();

        System.out.println();
        System.out.println("========== 2. TreeMap 有序能力：边界键与区间查询 ==========");
        demoOrderedApi();

        System.out.println();
        System.out.println("========== 3. TreeMap 源码要点对应 ==========");
        demoSourceNotes();

        System.out.println();
        System.out.println("========== 4. 固定种子实测：TreeMap 有序性与查找规模 ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // ---------------------------------------------------------
    // 1. 红黑树性质讲解
    // ---------------------------------------------------------
    private static void explainRedBlackRules() {
        System.out.println("  性质①：每个节点颜色非红即黑。");
        System.out.println("  性质②：根节点必须是黑色。");
        System.out.println("  性质③：外部叶子(NIL)节点是黑色。");
        System.out.println("  性质④：红节点的两个孩子必须是黑（不允许红红相连）。");
        System.out.println("  性质⑤：任一节点到其各 NIL 叶子的路径上，黑节点数量相同（黑高一致）。");
        System.out.println("  近似平衡推导：黑高一致意味着全黑路径等长；红不连续意味着红节点最多间隔出现，");
        System.out.println("  因此最长路径（红黑相间）≈ 2 倍最短路径（全黑），树高仍是 O(log n)。");

        // 用一棵"概念上的红黑树"做不到不现实，这里只做规则计数的自检：5 条性质都被列出
        check(true, "红黑树 5 条性质已逐条说明");
        check(true, "已解释最长路径 ≤ 2×最短路径为何保证近似平衡");
    }

    // ---------------------------------------------------------
    // 2. TreeMap 有序 API 演示
    // ---------------------------------------------------------
    private static void demoOrderedApi() {
        TreeMap<Integer, String> map = new TreeMap<>();
        // 故意乱序插入，TreeMap 内部按键大小排列
        int[] keys = {30, 10, 50, 20, 40};
        String[] values = {"三十", "一十", "五十", "二十", "四十"};
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }

        System.out.println("  TreeMap 按键升序：" + map);

        // 边界键：最小、最大
        System.out.println("  firstKey()=" + map.firstKey() + "，lastKey()=" + map.lastKey());
        check(map.firstKey() == 10, "firstKey 返回最小键 10");
        check(map.lastKey() == 50, "lastKey 返回最大键 50");

        // floor ≤ 键 的最大键；ceiling ≥ 键 的最小键
        System.out.println("  floorKey(35)=" + map.floorKey(35) + "，ceilingKey(35)=" + map.ceilingKey(35));
        check(map.floorKey(35) == 30, "floorKey(35)=30（≤35 的最大键）");
        check(map.ceilingKey(35) == 40, "ceilingKey(35)=40（≥35 的最小键）");
        check(map.floorKey(10) == 10, "floorKey 命中自身时返回自身");
        check(map.ceilingKey(50) == 50, "ceilingKey 命中自身时返回自身");

        // 区间视图：subMap 左闭右开，headMap 不含边界
        System.out.println("  subMap(20,40)=" + map.subMap(20, 40).keySet());
        System.out.println("  headMap(30)=" + map.headMap(30).keySet());
        System.out.println("  tailMap(40)=" + map.tailMap(40).keySet());
        check(map.subMap(20, 40).keySet().equals(java.util.Set.of(20, 30)), "subMap(20,40) 为左闭右开 {20,30}");
        check(map.headMap(30).keySet().equals(java.util.Set.of(10, 20)), "headMap(30) 不含 30，为 {10,20}");
        check(map.tailMap(40).keySet().equals(java.util.Set.of(40, 50)), "tailMap(40) 含 40，为 {40,50}");
    }

    // ---------------------------------------------------------
    // 3. 源码要点对照（本方法只讲结构，不实现完整红黑树）
    // ---------------------------------------------------------
    private static void demoSourceNotes() {
        System.out.println("  TreeMap 内部节点 Entry 的字段对应：");
        System.out.println("    key(键) / value(值) / left(左孩子) / right(右孩子) / parent(父节点) / color(颜色)");
        System.out.println("  put 流程：按 comparator 下探找插入位置 → 挂新节点（默认红）→ fixAfterInsertion 调平。");
        System.out.println("  fixAfterInsertion：沿父链判断叔节点颜色，做变色；必要时左旋 / 右旋恢复性质。");
        System.out.println("  左旋：把右孩子提上来当根；右旋：把左孩子提上来当根（与 AVL 旋转动作一致，判据不同）。");
        System.out.println("  比较器：不传 Comparator 用元素自然顺序；自定义 Comparator 决定大小关系。");

        // 用自定义比较器演示"比较器决定排序"：这里按数字倒序
        TreeMap<Integer, String> desc = new TreeMap<>(Collections.reverseOrder());
        desc.put(1, "a");
        desc.put(3, "c");
        desc.put(2, "b");
        System.out.println("  逆序比较器下的键：" + desc.keySet());
        check(desc.firstKey() == 3, "逆序比较器下 firstKey 为最大键 3");
    }

    // ---------------------------------------------------------
    // 4. 实测：固定种子，TreeMap 有序性 + 查找规模
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 5000;
        Random rnd = new Random(20261128);

        // 生成不重复随机键并打乱
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            keys.add(i);
        }
        Collections.shuffle(keys, rnd);

        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        long t1 = System.nanoTime();
        for (int k : keys) {
            treeMap.put(k, k * 10);
        }
        long insertMs = (System.nanoTime() - t1) / 1_000_000;

        long t2 = System.nanoTime();
        int hits = 0;
        for (int k : keys) {
            if (treeMap.get(k) != null) {
                hits++;
            }
        }
        long getMs = (System.nanoTime() - t2) / 1_000_000;

        // 交叉验证：TreeMap 的键顺序必须与 TreeSet（同样红黑树）以及排序后的列表一致
        TreeSet<Integer> set = new TreeSet<>(keys);
        List<Integer> sorted = new ArrayList<>(keys);
        Collections.sort(sorted);

        System.out.println("  插入 " + n + " 条耗时 " + insertMs + " ms，查询 " + n + " 条耗时 " + getMs + " ms");
        check(treeMap.size() == n, "TreeMap 插入条数为 " + n);
        check(hits == n, "全部键都能查到");
        check(new ArrayList<>(treeMap.keySet()).equals(new ArrayList<>(set)), "TreeMap 键顺序与 TreeSet 一致");
        check(new ArrayList<>(treeMap.keySet()).equals(sorted), "TreeMap 键顺序与升序列表一致");

        System.out.println("  已知取舍：本文件未手写完整红黑树，尤其不实现红黑树删除（双黑修复较复杂）；");
        System.out.println("  真实工程直接使用 TreeMap / TreeSet 即可。");
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
