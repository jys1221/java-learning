# Java 学习错题本

> **怎么用**：跟我说一句「存错题：……」或者「存今天的错题」，我就往这个文件里加一条。
> 默认只在**你让我存**的时候才写；遇到特别值得记的坑，我会先问一句「要存吗」。
>
> **打开方式**：记事本、IDEA、VS Code 都能打开。想排版好看，随时让我导出成 Word 或 PDF。

- **文件路径**：`C:\Users\jin\Doubao\chats\2026-10-11\new-chat\Java错题本.md`
- **建立日期**：2026-11-12

---

## 统计

| 分类 | 条数 |
|---|---|
| 一、环境配置（IDEA / Maven / JDK） | 3 |
| 二、Java 基础语法 | 6 |
| 三、Spring Boot | 1 |
| 四、Spring Cloud 微服务 | 2 |
| 五、Sentinel 限流熔断 | 3 |
| **合计** | **15** |

---

## 一、环境配置（IDEA / Maven / JDK）

| 日期 | 报错 / 现象 | 真正原因 | 怎么解决 | 下次怎么避免 |
|---|---|---|---|---|
| 2026-11-10 | `Unable to create injector ... No implementation for ArtifactHandlerManager was bound`（Guice 报错） | IDEA 用了 Maven Wrapper，Wrapper 版本和 IDEA 不匹配 | 设置 → 构建工具 → Maven → 主路径改成「IDEA 内置 Maven」，取消 Wrapper 后 Reload | 新建或复制项目后，第一件事就是确认用的是内置 Maven |
| 2026-11-10 | `java: 无法编译为 JVM 目标 5 配置的模块 'demo5'` | 模块的语言级别被设成 5（旧默认），GraalVM 24 不支持 | Project Structure → 模块 → 语言级别改成 17 | 新项目先检查 SDK 和语言级别，别等报错才查 |
| 2026-11-10 | `System::load ... restricted method`（Tomcat WARNING） | JDK 24 对 native 访问的警告，不影响运行 | 忽略即可 | 看到 WARNING 先确认服务是否正常启动，别被红黄字吓到 |

## 二、Java 基础语法

| 日期 | 报错 / 现象 | 真正原因 | 怎么解决 | 下次怎么避免 |
|---|---|---|---|---|
| 2026-11-12 | 一整片红字，提示 `Cannot resolve symbol` | import 的包名写错：`jakar.servlet` 应为 `jakarta.servlet` | 改成 `jakarta.servlet.http` | 抄 import 时逐字核对，注意是 **jakarta** 不是 jakar |
| 2026-11-12 | `Cannot resolve method 'setSataus'` | 方法名拼错：`Sataus` → `Status` | 改成 `setStatus` | 长方法名一律靠 IDEA 补全，不手打 |
| 2026-11-12 | `';' expected`（语法错误） | `print(...)` 结尾漏了分号 | 补上分号 | 写完一条语句立刻扫一眼结尾 |
| 2026-11-12 | 返回的 JSON 字段是 `"mag"` | 字符串拼写错，应为 `"msg"` | 改成 `msg` | 手写 JSON / 字符串时逐字核对 |
| 2026-11-12 | `Unhandled exception: java.lang.InterruptedException` | `Thread.sleep` 抛受检异常，方法没声明 throws | 方法签名加 `throws InterruptedException` | 用 sleep / IO 这类方法前，先想「谁负责处理异常」 |
| 2026-11-12 | import 的类根本不存在（`BlockExceptionHander`） | 类名凭记忆写错，正确的是 `BlockExceptionHandler` | 删掉错的，用 IDEA 自动导入 | 不凭记忆手写 import，用 Alt+Enter 自动导入 |

## 三、Spring Boot

| 日期 | 报错 / 现象 | 真正原因 | 怎么解决 | 下次怎么避免 |
|---|---|---|---|---|
| 2026-11-12 | 启动失败：`Ambiguous mapping. Cannot map 'helloController' method ... to {GET /callB}` | 两个方法都写了 `@GetMapping("/callB")` | 新接口路径改成 `/callBslow` | 新增接口前先搜一下这个路径有没有被占用 |

## 四、Spring Cloud 微服务

| 日期 | 报错 / 现象 | 真正原因 | 怎么解决 | 下次怎么避免 |
|---|---|---|---|---|
| 2026-11-08 | 访问 `/callB` 报 500 Whitelabel Error Page | 缺 `spring-cloud-starter-loadbalancer`，服务名无法解析成真实地址 | pom 里加 loadbalancer 依赖 | 用 `@LoadBalanced` + 服务名调用时，先确认这个依赖在 |
| 2026-11-08 | Nacos 里注册的 IP 是 `192.168.64.1`（VMware 虚拟网卡），服务间调用超时 | 自动挑了虚拟网卡 IP 注册 | 配置 `spring.cloud.nacos.discovery.ip=127.0.0.1` | 电脑装了虚拟机 / 多网卡时，显式指定注册 IP |

## 五、Sentinel 限流熔断

| 日期 | 报错 / 现象 | 真正原因 | 怎么解决 | 下次怎么避免 |
|---|---|---|---|---|
| 2026-11-12 | 重启 demo5 后限流完全失效，10 次请求全 200 | Sentinel 规则默认只存在控制台内存里，重启就丢 | 规则持久化到 Nacos（`sentinel-datasource-nacos` + `FlowRuleManager / DegradeRuleManager`） | 生产环境的规则一定要接数据源，别只在控制台点 |
| 2026-11-12 | 控制台「簇点链路」只有 sentinel-dashboard 自己的资源，看不到 demo5 | 页面默认显示 dashboard 自身，业务应用没被选中 | 用 `GET /app/briefinfos.json` 确认 demo5 已接入；改用 API 下发规则 | 页面看不见时，先用 API 验证服务到底接入没有 |
| 2026-11-12 | 自定义限流应答不生效，429 返回空白页 | 类没加 `@Component`，或没实现 `BlockExceptionHandler` | 加 `@Component` 并实现接口 | 想让 Sentinel 调你的类，必须让它进 Spring 容器 |

---

## 复习方法（重要）

每周末花 15 分钟，**只看「报错 / 现象」这一列，遮住后面三列**，自己想：

1. 这个错是什么原因？
2. 我会怎么修？
3. 下次怎么避免踩到？

想不起来的，做个记号。**想不起来的那几条，就是你真正的知识漏洞。**

---

## 记录模板（手动加时照着填）

```
| 日期 | 报错 / 现象 | 真正原因 | 怎么解决 | 下次怎么避免 |
| 2026-11-12 | 具体的报错文字或现象 | 一句话说清根因 | 实际做了什么操作 | 一条可执行的预防规则 |
```
