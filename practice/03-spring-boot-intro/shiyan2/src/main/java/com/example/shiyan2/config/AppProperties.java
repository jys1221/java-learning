package com.example.shiyan2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置绑定示例：使用 @ConfigurationProperties 把「一组」配置批量绑定到对象属性上。
 *
 * prefix = "app"：表示读取配置文件中所有以 app. 开头的配置项，
 * 例如 app.name / app.version / app.author，会自动对应到下面的 name / version / author。
 *
 * @Data（Lombok 注解）：自动生成 getter、setter、toString 等方法，省去手写代码。
 * @Component：交给 Spring 容器管理，Controller 才能注入它。
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /** 对应 app.name */
    private String name;

    /** 对应 app.version */
    private String version;

    /** 对应 app.author */
    private String author;
}
