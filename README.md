# Java 后端学习路线：从单体应用到微服务

一个完整记录「Java 后端从零到微服务」学习过程的仓库。

包含 **4 个可运行的 Spring Boot 工程** + **65 篇按知识点组织的教学代码**（其中 41 篇是数据结构与算法）+ **全部练习代码** + **全套学习笔记**，覆盖从 Java 基础语法、数据结构与算法，到微服务治理的完整链路。

---

## 目录结构

```
java-learning/
├── lessons/        # 按知识点组织的教学示例代码（65 篇，每篇独立可运行，逐段中文注释）
│   ├── 01-java-basics/     # 程序结构、变量类型、流程控制、方法与递归
│   ├── 02-oop/             # 类与对象、构造器、封装、继承、多态、重载重写、final/static
│   ├── 03-collections/     # List、Map、Set、Optional
│   ├── 04-lambda-stream/   # Lambda、Stream
│   ├── 05-generics/        # 泛型
│   ├── 06-exception/       # 异常处理
│   ├── 07-io/              # 文件读写
│   ├── 08-thread/          # 线程基础、线程安全、线程池
│   ├── 09-reflection/      # 反射
│   ├── 10-algorithm/       # 算法入门：排序、二分查找
│   ├── 11-data-structures/ # 数据结构 18 篇：数组/链表/栈/队列/堆/哈希/树/并查集/Trie/图/线段树/树状数组
│   └── 12-algorithms/      # 算法 23 篇：十大排序、二分模板、递归分治、贪心、DP、回溯、图论、字符串、技巧
├── practice/       # 学习过程中实际敲的练习代码
│   ├── 01-java-basics/        # 21 个主题练习（继承/泛型/IO/Lambda/集合/反射/Stream/线程/异常）
│   ├── 02-oop-course/         # 面向对象课程 18 个章节练习
│   ├── 03-spring-boot-intro/  # Spring Boot 入门工程（demo / demo2 / demo3 / shiyan2）
│   └── 04-misc/               # 零散小练习
├── demo4/          # 单体应用：学生管理系统（Spring Boot + MyBatis + MySQL + Spring Security）
├── demo5/          # 微服务 A（8081）：对外接口 + 调用服务 B
├── demo6/          # 微服务 B（8082）：被调用方，含快/慢两个接口
├── demo7/          # API 网关（9000）：统一入口，按路径转发
└── docs/           # 学习笔记
    ├── Java后端学习总纲.md      # 知识地图（676 行 / 35 小节）
    ├── Java数据结构与算法总纲.md # 数据结构与算法完整地图：18 个结构 + 23 个算法，配 41 个可运行代码
    ├── Java错题本.md            # 真实踩坑记录（15 条）
    ├── 微服务三实验验证.ps1      # 30 秒自检脚本
    ├── demo5-check.ps1          # 早期自检脚本（备用）
    └── java路线图进度.html       # 学习进度可视化
```

---

## 教学代码怎么用

`lessons/` 下每个文件就是一个知识点，**不需要搭工程**，用 IDEA 打开点绿色三角就能跑：

| 步骤 | 做什么 |
|---|---|
| 1 | 读文件开头的注释块，知道这个知识点解决什么问题 |
| 2 | 跟着 `main` 里的代码顺序，看每一段注释 |
| 3 | 跑一遍，对照控制台输出 |
| 4 | 改几个值（比如把 5 改成 10），观察结果怎么变 |

命令行方式：

```bash
javac -encoding UTF-8 HelloWorld.java
java HelloWorld
```

详细的学习顺序和每个文件对应的知识点，见 [lessons/README.md](lessons/README.md)。


---

## 技术栈

| 类别 | 技术 |
|---|---|
| 语言 | Java 17 |
| 框架 | Spring Boot 3.2.0 · Spring Cloud 2023.0.1 |
| 持久层 | MyBatis · MySQL |
| 安全 | Spring Security（BCrypt 密码加密） |
| 微服务 | Spring Cloud Alibaba（Nacos 注册/配置中心 · Gateway · Sentinel） |
| 构建 | Maven |

---

## 各工程说明

### demo4 · 学生管理系统（单体应用）

- 端口：8080
- 功能：学生列表 / 新增 / 修改 / 删除，带登录鉴权
- 涉及：三层架构、MyBatis Mapper、REST 接口、Spring Security 表单登录
- 数据库脚本：需自行创建 `school` 库与 `student` 表

> ⚠️ `application.properties` 里的数据库密码和登录密码已替换为 `CHANGE_ME_...` 占位符，本地运行前请填成自己的密码。

### demo5 · 服务 A（微服务）

- 端口：8081
- 接口：

| 路径 | 说明 |
|---|---|
| `/hello` | 返回自身介绍（用于限流演示） |
| `/callB` | 通过服务名调用 demo6 的 `/hello` |
| `/callBslow` | 调用 demo6 的 `/slow`，配熔断 + 兜底 |
| `/callBfast` | 调用 demo6 的 `/hello`，验证"快接口不会被熔断" |
| `/config` | 读取 Nacos 配置中心的值 |

- 涉及：RestTemplate + `@LoadBalanced`、Nacos 服务发现、配置中心热更新、Sentinel 限流/熔断、规则持久化

### demo6 · 服务 B（微服务）

- 端口：8082
- 接口：`/hello`（快）、`/slow`（`Thread.sleep(1000)`，用于触发慢调用熔断）

### demo7 · API 网关

- 端口：9000
- 路由：

| 外部访问 | 实际转发 | 说明 |
|---|---|---|
| `http://localhost:9000/a/hello` | demo5 的 `/hello` | `lb://demo5` + `StripPrefix=1` |
| `http://localhost:9000/b/hello` | demo6 的 `/hello` | `lb://demo6` + `StripPrefix=1` |

---

## 运行顺序

微服务部分依赖两个外部组件，**必须先启动**：

### 1. Nacos（注册中心 + 配置中心）

```bash
# Windows
D:\nacos\bin\startup.cmd -m standalone
```

启动后控制台：http://localhost:8848/nacos

### 2. Sentinel Dashboard（流量治理控制台）

```bash
java -Dserver.port=8858 -jar sentinel-dashboard-1.8.6.jar
```

控制台：http://localhost:8858（默认账号 `sentinel` / `sentinel`）

### 3. 启动业务服务

在 IDEA 中依次启动 `demo5`（8081）、`demo6`（8082）、`demo7`（9000）。

### 4. 验证

```bash
# 限流：连点 10 次，预期前 2 次 200，后 8 次 429
curl -s http://localhost:8081/hello

# 熔断：连续访问，预期前几次约 1013ms，之后 10ms 返回兜底文案
curl -s http://localhost:8081/callBslow

# 网关：统一入口
curl -s http://localhost:9000/a/hello
```

---

## Sentinel 规则

限流与熔断规则存放在 Nacos 配置中心，服务启动时自动加载，**重启不丢失**：

| dataId | 内容 |
|---|---|
| `sentinel-demo5-flow-rules.json` | 限流规则（`/hello`，QPS = 2） |
| `sentinel-demo5-degrade-rules.json` | 熔断规则（慢调用比例，>1000ms 判定为慢） |

规则结构示例：

```json
[{
  "resource": "callB_slow",
  "grade": 0,
  "count": 1000,
  "slowRatioThreshold": 0.5,
  "minRequestAmount": 5,
  "statIntervalMs": 10000,
  "timeWindow": 10
}]
```

---

## 学习文档

| 文档 | 内容 |
|---|---|
| [Java后端学习总纲](docs/Java后端学习总纲.md) | 全部知识点：Java 基础 → 数据库 → Spring Boot → 微服务，每个知识点配"一句话原理 + 关键代码 + 踩坑" |
| [Java数据结构与算法总纲](docs/Java数据结构与算法总纲.md) | 18 个数据结构 + 23 个算法主题，每个主题七段式讲解（为什么 → 图示 → 关键代码 → 复杂度 → 易错点 → Java 对应 → 口诀），配 41 个可独立运行的代码 |
| [Java错题本](docs/Java错题本.md) | 15 条真实错误记录：报错现象 / 根本原因 / 解决方式 / 如何避免 |
| [微服务三实验验证](docs/微服务三实验验证.ps1) | PowerShell 自检脚本：一键验证热更新、限流、熔断三个机制 |

---

## 学习进度

```
Java 基础            ✅ 完成
数据结构 + 算法      ✅ 完成 41 篇（18 个结构 + 23 个算法）
数据库 + MyBatis     ✅ 完成
Spring Boot 单体     ✅ 完成（demo4）
微服务架构           ✅ 完成 8/10（demo5/6/7 + Nacos + Gateway + Sentinel）
RocketMQ             ⬜ 待学习
Redis                ⬜ 待学习
Docker               ⬜ 待学习
综合项目实战         ⬜ 待学习
```

---

## 说明

本仓库用于记录个人学习过程，代码以「理解原理」为目标，注释尽量详尽。
欢迎交流，但请勿直接用于生产环境（未做完整的安全与异常处理）。

---

## License

本项目基于 [MIT License](LICENSE) 开源。

用大白话说，MIT 允许你：

- 自由地复制、修改、分发这份代码
- 用在商业项目里
- 唯一的要求：保留原作者的版权声明（也就是 `LICENSE` 文件）

作者不承担任何责任 —— 代码按「原样」提供，出了问题自己承担（学习代码本来就别直接上生产）。

**例外说明**：`practice/02-oop-course/` 下的练习代码源自 JetBrains 课程《Liu Course Java OOP》，版权归课程原作者，此处仅作为个人学习记录收录，不适用本仓库的 MIT 授权。

