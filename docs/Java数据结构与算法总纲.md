# Java 数据结构与算法总纲

> 配套代码：`lessons/11-data-structures/`（18 个主题）与 `lessons/12-algorithms/`（23 个主题），共 **41 个可独立运行的 `.java` 文件**，全部逐段中文注释、`main` 里带断言式校验，`javac` 编译 + `java` 运行即可看到每一步结果。

每个主题都按同一套七段式讲解，方便横向对比、也方便面试前快速回看：

**为什么需要它** → **核心思想（配示意图）** → **关键代码** → **复杂度** → **易错点** → **与 Java 的对应** → **一句话口诀**

---

## 这份文档怎么用

| 步骤 | 做什么 | 为什么 |
|---|---|---|
| 1 | 先读本文档的「推荐学习顺序」，只挑当前阶段的主题 | 数据结构和算法是有依赖关系的，跳着学容易"看得懂但写不出" |
| 2 | 打开主题对应的 `.java` 文件，读头部注释块，再跑一遍 `main` | 输出里的 `[通过]` 行就是"你以为对了"和"确实对了"的区别 |
| 3 | 回到本文档看「核心思想」的示意图，把代码里的指针/下标标到图上 | 图 + 代码对照，才不会把 `i`、`j`、`mid` 混在一起 |
| 4 | 只看「易错点」和「一句话口诀」做自测 | 面试挂掉通常不是不会思路，而是边界写错 |
| 5 | 改文件里的数据规模/参数再跑，观察输出变化 | 复杂度不是背出来的，是跑出来的 |

运行方式（在对应文件所在目录）：

```bash
javac -encoding UTF-8 XxxDemo.java
java XxxDemo
```

> Windows 命令行里中文可能乱码，可在 IDEA 里直接点 `main` 左边的绿色三角运行，或加 `-Dstdout.encoding=UTF-8` 再运行。

---

## 目录

### 第一部分：数据结构（18 个主题）

| 编号 | 主题 | 对应代码 |
|---|---|---|
| DS-1 | 数组与动态数组 | `lessons/11-data-structures/ArrayDemo.java` |
| DS-2 | 单链表 | `lessons/11-data-structures/SinglyLinkedListDemo.java` |
| DS-3 | 双向链表 | `lessons/11-data-structures/DoublyLinkedListDemo.java` |
| DS-4 | 循环链表 | `lessons/11-data-structures/CircularLinkedListDemo.java` |
| DS-5 | 栈 | `lessons/11-data-structures/StackDemo.java` |
| DS-6 | 队列 | `lessons/11-data-structures/QueueDemo.java` |
| DS-7 | 双端队列 | `lessons/11-data-structures/DequeDemo.java` |
| DS-8 | 优先队列与堆 | `lessons/11-data-structures/PriorityQueueHeapDemo.java` |
| DS-9 | 哈希表 | `lessons/11-data-structures/HashMapDemo.java` |
| DS-10 | 二叉树与四种遍历 | `lessons/11-data-structures/BinaryTreeDemo.java` |
| DS-11 | 二叉搜索树 BST | `lessons/11-data-structures/BinarySearchTreeDemo.java` |
| DS-12 | AVL 平衡树 | `lessons/11-data-structures/AvlTreeDemo.java` |
| DS-13 | 红黑树与 TreeMap | `lessons/11-data-structures/TreeMapRedBlackDemo.java` |
| DS-14 | 并查集 | `lessons/11-data-structures/UnionFindDemo.java` |
| DS-15 | Trie 字典树 | `lessons/11-data-structures/TrieDemo.java` |
| DS-16 | 图（邻接矩阵与邻接表） | `lessons/11-data-structures/GraphDemo.java` |
| DS-17 | 线段树 | `lessons/11-data-structures/SegmentTreeDemo.java` |
| DS-18 | 树状数组 | `lessons/11-data-structures/FenwickTreeDemo.java` |

### 第二部分：算法（23 个主题）

| 编号 | 主题 | 对应代码 |
|---|---|---|
| ALG-1 | 冒泡排序 | `lessons/12-algorithms/BubbleSortDemo.java` |
| ALG-2 | 选择排序 | `lessons/12-algorithms/SelectionSortDemo.java` |
| ALG-3 | 插入排序 | `lessons/12-algorithms/InsertionSortDemo.java` |
| ALG-4 | 希尔排序 | `lessons/12-algorithms/ShellSortDemo.java` |
| ALG-5 | 归并排序 | `lessons/12-algorithms/MergeSortDemo.java` |
| ALG-6 | 快速排序 | `lessons/12-algorithms/QuickSortDemo.java` |
| ALG-7 | 堆排序 | `lessons/12-algorithms/HeapSortDemo.java` |
| ALG-8 | 计数排序 | `lessons/12-algorithms/CountingSortDemo.java` |
| ALG-9 | 基数排序 | `lessons/12-algorithms/RadixSortDemo.java` |
| ALG-10 | 桶排序 | `lessons/12-algorithms/BucketSortDemo.java` |
| ALG-11 | 二分查找三套模板 | `lessons/12-algorithms/BinarySearchTemplateDemo.java` |
| ALG-12 | 递归与分治 | `lessons/12-algorithms/RecursionDivideConquerDemo.java` |
| ALG-13 | 贪心 | `lessons/12-algorithms/GreedyDemo.java` |
| ALG-14 | 动态规划 | `lessons/12-algorithms/DynamicProgrammingDemo.java` |
| ALG-15 | 回溯 | `lessons/12-algorithms/BacktrackingDemo.java` |
| ALG-16 | 图的 BFS/DFS | `lessons/12-algorithms/GraphTraversalDemo.java` |
| ALG-17 | 最短路径（Dijkstra/Bellman-Ford/Floyd） | `lessons/12-algorithms/ShortestPathDemo.java` |
| ALG-18 | 最小生成树（Prim/Kruskal） | `lessons/12-algorithms/MinimumSpanningTreeDemo.java` |
| ALG-19 | 拓扑排序 | `lessons/12-algorithms/TopologicalSortDemo.java` |
| ALG-20 | KMP 字符串匹配 | `lessons/12-algorithms/KmpDemo.java` |
| ALG-21 | 双指针与滑动窗口 | `lessons/12-algorithms/TwoPointersSlidingWindowDemo.java` |
| ALG-22 | 前缀和与差分 | `lessons/12-algorithms/PrefixSumAndDiffDemo.java` |
| ALG-23 | 位运算技巧 | `lessons/12-algorithms/BitManipulationDemo.java` |

---

## 推荐学习顺序

一共六个阶段，**按阶段走，不要按编号跳着看**（编号是为了查，不是为了学）。

### 阶段一：先把"线"搞明白（DS-1 ~ DS-8）

```
DS-1 数组/动态数组 → DS-2 单链表 → DS-3 双向链表 → DS-4 循环链表
→ DS-5 栈 → DS-6 队列 → DS-7 双端队列 → DS-8 堆
```

先建立最核心的一条直觉：**"连续内存"和"指针串起来"是两种完全不同的世界观**。
数组靠下标一步到位（O(1) 读到），链表靠顺着指针走（O(n) 才能读到），
但链表在"已知位置插删"时不用搬家。后面所有结构（栈、队列、堆）都是在
这两种世界观上套规则：栈是"只能在一头动"，队列是"一头进一头出"。

### 阶段二：树 —— 从"链"到"分叉"（DS-10 ~ DS-13 + ALG-12）

```
DS-10 二叉树与四种遍历 → ALG-12 递归与分治 → DS-11 BST → DS-12 AVL → DS-13 红黑树/TreeMap
```

树的难点从来不是结构，而是**递归**。所以把 ALG-12 插在树的中间学：
先用递归把树的三种深度遍历想明白，再看 BST 为什么"中序遍历就是升序"，
再看 AVL 为什么旋转四次就能把树压平，最后看红黑树怎么用"颜色"换"少旋转"。

### 阶段三：哈希与"集合型"结构（DS-9、DS-14、DS-15）

```
DS-9 哈希表 → DS-14 并查集 → DS-15 Trie
```

这三个的共同点：**不是靠比较、而是靠"组织方式"把查找变快**。
哈希表把 key 算成一个下标，并查集把元素挂到"代表元"下，Trie 把字符串按字符分层 ——
都是"用额外结构换时间"的典型。

### 阶段四：排序与查找（ALG-1 ~ ALG-11）

```
ALG-1 冒泡 → ALG-2 选择 → ALG-3 插入 → ALG-4 希尔 → ALG-5 归并
→ ALG-6 快排 → ALG-7 堆排 → ALG-8 计数 → ALG-9 基数 → ALG-10 桶 → ALG-11 二分
```

按这个顺序学是有讲究的：前三个 O(n²) 让你看清"比较 + 交换"的本质；
希尔是第一道"分组"的灵光；归并/快排是分治的两种写法（"先分再合" vs "先治再分"）；
最后三个不基于比较，用来打破"排序不可能低于 O(n log n)"的错觉。
二分放在最后，是因为它只需要"有序"这一个前提，和排序天然配套。

### 阶段五：算法思想四件套（ALG-12 ~ ALG-15）

```
ALG-12 递归分治（已在阶段二学过一遍，这里回看）→ ALG-13 贪心 → ALG-14 动态规划 → ALG-15 回溯
```

一条很清楚的演进线：**暴力枚举（回溯）→ 发现重复子问题（记忆化）→ 写成表（动态规划）**。
贪心插在中间是为了让你看清"什么时候贪心是对的、什么时候必须 DP"（0/1 背包就是最好的反例）。

### 阶段六：图 + 字符串 + 常考技巧（DS-16 ~ DS-18 + ALG-16 ~ ALG-23）

```
DS-16 图 → ALG-16 BFS/DFS → ALG-17 最短路 → ALG-18 最小生成树 → ALG-19 拓扑排序
→ ALG-20 KMP → ALG-21 双指针/滑动窗口 → ALG-22 前缀和/差分 → ALG-23 位运算
→ DS-17 线段树 → DS-18 树状数组
```

图算法全部依赖"遍历"这一个底座，所以先 BFS/DFS，再谈"带权"的最短路与生成树。
最后三个（前缀和、差分、位运算）是性价比最高的小技巧：代码短、面试出现率高。
线段树/树状数组放在最末尾，因为它们是"区间查询"的工程级答案，前面学完后才看得懂取舍。

---

## 时间复杂度速查总表

### 数据结构

| 结构 | 访问 | 查找 | 插入 | 删除 | 备注 |
|---|---|---|---|---|---|
| 数组（动态数组） | O(1) | O(n) | 尾部均摊 O(1)，中间 O(n) | O(n) | 扩容那一次是 O(n) |
| 单链表 | O(n) | O(n) | 头部 O(1) | 已知前驱 O(1)，否则 O(n) | 不支持随机访问 |
| 双向链表 | O(n) | O(n) | 头尾 O(1) | 已知节点 O(1) | 多一个 prev 指针换双向能力 |
| 循环链表 | O(n) | O(n) | 头部 O(1) | 已知前驱 O(1) | 尾回指 head，遍历要"走回起点" |
| 栈 | 只能看栈顶 O(1) | O(n) | 栈顶均摊 O(1) | 栈顶均摊 O(1) | LIFO |
| 队列 | 只能看队头 O(1) | O(n) | 队尾 O(1) | 队头 O(1) | FIFO，循环数组要牺牲一格 |
| 双端队列 | 两端 O(1) | O(n) | 两端 O(1) | 两端 O(1) | 既能当栈又能当队列 |
| 堆（优先队列） | 堆顶 O(1) | 任意元素 O(n) | O(log n) | 删堆顶 O(log n) | 只保证"顶"是极值 |
| 哈希表 | 平均 O(1) | 平均 O(1)，最坏 O(n) | 平均 O(1) | 平均 O(1) | 负载因子 0.75，扩容时 rehash |
| 二叉搜索树 | O(log n) 平均 | O(log n) 平均，最坏 O(n) | O(log n) | O(log n) | 可能退化成链表 |
| AVL 树 | O(log n) | O(log n) | O(log n)（最多一次旋转链） | O(log n) | 严格平衡，查询快、写稍贵 |
| 红黑树（TreeMap） | O(log n) | O(log n) | O(log n) | O(log n) | 近似平衡，插入删除旋转更少 |
| 并查集 | — | find 近似 O(1) | union 近似 O(1) | — | 路径压缩 + 按秩合并 |
| Trie | O(L) | O(L) | O(L) | O(L) | L = 单词长度，与词库大小无关 |
| 图（邻接矩阵） | 判边 O(1) | O(n) | O(1) | O(1) | 空间 O(n²)，稠密图合适 |
| 图（邻接表） | 判边 O(度) | O(n+m) | O(1) | O(度) | 空间 O(n+m)，稀疏图合适 |
| 线段树 | 区间查询 O(log n) | O(log n) | 单点/区间改 O(log n) | O(log n) | 支持懒标记区间加 |
| 树状数组 | 前缀和 O(log n) | O(log n) | 单点改 O(log n) | — | 代码短，不支持区间赋值的复杂操作 |

### 算法

| 算法 | 最好 | 平均 | 最坏 | 空间 | 稳定 | 适用场景 |
|---|---|---|---|---|---|---|
| 冒泡排序 | O(n)（已有序） | O(n²) | O(n²) | O(1) | 是 | 教学、极小数据 |
| 选择排序 | O(n²) | O(n²) | O(n²) | O(1) | 否 | 交换代价高的场景 |
| 插入排序 | O(n)（近乎有序） | O(n²) | O(n²) | O(1) | 是 | 小数组、近乎有序、快排小区间兜底 |
| 希尔排序 | 依赖增量 | 约 O(n^1.3) | O(n²) | O(1) | 否 | 中等规模、不想写复杂度高的排序 |
| 归并排序 | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 | 需要稳定、可接受额外空间、外部排序 |
| 快速排序 | O(n log n) | O(n log n) | O(n²)（基准极差） | O(log n) 栈 | 否 | 通用最快，工程首选（随机化基准） |
| 堆排序 | O(n log n) | O(n log n) | O(n log n) | O(1) | 否 | 内存紧、只要 top-k 或最坏也要保证 |
| 计数排序 | O(n+k) | O(n+k) | O(n+k) | O(n+k) | 是 | 值域小的整数（如年龄、分数） |
| 基数排序 | O(d(n+k)) | O(d(n+k)) | O(d(n+k)) | O(n+k) | 是 | 位数少、值域宽的非负整数 |
| 桶排序 | O(n)（均匀） | O(n+k) | O(n²)（全落一桶） | O(n+k) | 是 | 均匀分布的浮点数（0~1） |
| 二分查找 | O(1) | O(log n) | O(log n) | O(1) | — | 必须有序；找边界用 lowerBound/upperBound |
| 递归分治 | — | O(n log n) 常见 | — | O(log n) 栈 | — | 快速幂、归并、树相关 |
| 贪心 | — | 依赖问题 | — | O(1)~O(n) | — | 区间调度、找零（币值规范时）、跳跃游戏 |
| 动态规划 | — | O(n·W) / O(n²) 常见 | — | O(n·W) 可滚动压缩 | — | 背包、LIS、编辑距离、股票系列 |
| 回溯 | — | 指数级（剪枝后变好） | O(n!) / O(2^n) | O(n) 栈 | — | 全排列、组合、N 皇后、子集 |
| BFS | O(n+m) | O(n+m) | O(n+m) | O(n) 队列 | — | 无权最短路、层序、连通分量 |
| DFS | O(n+m) | O(n+m) | O(n+m) | O(n) 栈 | — | 连通性、路径、拓扑后序 |
| Dijkstra | O((n+m) log n) | 同左 | 同左 | O(n) | — | 非负权单源最短路 |
| Bellman-Ford | O(nm) | O(nm) | O(nm) | O(n) | — | 有负权边、判负环 |
| Floyd | O(n³) | O(n³) | O(n³) | O(n²) | — | 多源最短路、小图、可负权不可负环 |
| Prim | O(m log n) | 同左 | 同左 | O(n) | — | 稠密图最小生成树 |
| Kruskal | O(m log m) | 同左 | 同左 | O(n) 并查集 | — | 稀疏图最小生成树 |
| 拓扑排序 | O(n+m) | O(n+m) | O(n+m) | O(n) | — | 依赖排序、判 DAG 有环 |
| KMP | O(n+m) | O(n+m) | O(n+m) | O(m) | — | 单模式串匹配（有周期/大量失配时远胜暴力） |
| 双指针/滑动窗口 | O(n) | O(n) | O(n) | O(1)~O(k) | — | 有序两数之和、最长无重复子串、定长窗口 |
| 前缀和 | 构建 O(n)，查询 O(1) | 同左 | 同左 | O(n) | — | 大量区间和查询（不可边查边改） |
| 差分 | 区间加 O(1)，还原 O(n) | 同左 | 同左 | O(n) | — | 大量区间加操作 |
| 位运算 | O(1) / O(log x) | — | — | O(1) | — | 状态压缩、快速判奇偶/2 的幂、找唯一数 |

---

## 41 个代码文件一览（相对仓库路径）

| # | 文件 | 一句话内容 |
|---|---|---|
| 1 | `lessons/11-data-structures/ArrayDemo.java` | 原生数组的 O(1) 随机访问 + 手写 1.5 倍扩容的动态数组 |
| 2 | `lessons/11-data-structures/SinglyLinkedListDemo.java` | 单链表增删改查、反转（迭代+递归）、快慢指针、判环、合并有序链表 |
| 3 | `lessons/11-data-structures/DoublyLinkedListDemo.java` | 双向链表（prev/next）、带 tail 的 O(1) 尾插、双向遍历 |
| 4 | `lessons/11-data-structures/CircularLinkedListDemo.java` | 尾节点回指 head、约瑟夫环报数出列 |
| 5 | `lessons/11-data-structures/StackDemo.java` | 数组栈 + 链栈 + 括号匹配 + 最小栈 |
| 6 | `lessons/11-data-structures/QueueDemo.java` | 循环队列（牺牲一格判满）+ 链式队列 |
| 7 | `lessons/11-data-structures/DequeDemo.java` | 双端队列双指针取模 + 单调队列求窗口最大值 |
| 8 | `lessons/11-data-structures/PriorityQueueHeapDemo.java` | 手写小顶堆、siftUp/siftDown、建堆、TopK |
| 9 | `lessons/11-data-structures/HashMapDemo.java` | 开放寻址（墓碑删除）+ 链地址法 + 扩容 rehash |
| 10 | `lessons/11-data-structures/BinaryTreeDemo.java` | 前中后层四种遍历（递归+迭代）、高度、叶子、前序中序重建 |
| 11 | `lessons/11-data-structures/BinarySearchTreeDemo.java` | BST 插入/查找/删除三种情况、中序升序验证 |
| 12 | `lessons/11-data-structures/AvlTreeDemo.java` | 平衡因子、LL/RR/LR/RL 四种旋转、插入自平衡 |
| 13 | `lessons/11-data-structures/TreeMapRedBlackDemo.java` | 红黑树 5 条性质 + TreeMap 有序 API 与源码要点 |
| 14 | `lessons/11-data-structures/UnionFindDemo.java` | 并查集：路径压缩 + 按秩合并 + 连通块计数 |
| 15 | `lessons/11-data-structures/TrieDemo.java` | 前缀树插入/查找/前缀匹配/自动补全 |
| 16 | `lessons/11-data-structures/GraphDemo.java` | 邻接矩阵与邻接表两种存图方式及取舍 |
| 17 | `lessons/11-data-structures/SegmentTreeDemo.java` | 线段树区间和/区间最值 + 懒标记区间加 |
| 18 | `lessons/11-data-structures/FenwickTreeDemo.java` | 树状数组 lowbit、单点改、前缀和、逆序对 |
| 19 | `lessons/12-algorithms/BubbleSortDemo.java` | 冒泡 + 提前退出 + 稳定性 + 与 Arrays.sort 对拍 |
| 20 | `lessons/12-algorithms/SelectionSortDemo.java` | 选择排序（不稳定、交换次数少） |
| 21 | `lessons/12-algorithms/InsertionSortDemo.java` | 插入排序（近乎有序 O(n)）+ 二分插入优化 |
| 22 | `lessons/12-algorithms/ShellSortDemo.java` | 希尔排序分组插入、gap 减半 |
| 23 | `lessons/12-algorithms/MergeSortDemo.java` | 归并排序（稳定、O(n) 额外空间、自底向上） |
| 24 | `lessons/12-algorithms/QuickSortDemo.java` | 快排 Lomuto/Hoare/三路 + 随机基准 |
| 25 | `lessons/12-algorithms/HeapSortDemo.java` | 堆排序（原地建堆、下沉、不稳定） |
| 26 | `lessons/12-algorithms/CountingSortDemo.java` | 计数排序（前缀和定位保证稳定、负数偏移） |
| 27 | `lessons/12-algorithms/RadixSortDemo.java` | 基数排序 LSD，稳定性是正确性前提 |
| 28 | `lessons/12-algorithms/BucketSortDemo.java` | 桶排序分桶 + 桶内插入排序 |
| 29 | `lessons/12-algorithms/BinarySearchTemplateDemo.java` | 二分三套模板：精确/左边界/右边界 + 二分答案 |
| 30 | `lessons/12-algorithms/RecursionDivideConquerDemo.java` | 递归三要素、记忆化、汉诺塔、分治、快速幂 |
| 31 | `lessons/12-algorithms/GreedyDemo.java` | 找零、区间调度、跳跃游戏 + 贪心失效反例 |
| 32 | `lessons/12-algorithms/DynamicProgrammingDemo.java` | 0/1 背包、完全背包、LIS、编辑距离、股票系列 |
| 33 | `lessons/12-algorithms/BacktrackingDemo.java` | 全排列、组合、N 皇后、子集 + 剪枝 |
| 34 | `lessons/12-algorithms/GraphTraversalDemo.java` | BFS/DFS 两种遍历、距离数组、连通分量 |
| 35 | `lessons/12-algorithms/ShortestPathDemo.java` | Dijkstra / Bellman-Ford / Floyd 三法对照 |
| 36 | `lessons/12-algorithms/MinimumSpanningTreeDemo.java` | Prim 与 Kruskal，两法总权重必须相等 |
| 37 | `lessons/12-algorithms/TopologicalSortDemo.java` | Kahn 入度法 + DFS 后序法，判环 |
| 38 | `lessons/12-algorithms/KmpDemo.java` | 前缀函数 next 数组、O(n+m) 匹配、与暴力对拍 |
| 39 | `lessons/12-algorithms/TwoPointersSlidingWindowDemo.java` | 对撞/快慢指针、定长与变长滑动窗口 |
| 40 | `lessons/12-algorithms/PrefixSumAndDiffDemo.java` | 前缀和（pre 长 n+1、pre[r+1]-pre[l]）、二维前缀和、差分、溢出坑 |
| 41 | `lessons/12-algorithms/BitManipulationDemo.java` | 取最低位 1、消位、判 2 的幂、异或找唯一数、优先级坑 |

---

## 常见面试题索引

| 面试题 | 该看哪一节 |
|---|---|
| 手写 LRU 缓存 | DS-3 双向链表 + DS-9 哈希表（哈希存位置，链表管顺序） |
| 求 TopK / 数据流中位数 | DS-8 优先队列与堆 |
| 有效的括号 | DS-5 栈 |
| 滑动窗口最大值 | DS-7 双端队列（单调队列） |
| 数组去重 / 快速查重 | DS-9 哈希表 |
| 朋友圈 / 岛屿数量 / 连通性 | DS-14 并查集、ALG-16 图的 BFS/DFS |
| 前缀匹配 / 自动补全 / 敏感词 | DS-15 Trie |
| 链表反转 / 环形链表 / 合并两个有序链表 | DS-2 单链表 |
| 二叉树遍历 / 由前序中序重建 | DS-10 二叉树 |
| 验证二叉搜索树 / 第 k 小元素 | DS-11 BST |
| 排序算法的稳定性与复杂度对比 | ALG-1 ~ ALG-10 |
| 第一个错误的版本 / 寻找左右边界 / 二分答案 | ALG-11 二分三套模板 |
| 0/1 背包 / 编辑距离 / 买卖股票 / LIS | ALG-14 动态规划 |
| 全排列 / N 皇后 / 组合总和 | ALG-15 回溯 |
| 单源最短路 / 最小生成树 / 课程表 | ALG-17、ALG-18、ALG-19 |
| 实现 strStr / 最短循环节 | ALG-20 KMP |
| 最长无重复子串 / 有序数组两数之和 | ALG-21 双指针与滑动窗口 |
| 子数组和 / 区间加 / 二维区域和 | ALG-22 前缀和与差分 |
| 只出现一次的数字 / 判断 2 的幂 / 子集枚举 | ALG-23 位运算 |
| 区间查询 + 区间修改 | DS-17 线段树、DS-18 树状数组 |

---

## 与仓库里其它文档的关系

| 文档 | 定位 |
|---|---|
| [Java后端学习总纲](Java后端学习总纲.md) | 后端全链路知识地图（Java 基础 → 数据库 → Spring Boot → 微服务） |
| **本文档** | 数据结构与算法的完整地图，配 41 个可运行代码 |
| [Java错题本](Java错题本.md) | 真实报错记录：现象 / 根因 / 解决方式 |
| [lessons/README.md](../lessons/README.md) | 全部教学代码的目录导览（含老目录 01~10） |

---

## 第一部分：数据结构（18 个主题）

> 本部分对应仓库目录 `lessons/11-data-structures/` 下的 18 个可独立运行的 Demo 文件。每个主题按固定七块讲解：为什么需要它 → 核心思想（配纯文本图解）→ 关键代码 → 复杂度表 → 易错点 → 与 Java 集合类的对应 → 一句话口诀。关键代码取自真实 Demo，方法名、变量名与源码一致，省略处用注释标明。

### DS-1 数组与动态数组

> 对应代码：`lessons/11-data-structures/ArrayDemo.java`

**为什么需要它**

原生数组 `new` 出来长度就固定死了，而元素个数往往事先不知道。动态数组在装满以后自动换一块更大的连续空间、把老数据整体搬过去，对外表现得"可以一直往里塞"，这就是 `ArrayList` 的底层原理。

**核心思想**

数组是一排连续内存格子，按下标访问用公式 `首地址 + 下标 × 元素大小` 一步算出地址，随机访问 O(1)。动态数组维护 `data`（格子系统）和 `size`（已用个数）；`size == data.length` 时扩容：建 1.5 倍新数组、拷贝老数据、换引用，老数组交 GC。中间插入/删除要整体搬家，代价 O(n)。

```text
下标:      0      1      2      3      4
        ┌──────┬──────┬──────┬──────┬──────┐
data:   │  10  │  20  │  30  │  40  │  空  │
        └──────┴──────┴──────┴──────┴──────┘
                     ▲ data[2] 一步直达
size = 4（已用）   data.length = 当前容量
扩容：旧数组(10) → 新数组(15)，老数据整体拷贝，旧数组等待 GC
```

**关键代码**

```java
static class MyIntArrayList {
    private int[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    void add(int value) {
        ensureCapacityForOne();   // 满了先扩容
        data[size] = value;
        size++;
    }

    private void grow() {
        int oldCap = data.length;
        int newCap = oldCap + (oldCap >> 1); // 1.5 倍扩容
        int[] bigger = new int[newCap];
        System.arraycopy(data, 0, bigger, 0, size);
        data = bigger; // 换新引用，老数组由 GC 回收
    }
    // 省略：get/set/remove(index)/indexOf/checkIndex/toString
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 按下标访问 get(i) | O(1) | O(1) | 连续内存 + 地址计算 |
| 尾部添加 add | O(1) 均摊 | O(n) | 仅扩容瞬间 O(n) |
| 中间插入 add(i,v) | O(n) | O(n) | 后面元素整体后移 |
| 删除 remove(i) | O(n) | O(n) | 后面元素整体前移 |
| 按值查找 indexOf | O(n) | O(n) | 只能从头逐个比 |
| 空间 / 适用场景 | O(n) | — | 频繁下标访问、尾部增删；不适合频繁中间增删 |

**易错点**

1. 下标从 0 开始，合法范围 `0 ~ size-1`；写 `size` 抛 `ArrayIndexOutOfBoundsException`。
2. `remove(int index)` 与 `remove(Object o)` 是两个方法：传 `int` 按下标删，传对象按值删。
3. 扩容不是原地变大，而是"新建数组 + 拷贝 + 换引用"；旧数组引用不会自动更新。
4. 1.5 倍是折中：倍数太小频繁拷贝，太大浪费内存。
5. `System.arraycopy` 的起止下标和拷贝个数容易算错，写之前先标好移动区间。

**与 Java 的对应**

`java.util.ArrayList` 内部就是 `Object[] elementData` + `int size`；默认容量 10（首次 `add` 才分配），扩容 `newCapacity = oldCapacity + (oldCapacity >> 1)`，即 1.5 倍。`MyIntArrayList` 是它的手写迷你版，只装 `int`、无泛型。

**一句话口诀**

> 口诀：数组靠下标 O(1) 直达，增删靠搬家 O(n)；动态数组 = 数组 + 满了就 1.5 倍扩容。

### DS-2 单链表

> 对应代码：`lessons/11-data-structures/SinglyLinkedListDemo.java`

**为什么需要它**

数组中间增删要整体搬家 O(n)；链表只要改几个指针就能完成中间增删，也不要求连续内存、不事先知道容量。代价是失去下标随机访问，查找只能从头顺指针走。单链表是最简单的链表形态，每个节点只知道下一个是谁。

**核心思想**

节点 `Node` 存 `val` 和指向下一个的 `next`；末节点 `next = null`，整条链靠头指针 `head` 定位。头插新建节点指向老 `head` 再换 `head`；尾插无尾指针，必须走到最后节点。基本功：反转（三指针/递归）、快慢指针找中点与判环、合并两条有序链表（哑节点省特判）。

```text
head
 │
 ▼
┌──────┐  next ┌──────┐  next ┌──────┐  next ┌──────┐
│  5   │─────▶│  10  │─────▶│  20  │─────▶│  30  │──▶ null
└──────┘       └──────┘       └──────┘       └──────┘
头插 addFirst：新节点 next=老 head，head=新节点  —— O(1)
尾插 addLast ：从头走到 tail.next==null 处接上  —— O(n)
```

**关键代码**

```java
static class MySinglyLinkedList {
    static class Node {
        int val; Node next;
        Node(int val) { this.val = val; this.next = null; }
    }
    private Node head; private int size;

    void addFirst(int value) {
        Node node = new Node(value);
        node.next = head; head = node; size++;
    }

    // 迭代反转：prev 在前、cur 在后，逐个把 next 反向指回 prev
    Node reverseIterative() {
        Node prev = null, cur = head;
        while (cur != null) {
            Node next = cur.next; // 先存下一个，否则改完指针就丢了
            cur.next = prev; prev = cur; cur = next;
        }
        head = prev; return head;
    }
    // 省略：addLast/removeByValue/reverseRecursive/middleValue/hasCycle/mergeSorted
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 头部添加 addFirst | O(1) | O(1) | 新建节点接上 head |
| 尾部添加 addLast | O(n) | O(n) | 无 tail，要走到最后 |
| 按值删除 removeByValue | O(n) | O(n) | 先从头找到它 |
| 按下标访问 get(i) | O(n) | O(n) | 顺指针一步步走 |
| 反转 reverse | O(n) | O(n) | 迭代/递归都只走一遍 |
| 空间 / 适用场景 | O(n) | — | 频繁头部增删、不需随机访问 |

**易错点**

1. 删 `head` 必须先更新 `head`；删中间节点要拿到前驱才能改前驱的 `next`。
2. 反转时先存 `next` 再改 `cur.next`，否则后面节点找不到。
3. 递归反转终止条件 `head == null || head.next == null`，返回原尾节点（新头）。
4. 判环用快慢指针；另开 `visited` 集合会额外占 O(n)。
5. 偶数长度找中点，慢指针落在后半边起点，落点要想清楚。

**与 Java 的对应**

`java.util.LinkedList` 底层是双向链表（首尾增删都 O(1)），不是本文件这种单链表。`MySinglyLinkedList` 是最小化单向版本，把节点、指针、反转、判环讲透，并与 JDK `LinkedList` 逐元素比对一致。

**一句话口诀**

> 口诀：链表靠指针串起一串节点，头插 O(1) 查找 O(n)；反转先存 next，判环用快慢指针。

### DS-3 双向链表

> 对应代码：`lessons/11-data-structures/DoublyLinkedListDemo.java`

**为什么需要它**

单链表只能往后走，删节点还得先回头找前驱；双向链表从一个节点就能直接摸到前驱和后继，删除不用回头找。配一个 `tail` 尾指针，首尾增删都 O(1)。JDK 的 `LinkedList` 底层就是双向链表。

**核心思想**

每个节点同时握 `prev`（前驱）和 `next`（后继），链表维护 `head`、`tail` 两端点。正向从 `head` 顺 `next` 走，反向从 `tail` 顺 `prev` 走。删除必须同时改两个方向：前驱 `next` 接后继，后继 `prev` 接回前驱。

```text
head                                        tail
 │                                           │
 ▼                                           ▼
┌──────┐ prev ┌──────┐ prev ┌──────┐ prev ┌──────┐
│  5   │◀────▶│  10  │◀────▶│  20  │◀────▶│  30  │
└──────┘ next └──────┘ next └──────┘ next └──────┘
删中间 20：10.next=30；30.prev=10（两边都要改，只改一边留悬空指针）
```

**关键代码**

```java
static class MyDoublyLinkedList {
    static class Node {
        int val; Node prev, next;
        Node(int val) { this.val = val; this.prev = null; this.next = null; }
    }
    private Node head, tail; private int size;

    void addLast(int value) {
        Node node = new Node(value);
        if (tail == null) { head = tail = node; }
        else { node.prev = tail; tail.next = node; tail = node; }
        size++;
    }

    boolean removeByValue(int value) {
        Node cur = head;
        while (cur != null && cur.val != value) cur = cur.next;
        if (cur == null) return false;
        if (cur.prev != null) cur.prev.next = cur.next; else head = cur.next;
        if (cur.next != null) cur.next.prev = cur.prev; else tail = cur.prev;
        cur.prev = null; cur.next = null; size--;
        return true;
    }
    // 省略：addFirst/toListForward/toListBackward/size/isEmpty
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 头部添加 addFirst | O(1) | O(1) | 改 head 附近指针 |
| 尾部添加 addLast | O(1) | O(1) | 有 tail，不用走到队尾 |
| 按值删除 removeByValue | O(n) | O(n) | 先找到它；改指针 O(1) |
| 反向遍历 | O(n) | O(n) | 顺 tail.prev 往前 |
| 按位置访问 | O(n) | O(n) | 链表无下标 |
| 空间 / 适用场景 | O(n) | — | 首尾增删、双向遍历、LRU 缓存 |

**易错点**

1. 删除必须同时改两个方向指针；只改一边会留下悬空指针，链表直接断。
2. 只剩一个节点时删除，`head` 和 `tail` 都要置 `null`。
3. 插入先接好新节点前后指针，再改老节点指针，顺序反了容易断链。
4. 头尾节点的 `prev/next` 永远 `null`，遍历终止要判 `null`。
5. 双向只是"找到后删除快"，按值查找仍是 O(n)。

**与 Java 的对应**

`java.util.LinkedList` 内部就是双向链表（节点有 `prev/next`），首尾增删 O(1)，支持 `push/pop/offer/peek`。`MyDoublyLinkedList` 是其最小复刻版，与 JDK `LinkedList` 正反向逐元素比对（反向用 `descendingIterator`）。

**一句话口诀**

> 口诀：双向节点两头牵，prev 回头 next 向前；删除两头都要改，tail 在手尾插 O(1)。

### DS-4 循环链表

> 对应代码：`lessons/11-data-structures/CircularLinkedListDemo.java`

**为什么需要它**

普通链表尾节点 `next` 是 `null`；循环链表尾节点 `next` 指回 `head`，首尾相接，遍历一圈回到起点。约瑟夫环、轮转调度、循环播放这类"绕一圈又一圈"的场景用它最自然。

**核心思想**

末节点 `next` 不指向 `null` 而指向 `head`。空链表 `head == tail == null`；单节点特例：自己指向自己。遍历用 `do-while`：先走一步访问，再判 `cur != head`。删除先找前驱跨过待删者，同时维护 `head/tail` 的循环关系。

```text
        ┌──────────────────────────────┐
        │                              │
        ▼                              │
   ┌────────┐  ┌────────┐  ┌────────┐
   │  10    │─▶│  20    │─▶│  30    │
   └────────┘  └────────┘  └────────┘
     head                        tail
   tail.next = head
遍历：do { 访问 cur; cur = cur.next; } while (cur != head);
（普通链表判 cur==null；循环链表判 cur==head，否则死循环）
```

**关键代码**

```java
static class MyCircularLinkedList {
    static class Node {
        int label; Node next;
        Node(int label) { this.label = label; this.next = null; }
    }
    private Node head, tail; private int size;

    void addLast(int label) {
        Node node = new Node(label);
        if (head == null) { head = tail = node; node.next = node; }
        else { tail.next = node; node.next = head; tail = node; }
        size++;
    }

    List<Integer> toList() {
        List<Integer> result = new ArrayList<>();
        if (head == null) return result;
        Node cur = head;
        do { result.add(cur.label); cur = cur.next; } while (cur != head);
        return result;
    }
    // 省略：tailNextIsHead/size/josephus（找前驱 → 跨过待删者 → 维护 head/tail）
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 尾部添加 addLast | O(1) | O(1) | 接完尾 next 指回 head |
| 遍历一圈 | O(n) | O(n) | 终止条件是走回 head |
| 按值删除 | O(n) | O(n) | 先找前驱再改 next |
| 约瑟夫环模拟 | O(nk) | O(nk) | 每出列一个数 k 步 |
| 空间 / 适用场景 | O(n) | — | 轮转调度、约瑟夫环、循环播放 |

**易错点**

1. 遍历终止条件：循环链表判 `cur == head`，判 `null` 会死循环。
2. 空链表和单节点单独处理；单节点 `next` 必须指向自己。
3. 尾插后别忘记新尾 `next` 指回 `head`，否则断成普通链表。
4. 删 `head/tail` 时循环关系要一起维护。
5. 约瑟夫环删除后，下一轮起点是被删者的下一个节点。

**与 Java 的对应**

JDK 没有直接提供循环链表类；`LinkedList` 是普通双向链表。循环链表更多是结构思想，工程里常用 `ArrayDeque`/环形数组代替。本文件手写单向循环链表讲清回指与约瑟夫环，与 JDK `LinkedList` 模拟结果比对一致。

**一句话口诀**

> 口诀：尾指回头成一圈，遍历回到起点停；数到 k 就出列，绕着圈子一直轮。

### DS-5 栈

> 对应代码：`lessons/11-data-structures/StackDemo.java`

**为什么需要它**

栈只在栈顶一头进出，最后放进去的最先拿出来（后进先出 LIFO），像一摞盘子。括号匹配、表达式求值、方法调用栈、撤销、DFS 递归改迭代，本质都是"按相反顺序倒回去"。

**核心思想**

基本操作：`push` 入栈、`pop` 出栈、`peek` 看栈顶。数组栈用栈顶指针 `top`：空栈 `-1`，`push` 先 `++top` 再赋值，`pop` 先取值再 `--top`。链栈只留栈顶节点。最小栈用主栈 + 辅助栈：新值"小于等于"辅助栈顶才压入，弹出值等于辅助栈顶时同步弹走，实现 O(1) 取最小值。

```text
top=3  ┌──────┐  ← 40 栈顶（最后压入，最先弹出）
       │  40  │
top=2  ├──────┤
       │  30  │
top=1  ├──────┤
       │  20  │
top=0  ├──────┤
       │  10  │  ← 栈底（最先压入，最后弹出）
入栈 10→20→30→40；出栈 40→30→20→10（逆序，后进先出）
```

**关键代码**

```java
static class ArrayStack {
    private int[] data; private int top;
    ArrayStack() { data = new int[10]; top = -1; }

    void push(int value) {
        if (top == data.length - 1) grow();
        data[++top] = value;
    }
    int pop() {
        if (isEmpty()) throw new IllegalStateException("空栈不能 pop");
        return data[top--];
    }
    // 省略：peek/isEmpty/size/grow

    // 最小栈：主栈存全部，辅助栈记最小值轨迹；<= 栈顶才压辅助栈，min() 取辅助栈顶
    static class MinStack {
        private final ArrayStack main = new ArrayStack(), minAux = new ArrayStack();
        void push(int value) {
            main.push(value);
            if (minAux.isEmpty() || value <= minAux.peek()) minAux.push(value);
        }
        int min() { return minAux.peek(); }
    }
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 入栈 push | O(1) 均摊 | O(n) | 仅扩容瞬间 O(n) |
| 出栈 pop | O(1) | O(1) | 只动栈顶指针 |
| 看栈顶 peek | O(1) | O(1) | 不取走 |
| 取最小值 min | O(1) | O(1) | 最小栈靠辅助栈 |
| 空间 / 适用场景 | O(n) | — | 括号匹配、撤销、递归改迭代、DFS |

**易错点**

1. 空栈 `pop/peek` 越界，必须先判 `isEmpty()`。
2. `top` 空栈为 `-1`；`push` 先 `++top` 再赋值，`pop` 先取值再 `--top`，顺序别反。
3. 辅助栈入栈"小于等于"才压（等于也要压，否则重复最小值弹走会误删）。
4. 出栈值等于辅助栈顶时，辅助栈同步弹走。
5. 括号匹配：栈空遇右括号、配对不上、最后栈非空，都算不合法。

**与 Java 的对应**

`java.util.Stack` 是早期遗留类（继承 `Vector`），官方推荐用 `java.util.ArrayDeque` 作栈；`ArrayDeque` 底层是可扩容循环数组，性能更好。本文件手写数组栈 + 链栈，与 `ArrayDeque` 逐值比对。

**一句话口诀**

> 口诀：栈顶一头进和出，后进先出倒着数；括号匹配靠压弹，最小辅助栈记牢。

### DS-6 队列

> 对应代码：`lessons/11-data-structures/QueueDemo.java`

**为什么需要它**

队列只在队尾进、队首出，先排队的先办理（先进先出 FIFO）。任务调度、消息缓冲、BFS 广度优先遍历、生产者-消费者模型都要求按到达顺序处理。

**核心思想**

操作：`enqueue` 入队尾、`dequeue` 出队首、`peek` 看队首。链式队列用 `front/rear` 两指针。循环数组用 `head`（队首）、`tail`（下一可写位置），下标绕回取模。牺牲一格空间区分队空（`head==tail`）与队满（`(tail+1)%cap==head`）。

```text
容量 5（牺牲一格，实际最多装 4 个）：
下标:    0      1      2      3      4
      ┌──────┬──────┬──────┬──────┬──────┐
data: │ 30   │ 40   │ 空   │ 空   │ 空   │
      └──────┴──────┴──────┴──────┴──────┘
         ▲ head                    ▲ tail
判空 head==tail；判满 (tail+1)%capacity==head
长度 (tail-head+capacity)%capacity（加容量防负数）
```

**关键代码**

```java
static class ArrayCircularQueue {
    private int[] data;
    private int head, tail, capacity;

    ArrayCircularQueue(int capacity) {
        this.capacity = capacity; data = new int[capacity];
    }

    boolean isFull() { return (tail + 1) % capacity == head; }
    boolean isEmpty() { return head == tail; }

    void enqueue(int value) {
        if (isFull()) throw new IllegalStateException("队列已满");
        data[tail] = value;
        tail = (tail + 1) % capacity;
    }

    int dequeue() {
        if (isEmpty()) throw new IllegalStateException("队列为空");
        int value = data[head];
        head = (head + 1) % capacity;
        return value;
    }
    // 省略：peek/size/toList；链式队列 LinkedQueue(front/rear/size)
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 入队 enqueue | O(1) | O(1) | 循环数组动 tail |
| 出队 dequeue | O(1) | O(1) | 循环数组动 head |
| 看队首 peek | O(1) | O(1) | 不取走 |
| 判满 | O(1) | O(1) | `(tail+1)%cap==head` |
| 空间 / 适用场景 | O(n) | — | BFS、任务调度、消息缓冲 |

**易错点**

1. 牺牲一格：满 = `(tail+1)%cap == head`，否则空满两种状态分不出来。
2. 下标绕回必须取模，不能直接 `++`。
3. 链式队列删空时 `front/rear` 都要置 `null`。
4. 长度 `(tail-head+cap)%cap`，别直接 `tail-head`（可能为负）。
5. 容量 n 的牺牲一格队列最多装 n-1 个，留余量要算上这一格。

**与 Java 的对应**

`java.util.Queue` 是接口，推荐实现 `ArrayDeque`（循环数组）和 `LinkedList`（链表）；`ArrayDeque` 既能当队列也能当栈，优于老旧 `Stack`。本文件手写循环数组队列 + 链式队列，与 JDK 三方比对出队值。

**一句话口诀**

> 口诀：队尾进、队首出，先进先出守规矩；循环取模绕一圈，牺牲一格判得清。

### DS-7 双端队列

> 对应代码：`lessons/11-data-structures/DequeDemo.java`

**为什么需要它**

普通队列只能队尾进队首出；双端队列（Deque）两头都能进、两头都能出，一个结构既能当栈也能当队列。栈和队列本质都是受限的双端队列，`ArrayDeque` 因此能同时充当栈、队列、单调队列三种角色。

**核心思想**

数组双端队列用 `head/tail` 双指针 + 取模绕回。队头添加 `head` 先取模后退一格再写；队尾添加写 `tail` 再后移；删除对称。判空 `head==tail`，判满 `(tail+1)%cap==head`。单调队列求滑动窗口最大值：队列存下标、值单调递减，队首即窗口最大值。

```text
        addFirst ← ┌──────┐ ──→ addLast
                   │      │
        head ─────▶│ 5 10 20 30 │◀───── tail
                   └──────┘
队头前移：head = (head-1+capacity)%capacity（+capacity 防负数）
队尾后移：tail = (tail+1)%capacity
```

**关键代码**

```java
// 单调队列：存下标，值单调递减；队首即窗口最大值（双端队列作底座）
private static int[] maxSlidingWindow(int[] nums, int k) {
    Deque<Integer> dq = new ArrayDeque<>();
    List<Integer> out = new ArrayList<>();
    for (int i = 0; i < nums.length; i++) {
        while (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
        while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) dq.pollLast();
        dq.offerLast(i);
        if (i >= k - 1) out.add(nums[dq.peekFirst()]);
    }
    return out.stream().mapToInt(Integer::intValue).toArray();
}
// 省略：ArrayDequeHandwritten 双端队列 addFirst/addLast/remove/peek/isFull/isEmpty/size/toList
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 两端添加 | O(1) | O(1) | 只动 head/tail |
| 两端删除 | O(1) | O(1) | 只动 head/tail |
| 看两端 peek | O(1) | O(1) | 不取走 |
| 单调队列窗口最值 | O(n) | O(n) | 每下标最多入出队各一次 |
| 空间 / 适用场景 | O(n) | — | 栈/队列底座、滑动窗口最值 |

**易错点**

1. `head` 前移 `(head-1+cap)%cap`，别写成 `head-1` 出现负数。
2. 判满同样牺牲一格；判空 `head==tail`。
3. 单调队列存下标而不是值，靠下标判断滑出窗口、再取原值。
4. 队尾 `<=` 当前值的下标都弹走，保持递减。
5. 窗口未形成（`i < k-1`）不能取队首；结果长度 `n-k+1`。

**与 Java 的对应**

`java.util.Deque` 是接口，推荐实现 `ArrayDeque`（循环数组），`LinkedList` 也实现了 `Deque`。官方建议：栈用 `push/pop`，队列用 `offer/poll`，不要再用遗留 `Stack`。本文件手写双端队列并实现单调队列，结果与暴力扫描对拍一致。

**一句话口诀**

> 口诀：双端两头都开关，能当栈来能当队；单调队列存下标，队首永远是最大。

### DS-8 优先队列与堆

> 对应代码：`lessons/11-data-structures/PriorityQueueHeapDemo.java`

**为什么需要它**

任务按优先级处理、求 TopK、数据流中位数、Dijkstra 选最近节点，都要反复"取最值并删去"。普通数组取最值 O(n)，排序后 O(n log n)；堆把取最值和调整压到 O(log n)。

**核心思想**

堆是满足堆性质的完全二叉树：小顶堆父节点总不大于子节点，堆顶是最小值；大顶堆反之。完全二叉树用数组存。0 下标公式：父 `(i-1)/2`，左 `2i+1`，右 `2i+2`。入堆放末尾上浮 `siftUp`；出堆尾节点补堆顶再下沉 `siftDown`；批量建堆从最后父节点向前下沉，整体 O(n)。求 TopK 用容量 K 的小顶堆。

```text
小顶堆（数组 [1,2,3,5,8,9]）：
            1  ← 堆顶（最小值）
          /   \
         2     3
        / \   / \
       5   8 9
i=1：父=(1-1)/2=0；左=2*1+1=3；右=2*1+2=4
上浮：比父小就交换到根；下沉：左右孩子挑更小的交换到叶子
```

**关键代码**

```java
static class MinHeap {
    private int[] data; private int size;
    MinHeap(int capacity) { data = new int[capacity]; size = 0; }
    void offer(int value) {
        if (size == data.length) grow();
        data[size] = value; siftUp(size); size++;
    }
    int poll() {
        if (isEmpty()) throw new IllegalStateException("堆为空");
        int top = data[0];
        data[0] = data[size - 1]; size--;
        siftDown(0); return top;
    }
    private void siftDown(int i) {
        while (true) {
            int l = 2*i+1, r = 2*i+2, s = i;
            if (l < size && data[l] < data[s]) s = l;
            if (r < size && data[r] < data[s]) s = r;
            if (s == i) break;
            swap(i, s); i = s;
        }
    }
    // 省略：peek/siftUp/buildHeap/swap/grow
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 入堆 offer | O(log n) | O(log n) | 向上 siftUp |
| 出堆 poll | O(log n) | O(log n) | 尾补堆顶再 siftDown |
| 看堆顶 peek | O(1) | O(1) | 数组下标 0 |
| 批量建堆 | O(n) | O(n) | 从最后父节点向前下沉 |
| 空间 / 适用场景 | O(n) | — | TopK、中位数、优先级队列、Dijkstra |

**易错点**

1. 0 下标公式父 `(i-1)/2`、左 `2i+1`、右 `2i+2`；1 下标公式不同，别混。
2. `siftDown` 在左右孩子里挑更小的交换，只跟左孩子比会错。
3. 出堆是尾节点补堆顶再下沉，不是直接删堆顶。
4. 求最大 K 个数用小顶堆：堆顶是 K 个候选里最小者，新来更大就替换。
5. 建堆 O(n)：越靠下沉降层数越少，总调整次数是收敛级数。

**与 Java 的对应**

`java.util.PriorityQueue` 默认小顶堆；传 `Collections.reverseOrder()` 变最大顶堆。底层也是数组 + 二叉堆，与手写 `MinHeap` 同一思想。本文件出堆顺序与 JDK 逐值比对，堆排序结果与 `Arrays.sort` 对拍一致。

**一句话口诀**

> 口诀：父子下标算得准，上浮下沉调堆身；建堆线性出堆对数，TopK 用小顶守大门。

### DS-9 哈希表

> 对应代码：`lessons/11-data-structures/HashMapDemo.java`

**为什么需要它**

数组按下标访问 O(1)，但 key 常是字符串没法当下标；链表查找 O(n)。哈希表在两者间架桥：哈希函数把 key 算成数组下标，再处理碰撞，多数操作 O(1)。

**核心思想**

扰动哈希 `h ^ (h >>> 16)` 让高 16 位参与低位运算；容量 2 的幂时 `hash & (capacity-1)` 代替取模。碰撞两解法：开放寻址（线性探测往后探，删除留墓碑 `DELETED`，不能置空）；链地址法（每桶挂链表，扩容逐个 rehash 重挂）。负载因子 = 元素数/容量，超阈值（0.75）扩容。

```text
链地址法：
桶 0 ─▶ Node("张三",90) ─▶ Node("赵六",92) ─▶ null
桶 1 ─▶ null
桶 2 ─▶ Node("李四",85) ─▶ null

开放寻址（墓碑 DELETED）：
槽位:   0        1        2        3        4
      ┌──────┬──────┬──────┬──────┬──────┐
状态: │占用 a│墓碑  │占用 c│占用 d│空    │
      └──────┴──────┴──────┴──────┴──────┘
删 b 只打墓碑不置空 → 后面的 c、d 仍能探测到
```

**关键代码**

```java
private static int hash(String key) {
    int h = key.hashCode();
    return h ^ (h >>> 16);
}
static class ChainedMap {
    static class Node {
        String key; int value; Node next;
        Node(String key, int value, Node next) {
            this.key = key; this.value = value; this.next = next;
        }
    }
    Node[] buckets; int size, capacity;
    static final double LOAD_FACTOR = 0.75;
    void put(String key, int value) {
        if ((double) size / capacity >= LOAD_FACTOR) resize();
        Node cur = buckets[slot(key)];
        while (cur != null) {
            if (cur.key.equals(key)) { cur.value = value; return; }
            cur = cur.next;
        }
        buckets[slot(key)] = new Node(key, value, buckets[slot(key)]);
        size++;
    }
    // 省略：get/remove/keySet/capacity/resize；树化阈值 8 只做说明
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 插入 put | O(1) | O(n) | 最坏全撞同一格/探测链过长 |
| 查找 get | O(1) | O(n) | 同上 |
| 删除 remove | O(1) | O(n) | 开放寻址删除必须留墓碑 |
| 扩容 rehash | O(n) | O(n) | 重新算下标搬一遍，仅扩容瞬间 |
| 空间 / 适用场景 | O(n) | — | key-value 字典、计数缓存、去重 |

**易错点**

1. 开放寻址删除直接置空会中断探测链，链后元素查不到；必须打 `DELETED` 墓碑。
2. 负载因子超 0.75 就扩容；太高碰撞多，太低浪费空间。
3. 扰动让高位参与低位；直接 `hashCode & mask` 低位相同的 key 会大量撞车。
4. 链地址扩容要逐节点重算下标（rehash），不能整体搬老链表。
5. 字符串 key 用 `equals` 比较，不能用 `==`；同一 key 哈希值必须稳定。

**与 Java 的对应**

`java.util.HashMap` 底层是"数组 + 链表 + 红黑树"链地址法，默认容量 16、负载因子 0.75，扰动即 `h ^ (h >>> 16)`。**取舍说明：链表树化（长度达 8 且容量达 64 升级红黑树）本文件只做说明，未实现**；手写开放寻址与链地址两种迷你实现，最后与官方 `HashMap` 对拍。

**一句话口诀**

> 口诀：key 哈希算下标，撞车两种解法——开放寻址往后探，链地址法挂链表；删除留墓碑，满了就扩容。

### DS-10 二叉树与四种遍历

> 对应代码：`lessons/11-data-structures/BinaryTreeDemo.java`

**为什么需要它**

数组和链表都是"一条线"，只能线性遍历；树能表达层级、分支与父子关系（目录、表达式、决策、索引）。二叉树是每个节点最多两个孩子的层级结构，是 BST、AVL、线段树、堆的共同地基。

**核心思想**

节点存 `val/left/right`。前/中/后序的区别只在"访问根"发生在左右子树之前、之间还是之后：前序根左右，中序左根右，后序左右根；层序用队列一层一层从左到右。顺带统计：树高 = 最长路径节点数（空树 0），叶子是左右都为 `null` 的节点。由前序 + 中序重建：前序首元素是根，中序划分左右子树递归。

```text
样例二叉树：
            1
          /   \
         2     3
        / \
       4   5

前序（根左右）：1, 2, 4, 5, 3
中序（左根右）：4, 2, 5, 1, 3
后序（左右根）：4, 5, 2, 3, 1
层序（队列）  ：1, 2, 3, 4, 5
```

**关键代码**

```java
static class Node {
    int val; Node left, right;
    Node(int val) { this.val = val; }
}
// 递归前序（根左右）；中序/后序只改 out.add 位置
private static void preorderRec(Node root, List<Integer> out) {
    if (root == null) return;
    out.add(root.val);
    preorderRec(root.left, out);
    preorderRec(root.right, out);
}
// 层序：队列一层一层从左到右
private static List<Integer> levelOrder(Node root) {
    List<Integer> out = new ArrayList<>();
    if (root == null) return out;
    Deque<Node> q = new ArrayDeque<>();
    q.offer(root);
    while (!q.isEmpty()) {
        Node cur = q.poll(); out.add(cur.val);
        if (cur.left != null) q.offer(cur.left);
        if (cur.right != null) q.offer(cur.right);
    }
    return out;
}
// 省略：inorderRec/postorderRec/三种迭代版/height/leafCount/nodeCount/buildFromPreIn
```

**复杂度**

| 操作 | 平均 / 最坏 | 说明 |
|---|---|---|
| 前/中/后序遍历 | O(n) | 每节点访问一次；递归深度 h，退化时 h=n |
| 层序遍历 | O(n) | 队列逐层放入取出 |
| 求高度 / 叶子数 | O(n) | 一次 DFS 顺带统计 |
| 前序+中序重建 | O(n) | 哈希表记中序下标，查根 O(1) |
| 空间 / 适用场景 | O(n) | 递归栈/迭代栈/队列；层级数据建模 |

**易错点**

1. 递归遍历终止条件 `root == null`；忘记判空，叶子孩子处空指针。
2. 命名看"根在哪一步"：前根左右、中左根右、后左右根；左右顺序不变。
3. 迭代中序：一路向左压栈，弹栈访问后转向右孩子，顺序别写反。
4. 重建二叉树靠前序定根、中序分边；前后序不一致或有重复值会出错。
5. 叶子是左右孩子都为 `null`；只有一个孩子的节点不算叶子。

**与 Java 的对应**

JDK 没有直接暴露普通二叉树；`TreeMap/TreeSet` 底层是红黑树（平衡 BST），`PriorityQueue` 是完全二叉树堆。本文件手写二叉树与遍历，是这些结构的共同地基。

**一句话口诀**

> 口诀：前根左右，中左根右，后左右根；层序用队列，递归看终止，重建靠前序定根、中序分边。

### DS-11 二叉搜索树 BST

> 对应代码：`lessons/11-data-structures/BinarySearchTreeDemo.java`

**为什么需要它**

普通二叉树查找只能 O(n)；BST 在二叉树上加规矩：左子树所有值 < 根 < 右子树所有值，靠大小分边把每次比较砍掉一半候选，理想查找 O(log n)，中序遍历天然升序。`TreeSet/TreeMap` 的有序能力就建立在平衡 BST（红黑树）之上。

**核心思想**

插入：比根小走左、比根大走右，走到空位挂节点；重复值忽略。查找沿左右下探，走到 `null` 即不存在。删除三情况：叶子直接摘；单子把孩子接上；双子取后继（右子树最左节点）顶替，再删后继。按升序依次插入会退化成右斜链，树高 n，查找退回 O(n)。

```text
样例 BST（中序 2,3,4,5,6,7,8）：
            5
          /   \
         3     7
        / \   / \
       2   4 6   8

查找 6：5 → 7 → 6（每次比较砍掉一半候选）
删双子 5：后继 = 右子树最左 6；6 顶替 5，再删 6 原节点
退化反例：1,2,3,…,n 依次插入 → 一条右斜链，树高 = n
```

**关键代码**

```java
static class MyBST {
    static class Node {
        int val; Node left, right;
        Node(int val) { this.val = val; }
    }
    private Node root;

    private Node insertRec(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.val) node.left = insertRec(node.left, value);
        else if (value > node.val) node.right = insertRec(node.right, value);
        return node;
    }

    boolean contains(int value) {
        Node cur = root;
        while (cur != null) {
            if (value == cur.val) return true;
            cur = value < cur.val ? cur.left : cur.right;
        }
        return false;
    }
    // 删除：叶子直接摘；单子接孩子；双子取后继（右子树最左）顶替再删后继
    // 省略：removeRec/minNode/inorder/height/insert/remove 入口
}
```

**复杂度**

| 操作 | 平均 | 最坏 | 说明 |
|---|---|---|---|
| 查找 contains | O(log n) | O(n) | 平衡 h=log n；退化 h=n |
| 插入 insert | O(log n) | O(n) | 沿树高找空位挂上 |
| 删除 remove | O(log n) | O(n) | 双子要找后继再删 |
| 中序遍历 | O(n) | O(n) | 结果必升序 |
| 空间 / 适用场景 | O(n) | — | 有序字典；裸 BST 不保证平衡 |

**易错点**

1. 规矩是"整棵左子树小于根、整棵右子树大于根"，不只是直接孩子。
2. 双子删除不能直接删根；用后继/前驱顶替，再删顶替节点，保证中序有序。
3. 顺序插入退化成链表，查找 O(n)——这是需要 AVL/红黑树的原因。
4. 本文件不处理重复值；重复 key 直接忽略。
5. 删除递归返回值要赋回 `node.left/right`，别漏挂回父节点。

**与 Java 的对应**

`java.util.TreeSet` 底层是 `TreeMap`，`TreeMap` 底层是红黑树。本文件手写"裸 BST"，不做旋转平衡，有序但可能退化；与 `TreeSet` 对拍中序结果一致。真实工程要有序集合直接用 `TreeSet/TreeMap`。

**一句话口诀**

> 口诀：左小右大是 BST，中序遍历必升序；删叶直接摘，单孩接孩子，双子找后继顶替。

### DS-12 AVL 平衡树

> 对应代码：`lessons/11-data-structures/AvlTreeDemo.java`

**为什么需要它**

裸 BST 顺序插入退化成链表 O(n)。AVL 是"自带体检"的 BST：每个节点记左右子树高度差（平衡因子），失衡就旋转把高的一侧压下去，保证高度差不超过 1，树高始终 O(log n)。

**核心思想**

节点多一个 `height`（空树 0，叶子 1）。平衡因子 = 左高 - 右高；>1 左高，<-1 右高。插入后回溯：先更新高度，再判失衡类型——LL 左高且落左孩子左侧 → 右旋；RR 右高且落右孩子右侧 → 左旋；LR 先左旋左孩子再右旋根；RL 先右旋右孩子再左旋根。旋转后先算子节点高度、再算根高度。

```text
LL 失衡（30 → 20 → 10）右旋：
        30                 20
       /                  /  \
      20        ──▶      10   30
     /
    10
右旋：x（左孩子）提上来当根，y（失衡根）降到 x 右孩子，x 原右子树 t2 挂到 y 左边。
```

**关键代码**

```java
static class AvlTree {
    static class Node {
        int val; Node left, right; int height;
        Node(int val) { this.val = val; this.height = 1; }
    }
    private Node root;

    private int height(Node n) { return n == null ? 0 : n.height; }
    private int balanceFactor(Node n) { return height(n.left) - height(n.right); }

    private Node insertRec(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.val) node.left = insertRec(node.left, value);
        else if (value > node.val) node.right = insertRec(node.right, value);
        else return node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int bf = balanceFactor(node);
        if (bf > 1 && value < node.left.val) return rightRotate(node);
        if (bf < -1 && value > node.right.val) return leftRotate(node);
        if (bf > 1 && value > node.left.val) { node.left = leftRotate(node.left); return rightRotate(node); }
        if (bf < -1 && value < node.right.val) { node.right = rightRotate(node.right); return leftRotate(node); }
        return node;
    }
    // 省略：rightRotate/leftRotate/contains/inorder/height/insert 入口
}
```

**复杂度**

| 操作 | 平均 / 最坏 | 说明 |
|---|---|---|
| 插入 insert | O(log n) | 下探 + 回溯更新高度并按需旋转 |
| 查找 contains | O(log n) | 严格平衡，树高 <= 1.44 log2(n+2) |
| 删除 remove | O(log n) | 本文件只演示插入调平；删除同样需回溯旋转 |
| 旋转（左/右旋） | O(1) | 只改几个指针和高度 |
| 空间 / 适用场景 | O(n) | 查找频繁、对树高要求严格 |

**易错点**

1. 平衡因子 = 左高 - 右高；>1 左高，<-1 右高。
2. 先看哪边高、再看新节点落在哪侧：LL/RR 单旋，LR/RL 双旋，判据别套反。
3. 旋转后先更新被旋转节点高度，再更新新根高度（子先父后）。
4. 高度约定空树 0、叶子 1；平衡因子是高度差，不是节点个数差。
5. 回溯路径上每个祖先高度都要重算，不能只更新旋转涉及的两个节点。

**与 Java 的对应**

JDK 没有直接提供 AVL；`TreeSet/TreeMap` 用红黑树。AVL 平衡更严格查找更快，红黑树平衡略松、增删旋转更少。**取舍说明：本文件未实现删除后的回溯旋转**，只演示插入调平；AVL 中序与 `TreeSet` 对拍一致。

**一句话口诀**

> 口诀：左高右旋，右高左旋；LR 先左后右，RL 先右后左；转完先算子树高，再算根高。

### DS-13 红黑树与 TreeMap

> 对应代码：`lessons/11-data-structures/TreeMapRedBlackDemo.java`

**为什么需要它**

裸 BST 退化，AVL 严格但每次增删反复查高度；红黑树用颜色约束换取更宽松的平衡，增删旋转次数更少，适合做有序字典。JDK 的 `TreeMap/TreeSet` 底层就是红黑树。

**核心思想**

红黑树不要求高度差为 1（那是 AVL），只保证最长路径不超过最短路径两倍，整体 O(log n)。五条性质：① 节点非红即黑；② 根黑；③ 外部叶子 NIL 黑；④ 红节点孩子必须黑（不红红相连）；⑤ 任一节点到各 NIL 叶子黑节点数相同（黑高一致）。`TreeMap` 靠比较器决定左右；有序 API 取边界键与区间视图。

```text
概念形态（B 黑、R 红，红不连红，黑高一致）：
            B(根)
          /    \
         R      B
        / \      \
       B   B      R
                  / \
                 B   B
黑高一致 → 全黑路径等长；红不连续 → 红节点最多间隔出现
→ 最长路径（红黑相间）≈ 2 倍最短路径（全黑），树高仍是 O(log n)
```

**关键代码**

```java
// TreeMap 有序能力演示（底层红黑树，本文件不手写完整红黑树）
TreeMap<Integer, String> map = new TreeMap<>();
int[] keys = {30, 10, 50, 20, 40};
for (int i = 0; i < keys.length; i++) map.put(keys[i], "v" + keys[i]);

map.firstKey();     // 最小键 10
map.lastKey();      // 最大键 50
map.floorKey(35);   // ≤35 的最大键 30
map.ceilingKey(35); // ≥35 的最小键 40
map.subMap(20, 40).keySet(); // 左闭右开 {20, 30}
map.headMap(30).keySet();    // 不含 30：{10, 20}
map.tailMap(40).keySet();    // 含 40：{40, 50}

// 源码要点：节点 Entry = key/value/left/right/parent/color；
// put 下探找位置 → 挂新节点（默认红）→ fixAfterInsertion 变色/旋转；比较器决定大小关系。
```

**复杂度**

| 操作 | 平均 / 最坏 | 说明 |
|---|---|---|
| 插入 put | O(log n) | `fixAfterInsertion` 沿父链变色/旋转 |
| 查找 get | O(log n) | 按比较器沿左右下探 |
| 删除 remove | O(log n) | 本文件不手写红黑树删除（见取舍） |
| 有序区间查询 | O(log n + k) | floor/ceiling/subMap，k 为返回个数 |
| 空间 / 适用场景 | O(n) | 需按键有序遍历、边界键、区间视图的字典 |

**易错点**

1. 五条性质背熟：非红即黑、根黑、NIL 叶黑、红不连红、黑高一致。
2. 黑高一致 + 红不连续推出最长路径 ≤ 2×最短路径，树高 O(log n)。
3. `TreeMap` 靠比较器决定左右；不传 `Comparator` 用自然顺序。
4. `floorKey` 返回 ≤ key 最大键，`ceilingKey` 返回 ≥ key 最小键；不存在返回 `null`，别当 0。
5. `subMap` 左闭右开；`headMap(to)` 不含 to；区间边界易踩错。

**与 Java 的对应**

`java.util.TreeMap` 底层红黑树，节点是内部类 `TreeMap.Entry`（`key/value/left/right/parent/color`）；`put` 后调 `fixAfterInsertion`；`TreeSet` 内部包了一个 `TreeMap`。**取舍说明：本文件不手写完整红黑树，尤其不实现红黑树删除（双黑修复较复杂），只讲性质并用 TreeMap 演示有序能力与源码要点**。

**一句话口诀**

> 口诀：根黑叶黑红不连，到叶黑高都一样；路径不超两倍长，TreeMap 有序靠红黑。

### DS-14 并查集

> 对应代码：`lessons/11-data-structures/UnionFindDemo.java`

**为什么需要它**

判断两人是不是朋友、两个格子是否同属一个岛屿、两条路是否已连通，本质都是连通性问题。BFS/DFS 每次整片搜索；并查集用父指针树把元素分成互不相交集合，`find` 找根并压平路径，`union` 合并两树，接近 O(α(n))（反阿克曼函数，视为常数）。

**核心思想**

`parent` 数组初始化 `parent[i] = i`，各自一伙。`find(x)` 沿父指针找根，回溯时把路径节点直接挂到根（路径压缩）。`union(a,b)` 先 find 两根，再按秩（高度）把矮树挂高树下；`count` 记连通块个数，只有不同集合合并成功才减一。

```text
初始（5 个元素，各自一伙）：
  0   1   2   3   4
  │   │   │   │   │  parent[i] = i

合并 0-1,1-2,3-4 之后（按秩挂接）：
        1
       / \
      0   2          3
                     / \
                    4   …
find(0) 路径压缩：0 → 1，回溯 parent[0] 直接指向根 1
同伙判断：find(a) == find(b)
```

**关键代码**

```java
static class UnionFind {
    int[] parent;   // 根的 parent 指向自己
    int[] rank;     // 以 i 为根的树高上界
    int count;      // 连通块个数
    UnionFind(int n) {
        parent = new int[n]; rank = new int[n];
        for (int i = 0; i < n; i++) { parent[i] = i; rank[i] = 1; }
        count = n;
    }
    // 找根，回溯时把路径节点直接挂到根（路径压缩）
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[ra] > rank[rb]) parent[rb] = ra;
        else { parent[rb] = ra; rank[ra]++; }
        count--;
        return true;
    }
    // 省略：connected/components/pathLength
}
```

**复杂度**

| 操作 | 平均 / 摊还 | 说明 |
|---|---|---|
| find(x) | O(α(n)) | 路径压缩后接近常数 |
| union(a,b) | O(α(n)) | 先 find 两根，再按秩挂接 |
| 连通块计数 | O(1) | 维护 count，合并成功才减一 |
| 空间 / 适用场景 | O(n) | 朋友圈、岛屿、Kruskal 判环、账号合并 |

**易错点**

1. 初始化 `parent[i] = i`；不要全置 0 或 -1。
2. 路径压缩要在递归回溯写回 `parent[x]`，别漏。
3. 按秩/大小合并：矮树挂高树下，避免越合越高；只合并不挑方向会退化。
4. `union` 前必须先 find 到根；直接比 `parent[a]/parent[b]` 是错的。
5. 只有不同集合合并成功 `count` 才减一，同根合并不减。

**与 Java 的对应**

JDK 没有内置并查集；它是竞赛与图论基础工具。典型场景：朋友圈分组、岛屿数量、Kruskal 最小生成树判环、账号合并。本文件手写并与自写 BFS 数出的连通块对拍，随机图上一致。

**一句话口诀**

> 口诀：父指自己是初始，找根顺手压路径；矮树挂到高树下，一伙看根同不同。

### DS-15 Trie 字典树

> 对应代码：`lessons/11-data-structures/TrieDemo.java`

**为什么需要它**

输入联想、输入法候选、通讯录过滤、路由前缀匹配，都是"按前缀找词"。普通字符串集合每个词做 `startsWith` 是 O(n·L)；Trie 把单词按字母拆成字符路径，同前缀共享前面路径，查询只和词长 L 相关。

**核心思想**

节点存 26 路子节点数组（小写 `a~z`）和 `isEnd` 标记。插入逐字母走树，没分支就新建，终点标 `isEnd=true`。整词查找：路径走通且终点 `isEnd` 为真；前缀查找：路径走通即可。自动补全：走到前缀节点再 DFS 收集后缀拼成整词。

```text
插入 cat / car / card / cake / dog / do 后：
root
 ├─ c ── a ── t(词)
 │        ├─ r(词)
 │        │    └─ d(词)
 │        └─ k ── e(词)
 └─ d ── o(词)
          └─ g(词)
index(c) = c - 'a'；isEnd：这里是完整单词，前缀节点不一定是词
补全 "ca"：走到 a 节点，DFS 收集 cake/car/card/cat
```

**关键代码**

```java
static class Trie {
    static class Node {
        Node[] children = new Node[26]; // null 表示分支不存在
        boolean isEnd;
    }
    private final Node root = new Node();

    private int index(char c) { return c - 'a'; }

    void insert(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            int idx = index(word.charAt(i));
            if (cur.children[idx] == null) cur.children[idx] = new Node();
            cur = cur.children[idx];
        }
        cur.isEnd = true;
    }

    boolean search(String word) { Node n = walk(word); return n != null && n.isEnd; }
    boolean startsWith(String prefix) { return walk(prefix) != null; }
    // 省略：walk/autocomplete/dfsCollect（走到前缀节点 DFS 收集整词）
}
```

**复杂度**

| 操作 | 平均 / 最坏 | 说明 |
|---|---|---|
| 插入 insert | O(L) | 每个字母走一层，L 为词长 |
| 整词查找 search | O(L) | 走通且 isEnd 为真 |
| 前缀查找 startsWith | O(L) | 路径走通即可 |
| 自动补全 | O(L + k) | k 为补全单词个数，需 DFS 收集 |
| 空间 / 适用场景 | O(n·L) | 公共前缀存一份；输入联想、前缀匹配 |

**易错点**

1. `isEnd` 标记完整单词；前缀节点不一定是词（"ca" 是 "cat" 前缀但不是词）。
2. 整词查找要同时满足路径走通和 `isEnd=true`；只走通路径只说明是前缀。
3. 子节点数组按字符集定：小写 26；含大写/数字/中文要换 `Map` 子节点。
4. Trie 空间换时间：公共前缀只存一份，额外分支占指针。
5. 空串要单独处理；本文件约定插入非空小写字母词。

**与 Java 的对应**

JDK 没有内置 Trie；可借 `TreeMap/HashMap` 实现子节点映射。本文件用 26 路数组手写，自动补全结果与 `String.startsWith` 过滤集合的朴素做法对拍一致。

**一句话口诀**

> 口诀：一字母一层路，同前缀共享路径；isEnd 才算整词，前缀走通即可补全。

### DS-16 图（邻接矩阵与邻接表）

> 对应代码：`lessons/11-data-structures/GraphDemo.java`

**为什么需要它**

图是"顶点 + 边"的集合，导航、社交关系、网络拓扑、任务依赖都建模成图。选对存储直接决定空间占用和查询速度：稠密图适合矩阵，稀疏图适合邻接表。

**核心思想**

邻接矩阵 `boolean[n][n]`，`adj[u][v]=true` 表示有边；无向图一条边对称写两处。判边下标直达 O(1)，求度数一行 O(n)，空间 O(n²)。邻接表 `List<List<Integer>>`，每顶点一条邻居链表；无向图双向加。判边扫链表 O(deg)，求度链表长度 O(1)，空间 O(n+m)。

```text
样例图：边 {0-1,0-2,1-2,1-3,2-4,3-4}

邻接矩阵（1=有边）：
      0  1  2  3  4
  0 [ 0  1  1  0  0 ]
  1 [ 1  0  1  1  0 ]
  2 [ 1  1  0  0  1 ]
  3 [ 0  1  0  0  1 ]
  4 [ 0  0  1  1  0 ]

邻接表：
  0 -> [1, 2]   1 -> [0, 2, 3]   2 -> [0, 1, 4]
  3 -> [1, 4]   4 -> [2, 3]
度数：0=2, 1=3, 2=3, 3=2, 4=2
```

**关键代码**

```java
// 实现一：邻接矩阵
static class AdjacencyMatrix {
    private final boolean[][] adj;
    private final int n;
    AdjacencyMatrix(int n) { this.n = n; adj = new boolean[n][n]; }

    void addEdge(int u, int v) {
        adj[u][v] = true;
        adj[v][u] = true; // 无向图对称两处
    }

    boolean hasEdge(int u, int v) { return adj[u][v]; } // O(1) 下标直达

    int degree(int v) {
        int count = 0;
        for (int u = 0; u < n; u++) if (adj[v][u]) count++;
        return count;
    }
}

// 实现二：邻接表 List<List<Integer>>
// addEdge：adj.get(u).add(v); adj.get(v).add(u);
// hasEdge：adj.get(u).contains(v)；degree：adj.get(v).size()
```

**复杂度**

| 结构 | 空间 | 判边 | 求度 | 遍历邻居 |
|---|---|---|---|---|
| 邻接矩阵 | O(n²) | O(1) 下标直达 | O(n) 数一行 | O(n) 扫一行 |
| 邻接表 | O(n+m) | O(deg) 扫链表 | O(1) 链表长度 | O(deg) 走链表 |
| 适用 | 稠密图（边多） | 稀疏图（边少） | — | — |

**易错点**

1. 无向图一条边在矩阵写对称两处，在邻接表双向加入。
2. 矩阵判边快，但 n 大边少时大量格子 `false`，空间浪费 O(n²)。
3. 邻接表判边沿链表找，最坏 O(deg)，不能 O(1) 直达。
4. 度 = 邻居个数；有向图还要分入度、出度（本文件只讲无向无权图）。
5. 顶点下标必须在 `0~n-1`；加边前做合法性检查。

**与 Java 的对应**

JDK 没有内置图结构；工程常用 `List<List<Integer>>` 实现邻接表、二维数组实现矩阵。本文件手写两种结构，同一张图上对拍边存在性与度数，随机稀疏图一致。

**一句话口诀**

> 口诀：矩阵判边 O(1) 但吃空间，邻接表省空间但找边要链表；稠密用矩阵，稀疏用邻接表。

### DS-17 线段树

> 对应代码：`lessons/11-data-structures/SegmentTreeDemo.java`

**为什么需要它**

前缀和 O(1) 求区间和，但元素一改就要 O(n) 重建；树状数组擅长单点改 + 前缀和，但区间最值、区间批量修改不够直接。线段树把区间对半切开，每节点存一段汇总值，单点修改、区间查询沿树走 O(log n)；区间批量修改用懒标记先挂节点，下探时再 `pushDown` 下发。

**核心思想**

建树：叶子放原数组值，内部节点 = 左右孩子汇总（和相加 / 最大值取大）。递归区间闭 `[l,r]`，`mid=(l+r)/2`，左 `[l,mid]`、右 `[mid+1,r]`。查询：整段覆盖直接取汇总值，否则先 `pushDown` 再下探。区间加：整段覆盖时和加 `v×长度`，增量挂懒标记。递归线段树一般开 4n 数组兜底。

```text
数组 [1,3,5,7,9,11] 区间和线段树：
              [0,5] 和=36
             /            \
        [0,2] 和=9      [3,5] 和=27
        /    \          /    \
    [0,1]   [2]=5   [3,4]   [5]=11
    / \              / \
  [0]=1 [1]=3     [3]=7 [4]=9
lazy[node]：这一整段还没下发给孩子的增量；访问孩子前先 pushDown
```

**关键代码**

```java
static class SumSegmentTree {
    private final int[] tree, lazy;
    private final int n;
    SumSegmentTree(int[] a) {
        n = a.length;
        tree = new int[4 * n]; lazy = new int[4 * n];
        build(1, 0, n - 1, a);
    }
    private void build(int node, int l, int r, int[] a) {
        if (l == r) { tree[node] = a[l]; return; }
        int mid = (l + r) / 2;
        build(2 * node, l, mid, a);
        build(2 * node + 1, mid + 1, r, a);
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }
    private void pushDown(int node, int l, int r) {
        if (lazy[node] != 0 && l < r) {
            int mid = (l + r) / 2, L = 2*node, R = 2*node+1;
            tree[L] += lazy[node] * (mid - l + 1); lazy[L] += lazy[node];
            tree[R] += lazy[node] * (r - mid); lazy[R] += lazy[node];
            lazy[node] = 0;
        }
    }
    // 省略：rangeAdd/querySum；MaxSegmentTree 存区间最大值
}
```

**复杂度**

| 操作 | 平均 / 最坏 | 说明 |
|---|---|---|
| 建树 build | O(n) | 每节点汇总一次 |
| 区间和查询 | O(log n) | 覆盖节点直接取，未覆盖下探 |
| 区间最大值查询 | O(log n) | 汇总改为取 max |
| 区间加（懒标记） | O(log n) | 标记先挂节点，下探时 pushDown |
| 空间 / 适用场景 | O(4n) | 区间统计 + 区间修改并存；最值、区间加、赋值 |

**易错点**

1. 线段树数组开 4n；递归分治节点数略大于 2n，开 2n 可能越界。
2. 懒标记是"还没下发给孩子的增量"；访问孩子前必须先 `pushDown`，否则孩子值是旧的。
3. `pushDown` 按孩子区间长度折算和：增量 = 增量值 × 孩子区间长度。
4. 区间闭 `[l,r]`；先判"整段被查询区间覆盖"，再下探。
5. 区间赋值（覆盖）和区间加的懒标记不能混用；本文件只做区间加。

**与 Java 的对应**

JDK 没有内置线段树；区间统计常手写。与树状数组相比：树状数组轻量常数小，但只适合单点改 + 前缀和/区间和；线段树能做最值、区间加、赋值，适用面广但代码更重。本文件结果与朴素前缀和、朴素遍历对拍一致。

**一句话口诀**

> 口诀：区间对半切，节点存汇总；修改先挂懒标记，下探之前先下发；查询修改对数级。

### DS-18 树状数组

> 对应代码：`lessons/11-data-structures/FenwickTreeDemo.java`

**为什么需要它**

前缀和查询 O(1) 但单点修改 O(n)；树状数组在"单点改 + 前缀和/区间和"这个最常见组合上把两边都做到 O(log n)，代码短、空间省，是求逆序对、单点累计计数的常用工具。它用隐含二叉树把数组分成若干幂次长度的小块。

**核心思想**

下标从 1 开始，核心 `lowbit(x) = x & -x`：取二进制最低位 1 对应的值（`lowbit(6)=2`、`lowbit(8)=8`）。单点修改 `add(i,delta)` 沿 `i += lowbit(i)` 向上更新覆盖块；前缀和 `prefix(i)` 沿 `i -= lowbit(i)` 向下累加小块。区间和 `[l,r] = prefix(r) - prefix(l-1)`。求逆序对：BIT 存值出现次数，统计比当前值小的已出现个数。

```text
bit 数组（1 基）覆盖关系：
下标:  1   2   3   4   5   6   7   8
lowbit:1   2   1   4   1   2   1   8
bit[8] 管 1..8；bit[4] 管 1..4；bit[6] 管 5..6
bit[2] 管 1..2；bit[5] 管 5；bit[7] 管 7
add(i,v)：   i += lowbit(i)  向上跳，更新覆盖块
prefix(i)：  i -= lowbit(i)  向下跳，累加小块
区间和：     prefix(r) - prefix(l-1)
```

**关键代码**

```java
static class FenwickTree {
    private final int[] bit;
    private final int n;
    FenwickTree(int n) { this.n = n; bit = new int[n + 1]; }
    private int lowbit(int x) { return x & -x; }

    // 单点修改：下标 i（1 基）增加 delta
    void add(int i, int delta) {
        while (i <= n) {
            bit[i] += delta;
            i += lowbit(i); // 向上更新覆盖 i 的块
        }
    }
    // 前缀和：1..i 的累加
    int prefix(int i) {
        int sum = 0;
        while (i > 0) {
            sum += bit[i];
            i -= lowbit(i); // 向下累加不重叠小块
        }
        return sum;
    }
    int rangeSum(int l, int r) { return prefix(r) - prefix(l - 1); }
}
// 逆序对：从右往左扫，inv += bit.prefix(v-1); bit.add(v, 1);
```

**复杂度**

| 操作 | 平均 / 最坏 | 说明 |
|---|---|---|
| 单点修改 add | O(log n) | `i += lowbit(i)` 向上更新 |
| 前缀和 prefix | O(log n) | `i -= lowbit(i)` 向下累加 |
| 区间和 rangeSum | O(log n) | `prefix(r) - prefix(l-1)` |
| 建树 build | O(n log n) | 逐个 add；也可 O(n) 直接构造 |
| 空间 / 适用场景 | O(n) | 单点改 + 前缀和、计数、求逆序对 |

**易错点**

1. `lowbit(x) = x & -x`：取最低位 1 的值，`lowbit(6)=2`、`lowbit(8)=8`。
2. 下标从 1 开始；0 基数组要 +1 映射，别把下标 0 传进去（会死循环）。
3. 区间和 `prefix(r) - prefix(l-1)`，`l-1` 别写成 `l`。
4. 求逆序对 BIT 存的是值出现次数，不是原数组值；统计比当前值小的已出现个数。
5. 树状数组擅长求和/计数；区间最值、赋值、区间加做不了，需要线段树。

**与 Java 的对应**

JDK 没有内置树状数组；它是手写结构。与线段树对比：代码短、空间小、常数低，但功能窄（可加可减统计量）；线段树功能全但代码更重。本文件前缀和/区间和与朴素前缀数组对拍，逆序对与朴素双重循环对拍一致（样例 8 个）。

**一句话口诀**

> 口诀：lowbit 取最低位 1，修改向上跳，前缀向下加；区间和两前缀相减，逆序对靠计数树状数组。

## 第二部分：算法（23 个主题）

> 本部分对应仓库 `lessons/12-algorithms/` 下的 23 个可运行 Demo。每个主题按统一七段展开：
> 为什么需要它 → 核心思想（配纯文本示意图）→ 关键代码 → 复杂度 → 易错点 → 与 Java 的对应 → 一句话口诀。
> 文中变量名、方法名与真实源码保持一致；复杂度中的 n 统一表示元素个数。

### ALG-1 冒泡排序

> 对应代码：`lessons/12-algorithms/BubbleSortDemo.java`

**为什么需要它**

冒泡排序是所有排序里最直观的一个，只用到"比较 + 相邻交换"两件事，没有额外空间，也没有复杂分治。它的价值不在性能，而在"看得见的排序过程"：理解了它，才能看懂为什么插入排序更快、为什么快排要分治。学习排序时先掌握冒泡，可以建立"每一轮把一个元素归位"的直觉。

**核心思想**

每一轮都在还没排好的区间里，相邻两两比较；只要左边比右边大就交换，于是较大的元素像气泡一样一步步"冒"到区间末尾。冒完一轮，尾部就多一个已经归位的元素；如果某一轮一次交换都没发生，说明数组已经有序，可以提前收工。

```text
初始：  5  1  4  2  8  3
第1轮： 1  4  2  5  3  8   ← 8 冒到末尾归位
第2轮： 1  2  4  3  5  8   ← 5 冒到倒数第二归位
第3轮： 1  2  3  4  5  8   ← 4 归位
第4轮： 1  2  3  4  5  8   ← 本轮零交换，提前退出
```

**关键代码**

```java
/** 优化版冒泡：本轮零交换 → 已经有序 → 立刻退出 */
static void bubbleSortOptimized(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        boolean swapped = false;              // 每轮都要重置标志位
        for (int j = 0; j < n - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = true;
            }
        }
        if (!swapped) {
            break;                            // 一次交换都没有，后面的轮次纯属浪费
        }
    }
}
// 省略：对拍校验（与 Arrays.sort 比对）、稳定性演示（带标签 Item）、性能计时
```

**复杂度**

| 版本 | 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|---|
| 基础冒泡 | O(n²) | O(n²) | O(n²) | O(1) | 是 | 教学演示 |
| 优化冒泡 | O(n) | O(n²) | O(n²) | O(1) | 是 | 数据量极小且近乎有序 |

**易错点**

1. 内层循环边界写成 `j < n` 会访问 `arr[j+1]` 越界，正确边界是 `j < n - 1 - i`，因为尾部已有 `i` 个元素归位。
2. 交换条件写成 `>=` 会破坏稳定性：相等的两个元素被交换后先后次序被打乱；必须用严格大于 `>`。
3. 优化版的"提前退出"标志 `swapped` 必须每轮重置为 `false`，漏重置会导致第一轮交换过之后，后面再也无法触发退出判断。
4. 冒泡永远是 O(n²) 级别的比较，n 到十万量级时基本不能用，不要拿它处理大数据。

**与 Java 的对应**

JDK 的 `Arrays.sort(int[])` 底层是双轴快排（Dual-Pivot Quicksort），时间 O(n log n)，比冒泡快几个数量级；`Arrays.sort(Object[])` 是 TimSort。冒泡在 JDK 排序源码里没有出现，只在小区间兜底或教学演示中有意义。本文件手写的优化版只做最朴素的相邻交换，没有快排那种分区优化。

**一句话口诀**

> 口诀：相邻比较、逆序就换；一轮冒出一个最大值；没换过说明已经有序，提前收工。

### ALG-2 选择排序

> 对应代码：`lessons/12-algorithms/SelectionSortDemo.java`

**为什么需要它**

冒泡和插入都靠相邻交换挪位置，比较多、交换也多；选择排序反过来——每轮先把最小元素的下标找出来，整轮最多只做一次交换，写操作最多 n-1 次。所以在"交换/写回很贵"的场景（比如元素是大对象、复制成本高）里，它反而有讨论价值。它和冒泡一样是教学用的 O(n²) 算法，核心是把"找极值 + 放到前面"的思路讲透。

**核心思想**

外层指针 `i` 指向待排区间的最前面；内层在 `[i+1, n)` 里扫描一遍，记下最小值的下标 `minIndex`，扫描完才把它和 `arr[i]` 交换。注意是"先记下标、最后交换"，而不是一看到更小的就立刻换。

```text
初始：  5  1  4  2  8  3
第1轮： 找最小 1（下标1）→ 换到前面：1  5  4  2  8  3
第2轮： 找最小 2（下标3）→ 换到位置1：1  2  4  5  8  3
第3轮： 找最小 3（下标5）→ 换到位置2：1  2  3  5  8  4
第4轮： 找最小 4（下标5）→ 换到位置3：1  2  3  4  8  5
第5轮： 找最小 5（下标5）→ 换到位置4：1  2  3  4  5  8
```

**关键代码**

```java
/** 选择排序：每轮找最小下标，最多一次交换 */
static void selectionSort(int[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        int minIndex = i;                              // 先假设待排区间第一个就是最小的
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[minIndex]) {
                minIndex = j;                          // 只记录下标，不立刻交换
            }
        }
        if (minIndex != i) {                           // 自己和自己交换属于多余写操作
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }
}
// 省略：与冒泡/插入的耗时对比、200 组随机数组对拍、不稳定证明（Item 标签）
```

**复杂度**

| 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|
| O(n²) | O(n²) | O(n²) | O(1) | 否（不稳定） | 数据量极小、每次交换成本很高时 |

**易错点**

1. 每轮要先记录最小值下标 `minIndex`，不能一看到更小的就立刻交换；立刻交换会产生大量无效写操作。
2. 内层循环必须从 `i+1` 开始，不要把已经排好的前缀再比一遍。
3. 找到 `minIndex` 后要判断 `minIndex != i` 再交换；自己和自己交换虽然不报错，但属于多余写操作。
4. 选择排序是不稳定的：跨位置交换会把相等元素的先后顺序打乱。例如 `2(a)、2(b)、1(c)`，第一轮把 1 换到最前面后，两个 2 的次序变成 `2(b)、2(a)`。

**与 Java 的对应**

JDK 的 `Arrays.sort(int[])` 是双轴快排，O(n log n)；选择排序在工程上几乎不直接使用。它和冒泡一样属于教学排序，只在"写回操作昂贵、比较便宜"的特定假设下才有意义。本文件对比了选择、冒泡、插入三者在同一随机数据上的耗时，插入通常最省（移动是连续写、缓存友好），选择交换次数最少但比较次数一点不少。

**一句话口诀**

> 口诀：每轮扫描找最小，记好下标再交换；比较很多交换少，相等顺序保不住。

### ALG-3 插入排序

> 对应代码：`lessons/12-algorithms/InsertionSortDemo.java`

**为什么需要它**

冒泡和选择都在"未排好的部分"里反复扫描；插入排序只维护一个"已经有序的前缀"，每次处理一个新元素。它在数据近乎有序时几乎是 O(n)，而且是稳定排序，所以常被用作高级排序（归并、快排、希尔）在小区间上的兜底子程序。理解摸扑克牌的模型，是理解"原地建有序序列"的起点。

**核心思想**

像摸扑克牌：手里左边一叠已经排好，新摸到的牌 `key` 从右往左找位置，比它大的牌依次右移一格，最后把 `key` 插进腾出的空位。已排好的前缀始终有序，新元素只在这叠前缀里找家。

```text
初始： 5  1  4  2  8  3
插1：  [1]  5  4  2  8  3   ← 1 把 5 挤右移
插4：  [1  5] 4  2  8  3   ← 4 把 5 挤右移
插2：  [1  2  5] 4  8  3   ← 2 把 5、4 依次挤右移
插8：  [1  2  4  5] 8  3
插3：  [1  2  3  4  5  8]
```

**关键代码**

```java
/** 普通插入排序 */
static void insertionSort(int[] arr) {
    for (int i = 1; i < arr.length; i++) {
        int key = arr[i];                       // 新摸到的牌
        int j = i - 1;
        // 把比 key 大的牌依次右移，给 key 腾出位置
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;                       // 插入到腾出的位置
    }
}

/** 二分插入：先用二分在有序前缀里找位置，再整体右移（比较变少，移动不变） */
static void binaryInsertionSort(int[] arr) {
    // 省略：二分在 [0, i) 找第一个 > key 的位置 left，再 System.arraycopy 整体右移
}
```

**复杂度**

| 版本 | 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|---|
| 普通插入 | O(n) | O(n²) | O(n²) | O(1) | 是 | 小数组、近乎有序 |
| 二分插入 | O(n log n) 比较 | O(n²) 移动 | O(n²) 移动 | O(1) | 是 | 比较昂贵、移动便宜 |

**易错点**

1. 内层 `while` 的条件是 `arr[j] > key`（严格大于），写成 `>=` 会把相等元素右移，破坏稳定性。
2. 移动元素时先把 `arr[j]` 后移到 `j+1`，最后再把 `key` 放进 `arr[j+1]`，顺序不能反。
3. `j` 越界风险：`while` 里必须先判断 `j >= 0`，再访问 `arr[j]`。
4. 二分插入找位置时，相等元素要排在原有元素右边，才能保住稳定性；二分只减少比较次数，元素移动次数一点没变，整体仍是 O(n²)。

**与 Java 的对应**

JDK 的 `Arrays.sort(int[])` 是双轴快排；但在归并排序、TimSort 等高级排序里，小区间（通常 32 以下）会退回插入排序，因为小数组上 O(n²) 常数小、缓存友好。本文件还实现了二分插入版，并用 `System.arraycopy` 做整体右移；普通插入在连续内存上的移动是顺序写，实测通常比冒泡快。

**一句话口诀**

> 口诀：左边有序右边摸，往左找到家再插；近乎有序特别快，相等不动才稳定。

### ALG-4 希尔排序

> 对应代码：`lessons/12-algorithms/ShellSortDemo.java`

**为什么需要它**

普通插入排序在逆序时要把元素一步步挪很远，效率低；希尔排序先让元素"跨大步"接近自己的位置，再用小步精修。它是第一批突破 O(n²) 思路的排序之一，理解它能看懂"先粗排、再细排"的工程思想：用一个增量序列把大问题切成多个小子问题，最后退化成插入排序收尾。

**核心思想**

先按一个较大的间隔 `gap` 把元素分组（下标相差 `gap` 的归为一组），组内做插入排序；`gap` 一步步减半，最后 `gap = 1` 时，数组已经大体有序，普通插入排序几乎顺手就能排完。

```text
n=9，初始 gap = 4：
  分组：(0,4,8) (1,5) (2,6) (3,7)，各组内插入排序
gap = 2：
  分组：(0,2,4,6,8) (1,3,5,7)，再次组内插入排序
gap = 1：
  整组就是普通插入排序，精修到完全有序
```

**关键代码**

```java
/** 希尔排序：gap 从 n/2 逐步减半到 1，组内插入排序 */
static void shellSort(int[] arr) {
    int n = arr.length;
    for (int gap = n / 2; gap > 0; gap /= 2) {
        for (int i = gap; i < n; i++) {
            int key = arr[i];
            int j = i;
            // 跳跃步长是 gap，判断 j >= gap 而不是 j >= 0，防止 j-gap 越界
            while (j >= gap && arr[j - gap] > key) {
                arr[j] = arr[j - gap];
                j -= gap;
            }
            arr[j] = key;
        }
    }
}
// 省略：稳定性随机探测、与普通插入排序结果一致性校验、性能实测
```

**复杂度**

| 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|
| O(n log n) | 约 O(n^1.3) | O(n²) | O(1) | 否（不稳定） | 教学与历史资料，工程基本被快排/归并取代 |

**易错点**

1. 分组插入时内层下标跳跃步长是 `gap`，判断 `j >= gap` 而不是 `j >= 0`，否则 `j-gap` 会越界。
2. `gap` 必须最后降到 1；只分组不做 `gap=1` 的精修，数组不会完全有序。
3. 复杂度强依赖增量序列：这里用最简单的 `gap = n/2` 逐步减半，最坏仍可能退化成 O(n²)；增量序列选得不好退化会很明显。
4. 希尔排序不稳定：元素按 `gap` 跨组移动，相等元素可能被交换到彼此后面。

**与 Java 的对应**

JDK 没有直接提供希尔排序；`Arrays.sort(int[])` 用双轴快排。希尔排序今天主要出现在教学和历史资料里，工程实现基本被快排、归并、TimSort 取代。本文件用 300 组带标签随机数据实测，观察到相等元素次序被打乱，用数据证明它不稳定。

**一句话口诀**

> 口诀：大步分组先粗排，间隔减半再细排；间隔为一退回插入，复杂度看增量序列。

### ALG-5 归并排序

> 对应代码：`lessons/12-algorithms/MergeSortDemo.java`

**为什么需要它**

冒泡、选择、插入都是 O(n²)，碰上上万数据就吃力；归并是最早稳定达到 O(n log n) 的排序之一，而且是稳定排序。它的代价是需要一块 O(n) 的临时数组。理解归并也是理解"分治"（拆开 → 各自解决 → 合并）的最好入口，后面的分治题大多沿用这套三步走。

**核心思想**

先把数组对半拆到最小（区间只剩 0 或 1 个元素，天然有序），再把两个已经有序的小段"合并"成一个有序的大段：双指针各看一段头部，谁小取谁；相等时优先取左半段，保证稳定。

```text
拆：[5 1 4 2 8 3 7 6]
    ├ [5 1 4 2]        ├ [8 3 7 6]
    │  ├ [5 1] ├ [4 2] │  ├ [8 3] ├ [7 6]
合：[1 5] [2 4] → [1 2 4 5]   [3 8] [6 7] → [3 6 7 8]
    最终合并：[1 2 3 4 5 6 7 8]
```

**关键代码**

```java
/** 入口：申请一块临时数组，避免递归里反复 new */
static void mergeSort(int[] arr) {
    if (arr.length <= 1) return;
    int[] temp = new int[arr.length];
    mergeSort(arr, temp, 0, arr.length - 1);
}

private static void mergeSort(int[] arr, int[] temp, int left, int right) {
    if (left >= right) return;                 // 区间只剩 0 或 1 个元素，天然有序
    int mid = left + (right - left) / 2;       // 防溢出的中点写法
    mergeSort(arr, temp, left, mid);           // 排左半段
    mergeSort(arr, temp, mid + 1, right);      // 排右半段
    merge(arr, temp, left, mid, right);        // 合并两段
}

/** 把 [left, mid] 与 [mid+1, right] 两个有序段合并回 arr */
private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
    // 省略：arraycopy 拷入 temp；i=left、j=mid+1、k=left；
    // 相等优先取左半段（temp[i] <= temp[j]）；两段剩余元素分别拷回
}
```

**复杂度**

| 版本 | 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|---|
| 自顶向下 | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 | 要求稳定、数据量大 |
| 自底向上 | O(n log n) | O(n log n) | O(n log n) | O(n) | 是 | 避免递归栈深 |

**易错点**

1. 合并时两段都要防越界：`i` 不能超过 `mid`，`j` 不能超过 `right`。
2. 相等元素必须优先取左半段（用 `<=` 而不是 `<`），否则会破坏稳定性。
3. 合并后剩余元素要分别拷贝完，不能只拷其中一段；左半剩余和右半剩余各有一段收尾循环。
4. 递归终止条件是 `left >= right`（区间只剩 0 或 1 个元素）；中点用 `left + (right-left)/2` 防止两个大数相加溢出。

**与 Java 的对应**

对象排序 `Arrays.sort(Object[])` 底层是 TimSort（归并 + 插入的混合）；本文件手写的是纯归并，思路相同但没有 TimSort 的小区间优化与原地合并技巧。文件还实现了自底向上迭代版（按宽度 1、2、4、8… 逐层合并），两种写法结果一致。归并也是求逆序对的经典载体：合并时右半元素先于左半取出，即构成一个逆序对。

**一句话口诀**

> 口诀：一拆两半各自排，再比头部并起来；相等先取左边段，稳是稳来费空间。

### ALG-6 快速排序

> 对应代码：`lessons/12-algorithms/QuickSortDemo.java`

**为什么需要它**

归并虽然稳定 O(n log n)，但要 O(n) 额外空间；快排原地分区、常数因子小，平均 O(n log n)，是大多数语言内置排序的主力思路。理解分区、基准选择和退化场景，是面试和读源码都绕不开的内容。快排还延伸出三路划分处理大量重复元素的变体。

**核心思想**

选一个基准 `pivot`，把比它小的甩到左边、比它大的甩到右边，基准归位后，左右两段结构相同，递归重复。文件里讲了三种分区：Lomuto（单边扫描，基准最后换到分界点）、Hoare（双指针对撞，分界点不一定是基准本身）、三路快排（小于/等于/大于基准三段）。

```text
区间 [5 1 4 2 8 3 7 6]，随机选基准
分区后：[小堆] | pivot | [大堆]
递归小堆、递归大堆，直到区间长度 ≤ 1

三路划分示意：
  [ < pivot ] [ == pivot ] [ > pivot ]
     lt~i-1      i~gt      gt+1~hi
```

**关键代码**

```java
// Lomuto 分区：随机挑一个基准换到 hi，扫一遍后把基准归位到分界点 i
private static int partitionLomuto(int[] arr, int lo, int hi) {
    int pivotIndex = lo + RANDOM.nextInt(hi - lo + 1);   // 随机基准，防有序数组退化
    swap(arr, pivotIndex, hi);
    int pivot = arr[hi];
    int i = lo;                                          // 小于 pivot 区域的下一个放置位置
    for (int j = lo; j < hi; j++) {
        if (arr[j] < pivot) {
            swap(arr, i, j);
            i++;
        }
    }
    swap(arr, i, hi);                                   // 基准归位到分界点 i
    return i;
}
// 省略：quickSortLomuto 递归、Hoare 对撞分区、三路快排 quickSortThreeWay、DepthTracker 栈深追踪
```

**复杂度**

| 版本 | 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|---|
| 普通快排 | O(n log n) | O(n log n) | O(n²) | O(log n) 栈 | 否 | 通用原地排序 |
| 随机化基准 | O(n log n) | O(n log n) | 概率上极难 O(n²) | O(log n) 栈 | 否 | 工程常用 |
| 三路快排 | O(n log n) | O(n log n) | O(n²) | O(log n) 栈 | 否 | 大量重复元素 |

**易错点**

1. 固定取端点当基准，遇到有序/逆序数组会退化成 O(n²)；必须随机化基准（文件用固定种子的 `RANDOM` 保证可复现）。
2. Lomuto 分区最后要把基准换到分界点 `i`；Hoare 分区返回的是 `j`，递归区间是 `[lo, j]` 与 `[j+1, hi]`，两种写法的边界不能混用。
3. 三路快排把等于基准的元素单独划成中间段，避免大量重复元素时反复比较相等值；大于段换过来的元素还要再判断，`i` 不能动。
4. 快排不稳定：分区交换会把相等元素的先后顺序打乱；递归深度平均 O(log n)，最坏 O(n)，会占用调用栈。

**与 Java 的对应**

`Arrays.sort(int[])` 是双轴快排（Dual-Pivot Quicksort），比单轴快排分区更细，且对小数组插入排序兜底；本文件手写单轴 Lomuto/Hoare/三路，思路同源但常数因子更大。文件用 `DepthTracker` 实测最大递归深度约 O(log n)，没有退化成 O(n) 链表式递归。

**一句话口诀**

> 口诀：选个基准分两边，小左大右递归干；随机选枢防退化，重复元素走三路。

### ALG-7 堆排序

> 对应代码：`lessons/12-algorithms/HeapSortDemo.java`

**为什么需要它**

归并快但要 O(n) 临时空间，快排平均快但最坏会退化；堆排序把这两点都兼顾了——O(n log n) 时间、O(1) 额外空间，且不会出现快排那样的最坏 O(n²)。它也是 TopK、优先队列、定时器等问题的底层思路，理解堆的下沉调整是读 `PriorityQueue` 源码的基础。

**核心思想**

先把数组原地建成大顶堆（堆顶永远是当前最大值），再反复把堆顶换到末尾、缩小堆范围并下沉修复。建堆从最后一个非叶子节点向前逐个下沉；每次取出堆顶 O(log n)，共取 n 次。

```text
0 下标堆：父 i，左孩子 2i+1，右孩子 2i+2
建堆：从 n/2-1 向前逐个 siftDown
  堆：        [8]
            /     \
          5         7
        /   \     /   \
       1     4   6     3

排序：交换堆顶与末尾 → 8 归位；堆范围减一，对新堆顶 siftDown 修复
```

**关键代码**

```java
/** 堆排序：原地建堆 + 反复交换堆顶并下沉 */
static void heapSort(int[] arr) {
    int n = arr.length;
    if (n <= 1) return;
    // 从最后一个非叶子节点向前逐个下沉，建成大顶堆
    for (int i = n / 2 - 1; i >= 0; i--) {
        siftDown(arr, i, n);
    }
    // 堆顶（最大值）换到末尾，堆范围减一，再下沉修复堆
    for (int end = n - 1; end > 0; end--) {
        swap(arr, 0, end);
        siftDown(arr, 0, end);
    }
}

/** 下沉：把下标 i 的元素向下调整到合适位置，heapSize 是当前堆的有效长度 */
private static void siftDown(int[] arr, int i, int heapSize) {
    // 省略：left=2i+1、right=2i+2；先判孩子下标 < heapSize；找最大孩子交换，继续向下
}
```

**复杂度**

| 操作 | 最好时间 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|---|
| 建堆 | O(n) | O(n) | O(n) | O(1) | —— | 一次性整理成堆 |
| 堆排序 | O(n log n) | O(n log n) | O(n log n) | O(1) | 否（不稳定） | 要求 O(1) 空间、无最坏退化 |

**易错点**

1. 建堆要从最后一个非叶子节点（下标 `n/2-1`）往前下沉；从 0 开始下沉会漏掉下层节点。
2. 下沉时左右孩子下标是 `2i+1`、`2i+2`，且必须先判断孩子下标 `< heapSize` 再访问，否则越界。
3. 每次把堆顶换到末尾后，堆范围要减一；末尾已归位的元素不能再参与下沉。
4. 堆排序不稳定：堆顶与末尾元素跨距离交换，相等元素可能被打乱次序。建堆之所以是 O(n) 而不是 O(n log n)，是因为越靠下的节点下沉层数越少，分摊后总和线性。

**与 Java 的对应**

`java.util.PriorityQueue` 是小顶堆（堆顶最小），本文件用大顶堆做升序排序；`Arrays.sort` 底层不是堆排序。堆的思想大量出现在优先队列、TopK 与定时器实现里。文件实测观察到堆排序额外空间 O(1)，但下沉是跳跃式访问、缓存不连续，常数因子通常不如快排。

**一句话口诀**

> 口诀：原地建成大顶堆，堆顶最大换末尾；缩小堆来再下沉，对数时间常量空间。

### ALG-8 计数排序

> 对应代码：`lessons/12-algorithms/CountingSortDemo.java`

**为什么需要它**

比较类排序有 O(n log n) 的理论下界；计数排序绕过比较，在"值域小、取值离散"的场景（考试分数、年龄、学号尾数）能做到 O(n + k)。它不两两比较，而是开一个计数桶记录每个值出现几次，再按值从小到大依次放回。它也是基数排序、桶排序的基础零件。

**核心思想**

先求最小值 `min`，用 `x - min` 做偏移保证下标非负；统计每个值出现次数，再做前缀和让 `count[i]` 表示"值不超过 i+min 的元素总数"；倒序遍历原数组，按前缀和定位到 `output`，保证相等元素相对顺序不变（稳定）。

```text
原数组：4 2 2 8 3 3 1
计数（偏移 0）：        值 1 2 3 4 8
                       次数 1 2 2 1 1
前缀和 count：          1 3 5 6 7
倒序回填：相等元素按原先后顺序落位 → 1 2 2 3 3 4 8
```

**关键代码**

```java
/** 稳定计数排序（偏移 + 前缀和 + 倒序回填） */
static void countingSort(int[] arr) {
    if (arr.length <= 1) return;
    // 1. 求值域最小值、最大值，确定计数数组长度
    int min = arr[0], max = arr[0];
    for (int x : arr) { min = Math.min(min, x); max = Math.max(max, x); }
    int range = max - min + 1;
    int[] count = new int[range];
    // 2. 统计每个值出现次数（x - min 做偏移，保证下标非负）
    for (int x : arr) count[x - min]++;
    // 3. 前缀和：count[i] 变成"值不超过 i+min 的元素总数"
    for (int i = 1; i < range; i++) count[i] += count[i - 1];
    // 4. 倒序遍历原数组，按前缀和定位到 output，保证相等元素相对顺序不变
    int[] output = new int[arr.length];
    for (int i = arr.length - 1; i >= 0; i--) {
        int idx = arr[i] - min;
        output[count[idx] - 1] = arr[i];
        count[idx]--;
    }
    System.arraycopy(output, 0, arr, 0, arr.length);
}
```

**复杂度**

| 版本 | 时间 | 额外空间 | 稳定性 | 适用条件 |
|---|---|---|---|---|
| 基础计数 | O(n + k) | O(k) | 否（直接回填） | 值域小、不需要保序 |
| 前缀和稳定版 | O(n + k) | O(n + k) | 是 | 值域小、需要保序 |

**易错点**

1. 有负数不能直接拿值当下标——要先求最小值 `min`，用 `x - min` 做偏移。
2. 想要稳定必须"前缀和定位 + 倒序遍历原数组"；正序随意回填会打乱相等元素次序。
3. 前缀和 `count[i]` 表示"<= i 的元素个数"，放元素时下标是 `count[idx]-1`，放完 `count[idx]--`。
4. 值域跨度很大（如 0 与 10 亿各一个）时，计数数组直接爆内存，不能用计数排序；k 接近甚至远大于 n 时这套方法不划算。

**与 Java 的对应**

`Arrays.sort` 是比较排序，不会在底层调用计数排序；JDK 没有公开的计数排序工具类。但在字符串排序、基数排序等场景内部会用到"计数 + 前缀和"这套定位思路。文件用 200 组窄值域随机数组（-10 ~ 10）与 `Arrays.sort` 对拍，并实测窄值域下计数排序的线性优势。

**一句话口诀**

> 口诀：数清每个值几次，前缀和定位置；倒序回填保稳定，值域小才占便宜。

### ALG-9 基数排序

> 对应代码：`lessons/12-algorithms/RadixSortDemo.java`

**为什么需要它**

计数排序只能处理"值域小"的整数；基数排序把大整数拆成一位一位的小数字（0~9），每一位都退化成计数排序，于是能排较大的整数。它是 O(d·(n+k)) 的线性排序，d 是位数、k 是每一位的基数（十进制 k=10），常见于字符串排序、大批量整数排序与外部排序场景。

**核心思想**

LSD（低位优先）：从个位开始，每一位单独做一次稳定计数排序；一位位排上去，最高位排完整体有序。稳定性是前提——只要每一趟计数排序是稳定的，低位已排好的次序才不会被高位打乱。负数先用最小值整体偏移变成非负，排完再减回去。

```text
数组：329 457 657 839 436 720 355
按个位(exp=1)：720 355 436 457 657 329 839
按十位(exp=10)：720 329 436 355 457 657 839
按百位(exp=100)：329 355 436 457 657 720 839  ← 整体有序
```

**关键代码**

```java
/** 入口：先偏移消去负数，再做 LSD 基数排序，最后还原 */
static void radixSort(int[] arr) {
    if (arr.length <= 1) return;
    int min = arr[0];
    for (int x : arr) min = Math.min(min, x);
    // 整体减去 min，平移成全非负；偏移不改变元素之间的大小关系
    int[] shifted = new int[arr.length];
    for (int i = 0; i < arr.length; i++) shifted[i] = arr[i] - min;
    radixSortNonNegative(shifted);
    for (int i = 0; i < arr.length; i++) arr[i] = shifted[i] + min;
}

/** 按某一位做一趟稳定计数排序；取第 exp 位：(x / exp) % 10，exp 按 1、10、100… 翻倍 */
private static void countingPassByDigit(int[] arr, int exp, boolean unstable) {
    // 省略：count[10] 计数 → 前缀和 → 稳定版倒序遍历回填；unstable=true 故意反序制造反例
}
```

**复杂度**

| 版本 | 时间 | 额外空间 | 稳定性 | 说明 |
|---|---|---|---|---|
| LSD 低位优先 | O(d·(n+k)) | O(n+k) | 是（依赖每趟稳定） | 从个位往高位排，d=位数，k=基数 |

**易错点**

1. 每一趟按位排序必须稳定，否则高位排序会把同高位元素已有的低位次序冲乱；文件用 `{19,12,15}` 构造了不稳定反例。
2. LSD 是低位优先，先排个位再排十位百位；写成 MSD 高位优先则要递归处理每个桶，流程不同。
3. 取第 `exp` 位数字：`(x / exp) % 10`；`exp` 按 1、10、100… 翻倍，`max / exp > 0` 控制趟数。
4. 负数不能直接按十进制位排：先整体加偏移量变成非负，排完再减回去；位数多、值域宽时线性优势会被多趟搬移的常数吃掉。

**与 Java 的对应**

`Arrays.sort` 不使用基数排序；基数排序常见于字符串排序、大批量整数排序与外部排序。文件里的稳定版与不稳定版对照证明：稳定性是基数排序正确性的前提，而不是可选优化。

**一句话口诀**

> 口诀：低位优先逐趟稳，每趟计数按位分；稳定丢了位序乱，偏移先把负数平。

### ALG-10 桶排序

> 对应代码：`lessons/12-algorithms/BucketSortDemo.java`

**为什么需要它**

当数据在值域上均匀分布时，每个桶里元素很少，桶内排序很快，整体接近 O(n)。它和计数排序、基数排序同属"分布排序"，是理解"先粗分组、再细处理"思路的一环。典型场景是 0~1 之间的浮点均匀分布、海量均匀数据的分片处理。

**核心思想**

按值域把元素映射到若干个桶里（桶数常取 √n），每个桶内部单独排序（一般用插入排序，小数组常数小），最后按桶顺序依次拼回来。均匀分布时桶内元素少；最坏情况是所有元素挤进同一个桶，桶内排序退化成 O(n²)。

```text
值域 [0,1)，桶数 √n：
  桶0 [0.0,0.2)  → 0.17 0.24
  桶1 [0.2,0.4)  → 0.33
  桶2 [0.4,0.6)  → 0.42 0.58
  桶3 [0.6,0.8)  → 0.65 0.71
  桶4 [0.8,1.0)  → 0.89
桶内插入排序后，按桶顺序拼回 → 0.17 0.24 0.33 0.42 0.58 0.65 0.71 0.89
```

**关键代码**

```java
/** 浮点桶排序：值域 [0,1]，桶数取 √n */
static void bucketSortDouble(double[] arr) {
    int n = arr.length;
    if (n <= 1) return;
    int bucketCount = Math.max(1, (int) Math.sqrt(n));
    List<List<Double>> buckets = new ArrayList<>();
    for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());

    for (double x : arr) {
        int idx = (int) (x * bucketCount);
        if (idx == bucketCount) idx = bucketCount - 1;   // x 恰为 1.0 的边界保护
        buckets.get(idx).add(x);
    }
    int k = 0;
    for (List<Double> bucket : buckets) {
        insertionSortDouble(bucket);          // 桶内用插入排序（稳定）
        for (double x : bucket) arr[k++] = x;
    }
}
// 省略：整数版 bucketSortInt（先求 min/max 偏移映射）、桶内插入排序、最坏退化演示
```

**复杂度**

| 分布情况 | 平均时间 | 最坏时间 | 额外空间 | 稳定性 | 适用场景 |
|---|---|---|---|---|---|
| 均匀分布 | O(n + m) | O(n²) | O(n + m) | 视桶内排序而定 | 0~1 浮点、均匀分片 |

**易错点**

1. 元素恰好落在值域上界（如 1.0）时，桶下标可能等于桶数，要 clamp 到最后一桶。
2. 所有元素相等或高度集中时，桶排序退化成桶内插入排序，别以为它一定线性。
3. 桶内用稳定排序、按原顺序追加，桶排序才稳定；桶内换成快排则不稳定。
4. 桶数不是越多越好：桶太多会有建桶和遍历的开销，太少又挤成大桶；整数映射时 `(x-min)*bucketCount/range` 要注意乘法先转 `long` 防溢出。

**与 Java 的对应**

`Arrays.sort` 不使用桶排序；桶排序思路常见于海量均匀数据的分片处理、外部排序与哈希分桶。文件实测了均匀 [0,1) 数据上桶排序与 `Arrays.sort` 的对比，并演示了全部相等、窄值域集中两种退化情形，结果依然正确但失去线性优势。

**一句话口诀**

> 口诀：按值域来分小桶，桶内插入排整齐；均匀分布接近线，挤成一桶退平方。

### ALG-11 二分查找三套模板

> 对应代码：`lessons/12-algorithms/BinarySearchTemplateDemo.java`

**为什么需要它**

普通二分只会"找到就返回"，但工程里更常问"第一个 >= x 的位置""最后一个 <= x 的位置""最小的满足条件的值"。这些题都靠二分模板，区间定义（闭还是开）一旦写错就会漏元素或死循环。本主题把精确查找、左边界、右边界、插入位置、二分答案五套写法统一在左闭右开区间下模板化记忆。

**核心思想**

有序数组上每次砍掉一半。区间统一约定为左闭右开 `[left, right)`：`right` 初值是 `n`（不是 `n-1`），收缩时 `right = mid`（不是 `mid-1`）。`mid` 用 `left + (right-left)/2` 防溢出。左边界 `lowerBound` 找第一个 `>= target`，右边界 `upperBound` 找第一个 `> target`，两者只差一个等号。

```text
数组：1 2 2 2 2 3 5，target = 2
      ↑ lowerBound=1（第一个 >=2）
                ↑ upperBound=5（第一个 >2）
等于 2 的区间是 [1, 5)

二分答案：predicate(x) 单调，在答案区间上二分找最小成立点
```

**关键代码**

```java
/** 模板二：lowerBound，返回第一个 >= target 的下标；若全小于 target，返回 n */
static int lowerBound(int[] arr, int target) {
    int left = 0;
    int right = arr.length;                 // 右开：right 初始化为 n
    while (left < right) {
        int mid = left + (right - left) / 2; // 防溢出写法
        if (arr[mid] < target) {
            left = mid + 1;                  // 严格小于，答案不可能在 mid 及左边
        } else {
            right = mid;                     // >= target，答案可能就是 mid
        }
    }
    return left;
}

/** 模板三：upperBound，返回第一个 > target 的下标；相等也归到左边 */
static int upperBound(int[] arr, int target) {
    // 省略：与 lowerBound 同结构，条件改为 arr[mid] <= target 时 left = mid + 1
}
// 省略：exactSearch 精确查找返回 -1、insertionPosition 等价 lowerBound、minSqrtCeil 二分答案
```

**复杂度**

| 模板 | 区间约定 | 返回值 | 时间 |
|---|---|---|---|
| 精确查找 | `[left, right)` | 找到返回下标，找不到 -1 | O(log n) |
| lowerBound 左边界 | `[left, right)` | 第一个 `>= target` 的下标 | O(log n) |
| upperBound 右边界 | `[left, right)` | 第一个 `> target` 的下标 | O(log n) |
| 插入位置 | 同 lowerBound | 元素应插入的下标 | O(log n) |
| 二分答案 | 答案区间 | 最小的满足条件 predicate 的值 | O(log 答案范围) |

**易错点**

1. 区间必须前后一致：本主题统一左闭右开 `[left, right)`，所以 `right` 初值是 `n`，收缩时 `right = mid`；混用闭区间写法是二分最常见的死循环/漏元素原因。
2. `mid` 写成 `(left + right) / 2` 在两数都接近 int 上限时会溢出，标准写法 `left + (right-left)/2`。
3. `lowerBound` 用 `arr[mid] < target` 才右移；`upperBound` 用 `arr[mid] <= target` 才右移，差一个等号。
4. `Arrays.binarySearch` 遇到重复元素只返回"其中任意一个"下标，要最左/最右必须自己用 lower/upperBound；二分的前提是数组有序，无序数组上结果不可靠。

**与 Java 的对应**

`Arrays.binarySearch(int[], key)` 即精确查找，找到返回任意一个匹配下标，找不到返回 `-(插入点)-1`，可反推插入点；JDK 没有直接提供 `lowerBound` / `upperBound`，需要按模板手写。文件用线性扫描做标准答案对拍 200 组随机有序数组，并实测二分约 15 次比较 vs 线性最坏 O(n)。

**一句话口诀**

> 口诀：左闭右开记心间，mid 防溢写中间；小于右移否则左，左界右界看等号；找不到时回 -1，答案二分找最小。

### ALG-12 递归与分治

> 对应代码：`lessons/12-algorithms/RecursionDivideConquerDemo.java`

**为什么需要它**

很多问题天生就是"自己包含自己"的结构：汉诺塔、二叉树、快速幂、排序分治。用递归写代码短、思路清晰；用循环硬写反而要自己维护一个栈，容易错。分治则是"递归思想 + 合并步骤"的工程化版本：分成两半 → 各算各的 → 合并答案。掌握递归三要素，是后面理解树、图、DP 的基础。

**核心思想**

递归三要素：① 终止条件（不能再拆的最小问题）；② 递推关系（当前层把问题交给更小的一层）；③ 回归（下一层算完带回来，当前层再加工）。分治三步走：分（切成互不重叠的两半）、治（左右各自求解）、合（两半答案合并）。朴素递归会重复计算子问题，记忆化把算过的子问题记进 `memo`。

```text
朴素斐波那契 fib(4) 的递归树（重复子问题爆炸）：
                 fib(4)
                /      \
            fib(3)      fib(2)
           /     \      /    \
       fib(2)  fib(1) fib(1) fib(0)
      /    \
  fib(1)  fib(0)
同一个 fib(2) 被反复算；记忆化后每个子问题只算一次。

分治求最大值：[a b c d] → max(max(a,b), max(c,d))
```

**关键代码**

```java
/** 阶乘递归：三要素对照 —— 终止 n<=1 返回 1；递推 n*fact(n-1)；回归后乘 n */
static int factorial(int n) {
    if (n <= 1) return 1;                    // ① 终止条件
    return n * factorial(n - 1);              // ② 递推 + ③ 回归
}

/** 记忆化斐波那契：memo[k] != -1 说明算过，直接返回 */
static long memoFib(int n, long[] memo) {
    if (n <= 1) return n;
    if (memo[n] != -1) return memo[n];       // 命中缓存，不再往下拆
    memo[n] = memoFib(n - 1, memo) + memoFib(n - 2, memo);
    return memo[n];
}

/** 快速幂：指数每次折半，O(log n) 次乘法 */
static long fastPow(long base, long exp) {
    if (exp == 0) return 1;
    long half = fastPow(base, exp / 2);
    long result = half * half;
    if (exp % 2 == 1) result *= base;        // 指数奇数，再补一个 base
    return result;
}
// 省略：汉诺塔 hanoi、分治求最大值 divideMax、朴素斐波那契 naiveFib、迭代对照 iterativeFib
```

**复杂度**

| 算法 | 时间 | 栈空间 | 说明 |
|---|---|---|---|
| 阶乘递归 | O(n) | O(n) | 每层压一个栈帧，深度 = n |
| 朴素斐波那契 | O(2^n) | O(n) | 大量重复子问题，指数爆炸 |
| 记忆化斐波那契 | O(n) | O(n) | 每个子问题只算一次 |
| 汉诺塔 | O(2^n) | O(n) | 移动步数 = 2^n - 1 |
| 分治求最大值 | O(n) | O(log n) | 对半切，递归树高度 log n |
| 快速幂 | O(log n) | O(log n) | 指数按二进制折半下降 |

**易错点**

1. 忘了写终止条件，或终止条件永远到不了 → 一直递归到 JVM 栈溢出 `StackOverflowError`。
2. 递归深度太大（几万层）时，即使算法本身正确也会栈溢出；这时要改成迭代或减小深度。
3. 记忆化的占位值不能和合法结果冲突：斐波那契结果可能是 0、1，所以 `memo` 用 -1 而不是 0。
4. 分治切区间时左右两半必须无缝衔接：`mid` 归左、`mid+1` 归右，写成 `[l,mid]` 和 `[l,mid]` 就会漏元素或重复算。
5. 阶乘、斐波那契增长极快，int 很快溢出；n 稍大就要用 `long`，再大要换 `BigInteger`。

**与 Java 的对应**

JDK 里 `Arrays.sort(int[])` 底层是双轴快排（递归 + 分治），`Collections.sort` 底层是归并（分治合并）；递归本身没有现成类，快速幂也没有公开的 JDK 方法，但 `BigInteger.modPow` 内部就是基于模幂分治实现的。文件用迭代滚动两变量的 `iterativeFib` 当对照标准答案，验证朴素/记忆化两种递归结果一致。

**一句话口诀**

> 口诀：先想出口再写递推；小问题算完带回结果；重复子问题记下来；分治三步走：分、治、合。

### ALG-13 贪心

> 对应代码：`lessons/12-algorithms/GreedyDemo.java`

**为什么需要它**

贪心思路最朴素、代码最短、通常最快（往往省掉 DP 的嵌套循环）。但它有严格前提：问题必须具备"贪心选择性质"（局部最优能推出全局最优）和"最优子结构"。前提不满足时，贪心得到的就是错误答案。文件用一个具体背包反例把"贪心失效"当场算给你看，避免在不该用贪心的题上硬套。

**核心思想**

每一步都选"当下看起来最好"的那个选择，不回头、不后悔，指望一路局部最优拼出全局最优。典型成立场景：规范币值找零钱、区间调度（按右端点排序）、跳跃游戏（维护能到达的最远距离）。典型失效场景：0/1 背包按性价比贪心会错，必须 DP 或枚举。

```text
区间调度：按右端点升序，越早结束越留空间
  区间：[1,3] [2,4] [3,5] [0,6] [4,7] [5,9] [6,10] [8,11]
  选中：[1,3] → [3,5] → [5,9]?  （相接视为不重叠，用 >= 判断）

0/1 背包反例（容量 8）：
  A(2,3) 性价比1.5  B(3,4) 1.33  C(4,5) 1.25  D(5,6) 1.20
  贪心：先 A 再 B，剩 3 装不下 → 价值 7
  正解：B + D，重量 3+5=8，价值 10
```

**关键代码**

```java
/** 区间调度贪心：按右端点升序；起点 >= 上一个终点就选它（相接视为不重叠） */
static int greedyMaxNonOverlap(int[][] intervals) {
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
    int count = 0;
    int lastEnd = -1;                       // 上一个选中区间的终点
    for (int[] iv : intervals) {
        if (iv[0] >= lastEnd) {
            count++;
            lastEnd = iv[1];
        }
    }
    return count;
}

/** 跳跃游戏：维护全程能到达的最远下标 furthest */
static boolean greedyCanReach(int[] nums) {
    int furthest = 0;
    for (int i = 0; i < nums.length; i++) {
        if (i > furthest) return false;     // 当前位置都到不了，后面更不可能
        furthest = Math.max(furthest, i + nums[i]);
        if (furthest >= nums.length - 1) return true;
    }
    return true;
}
// 省略：贪心找零钱 greedyCoinChange、0/1 背包贪心 greedyKnapsack、DP/枚举对照
```

**复杂度**

| 问题 | 贪心时间 | 说明 |
|---|---|---|
| 找零钱 | O(k) | k = 币值种数；只对规范币值最优 |
| 区间调度 | O(n log n) | 排序占大头，扫描 O(n) |
| 跳跃游戏 | O(n) | 维护一个能跳到的最远距离 |
| 0/1 背包 | 贪心不可靠 | 按性价比贪心会错，必须 DP 或枚举 |

**易错点**

1. 不判断问题能不能贪心就直接上贪心：0/1 背包、任务调度带权重等场景贪心会给出次优甚至错误答案。
2. 区间调度按"左端点"排序是经典错误；正确做法是按"右端点"升序，越早结束越留出空间。
3. 找零钱贪心只对规范币值成立（人民币、美分这种）；币值设计不规范时（如 `{1,3,4}` 凑 6），贪心得不出最少张数。
4. 比较器写反（降序/升序）会让贪心选择的方向完全相反，结果看似合理其实错；跳跃游戏要维护"全程能到达的最远下标"，不能只看当前这一步能不能跳。

**与 Java 的对应**

JDK 没有现成的"贪心求解器"；排序底座是 `Arrays.sort` / `List.sort`，按权重逐个挑选可以用 `java.util.PriorityQueue`（堆）。JDK 自带的 Knapsack 相关工具并不存在，背包问题要自己写 DP。文件用 DP 完全背包思路的 `bruteMinCoins`、枚举 2^n 子集的 `bruteKnapsack` 做对照，证明贪心在失效场景确实更差。

**一句话口诀**

> 口诀：先证可贪心，再挑当下最优；区间先比右端点；背包贪心会翻车，老老实实 DP。

### ALG-14 动态规划

> 对应代码：`lessons/12-algorithms/DynamicProgrammingDemo.java`

**为什么需要它**

递归 + 记忆化是 DP 的雏形，但真正的 DP 强调"先定义状态，再写转移"。背包、最长子序列、编辑距离、股票买卖这些面试高频题，暴力枚举要么指数爆炸，要么根本写不出来；DP 用一张表把复杂度压到 O(n²) 甚至 O(n log n)。掌握四步法，是把"重复子问题"系统化解决的关键。

**核心思想**

四步法：① 状态定义（`dp[i][j]` 到底表示什么含义）；② 转移方程（由前面的状态推出来）；③ 初始化（划清边界，不可达状态给 INF）；④ 遍历顺序（0/1 背包倒序、完全背包正序）。文件覆盖 0/1 背包（二维 → 一维倒序）、完全背包（一维正序）、LIS（O(n²) 与贪心+二分 O(n log n)）、编辑距离、股票系列。

```text
0/1 背包二维表 dp[i][c]：前 i 件物品、容量 c 的最大价值
        c=0  1  2  3  4  5  6  7  8
  i=0    0  0  0  0  0  0  0  0  0
  i=1    0  0  3  3  3  3  3  3  3
  i=2    0  0  3  4  4  7  7  7  7
  ...
  转移：dp[i][c] = max(不选 dp[i-1][c], 选 dp[i-1][c-w[i-1]]+v[i-1])

编辑距离表：dp[i][j] = word1 前 i 个变 word2 前 j 个的最少操作数
```

**关键代码**

```java
/** 一维 0/1 背包（滚动数组）：容量必须从大往小遍历 */
static int knapsack1D(int[] weight, int[] value, int capacity) {
    int[] dp = new int[capacity + 1];
    for (int i = 0; i < weight.length; i++) {
        for (int c = capacity; c >= weight[i]; c--) { // 倒序：保证第 i 件只考虑一次
            dp[c] = Math.max(dp[c], dp[c - weight[i]] + value[i]);
        }
    }
    return dp[capacity];
}

/** 编辑距离：字符相同继承对角，否则删/插/替三者取最小 */
static int editDistance(String word1, String word2, boolean printTable) {
    // 省略：dp[m+1][n+1]，边界 dp[i][0]=i、dp[0][j]=j；
    // 相同 dp[i-1][j-1]，不同 min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])+1
}
// 省略：完全背包 completeKnapsack（正序）、LIS 两版、股票三题
```

**复杂度**

| 问题 | 时间 | 空间 | 说明 |
|---|---|---|---|
| 0/1 背包（二维） | O(n·W) | O(n·W) | 最容易理解，表完整可打印 |
| 0/1 背包（一维） | O(n·W) | O(W) | 倒序遍历，滚动压缩空间 |
| 完全背包（一维） | O(n·W) | O(W) | 正序遍历，同物可重复取 |
| LIS（O(n²)） | O(n²) | O(n) | 双重循环，好理解 |
| LIS（贪心+二分） | O(n log n) | O(n) | tails 数组 + 二分找位置 |
| 编辑距离 | O(m·n) | O(m·n) | m、n 两个字符串长度 |
| 股票系列 | O(n) | O(1)~O(n) | 每天只维护几个状态值 |

**易错点**

1. 【0/1 背包一维必须倒序】容量从 W 往 `w[i]` 遍历：正序时 `dp[c-w[i]]` 已经被本轮更新过，同一件物品会被再用一次，0/1 背包就错成了完全背包。
2. 【初始化边界】背包求最大价值，`dp[0][c]=0`、`dp[i][0]=0`；求最小代价（编辑距离、凑钱最少）时，不可达状态要初始化成 INF（如 `Integer.MAX_VALUE`），初始化成 0 会让最小值被错误地保留成 0。
3. 【int 溢出】价值、利润累加得多了 int 会溢出；先估算量级，必要时换 `long`；编辑距离 INF 相加也要防溢出（用 INF/2 或先判空）。
4. 状态定义没写清就动手转移：先回答"`dp[i][j]` 到底表示什么含义"，含义错了，转移方程再工整也是错的；遍历顺序搞反：0/1 倒序、完全背包正序。

**与 Java 的对应**

JDK 没有内置背包 / LIS / 编辑距离求解器；排序底座是 `Arrays.sort`，二分查找是 `Arrays.binarySearch`。编辑距离的思想被用在文本 diff、git 版本对比、拼写检查（Levenshtein 距离）里。文件每个题都打印 dp 表或关键中间量，并用枚举子集、递归枚举做对拍；LIS 的 `tails` 数组本身不一定是真实子序列，它的长度才是 LIS 长度。

**一句话口诀**

> 口诀：状态定义先写清，转移方程照着推；初始化划边界，遍历顺序别搞反；表打出来，一步一步对。

### ALG-15 回溯

> 对应代码：`lessons/12-algorithms/BacktrackingDemo.java`

**为什么需要它**

排列、组合、子集、棋盘放置（N 皇后）这类问题，本质是"枚举所有可能性"。层数不固定，硬写 for 循环根本写不出来；回溯用"递归 + 状态撤销"一套模板处理所有这类问题，并在中途通过剪枝去掉不可能的分支。它是暴力枚举的系统化写法，也是理解搜索树、约束满足问题的入口。

**核心思想**

在一棵决策树上深度优先往下走：每走一步先做选择（加入路径、标记状态），走到叶子就记录答案；回头时把这个选择撤销（对称操作），再试下一条岔路。排列用 `used` 数组标记"这个元素用过没"；组合/子集用 `start` 参数规定"只能从后面选"；存答案必须拷贝路径。

```text
全排列 {1,2,3} 的决策树：
                []
          /      |      \
        1        2        3
       / \      / \      / \
     1,2 1,3  2,1 2,3  3,1 3,2
      |   |    |   |    |   |
    123 132  213 231  312 321

N 皇后：三个标记数组 —— 列 colUsed、主对角线 mainDiag、副对角线 antiDiag
```

**关键代码**

```java
/** 全排列回溯：选择 → 递归深入 → 撤销（对称操作） */
private static void backtrackPermute(int[] nums, boolean[] used,
                                     List<Integer> path, List<List<Integer>> result) {
    if (path.size() == nums.length) {
        result.add(new ArrayList<>(path));       // 存答案必须拷贝路径
        return;
    }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true;                          // ① 选择
        path.add(nums[i]);
        backtrackPermute(nums, used, path, result); // ② 递归深入
        path.remove(path.size() - 1);            // ③ 撤销
        used[i] = false;
    }
}

/** N 皇后：主对角线 row-col+n-1 平移负下标；副对角线 row+col */
private static void backtrackQueen(int row, int n, int[] placement,
        boolean[] colUsed, boolean[] mainDiag, boolean[] antiDiag, List<List<String>> solutions) {
    // 省略：逐列尝试，三个标记任一冲突就 continue；放置后递归 row+1，回来撤销四个标记
}
// 省略：组合 combine（start 剪枝）、子集 subsets（选/不选）、剪枝节点计数对比
```

**复杂度**

| 问题 | 时间量级 | 栈空间 | 说明 |
|---|---|---|---|
| 全排列 | O(n · n!) | O(n) | 共 n! 个排列，每个拷贝 O(n) |
| 组合 C(n,k) | O(C(n,k) · k) | O(n) | 靠 start 参数避免重复组合 |
| 子集枚举 | O(2^n · n) | O(n) | 每个元素选或不选，共 2^n |
| N 皇后 | O(n!) 量级 | O(n) | 三个标记数组剪枝后远小于暴力 |

**易错点**

1. 【选择与撤销必须对称】递归回来后一定要把 `used` 改回 `false`、把路径末尾 `remove`；忘了撤销，状态会串到后面的分支，结果全错。
2. 去重方式别混用：排列用 `used` 数组；组合/子集用 `start` 参数，否则会出现 `[1,2]` 和 `[2,1]` 重复。
3. 存答案必须拷贝路径：`add(new ArrayList<>(path))`；直接存 path 引用，后面撤销时列表被清空，记录下来的答案也跟着空了。
4. N 皇后对角线下标映射：主对角线 `i-j` 可能为负数，要加 `n-1` 平移到非负下标；副对角线 `i+j` 范围是 0 ~ 2n-2，数组长度要开 `2n-1`。
5. 剪枝是回溯的核心价值：不剪枝就是纯暴力枚举；提前判断"这条分支不可能出答案"直接返回，能省下整棵子树。

**与 Java 的对应**

JDK 没有内置排列组合/回溯求解器；路径与答案用 `ArrayList` 存储，去重用 `HashSet`。回溯思想广泛用于正则匹配、数独求解、约束搜索。文件统计了 C(8,3) 上剪与不剪访问的节点数，并验证剪枝不会漏掉答案（叶子组合数仍是 C(8,3)=56）；8 皇后解数 92 是经典标准答案。

**一句话口诀**

> 口诀：做选择、往下钻、撤选择；排列标 used，组合传 start；存答案先拷贝；能剪就剪不犹豫。

### ALG-16 图的 BFS/DFS

> 对应代码：`lessons/12-algorithms/GraphTraversalDemo.java`

**为什么需要它**

图是"点 + 边"连成的结构，机器没法一眼看出哪些点相通。遍历是一切图算法的地基：求连通块、找最短路（无权图）、拓扑排序、判环、最小生成树，都先要有"把图走一遍"的能力。BFS 和 DFS 是两种最基础的遍历策略，分别对应队列层序和递归深入。

**核心思想**

BFS 像水面涟漪一圈一圈扩（队列）：起点入队，每次取出队首，把未访问的邻居标记并入队，天然给出"到起点的最少边数"。DFS 像一条路走到黑再回头（递归 / 显式栈）：走到一个未访问的邻居就立刻钻进去，走不下去再回来。`visited` 数组挡住环上的回头路。

```text
样例图：0-1-3-4-5，0-2-3；6-7 是另一个连通分量

BFS 层序（队列）：
  第0层：0
  第1层：1 2
  第2层：3
  第3层：4
  第4层：5
  dist:  0 1 1 2 3 4  -1 -1

DFS（递归/显式栈）：0 → 1 → 3 → 4 → 5，回来再走 2
```

**关键代码**

```java
/** BFS：队列层序；visited 入队时就标记，避免同一个点进两次队列 */
private static List<Integer> bfs(List<List<Integer>> graph, int start) {
    int n = graph.size();
    boolean[] visited = new boolean[n];
    List<Integer> order = new ArrayList<>();
    Queue<Integer> queue = new ArrayDeque<>();
    visited[start] = true;
    queue.offer(start);
    while (!queue.isEmpty()) {
        int u = queue.poll();
        order.add(u);
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                visited[v] = true;
                queue.offer(v);
            }
        }
    }
    return order;
}

/** BFS 距离：dist 初始 -1（不可达），dist[v] = dist[u] + 1 */
private static int[] bfsDistance(List<List<Integer>> graph, int start) {
    // 省略：Arrays.fill(dist, -1)；起点 dist=0；邻居未访问则距离 +1 入队
}
// 省略：DFS 递归版 dfsRecursive、显式栈版 dfsExplicitStack、连通分量计数 countComponents
```

**复杂度**

| 算法 | 时间 | 空间 | 特点 |
|---|---|---|---|
| BFS（邻接表） | O(n + m) | O(n) | 按层扩展，天然给出到起点的最少边数 |
| DFS 递归版 | O(n + m) | O(n) | 代码短，但图很深时会递归爆栈 |
| DFS 显式栈版 | O(n + m) | O(n) | 手动压栈，栈深可控，序列与递归版一致 |

**易错点**

1. 不写 `visited` 数组：图上有环，会在环里无限来回走，永不退出。
2. BFS 距离数组忘了初始化为 -1 / INF：未访问的点会被当成距离 0。
3. DFS 递归版在点极多时栈溢出（JVM 默认栈深约几千），深图要换显式栈。
4. 邻接表里邻居顺序不同，访问序列会不同；比较序列时必须固定邻居顺序。
5. 遍历只从一个起点出发，不连通的点走不到；统计连通分量要循环遍历所有点，每个没走过的点当新起点。

**与 Java 的对应**

BFS 用 `java.util.ArrayDeque`（官方推荐的队列实现，比旧的 `java.util.Stack` 快）；DFS 显式栈也用 `ArrayDeque` 的 push/pop；JDK 没有内置通用图遍历，需要自己写邻接表。文件里显式栈用栈帧 `{节点编号, 邻居游标}` 模拟递归调用栈，保证访问顺序和递归版逐条一致；普通"压邻居"写法会因老节点卡在栈底而错位。

**一句话口诀**

> 口诀：队列一层一层扩，递归一条路走到黑；visited 挡住回头路，距离数组记层数。

### ALG-17 最短路径（Dijkstra/Bellman-Ford/Floyd）

> 对应代码：`lessons/12-algorithms/ShortestPathDemo.java`

**为什么需要它**

地图导航、网络路由、任务调度都要回答"从 A 到 B 最少花多少代价"。权值可以是距离、时间、费用；边权非负用 Dijkstra 最快，出现负权边用 Bellman-Ford，要全两点之间的最短路用 Floyd。三套算法各有适用边界，选错算法（如对负权图用 Dijkstra）会得到错误答案。

**核心思想**

Dijkstra：优先队列每次取出当前距离最小的点逐步定案，松弛它的出边（只认非负权）。Bellman-Ford：对全部边松弛 n-1 轮，每轮都可能把距离再压小；第 n 轮还能松弛就说明有负环。Floyd：三重循环把任意两点最短路一次算全，中间点 `k` 必须在最外层。

```text
Dijkstra（小顶堆，懒删除）：
  dist[源]=0，其余 INF；弹出 d 最小的 u，若 d > dist[u] 跳过；松弛出边

Bellman-Ford：
  第 1 轮 … 第 n-1 轮：每条边 dist[v] = min(dist[v], dist[u]+w)
  第 n 轮仍能松弛 → 负环

Floyd：
  for k（中间点，最外层）
    for i
      for j
        dist[i][j] = min(dist[i][j], dist[i][k]+dist[k][j])
```

**关键代码**

```java
/** Dijkstra：优先队列按距离小顶堆；懒删除跳过过期条目 */
private static int[] dijkstra(int n, List<Edge> edges, int source) {
    // 省略：建邻接表 adj；dist 填 INF，dist[source]=0
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, source});
    while (!pq.isEmpty()) {
        int[] top = pq.poll();
        int d = top[0], u = top[1];
        if (d > dist[u]) continue;              // 懒删除：过期记录，跳过
        for (int[] road : adj.get(u)) {
            int v = road[0], w = road[1];
            if (dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;          // 松弛：找到更短路径就更新
                pq.offer(new int[]{dist[v], v});
            }
        }
    }
    return dist;
}
// 省略：bellmanFord（n-1 轮 + 提前收敛）、bellmanFordHasNegativeCycle、floyd 三重循环
```

**复杂度**

| 算法 | 时间 | 空间 | 能处理负权 | 能抓负环 | 适用 |
|---|---|---|---|---|---|
| Dijkstra（堆） | O((n+m) log n) | O(n) | 不能 | 不能 | 非负权、单源最短路 |
| Bellman-Ford | O(n·m) | O(n) | 能 | 能 | 有负权边 / 判负环 |
| Floyd | O(n³) | O(n²) | 能（无负环） | 不能 | 全源最短路、点很少 |

**易错点**

1. Dijkstra 遇到负权边会出错：贪心"最近点已定案"的前提是后面不可能再更短，负权会打破这个前提。
2. Bellman-Ford 必须松弛 n-1 轮；第 n 轮还能松弛，说明存在负环（绕一圈代价变负，最短路无下界）。
3. Floyd 三重循环的 `k`（中间点）必须在最外层，写反了结果是错的。
4. 距离数组初始化要用 INF（足够大），对角线为 0；别把不可达点留成 0。
5. 负权 ≠ 负环：单条负权边可以有最短路；负环是一条绕回自身、总权为负的环。

**与 Java 的对应**

Dijkstra 用 `java.util.PriorityQueue`（小顶堆，按距离取最小）；JDK 没有内置图最短路实现，需要自己建邻接表和边表。文件在同一张非负权图上让三种算法结果交叉验证（Dijkstra 与 BF、BF 与 Floyd 第 0 行一致），并单独构造负权边图与负环图对照；INF 取 1_000_000_000，保证加法时不溢出。

**一句话口诀**

> 口诀：迪杰斯特拉挑最小，负权不能用；贝尔曼松弛 n-1，再来一轮有负环；弗洛伊德中间点 k 在外，任意两点一次算全。

### ALG-18 最小生成树（Prim/Kruskal）

> 对应代码：`lessons/12-algorithms/MinimumSpanningTreeDemo.java`

**为什么需要它**

要把若干地点用最低成本连通（修路、铺电缆、集群组网），又不能有多余的环，这就是生成树问题；在所有生成树里总权值最小的那棵，叫最小生成树（MST）。Prim 和 Kruskal 是两套经典求法，分别适合稠密图和稀疏图，两者得到的总权值一定相等。

**核心思想**

Prim 从一个点出发，每次挑连接"树内/树外"的最小横切边往里扩，用优先队列维护横切边。Kruskal 把所有边按权排序，从小到大挑不形成环的边，用并查集判环：两端已在同一集合就跳过。生成树一定有 n-1 条边；选中不足 n-1 条说明原图不连通。

```text
Prim（围着当前树扩）：
  树内集合 {0} → 挑最小横切边 0-2(1) → 树内 {0,2}
  → 挑 1-2(2) → 再挑 1-3(3) → 3-4(2) → 4-5(6)

Kruskal（边排序 + 并查集）：
  边按权升序：0-2(1) 1-2(2) 3-4(2) 1-3(3) 0-1(4) 2-3(5) 4-5(6) 3-5(7)
  依次加边，两端不连通就收进 MST，成环则跳过
```

**关键代码**

```java
/** Kruskal：边按权排序；并查集判环，两端不连通就收进 MST */
private static List<WeightedEdge> kruskal(int n, List<WeightedEdge> edges) {
    List<WeightedEdge> sorted = new ArrayList<>(edges);
    Collections.sort(sorted, (a, b) -> a.w - b.w);
    UnionFind uf = new UnionFind(n);
    List<WeightedEdge> tree = new ArrayList<>();
    for (WeightedEdge e : sorted) {
        if (uf.union(e.u, e.v)) {              // 两端不在同一集合，收进 MST
            tree.add(e);
            if (tree.size() == n - 1) break;   // 凑够 n-1 条，提前收工
        }
    }
    return tree;
}
// 省略：prim（优先队列 + dist/parent/inTree）、UnionFind 路径压缩与按秩合并、sumWeight
```

**复杂度**

| 算法 | 时间 | 空间 | 核心数据结构 | 适合 |
|---|---|---|---|---|
| Prim（堆） | O((n+m) log n) | O(n) | 优先队列 | 稠密图（边多） |
| Kruskal | O(m log m) | O(m) | 边排序 + 并查集 | 稀疏图（边少） |

**易错点**

1. Kruskal 选边前必须先排序；一条边两端已经在同一集合，加进去就成环，必须跳过。
2. Prim 里"横切边"指一头在树内、一头在树外；树内到树内的旧边不能再选。
3. 生成树一定有 n-1 条边；选中不足 n-1 条说明原图不连通（无生成树）。
4. 边权相同可能有多棵 MST，总权值一定相同，但具体选哪条边可以不同。
5. 并查集不做路径压缩，最坏会退化成 O(n)；合并要按秩/大小，别无脑挂。

**与 Java 的对应**

Prim 用 `java.util.PriorityQueue`；Kruskal 用 `Collections.sort` 对边排序；并查集 JDK 没有内置，文件手写了一个带路径压缩和按秩合并的嵌套类 `UnionFind`。文件在稠密图（概率 0.5）和稀疏图（概率 0.05）上分别对拍，验证两种算法总权值相等、边数相同；样例 MST 总权值手工核算为 14。

**一句话口诀**

> 口诀：Prim 围着树找最小横切边；Kruskal 边排序、并查集判环；稠密选 Prim，稀疏选 Kruskal。

### ALG-19 拓扑排序

> 对应代码：`lessons/12-algorithms/TopologicalSortDemo.java`

**为什么需要它**

课程先修、编译依赖、任务排期、构建顺序，都是"谁必须先做完"的有向关系。拓扑排序就是把这种依赖关系排成可执行顺序；排不出来（有环）就说明依赖成了死锁。它是 DAG（有向无环图）上最基础的算法，也是 DAG 上做 DP（如关键路径）的前置步骤。

**核心思想**

Kahn：统计入度，所有入度 0 的点入队；取出一个点，把它邻居的入度减 1，减到 0 的邻居继续入队。队列为空时若还有点没排到，即有环。DFS 版：走完所有孩子后把当前点压进后序列表，最后整体 reverse 才是拓扑序。

```text
课程先修 DAG：0→1、0→2、1→3、2→3、3→4、3→5

Kahn：
  入度：0:0  1:1  2:1  3:2  4:1  5:1
  起点 0 入队 → 排出 0 → 1、2 入度变 0 入队 → … → 排出全部 6 个点

有环图 0→1→2→0：谁都无法成为入度 0 的起点，Kahn 结果长度 < 3 → 有环
```

**关键代码**

```java
/** Kahn：入度为 0 先排队；剥完一个减一个；队列为空仍有点未排即有环 */
private static List<Integer> kahn(List<List<Integer>> adj) {
    int n = adj.size();
    int[] indeg = new int[n];
    for (int u = 0; u < n; u++) {
        for (int v : adj.get(u)) indeg[v]++;   // 每条 u→v 都让 v 的入度 +1
    }
    Queue<Integer> queue = new ArrayDeque<>();
    for (int i = 0; i < n; i++) {
        if (indeg[i] == 0) queue.offer(i);
    }
    List<Integer> order = new ArrayList<>();
    while (!queue.isEmpty()) {
        int u = queue.poll();
        order.add(u);
        for (int v : adj.get(u)) {
            indeg[v]--;                        // u 排掉了，v 的一项先修完成
            if (indeg[v] == 0) queue.offer(v);
        }
    }
    return order;
}
// 省略：dfsTopo（state 0/1/2 + 后序 reverse）、isValidTopo 合法性校验、hasCycle
```

**复杂度**

| 算法 | 时间 | 空间 | 特点 |
|---|---|---|---|
| Kahn（队列） | O(n + m) | O(n) | 直观，顺便能数出度变化；结果顺序确定 |
| DFS 后序逆序 | O(n + m) | O(n) | 代码短，天然带递归栈；深图换显式栈 |

**易错点**

1. 结果长度 < n：说明图里有环，剩下的点入度永远不为 0，排不出来。
2. 入度表要在加边时同步累加 `indeg[v]++`，别只建邻接表忘了计入度。
3. DFS 版存的是"后序"（孩子都走完才记录），必须 reverse 才是拓扑序。
4. 拓扑序不唯一：只要每条边 u 在 v 前，任何排列都合法，不要强求两版输出完全相同。
5. 有向图才有拓扑序；无向边不能当成有向依赖，否则会被误判成环。

**与 Java 的对应**

Kahn 用 `java.util.ArrayDeque` 做队列；JDK 没有内置拓扑排序，需要自己建邻接表和入度表。构建工具（如 Maven/Gradle）内部做依赖解析，本质就是拓扑排序。文件用 `isValidTopo` 校验每条边 u→v 中 u 的位置都在 v 之前，并在随机 DAG（只连 i→j 且 i<j，天然无环）上对比两版耗时。

**一句话口诀**

> 口诀：入度为 0 先排队，剥完一个减一个；DFS 后序倒着读；排不满就是有环。

### ALG-20 KMP 字符串匹配

> 对应代码：`lessons/12-algorithms/KmpDemo.java`

**为什么需要它**

在长文本里找一个子串，暴力法一失配就把主串指针往回挪，最坏 O(n·m)。KMP 利用"已经匹配过的部分里前后缀相同"的信息，避免重复比对，是字符串算法的基本功。理解前缀函数 `next`，也是后面学自动机、字符串哈希的基础。

**核心思想**

先用模式串自己算出"失配时能回退到哪儿"的 `next` 数组：`next[i]` 是 `pattern[0..i]` 的最长相等真前缀与真后缀长度。匹配时主串指针 `i` 从不回退，只回退模式串指针 `j`；失配用 `j = next[j-1]` 回退，匹配成功一次后也要回退一格继续找重叠的下一处。

```text
模式串 abab，next 数组：
  a      → next[0]=0
  ab     → 无公共前后缀，next[1]=0
  aba    → 公共前缀/后缀 a，next[2]=1
  abab   → 公共 ab，next[3]=2

匹配：text = abababab，pattern = abab
  i 不回退；j 沿 next 回退；命中起点 0、2、4（重叠匹配 3 处）
```

**关键代码**

```java
/** 构造前缀函数 next：next[i] = pattern[0..i] 最长相等真前缀/真后缀长度 */
private static int[] buildNext(String pattern) {
    int m = pattern.length();
    int[] next = new int[m];
    next[0] = 0;                             // 单字符没有真前缀，固定 0
    for (int i = 1; i < m; i++) {
        int j = next[i - 1];
        while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
            j = next[j - 1];                 // 对不上就沿 next 回退
        }
        if (pattern.charAt(i) == pattern.charAt(j)) j++;
        next[i] = j;
    }
    return next;
}

/** KMP 匹配：i 遍历主串不回退；j 是已匹配长度，失配沿 next 回退 */
private static List<Integer> kmpSearch(String text, String pattern) {
    // 省略：m > n 直接返回空；next 建好后逐字符匹配；
    // 命中记录 i-m+1，j = next[j-1] 回退继续找重叠下一处
}
```

**复杂度**

| 算法 | 最好 | 最坏 | 空间 | 说明 |
|---|---|---|---|---|
| 暴力匹配 | O(n·m) | O(n·m) | O(1) | 失配就主串回退，简单但慢 |
| KMP | O(n + m) | O(n + m) | O(m) | i 不回退，j 沿 next 回退 |

**易错点**

1. `next[i]` 表示 `pattern[0..i]` 的"最长相等真前缀与真后缀"长度，不是下标，别混。
2. 回退必须用 `j = next[j-1]`，而不是 `next[j]`；`j-1` 才是当前已匹配段的最后位置。
3. 匹配成功一次后，`j` 也要用 `next[j-1]` 回退，才能继续找重叠的下一处。
4. `next[0]` 固定为 0；算 next 时下标从 1 开始，别从 0 起步越界。
5. 模式串比文本长时直接返回空，不要让 `j` 越界访问。

**与 Java 的对应**

`String.indexOf(...)` 是 JDK 自带的查找方法；它内部并不是 KMP（早期偏朴素匹配，新版本有 Boyer-Moore 类的优化思路），文件只对照"结果是否一致"，不要求算法相同。文件用暴力双重循环 `bruteSearch` 和 `indexOfAll`（循环调 `indexOf`，移动一格允许重叠）做三方对拍，长文本上 KMP 与暴力结果一致。

**一句话口诀**

> 口诀：先求 next 看前后缀，失配回退 j 不回退 i；匹配成功再回退，重叠位置接着找。

### ALG-21 双指针与滑动窗口

> 对应代码：`lessons/12-algorithms/TwoPointersSlidingWindowDemo.java`

**为什么需要它**

很多线性问题用双重循环是 O(n²)，换成两个指针各走各的，就能压到 O(n)。两数之和、回文判断、链表中点、窗口统计、最长无重复子串，都是双指针/窗口的典型场景。掌握对撞、快慢、定长窗口、变长窗口四种套路，能覆盖一大批数组与字符串面试题。

**核心思想**

对撞指针：左右一左一右往里挤，只在有序数组上成立。快慢指针：一前一后不同速，找中点、判环形链表。定长窗口：先算初始窗口，再每轮减去左出、加上右进。变长窗口模板：进窗口 → 判条件 → 出窗口 → 更新答案；有重复就 `while` 收缩左边界，一次缩一格。

```text
对撞（有序两数之和）：
  1  2  4  6  8  9，target=10
  L↑            R↑  和=10 ✓

变长窗口（最长无重复子串）：
  s = abcabcbb
  right 进窗口 → 集合判重复 → 有重复 while 移 left 出窗口 → 合法后更新 maxLen

快慢指针：slow 走一步，fast 走两步；fast 到尾 slow 在中点；追上即有环
```

**关键代码**

```java
/** 变长窗口：最长无重复子串；模板 = 进窗口 → 判重复 → while 收缩 → 更新答案 */
private static int longestUniqueSubstring(String s) {
    Set<Character> window = new HashSet<>();
    int left = 0;
    int maxLen = 0;
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        while (window.contains(c)) {            // 有重复，不断把左边界移出
            window.remove(s.charAt(left));
            left++;
        }
        window.add(c);                           // 进窗口
        maxLen = Math.max(maxLen, right - left + 1); // 合法后更新答案
    }
    return maxLen;
}
// 省略：twoSumSorted 对撞、isPalindrome 回文、findMiddle/hasCycle 快慢、fixedWindowAverage/Max 定长窗口
```

**复杂度**

| 技巧 | 时间 | 空间 | 典型问题 |
|---|---|---|---|
| 对撞指针 | O(n) | O(1) | 有序数组两数之和、回文判断 |
| 快慢指针 | O(n) | O(1) | 找中点、判环形链表 |
| 定长窗口 | O(n) | O(1) | 窗口平均值、窗口最大值 |
| 变长窗口 | O(n) | O(k) | 最长无重复子串（k 为字符集大小） |

**易错点**

1. 对撞指针只在"有序数组"上成立；无序数组要先排序，或改用哈希表。
2. 变长窗口必须用 `while` 收缩左边界，条件不满足一次缩一格；用 `if` 会漏缩。
3. 窗口更新答案要在收缩之后（保证窗口合法），别在加进右边界前就记答案。
4. 快慢指针判环：快指针能追上慢指针才有环；追上不了就走到尾，无环。
5. 定长窗口先算初始窗口，再每轮减去左出、加上右进，别每轮重算整段；定长窗口最大值这里用朴素逐个比较，大窗口可换单调队列。

**与 Java 的对应**

字符是否重复用 `HashSet` / 布尔数组；JDK 的 `Arrays` 只提供排序等基础能力，滑动窗口本身没有现成类，需要自己维护左右边界。文件用暴力枚举子串的 `bruteLongestUnique`、双重循环的 `bruteTwoSum` 做对拍，并在随机长串上对比变长窗口 O(n) 与暴力 O(n²) 的耗时。

**一句话口诀**

> 口诀：有序对撞、链表快慢；定长窗口左右滑；变长窗口进判出更，while 收缩左边界。

### ALG-22 前缀和与差分

> 对应代码：`lessons/12-algorithms/PrefixSumAndDiffDemo.java`

**为什么需要它**

没有前缀和时，每次问 `[l,r]` 的和都要从头遍历，O(n)；问几百次就是 O(n·q)。预处理一次前缀和 O(n)，之后每次查询 O(1)。差分则反过来：先记修改，最后一次前缀和还原，把多次区间加法从 O(n·q) 压到 O(n + q)。这是多次区间查询/多次区间修改的标配工具。

**核心思想**

一维前缀和：`pre` 长度为 `n+1`，`pre[i]` 表示前 `i` 个元素之和，闭区间 `[l,r]`（0 下标）的和 = `pre[r+1] - pre[l]`。二维前缀和用容斥：当前格 = 上方 + 左方 - 左上重复 + 自身。差分：给 `[l,r]` 整体加 `v`，只需 `diff[l]+=v`、`diff[r+1]-=v`，最后对 diff 做前缀和还原。

```text
原数组：2 4 6 8 10
pre（长度 n+1）：0 2 6 12 20 30   ← pre[i] = 前 i 个之和
区间 [1,3]：4+6+8 = 18 = pre[4]-pre[1] = 20-2

差分：[1,3] +3，[2,4] +5
  diff 记两头：diff[1]+=3 diff[4]-=3；diff[2]+=5 diff[5]-=5
  还原（前缀和）：0 3 8 8 5 0 → 原数组增量 0 3 8 8 5
```

**关键代码**

```java
/** 一维前缀和：pre[0]=0；pre[i] = arr[0]+…+arr[i-1]（前 i 个元素之和） */
private static long[] buildPrefix(int[] arr) {
    int n = arr.length;
    long[] pre = new long[n + 1];
    for (int i = 0; i < n; i++) {
        pre[i + 1] = pre[i] + arr[i];
    }
    return pre;
}
// 区间和（0 下标闭区间 [l,r]）：pre[r + 1] - pre[l]

/** 差分：给 [l,r] 整体加 v，只需 diff[l]+=v、diff[r+1]-=v */
private static void rangeAdd(long[] diff, int l, int r, int v) {
    diff[l] += v;
    diff[r + 1] -= v;                          // r+1 可能等于 n，数组已多开一格
}

/** 二维前缀和容斥：pre[i][j] = matrix[i-1][j-1] + pre[i-1][j] + pre[i][j-1] - pre[i-1][j-1] */
private static long[][] buildPrefix2D(int[][] matrix) {
    // 省略：rows/cols +1；逐格按容斥公式填 pre
}
```

**复杂度**

| 操作 | 预处理 | 单次查询/修改 | 空间 |
|---|---|---|---|
| 一维前缀和 | O(n) | O(1) | O(n) |
| 二维前缀和 | O(n²) | O(1) | O(n²) |
| 差分数组 | O(n) | O(1) 记修改 | O(n)，最后 O(n) 还原 |

**易错点**

1. 【最大坑：下标错位】`pre` 数组长度是 `n+1`，`pre[i]` 表示"前 i 个元素之和"，闭区间 `[l,r]` 的和 = `pre[r+1] - pre[l]`；写成 `pre[r]-pre[l]` 会漏一格（恰好漏掉右端元素 `arr[r]`）。
2. `diff[r+1]` 当 `r` 是最后一个下标时 `r+1 == n`，越界；要判断或允许长度 `n+1` 的差分数组。
3. 区间和累加可能超过 int 范围，求和要用 `long`；int 溢出会变成负数且不报错。
4. 二维前缀和容斥：减去两份重复时要把多减的左上角加回来，公式最后是 `+pre[i-1][j-1]`。
5. 差分做完所有修改后，必须再做一次前缀和才能还原成原数组，直接看 diff 不是答案。

**与 Java 的对应**

JDK 没有内置前缀和工具；`Arrays` 只提供排序、填充等基础方法。工程里常用 `long[]` 存前缀和避免溢出；本文件手写一维/二维前缀和与差分。文件专门演示了 int 累加溢出成负数、long 得到正确值的对照，并在 n=20000、q=20000 的随机查询上验证前缀和 O(1) 与暴力 O(n) 总和一致。

**一句话口诀**

> 口诀：pre 长度 n+1，前 i 个之和；区间和 pre[r+1] 减 pre[l]；差分两头记增减，最后前缀和还原。

### ALG-23 位运算技巧

> 对应代码：`lessons/12-algorithms/BitManipulationDemo.java`

**为什么需要它**

状态压缩、集合表示、权限标志位、高频计数，用一个 int 的 32 个二进制位就能搞定，比存布尔数组省空间也快。很多面试题（判 2 的幂、数 1 的个数、找只出现一次的数、枚举子集）都靠位运算。记住几个固定套路，能写出比循环更短更快的代码。

**核心思想**

基础运算：`&`（与，留双 1）、`|`（或，留任一 1）、`^`（异或，成对抵消）、`~`（取反）、`<<` 左移、`>>` 算术右移、`>>>` 逻辑右移。固定套路：`x & -x` 取最低位 1（lowbit）；`x & (x-1)` 消掉最低位 1；异或成对抵消；掩码 `mask` 枚举子集。

```text
x = 12（1100）
  x & -x  = 0100 = 4   ← lowbit：最低位那个 1
  x & (x-1) = 1100 & 1011 = 1000 = 8  ← 消掉最低位 1

异或找单数：成对抵消为 0
  1 ^ 2 ^ 1 ^ 3 ^ 2 ^ 5 = 3 ^ 5
  取最低位不同位 diff = xor & -xor，按这一位分两组，各组内部异或

枚举子集：mask 从 0 到 (1<<n)-1，第 j 位为 1 表示选第 j 个元素
```

**关键代码**

```java
/** 统计二进制中 1 的个数：每用 x & (x-1) 消掉一个 1，计数 +1 */
private static int countOnes(int x) {
    int count = 0;
    while (x != 0) {
        x &= x - 1;                            // 消掉最低位的 1
        count++;
    }
    return count;
}

/** 判断 2 的幂：先排除 0 和负数；2 的幂只有一个 1，消掉它就变成 0 */
private static boolean isPowerOfTwo(int x) {
    return x > 0 && (x & (x - 1)) == 0;
}

/** 找两个只出现一次的数：全部异或 → lowbit 分组 → 各组内部异或 */
private static int[] findTwoSingles(int[] nums) {
    int xor = 0;
    for (int v : nums) xor ^= v;
    int diff = xor & -xor;                     // 最低位不同的那一位
    int x = 0, y = 0;
    for (int v : nums) {
        if ((v & diff) == 0) x ^= v; else y ^= v;
    }
    return new int[]{x, y};
}
// 省略：enumerateSubsets 掩码枚举子集、基础 & | ^ ~ << >> >>> 演示
```

**复杂度**

| 操作 | 时间 | 说明 |
|---|---|---|
| 取最低位 1：`x & -x` | O(1) | 结果是只有最低位那个 1 的数 |
| 消最低位 1：`x & (x-1)` | O(1) | 把最右边的 1 清成 0 |
| 统计 1 的个数 | O(k) | k 为 1 的个数；JDK 的 bitCount 是内置优化版 |
| 枚举子集 | O(2^n) | 用掩码 0 ~ (1<<n)-1 |

**易错点**

1. 优先级坑：`&` 的优先级低于 `==`，`if (x & 1 == 1)` 会按 `x & (1==1)` 解析，编译报错；必须写成 `if ((x & 1) == 1)`，括号不能省。
2. `>>` 是算术右移（补符号位），`>>>` 是逻辑右移（补 0）；负数右移两者结果天差地别（-1 >> 1 仍是 -1，-1 >>> 1 是 `Integer.MAX_VALUE`）。
3. 判断 2 的幂要先排除 0 和负数：`x > 0 && (x & (x-1)) == 0`。
4. 异或找单数：成对出现的数异或抵消为 0；找两个单数要按某一位不同把数组分成两组。
5. 左移 `1<<n` 当 n 接近 31 时会溢出负数，枚举子集前确认 n 很小。

**与 Java 的对应**

`Integer.bitCount(int)` 是 JDK 内置的统计二进制 1 个数方法（底层用硬件指令/查表优化），文件手写 Brian Kernighan 算法与它对拍了 50 个随机数；`Integer.highestOneBit` / `lowestOneBit` 也有现成实现。`Math` 类不直接提供位运算，但移位、掩码是权限标志位、状态压缩的基础手段。

**一句话口诀**

> 口诀：与留 1、或留 1、异或成对消；lowbit 取最低 1，x&(x-1) 清最低 1；掩码循环枚举子集。
