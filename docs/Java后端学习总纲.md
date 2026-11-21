# Java 后端学习总纲

> **这是什么**：你从零到现在所有知识点、关键代码、核心原理的汇总。不是教程，是**能随时翻的地图**。
>
> **配套文件**（同一目录下）：
> - `Java错题本.md` —— 真实踩过的坑（带解决方案）
> - `微服务三实验验证.ps1` —— 30 秒自检脚本
>
> **怎么用**：
> - **复习**：按目录从头顺一遍，约 40 分钟
> - **卡壳**：`Ctrl+F` 搜关键词，直接跳小节
> - **面试**：只看每节的「一句话原理」和「关键代码」

## 目录

- [第一部分 Java 基础](#第一部分-java-基础)
- [第二部分 数据库与持久层](#第二部分-数据库与持久层)
- [第三部分 Spring Boot 单体应用](#第三部分-spring-boot-单体应用)
- [第四部分 微服务架构](#第四部分-微服务架构)
- [第五部分 环境与自检](#第五部分-环境与自检)
- [第六部分 待学清单](#第六部分-待学清单)
- [附录 一条请求的完整链路](#附录-一条请求的完整链路)

---

# 第一部分 Java 基础

## 1.1 程序的基本结构

**一句话原理**：Java 世界是「包 → 类 → 方法 → 语句」四级嵌套，和文件夹 → 文件 → 段落 → 句子一样。

```java
package com.example.demo5;          // 包：相当于文件夹，防止类名冲突

public class HelloController {      // 类：一个功能模块
    public String hello() {         // 方法：一件具体能做的事
        return "你好";              // 语句：最小执行单位
    }
}
```

- 一个 `.java` 文件里**只能有一个 public 类**，且文件名必须和类名一致
- 包名一般用「公司域名倒写 + 项目名」，如 `com.example.demo5`

---

## 1.2 变量与数据类型

**一句话原理**：基本类型存「值」，引用类型存「地址」。

| 类别 | 类型 | 例子 |
|---|---|---|
| 整数 | `int`、`long` | `int age = 20;` |
| 小数 | `double`（默认）、`float` | `double score = 88.5;` |
| 真假 | `boolean` | `boolean ok = true;` |
| 字符 | `char` | `char c = 'A';` |
| 字符串 | `String`（引用类型） | `String name = "佳良";` |

```java
// 基本类型：变量里装的就是数字本身
int age = 20;

// 引用类型：变量里装的是"对象在内存里的地址"
String name = "佳良";
```

> **坑**：`String` 不是基本类型，是类。所以它能调方法（`name.length()`），而 `int` 不能。

---

## 1.3 面向对象的三大特性

**一句话原理**：封装 = 藏起来，继承 = 拿过来，多态 = 同一个接口不同实现。

```java
// 封装：字段私有，只暴露方法
public class Student {
    private int age;                    // 外面不能直接改
    public int getAge() { return age; } // 只能通过方法读
    public void setAge(int a) { this.age = a; }
}

// 继承：子类 extends 父类，拿到父类的方法
class Animal { void speak() {} }
class Dog extends Animal { }

// 多态：父类引用指向子类对象
Animal a = new Dog();     // 编译时看 Animal，运行时跑 Dog
```

---

## 1.4 集合

**一句话原理**：数组长度固定，集合长度可变——三种集合对应三种需求。

| 集合 | 特点 | 什么时候用 |
|---|---|---|
| `List`（ArrayList） | 有序、可重复、按下标取 | 存列表数据（学生列表） |
| `Set`（HashSet） | 无序、不可重复 | 去重 |
| `Map`（HashMap） | 键值对，按 key 取 | 存"名字 → 值"（配置、字典） |

```java
// List：像排队，有顺序，可以有重复
List<Student> list = new ArrayList<>();
list.add(student);

// Map：像字典，一个 key 对一个 value
Map<String, Integer> scores = new HashMap<>();
scores.put("语文", 90);
int chinese = scores.get("语文");
```

---

## 1.5 异常：受检 vs 运行时

**一句话原理**：受检异常编译器**逼你处理**，运行时异常编译器**不管**。

| 类型 | 例子 | 编译器态度 |
|---|---|---|
| 受检异常（Exception） | `InterruptedException`、`IOException` | 不处理就编译报错 |
| 运行时异常（RuntimeException） | `NullPointerException`、`ArrayIndexOutOfBoundsException` | 不强制，运行时才炸 |

```java
// Thread.sleep 抛的是受检异常，方法必须声明 throws，或者 try-catch
public String slow() throws InterruptedException {
    Thread.sleep(1000);          // 睡 1 秒
    return "我很慢";
}
```

> **踩过的坑**：第一次写 `/slow` 时漏了 `throws InterruptedException`，编译直接报 `Unhandled exception`。

---

## 1.6 注释 vs 注解（最容易混）

**一句话原理**：注释给人看（编译器丢掉），注解给框架看（运行时会执行）。

| | 注释 comment | 注解 annotation |
|---|---|---|
| 长什么样 | `// 文字`、`/* 文字 */` | `@Component`、`@GetMapping` |
| 谁读它 | 人 | Spring / Sentinel 等框架 |
| 作用 | 说明、备忘、临时屏蔽代码 | 改变程序行为 |
| 编译器 | 直接忽略 | 会解析 |

### 三种注释写法

```java
// ① 单行注释 —— 日常 90% 用它
int a = 1;   // 行尾也能写

/* ② 多行注释
   用来临时屏蔽一整段代码 */

/**
 * ③ 文档注释（Javadoc）
 * 写在 public 类/方法上，可被工具提取成 API 文档
 * @param name 参数说明
 * @return 返回值说明
 */
```

### 注释该写什么

**唯一的判断标准**：「三个月后的我，只看注释能不能懂？」

```java
// ❌ 废话：把代码翻译一遍
i++;                          // i 加 1

// ✅ 有用：说清"为什么"
i++;                          // 跳过表头那一行，数据从第二行开始

// ❌ 废话
restTemplate.getForObject("http://demo6/hello", String.class);   // 调用服务B

// ✅ 说清关键设计
restTemplate.getForObject("http://demo6/hello", String.class);   // 写服务名而不是IP，靠Nacos解析
```

> **口诀：注释写「为什么」，不写「是什么」。**

---

## 1.7 import：不用背，用 Alt+Enter

**一句话原理**：写类名 → 变红 → `Alt+Enter` → IDEA 自动补 import。

### 真实流程

1. 先想清楚要干什么（"我要拿到请求对象"）
2. 直接写类名：`HttpServletRequest request`
3. 变红 → 光标放红字上 → `Alt + Enter` → 选 `Import class`
4. IDEA 自动补上完整路径

### 只有一种情况需要你判断：同名类冲突

```
Date → java.util.Date（普通日期）  vs  java.sql.Date（数据库日期）
```

**判断方法：看这段代码在干什么。** 在操作数据库字段 → 选 sql；否则 → 选 util。

### 领域 → 包 速查表

| 要做的事 | 类大概在哪 | 关键词 |
|---|---|---|
| 写 Web 接口 | `@RestController`、`@GetMapping` | `web.bind.annotation` |
| 让 Spring 管类 | `@Component`、`@Service` | `stereotype` |
| 读配置 / 注入 | `@Value`、`@Autowired` | `beans.factory.annotation` |
| 发 HTTP 请求 | `RestTemplate` | `web.client` |
| 拿请求 / 写响应 | `HttpServletRequest`（**jakarta** 不是 jakar） | `servlet.http` |
| 限流熔断 | `@SentinelResource`、`BlockException` | `sentinel` |
| 数据库操作 | `JdbcTemplate`、`@Mapper` | `jdbc` / `mybatis` |
| 集合工具 | `List`、`Map` | `java.util` |
| 文件读写 | `File`、`Files` | `java.io` / `java.nio` |
| 日期时间 | `LocalDate` | `java.time` |

> **铁律：永远不手写 import。** 你踩过的两个坑（`jakar.servlet`、`BlockExceptionHander`）都是手写导致的。

---

# 第二部分 数据库与持久层

## 2.1 MySQL 基本结构

**一句话原理**：库 → 表 → 行（记录）→ 列（字段）。

```sql
-- 建库
CREATE DATABASE school;

-- 建表：四列，id 自增主键
CREATE TABLE student (
    id    INT PRIMARY KEY AUTO_INCREMENT,   -- 主键，自动编号
    name  VARCHAR(50),                      -- 姓名，最多 50 字符
    age   INT,                              -- 年龄
    score DOUBLE                            -- 分数
);
```

---

## 2.2 五条必会 SQL

```sql
-- 查全部
SELECT * FROM student;

-- 条件查
SELECT * FROM student WHERE age > 18;

-- 新增
INSERT INTO student(name, age, score) VALUES ('张三', 20, 88);

-- 修改
UPDATE student SET score = 90 WHERE id = 1;

-- 删除
DELETE FROM student WHERE id = 2;
```

> **注意**：`UPDATE` / `DELETE` 一定要带 `WHERE`，否则会改/删全表。

---

## 2.3 JDBC 的核心四步（原理，MyBatis 底下就是它）

**一句话原理**：拿连接 → 发语句 → 取结果 → 关连接。

```java
Connection conn = DriverManager.getConnection(url, user, pwd);   // 1. 连接
PreparedStatement ps = conn.prepareStatement("SELECT * FROM student WHERE id = ?");
ps.setInt(1, 1);                                                 // 2. 填参数
ResultSet rs = ps.executeQuery();                                // 3. 查
while (rs.next()) { System.out.println(rs.getString("name")); }
rs.close(); ps.close(); conn.close();                            // 4. 关
```

---

## 2.4 MyBatis

**一句话原理**：你写 SQL + 接口，MyBatis 帮你把结果自动装进对象。

```java
// Mapper 接口：只声明方法，不写实现（MyBatis 用动态代理生成）
@Mapper
public interface StudentMapper {

    // @Select 里写 SQL，返回值自动映射成 List<Student>
    @Select("SELECT * FROM student")
    List<Student> findAll();

    // #{id} 是参数占位符，会被预编译（防 SQL 注入）
    @Select("SELECT * FROM student WHERE id = #{id}")
    Student findById(int id);
}
```

### `#{}` 和 `${}` 的区别（面试常问）

| 写法 | 行为 | 安全性 |
|---|---|---|
| `#{id}` | 预编译，参数当值传入 | **安全**，防注入 |
| `${id}` | 直接拼接字符串 | 危险，能被注入 |

> **优先一律用 `#{}`。**

---

# 第三部分 Spring Boot 单体应用

## 3.1 Spring 到底做了什么

**一句话原理**：Spring 是一个「对象工厂」——对象由它创建、由它保管、由它塞给你。

```java
// 你声明"我要用"，Spring 就把现成对象塞进来，不用自己 new
@Autowired
private RestTemplate restTemplate;
```

- **IoC**（控制反转）：对象的创建权从"你自己 new"变成"Spring 给你"
- **依赖注入**：`@Autowired` 就是"给我一个现成的"

---

## 3.2 启动类的三个职责

```java
@SpringBootApplication   // = 三合一：配置类 + 自动配置 + 扫描本包
public class Demo5Application {

    // @Bean：亲手往"对象工厂"里放一个对象
    @Bean
    @LoadBalanced        // 让这个 RestTemplate 会"按服务名找地址"
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo5Application.class, args);
    }
}
```

- `@SpringBootApplication` 只扫描**自己所在包及子包**——所以启动类必须放在最外层
- 别人写的类要靠 `@ComponentScan` 才能扫到

---

## 3.3 三层架构（写任何业务都按这个分）

| 层 | 注解 | 职责 | 不该做什么 |
|---|---|---|---|
| Controller | `@RestController` | 收请求、返回结果 | 不写复杂业务 |
| Service | `@Service` | 业务逻辑、事务 | 不直接处理 HTTP |
| Mapper | `@Mapper` | 只碰数据库 | 不写业务判断 |

---

## 3.4 接口注解速查

```java
@RestController                       // 这个类是接口类，返回值直接写回浏览器
public class StudentController {

    @GetMapping("/students")          // GET 请求
    public List<Student> list() { ... }

    @PostMapping("/students")         // POST 请求
    public String add(@RequestBody Student s) { ... }   // 从请求体 JSON 取

    @GetMapping("/students/{id}")     // 路径参数
    public Student one(@PathVariable int id) { ... }

    @GetMapping("/search")            // 查询参数 ?name=xx
    public List<Student> search(@RequestParam String name) { ... }
}
```

| 注解 | 参数从哪来 | 例子 |
|---|---|---|
| `@PathVariable` | URL 路径里 | `/students/3` → id=3 |
| `@RequestParam` | `?key=value` | `/search?name=张三` |
| `@RequestBody` | 请求体 JSON | POST 传对象 |

---

## 3.5 配置文件

```properties
# application.properties —— Spring Boot 启动时自动读
server.port=8081                              # 端口
spring.datasource.url=jdbc:mysql://localhost:3306/school
spring.datasource.username=root
spring.datasource.password=你的数据库密码
```

---

## 3.6 Spring Security（demo4 登录）

**一句话原理**：认证 = 你是谁；授权 = 你能干什么。

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**").permitAll()   // 登录页和静态资源放行
                .anyRequest().authenticated()                      // 其他都要登录
            )
            .formLogin(form -> form.loginPage("/login").permitAll())
            .logout(logout -> logout.permitAll());
        return http.build();
    }

    // 密码用 BCrypt 加密存储，不明文存数据库
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

> **重点**：密码永远不明文入库；登录成功后 Spring Security 会写一个 Session。

---

# 第四部分 微服务架构

## 4.1 为什么要拆微服务

| 单体应用的问题 | 微服务的解法 |
|---|---|
| 一个模块崩，整站崩 | 各服务独立，互不影响 |
| 改一行要全量重新发布 | 只发布改动的那个服务 |
| 只能整体扩容，浪费机器 | 哪个服务压力大就单独加机器 |
| 不同团队改同一个项目互相冲突 | 各团队管各自的服务 |

**代价**：服务变多 → 地址管理、配置管理、流量治理全变复杂 → 所以才有 Nacos、Gateway、Sentinel。

---

## 4.2 你现在的服务清单

| 服务 | 端口 | 角色 | 主要接口 |
|---|---|---|---|
| demo5 | 8081 | 服务 A | `/hello`、`/callB`、`/callBslow`、`/callBfast`、`/config` |
| demo6 | 8082 | 服务 B | `/hello`、`/slow`（睡 1 秒） |
| demo7 | 9000 | 网关 | `/a/**` → demo5，`/b/**` → demo6 |
| Nacos | 8848 | 注册中心 + 配置中心 | 控制台 `/nacos` |
| Sentinel 控制台 | 8858 | 流量治理台 | 登录 `sentinel/sentinel` |

---

## 4.3 服务间调用：RestTemplate

**一句话原理**：A 要问 B，就发一个 HTTP 请求过去。

```java
@GetMapping("/callB")
public String callB() {
    // 关键：地址里写"服务名 demo6"，不是 IP
    String result = restTemplate.getForObject("http://demo6/hello", String.class);
    return "我是服务A,我去问了服务B,他说:" + result;
}
```

| 方法 | 用途 |
|---|---|
| `getForObject(url, 类型)` | 发 GET，直接拿返回内容 |
| `postForObject(url, 参数, 类型)` | 发 POST |

---

## 4.4 注册中心 Nacos：解决"地址从哪来"

**一句话原理**：服务启动时自己"报户口"，调用方拿服务名来查地址。

**注册**（自动，加依赖 + 配置就生效）：

```properties
spring.application.name=demo6            # 我在户口本上的名字
spring.cloud.nacos.server-addr=localhost:8848
spring.cloud.nacos.discovery.ip=127.0.0.1 # 显式指定注册IP，避免选到虚拟网卡
```

**发现**：调用方写 `http://demo6/hello`，Nacos 负责翻译成 `127.0.0.1:8082`。

> **踩过的坑**：不配 `discovery.ip` 时，Nacos 注册的是 VMware 虚拟网卡 IP（192.168.x.x），服务间调用直接超时。

---

## 4.5 `@LoadBalanced`：让 RestTemplate 会查户口

```java
@Bean
@LoadBalanced        // 关键就在这一行
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

- **不加**：`http://demo6/hello` 会报"未知主机 demo6"
- **加了**：RestTemplate 会截取 `demo6` 这个服务名，去 Nacos 查真实地址

---

## 4.6 网关 Gateway：统一入口

**一句话原理**：所有外部请求先到网关，网关按路径分发给不同服务。

```properties
# 规则 1：浏览器访问 /a/xxx → 去掉 /a 前缀 → 转发给 demo5
spring.cloud.gateway.routes[0].id=demo5-route
spring.cloud.gateway.routes[0].uri=lb://demo5            # lb:// = 通过 Nacos 查实例，不写死 IP
spring.cloud.gateway.routes[0].predicates[0]=Path=/a/**   # 只要路径以 /a/ 开头就走这条规则
spring.cloud.gateway.routes[0].filters[0]=StripPrefix=1   # 去掉第一段路径（/a）

# 规则 2：浏览器访问 /b/xxx → 去掉 /b 前缀 → 转发给 demo6
spring.cloud.gateway.routes[1].id=demo6-route
spring.cloud.gateway.routes[1].uri=lb://demo6
spring.cloud.gateway.routes[1].predicates[0]=Path=/b/**
spring.cloud.gateway.routes[1].filters[0]=StripPrefix=1
```

访问 `http://localhost:9000/a/hello` → 实际打到 demo5 的 `/hello`。

### 三个关键词（缺一个就 404）

| 配置 | 作用 | 不加会怎样 |
|---|---|---|
| `lb://demo5` | `lb` = load balance，去 Nacos 查 demo5 的真实地址 | 得写死 IP，服务换端口就失效 |
| `Path=/a/**` | 断言：什么路径走这条规则 | 所有请求都走第一条规则 |
| `StripPrefix=1` | **去掉第一段路径**：`/a/hello` → `/hello` | 转过去变成 `/a/hello`，demo5 没这个路径 → **404** |

**好处**：外部只认 9000 一个端口，内部服务端口爱怎么改怎么改。

---

## 4.7 配置中心 Nacos Config：改配置不重启

**一句话原理**：配置放在 Nacos，服务启动时拉一份，之后 Nacos 一改，服务自动刷新。

```properties
# demo5 的 application.properties
spring.config.import=optional:nacos:demo5.properties   # 启动时去 Nacos 拉这个配置
spring.cloud.nacos.config.refresh-enabled=true         # 允许运行时刷新
```

```java
@RestController
@RefreshScope        // 关键：加了它，配置变了这个类里的 @Value 才会重新读
public class ConfigController {

    @Value("${custom.message:（Nacos里还没配这一项）}")   // 冒号后面是"取不到时的默认值"
    private String message;

    @GetMapping("/config")
    public String get() {
        return "当前配置的值: " + message;
    }
}
```

**实测结论**：改完 Nacos，3 秒内 `/config` 就返回新值，服务一次没重启。

---

## 4.8 Sentinel 限流

**一句话原理**：请求太多就把后面的一部分直接挡掉，返回 429，保护自己。

### 规则长什么样

```json
[{
  "resource": "/hello",     // 保护哪个资源（接口路径，或 @SentinelResource 的 value）
  "grade": 1,               // 1 = QPS 模式
  "count": 2,               // 每秒最多 2 个
  "limitApp": "default",
  "strategy": 0,
  "controlBehavior": 0      // 0 = 直接拒绝（快速失败）
}]
```

### 自定义被拦时的返回内容

```java
@Component                                    // 必须进 Spring 容器，否则 Sentinel 找不到
// 注意：工程里这个类的实际名字是 CustomBlockHander（少一个 l），是当初笔误留下的
//   能正常运行，但不规范；知道这件事就好，以后新写的类别跟着错
public class CustomBlockHander implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       BlockException e) throws Exception {
        response.setStatus(429);                              // HTTP 状态码
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print("{\"code\":429,\"msg\":\"系统繁忙，请稍后再试\"}");
    }
}
```

**实测**：连点 10 次 `/hello` → 前 2 次 200，后 8 次 `{"code":429,"msg":"系统繁忙，请稍后再试"}`。

---

## 4.9 Sentinel 熔断

**一句话原理**：下游变慢或老出错，就先别调它了，直接给用户兜底结果，等它恢复再试。

### 熔断规则

```json
[{
  "resource": "callB_slow",       // 对应 @SentinelResource 的 value
  "grade": 0,                     // 0 = 慢调用比例模式
  "count": 1000,                  // 超过 1000ms 算"慢"
  "slowRatioThreshold": 0.5,      // 慢请求占比 ≥ 50%
  "minRequestAmount": 5,          // 至少 5 个请求才统计
  "statIntervalMs": 10000,        // 统计窗口 10 秒
  "timeWindow": 10                // 熔断后 10 秒内不再尝试
}]
```

### 代码怎么配合

```java
@GetMapping("/callBslow")
@SentinelResource(
    value = "callB_slow",              // 资源名，和 Nacos 规则里的一模一样
    blockHandler = "callBslowBlocked"  // 被熔断时改调这个方法（字符串，编译器不检查！）
)
public String callBslow() {
    String result = restTemplate.getForObject("http://demo6/slow", String.class);
    return "服务A去问服务B(慢接口),他说:" + result;
}

// 兜底方法：三条硬规则 → public、返回值同原方法、参数只有 BlockException
public String callBslowBlocked(BlockException e) {
    return "【已熔断】服务B太慢,我先给你兜底:稍后再试";
}
```

### 熔断的三个状态（必须理解）

```html type="renderer">
<html style="margin:0;padding:0;">
<title>熔断器的三个状态</title>
<div style="font-family:'Microsoft YaHei',sans-serif; width:100%; box-sizing:border-box; padding:16px; background:#f8f9fa; border-radius:10px;">
  <div style="font-size:17px; font-weight:bold; color:#1a73e8; margin-bottom:14px;">熔断器不是开关，是带记忆的状态机</div>
  <div style="display:flex; flex-wrap:wrap; gap:12px; align-items:stretch;">
    <div style="flex:1 1 210px; background:#fff; border-radius:8px; padding:12px; border-top:4px solid #34a853;">
      <div style="font-weight:bold; color:#137333; font-size:14px;">① 关闭 Closed</div>
      <div style="font-size:12px; color:#555; margin-top:8px; line-height:1.9;">
        正常放行所有请求，<b>同时在统计</b>。<br>
        窗口内样本够了、慢比例超阈值 → 转「打开」
      </div>
    </div>
    <div style="flex:1 1 210px; background:#fff; border-radius:8px; padding:12px; border-top:4px solid #d93025;">
      <div style="font-weight:bold; color:#a50e0e; font-size:14px;">② 打开 Open</div>
      <div style="font-size:12px; color:#555; margin-top:8px; line-height:1.9;">
        <b>所有请求直接走兜底</b>，根本不去调下游。<br>
        持续 timeWindow（这里 10 秒）后 → 转「半开」
      </div>
    </div>
    <div style="flex:1 1 210px; background:#fff; border-radius:8px; padding:12px; border-top:4px solid #e8710a;">
      <div style="font-weight:bold; color:#b06000; font-size:14px;">③ 半开 Half-Open</div>
      <div style="font-size:12px; color:#555; margin-top:8px; line-height:1.9;">
        只放 <b>1 个</b>请求去试探。<br>
        这次快 → 转回「关闭」；<br>
        这次还慢 → <b>立刻重新打开</b>
      </div>
    </div>
  </div>
  <div style="background:#e8f0fe; border-left:4px solid #1a73e8; border-radius:6px; padding:10px 12px; margin-top:14px; font-size:13px; color:#333; line-height:1.9;">
    <b>两次实测结果不同，就是这个原因：</b><br>
    冷启动（关闭态）→ 攒到第 8 次才熔断；<br>
    刚熔断过（半开态）→ 第 1 次探测慢，第 2 次起立刻全兜底。<br>
    <b>配置没变，是状态不同。</b>
  </div>
</div>
```

---

## 4.10 规则持久化：为什么规则要存 Nacos

**一句话原理**：控制台里配的规则只存在内存，重启就没了；存 Nacos 才能活过重启。

```java
@Configuration
public class SentinelRuleConfig {

    // ---- 1. Nacos 地址、配置名 ----
    @Value("${spring.cloud.nacos.server-addr}")
    private String serverAddr;

    private static final String GROUP_ID = "DEFAULT_GROUP";
    private static final String FLOW_DATA_ID = "sentinel-demo5-flow-rules.json";       // 限流规则
    private static final String DEGRADE_DATA_ID = "sentinel-demo5-degrade-rules.json";  // 熔断规则

    // ---- 2. 服务启动时就把规则拉下来 ----
    @PostConstruct                                 // 表示"本类初始化完成后立即执行"
    public void loadRulesFromNacos() {
        loadFlowRules();
        loadDegradeRules();
    }

    // ---- 3. 加载限流规则 ----
    private void loadFlowRules() {
        // NacosDataSource 会持续监听配置变化，改了自动推送，不用重启
        NacosDataSource<List<FlowRule>> ds = new NacosDataSource<>(
            serverAddr, GROUP_ID, FLOW_DATA_ID,
            source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {})
        );
        FlowRuleManager.register2Property(ds.getProperty());   // 注册进 Sentinel
    }

    // ---- 4. 加载熔断规则（写法一模一样，只是数据类型换成 DegradeRule）----
    private void loadDegradeRules() {
        NacosDataSource<List<DegradeRule>> ds = new NacosDataSource<>(
            serverAddr, GROUP_ID, DEGRADE_DATA_ID,
            source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {})
        );
        DegradeRuleManager.register2Property(ds.getProperty());
    }

    // ---- 5. 让 @SentinelResource 注解真正生效 ----
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
```

**实测**：重启 demo5 后不手动配规则，10 次请求仍然是 `200×2 + 429×8`——规则自己从 Nacos 回来了。

---

## 4.11 微服务配置速查

```properties
# demo5/application.properties 全文关键项
spring.application.name=demo5
server.port=8081

# 注册中心
spring.cloud.nacos.server-addr=localhost:8848
spring.cloud.nacos.discovery.ip=127.0.0.1

# 配置中心
spring.config.import=optional:nacos:demo5.properties
spring.cloud.nacos.config.refresh-enabled=true

# Sentinel 控制台
spring.cloud.sentinel.transport.dashboard=localhost:8858
```

> **关键词拆解**：`server-addr` = 地址；`discovery.ip` = 注册时用的 IP；`config.import` = 从哪拉配置；`transport.dashboard` = 汇报给哪个控制台。

---

# 第五部分 环境与自检

## 5.1 启动顺序（每次开机都要）

| 顺序 | 启动什么 | 命令 / 操作 | 端口 |
|---|---|---|---|
| 1 | Nacos | 设好 `JAVA_HOME` 后跑 `D:\nacos\bin\startup.cmd -m standalone` | 8848 |
| 2 | Sentinel 控制台 | `java -Dserver.port=8858 -jar D:\sentinel-dashboard-1.8.6.jar` | 8858 |
| 3 | demo5 / demo6 | IDEA 里点绿色三角 | 8081 / 8082 |

> Nacos 约 20 秒、Sentinel 约 30 秒起来。**先起 Nacos 再起服务**，否则服务注册失败。

---

## 5.2 端口检查

```powershell
# 看某个端口有没有在监听
Get-NetTCPConnection -LocalPort 8081 -State Listen -ErrorAction SilentlyContinue

# 一次看多个
foreach ($port in @(8848, 8858, 8081, 8082)) {
    $c = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    if ($c) { "$port 在跑" } else { "$port 没跑" }
}
```

---

## 5.3 状态码含义

| 状态码 | 含义 | 在你这儿对应什么 |
|---|---|---|
| 200 | 正常 | 请求成功 |
| 404 | 路径不存在 | 接口路径写错 |
| 429 | 被限流 | Sentinel 拦下了 |
| 500 | 服务端出错 | 代码异常，看控制台日志 |
| 503 | 服务不可用 | 服务没起来 |

---

## 5.4 PowerShell 的坑（这台机器特有）

| 现象 | 原因 | 正确做法 |
|---|---|---|
| `Invoke-WebRequest` 抓不到 429 的响应体 | 异常分支里流已被消费 | 用浏览器或 `curl.exe -o 文件` |
| `.ps1` 里中文路径变乱码 | PowerShell 5.1 按 GBK 读 UTF-8 无 BOM | 脚本存成 **UTF-8 with BOM** |
| `curl.exe -w "%{http_code}"` 无输出 | 输出被吞 | 改 `-o 文件`，再读文件 |
| `-SkipHttpErrorCheck` 报参数不存在 | 这是 PowerShell 7 的参数 | 用 try/catch 包住 |

---

## 5.5 自检脚本

`微服务三实验验证.ps1`：一次跑完热更新、限流、熔断三个验证，结果写到 `D:\三实验验证结果.txt`。

**什么时候跑**：每次开机、每次隔几天没学、每次怀疑"我是不是忘了"。

---

# 第六部分 待学清单

| 优先级 | 内容 | 解决什么问题 | 状态 |
|---|---|---|---|
| 🔴 | **RocketMQ** | 服务间异步通信、削峰、解耦 | 未开始 |
| 🔴 | **Redis** | 缓存热点数据、分布式锁 | 未开始 |
| 🟡 | **Docker** | 把服务打包成镜像，一键运行 | 未开始 |
| 🟡 | **综合项目** | 把 demo4 拆成微服务 + 加缓存 + 加 MQ | 未开始 |
| 🟢 | **K8s / CI-CD** | 集群编排、自动发布 | 后续 |

---

# 附录 一条请求的完整链路

**这是把所有知识点串起来的图——面试时能讲清这条链路，就算真的懂了。**

```html type="renderer">
<html style="margin:0;padding:0;">
<title>一次请求经过的所有环节</title>
<div style="font-family:'Microsoft YaHei',sans-serif; width:100%; box-sizing:border-box; padding:16px; background:#f8f9fa; border-radius:10px;">
  <div style="font-size:17px; font-weight:bold; color:#1a73e8; margin-bottom:14px;">浏览器点一下，背后发生了什么</div>

  <div style="display:flex; flex-direction:column; gap:6px;">
    <div style="background:#fff; border-left:4px solid #1a73e8; border-radius:6px; padding:10px 12px;">
      <b style="color:#1a73e8; font-size:13px;">① 浏览器</b>
      <span style="font-size:13px; color:#444;">发起请求 → <code>http://localhost:9000/a/hello</code></span>
    </div>
    <div style="text-align:center; color:#888; font-size:13px;">↓</div>
    <div style="background:#fff; border-left:4px solid #6a1b9a; border-radius:6px; padding:10px 12px;">
      <b style="color:#6a1b9a; font-size:13px;">② 网关 demo7（9000）</b>
      <span style="font-size:13px; color:#444;">看路径 <code>/a/**</code> → 决定转给 demo5</span>
    </div>
    <div style="text-align:center; color:#888; font-size:13px;">↓</div>
    <div style="background:#fff; border-left:4px solid #d93025; border-radius:6px; padding:10px 12px;">
      <b style="color:#d93025; font-size:13px;">③ Sentinel 限流</b>
      <span style="font-size:13px; color:#444;">/hello 这一秒已经被访问 2 次了吗？是 → 直接返回 <b>429</b>，请求到此为止</span>
    </div>
    <div style="text-align:center; color:#888; font-size:13px;">↓ 放行</div>
    <div style="background:#fff; border-left:4px solid #34a853; border-radius:6px; padding:10px 12px;">
      <b style="color:#137333; font-size:13px;">④ demo5 Controller</b>
      <span style="font-size:13px; color:#444;">执行方法。如果方法上带 <code>@SentinelResource</code>，同时会被统计"快慢"</span>
    </div>
    <div style="text-align:center; color:#888; font-size:13px;">↓ 需要调别的服务时</div>
    <div style="background:#fff; border-left:4px solid #e8710a; border-radius:6px; padding:10px 12px;">
      <b style="color:#e8710a; font-size:13px;">⑤ 注册中心 Nacos（8848）</b>
      <span style="font-size:13px; color:#444;"><code>@LoadBalanced</code> 拿服务名 <code>demo6</code> 来查 → 得到 <code>127.0.0.1:8082</code></span>
    </div>
    <div style="text-align:center; color:#888; font-size:13px;">↓</div>
    <div style="background:#fff; border-left:4px solid #e8710a; border-radius:6px; padding:10px 12px;">
      <b style="color:#e8710a; font-size:13px;">⑥ demo6 处理并返回</b>
      <span style="font-size:13px; color:#444;">把结果一路返回到浏览器</span>
    </div>
    <div style="text-align:center; color:#888; font-size:13px;">↓（如果 demo6 太慢）</div>
    <div style="background:#fce8e6; border-left:4px solid #a50e0e; border-radius:6px; padding:10px 12px;">
      <b style="color:#a50e0e; font-size:13px;">⑦ 熔断接管</b>
      <span style="font-size:13px; color:#444;">慢请求比例超 50% → 熔断打开 → 后续请求不再等 demo6，直接走 <code>blockHandler</code> 兜底</span>
    </div>
  </div>

  <div style="background:#e8f0fe; border-left:4px solid #1a73e8; border-radius:6px; padding:10px 12px; margin-top:14px; font-size:13px; color:#333; line-height:1.9;">
    <b>还有两条隐形链路（不在请求路径上，但一直在跑）：</b><br>
    ⑧ <b>配置中心</b>：启动时拉 <code>demo5.properties</code>，运行中改了就热更新<br>
    ⑨ <b>规则中心</b>：限流/熔断规则存在 Nacos，启动自动加载，重启不丢
  </div>
</div>
```

---

## 最后一句

> **这张图能自己讲一遍，微服务就算毕业了。**
> 讲不清的地方，就是你的漏洞——回目录找对应小节，或者记进错题本。
