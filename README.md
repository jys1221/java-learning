# Java 后端学习路线：从单体应用到微服务

一个完整记录「Java 后端从零到微服务」学习过程的仓库。

包含 **4 个可运行的 Spring Boot 工程** + **全套学习笔记**，覆盖从单体增删改查到微服务治理的完整链路。

---

## 目录结构

```
java-learning/
├── demo4/          # 单体应用：学生管理系统（Spring Boot + MyBatis + MySQL + Spring Security）
├── demo5/          # 微服务 A（8081）：对外接口 + 调用服务 B
├── demo6/          # 微服务 B（8082）：被调用方，含快/慢两个接口
├── demo7/          # API 网关（9000）：统一入口，按路径转发
└── docs/           # 学习笔记
    ├── Java后端学习总纲.md      # 知识地图（676 行 / 35 小节）
    ├── Java错题本.md            # 真实踩坑记录（15 条）
    ├── 微服务三实验验证.ps1      # 30 秒自检脚本
    ├── demo5-check.ps1          # 早期自检脚本（备用）
    └── java路线图进度.html       # 学习进度可视化
```

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
| [Java错题本](docs/Java错题本.md) | 15 条真实错误记录：报错现象 / 根本原因 / 解决方式 / 如何避免 |
| [微服务三实验验证](docs/微服务三实验验证.ps1) | PowerShell 自检脚本：一键验证热更新、限流、熔断三个机制 |

---

## 学习进度

```
Java 基础            ✅ 完成
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
