# lessons —— 按知识点组织教学示例代码

这里每个 `.java` 文件 = 一个知识点，**独立可运行**，全部逐段中文注释。

建议的学习方式：

1. 先读文件开头的注释块，知道这个知识点解决什么问题
2. 看 `main` 方法里的代码顺序，跟着注释理解每一步
3. 自己动手跑一遍，对照输出
4. 尝试改几个值（比如把 5 改成 10），观察结果变化

## 运行方式

### 方式一：用 IDEA（推荐）
1. 打开这个文件夹（或整个 java-learning 仓库）
2. 点开任意 `.java` 文件
3. 点 `main` 方法左边的绿色三角，选 Run

### 方式二：用命令行（需已装 JDK）
```bash
# 编译（生成 Xxx.class）
javac -encoding UTF-8 HelloWorld.java

# 运行
java HelloWorld
```

> 注意：Windows 命令行里中文输出可能乱码，这是控制台编码问题，不是代码问题。在 IDEA 里运行就正常。

---

## 目录与知识点对照

### 01-java-basics —— Java 语言基础

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `HelloWorld.java` | 程序结构 | 类、main 方法、四种输出方式、命令行参数、转义字符 |
| `VariablesAndTypes.java` | 变量与类型 | 8 种基本类型、String 常用方法、自动/强制类型转换、final 常量、var |
| `ControlFlow.java` | 流程控制 | if/else、switch（含 Java 14 箭头写法）、for/while/do-while、break/continue、九乘九 |
| `MethodsAndRecursion.java` | 方法与递归 | 方法定义、重载、可变参数、递归的终止条件、值传递 vs 引用传递 |

### 02-oop —— 面向对象核心

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `ClassAndObject.java` | 类与对象 | 类/对象的关系、new 的过程、成员变量 vs 静态变量、this、空指针 |
| `Constructor.java` | 构造器 | 无参/有参构造、构造器重载、`this(...)` 链式调用、为什么要保留无参构造 |
| `Encapsulation.java` | 封装 | private + getter/setter、在 setter 里做校验、只读字段 |
| `Inheritance.java` | 继承 | extends、super、protected、方法重写、单继承的限制 |
| `Polymorphism.java` | 多态 | 父类引用指向子类对象、动态绑定、接口、抽象类、instanceof |
| `OverloadVsOverride.java` | 重载 vs 重写 | 两者的判定规则、编译期绑定 vs 运行期绑定、@Override 的作用 |
| `FinalStaticAbstract.java` | final/static/抽象 | final 三种用法、静态变量/方法/代码块/内部类、抽象类与接口的区别 |

### 03-collections —— 集合框架

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `ListDemo.java` | List | ArrayList vs LinkedList、增删改查、三种遍历方式、`List.of` 只读列表 |
| `MapDemo.java` | Map | HashMap 基本操作、三种遍历、TreeMap 排序、词频统计实战 |
| `SetAndOptional.java` | Set + Optional | HashSet 去重原理、TreeSet 排序、LinkedHashSet 保序、Optional 防空指针 |

### 04-lambda-stream —— 函数式编程

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `LambdaDemo.java` | Lambda | 函数式接口、四种内置接口（Predicate/Consumer/Function/Supplier）、方法引用 |
| `StreamDemo.java` | Stream | filter/map/sorted/distinct/limit、reduce/count、Collectors 收集与分组 |

### 05-generics —— 泛型

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `GenericsDemo.java` | 泛型 | 泛型类、泛型方法、通配符 `?` / `? extends` / `? super`、类型擦除 |

### 06-exception —— 异常处理

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `ExceptionDemo.java` | 异常 | try-catch-finally、多重 catch、throw vs throws、自定义异常、try-with-resources |

### 07-io —— 文件读写

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `FileIoDemo.java` | IO 流 | File 类、字符流读写文本、字节流复制任意文件、缓冲流、Properties 配置读写 |

### 08-thread —— 多线程

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `ThreadDemo.java` | 线程基础 | Thread / Runnable、start vs run、sleep、join、守护线程、串行 vs 并行 |
| `ThreadSyncDemo.java` | 线程安全 + 线程池 | 竞态条件复现、synchronized、AtomicInteger、ExecutorService、Future |

### 09-reflection —— 反射

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `ReflectionDemo.java` | 反射 | 获取 Class 的三种方式、读写私有字段、调用方法、动态创建对象、模拟依赖注入 |

### 10-algorithm —— 算法入门

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `SortDemo.java` | 排序 | 冒泡、插入、快排的实现与复杂度对比（含 2 万条实测耗时） |
| `BinarySearchDemo.java` | 二分查找 | 迭代版与递归版、为什么快、三个常见易错点 |

### 11-data-structures —— 数据结构（18 篇）

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `ArrayDemo.java` | 数组与动态数组 | 原生数组的 O(1) 随机访问、手写 1.5 倍扩容的 ArrayList、头部插入 vs 尾部插入实测 |
| `SinglyLinkedListDemo.java` | 单链表 | 头插尾插、按值删除、反转（迭代 + 递归）、快慢指针找中点、判环、合并两个有序链表 |
| `DoublyLinkedListDemo.java` | 双向链表 | prev/next 两个方向的指针、带 tail 的 O(1) 尾插、按值删除要改两处指针 |
| `CircularLinkedListDemo.java` | 循环链表 | 尾节点回指 head、遍历终止条件、约瑟夫环报数出列 |
| `StackDemo.java` | 栈 | 数组栈 / 链栈、括号匹配、最小栈、为什么官方推荐 `ArrayDeque` |
| `QueueDemo.java` | 队列 | 循环队列判满要牺牲一格、链式队列、`ArrayDeque` / `LinkedList` 对照 |
| `DequeDemo.java` | 双端队列 | 双指针取模两端增删、单调队列求滑动窗口最大值 |
| `PriorityQueueHeapDemo.java` | 优先队列与堆 | siftUp/siftDown、建堆 O(n)、TopK、`PriorityQueue` 默认是最小堆 |
| `HashMapDemo.java` | 哈希表 | 开放寻址（墓碑删除）、链地址法、扩容 rehash、JDK 的 0.75 负载因子与树化 |
| `BinaryTreeDemo.java` | 二叉树 | 前中后层四种遍历（递归 + 迭代）、高度/叶子数、前序 + 中序重建 |
| `BinarySearchTreeDemo.java` | 二叉搜索树 | 插入/查找/删除三种情况、中序必升序、退化成链表的最坏情况 |
| `AvlTreeDemo.java` | AVL 平衡树 | 平衡因子、LL/RR/LR/RL 四种旋转、插入后回溯调平 |
| `TreeMapRedBlackDemo.java` | 红黑树与 TreeMap | 5 条性质、最长路径 ≤ 2×最短路径、`floorKey/ceilingKey/subMap` |
| `UnionFindDemo.java` | 并查集 | 路径压缩、按秩合并、连通块计数、朋友圈问题 |
| `TrieDemo.java` | Trie 字典树 | 插入 / 整词查找 / 前缀查找 / 自动补全、空间换时间 |
| `GraphDemo.java` | 图 | 邻接矩阵与邻接表两种存法、空间与判边速度的取舍 |
| `SegmentTreeDemo.java` | 线段树 | 区间和 / 区间最值、懒标记区间加与 pushDown |
| `FenwickTreeDemo.java` | 树状数组 | lowbit、单点修改 + 前缀和、树状数组求逆序对 |

### 12-algorithms —— 算法（23 篇）

| 文件 | 知识点 | 学完你会知道 |
|---|---|---|
| `BubbleSortDemo.java` | 冒泡排序 | 提前退出优化、稳定性实证、与 `Arrays.sort` 200 组对拍 |
| `SelectionSortDemo.java` | 选择排序 | 交换次数最多 n-1（写操作少）、为什么它不稳定 |
| `InsertionSortDemo.java` | 插入排序 | 摸扑克牌模型、近乎有序时 O(n)、二分插入优化 |
| `ShellSortDemo.java` | 希尔排序 | gap 减半分组插入、为什么比插入排序快 |
| `MergeSortDemo.java` | 归并排序 | 分治 + 临时数组归并、稳定、O(n) 额外空间 |
| `QuickSortDemo.java` | 快速排序 | Lomuto / Hoare / 三路分区、随机化基准避免最坏 O(n²) |
| `HeapSortDemo.java` | 堆排序 | 原地建堆 O(n)、下沉调整、O(1) 额外空间 |
| `CountingSortDemo.java` | 计数排序 | 用前缀和定位保证稳定、负数偏移、值域小的前提 |
| `RadixSortDemo.java` | 基数排序 | LSD 按位、稳定性是正确性前提（有反例演示） |
| `BucketSortDemo.java` | 桶排序 | 分桶 + 桶内插入排序、适合均匀分布的浮点数 |
| `BinarySearchTemplateDemo.java` | 二分三套模板 | 精确查找 / 左边界 / 右边界、mid 防溢出、二分答案 |
| `RecursionDivideConquerDemo.java` | 递归与分治 | 递归三要素、记忆化、汉诺塔、快速幂 |
| `GreedyDemo.java` | 贪心 | 区间调度、跳跃游戏、贪心在 0/1 背包上为什么会失效 |
| `DynamicProgrammingDemo.java` | 动态规划 | 0/1 背包一维倒序、完全背包、LIS 两种解法、编辑距离、股票系列 |
| `BacktrackingDemo.java` | 回溯 | 选择 → 递归 → 撤销、全排列 / 组合 / N 皇后 / 子集、剪枝 |
| `GraphTraversalDemo.java` | 图的 BFS/DFS | 队列层序、递归与显式栈、距离数组、连通分量 |
| `ShortestPathDemo.java` | 最短路径 | Dijkstra 懒删除、Bellman-Ford 判负环、Floyd 的 k 必须最外层 |
| `MinimumSpanningTreeDemo.java` | 最小生成树 | Prim 与 Kruskal、两法总权重必须相等 |
| `TopologicalSortDemo.java` | 拓扑排序 | Kahn 入度法、DFS 后序法、用"结果长度 < n"判环 |
| `KmpDemo.java` | KMP | next 数组构造、i 指针不回退、与暴力匹配对拍 |
| `TwoPointersSlidingWindowDemo.java` | 双指针与滑动窗口 | 对撞指针、定长窗口、最长无重复子串的 while 收缩 |
| `PrefixSumAndDiffDemo.java` | 前缀和与差分 | pre 长度 n+1、区间和 `pre[r+1]-pre[l]`、二维容斥、差分区间加、int 溢出坑 |
| `BitManipulationDemo.java` | 位运算技巧 | 取最低位的 1、消位、判 2 的幂、异或找唯一数、优先级坑 |

---

## 学习顺序建议

按目录编号来，不要跳：

```
01 基础语法 → 02 面向对象 → 03 集合 → 04 Lambda/Stream
→ 05 泛型 → 06 异常 → 07 IO → 08 多线程 → 09 反射 → 10 算法入门
→ 11 数据结构（18 篇）→ 12 算法（23 篇）
```

`11-data-structures` 和 `12-algorithms` 是配套的一整套：每个主题一个文件，文件头部注释就有
"为什么需要 → 复杂度速查表 → 易错点 → 与 Java 集合类的对应 → 一句话口诀"，正文逐段中文注释，
`main` 里带断言式校验（跑完最后一行会打印"全部断言通过，共 N 条"），跑不对会直接抛异常，不会"看起来通过"。

配套的完整讲解（示意图 + 复杂度表 + 面试题索引）见 [docs/Java数据结构与算法总纲.md](../docs/Java数据结构与算法总纲.md)。

每个模块学完，再去 `practice/` 里看你当时敲的练习代码，对照理解。
