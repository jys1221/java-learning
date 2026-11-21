package com.example.shiyan2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置读取示例：使用 @Value 注解「逐项注入」配置文件中的值。
 *
 * @Component 的作用：把这个类交给 Spring 容器管理，
 * 这样启动类里才能通过 context.getBean(AppInfo.class) 拿到它。
 */
@Component
public class AppInfo {

    /**
     * 读取 application.yml 中的 spring.application.name。
     * ${} 是 Spring 的属性占位符语法，括号里写的是配置文件的「完整键名」。
     */
    @Value("${spring.application.name}")
    private String appName;

    /**
     * 读取 server.port 端口号。
     * 这里故意用 String 接收，是为了避免类型转换问题，打印时也不需要转换。
     */
    @Value("${server.port}")
    private String port;

    /**
     * 打印注入结果，用于在控制台观察配置是否读取成功。
     */
    public void printAppInfo() {
        // 输出格式与实验指导书保持一致：应用名称: shiyan2, 端口: 8088
        System.out.println("应用名称: " + appName + ", 端口: " + port);
    }

    /**
     * 实验指导书正文中提到的 print() 方法，内部直接复用 printAppInfo()，
     * 方便两种写法（print() / printAppInfo()）都能正常调用。
     */
    public void print() {
        printAppInfo();
    }
}
