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

---

## 学习顺序建议

按目录编号来，不要跳：

```
01 基础语法 → 02 面向对象 → 03 集合 → 04 Lambda/Stream
→ 05 泛型 → 06 异常 → 07 IO → 08 多线程 → 09 反射 → 10 算法
```

每个模块学完，再去 `practice/` 里看你当时敲的练习代码，对照理解。
