// ==========================================================================
// 文件作用：
//   整个 demo5 服务的"启动入口"。
//   运行 main 方法，Spring Boot 就会启动内嵌的 Tomcat，把本服务跑起来（端口 8081）。
//   这里同时创建了一个"会查 Nacos 电话簿"的 RestTemplate 工具，
//   供 HelloController 调服务 B（demo6）时使用。
// ==========================================================================
package com.example.demo5;

// Spring Boot 的启动工具：用来运行整个应用
import org.springframework.boot.SpringApplication;
// 核心注解 @SpringBootApplication：一键开启自动配置、组件扫描等功能
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 负载均衡注解：让 RestTemplate 认识"服务名"这种地址写法
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
// @Bean 注解：把方法的返回值注册成 Spring 容器里的一个对象
import org.springframework.context.annotation.Bean;
// 用来发 HTTP 请求的工具类（类似浏览器发请求）
import org.springframework.web.client.RestTemplate;

// @SpringBootApplication：这个注解包含三件事：
//   ① 开启自动配置（Spring Boot 帮你把各种组件配好）
//   ② 开启组件扫描（自动找到 @Component、@RestController 等类）
//   ③ 标记这是配置类
@SpringBootApplication
public class Demo5Application {

	// main 方法：整个程序的入口，右键运行/点绿色三角就是跑这里
	public static void main(String[] args) {
		// SpringApplication.run：启动 Spring Boot 应用
		//   第一个参数是启动类本身，第二个是命令行参数
		SpringApplication.run(Demo5Application.class, args);
	}

	// @Bean：把下面这个方法返回的对象交给 Spring 容器管理，
	//       以后别的地方用 @Autowired 就能直接拿到它，不用自己 new
	// @LoadBalanced：关键开关。加了它之后，RestTemplate 遇到
	//   http://demo6/hello 这种"服务名"地址，会自动去 Nacos 查 demo6 的真实 IP:端口
	//   如果不加，它会把 demo6 当成一个普通域名去解析，结果就是连不上
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		// 创建一个默认的 RestTemplate 对象并返回
		return new RestTemplate();
	}

}
