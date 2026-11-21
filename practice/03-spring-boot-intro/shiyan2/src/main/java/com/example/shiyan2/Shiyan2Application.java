package com.example.shiyan2;

import com.example.shiyan2.config.AppInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 项目主启动类。
 *
 * @SpringBootApplication 是一个「组合注解」，等价于下面三个注解：
 * 1. @SpringBootConfiguration —— 声明这是一个 Spring Boot 配置类；
 * 2. @EnableAutoConfiguration —— 开启自动配置，根据依赖自动装配所需组件；
 * 3. @ComponentScan —— 扫描当前包及子包下的 @Component/@Service/@Controller 等组件。
 */
@SpringBootApplication
public class Shiyan2Application {

    public static void main(String[] args) {
        // SpringApplication.run() 会启动 Spring 容器并返回容器上下文对象，
        // 这里用变量接收返回值，才能进一步从容器中获取 Bean。
        ConfigurableApplicationContext context = SpringApplication.run(Shiyan2Application.class, args);

        // 从 Spring 容器中获取 AppInfo 这个 Bean（它由 @Component 注册进容器）
        AppInfo appInfo = context.getBean(AppInfo.class);

        // 调用打印方法，验证 @Value 是否成功读取到配置文件中的值
        appInfo.printAppInfo();
    }

}
