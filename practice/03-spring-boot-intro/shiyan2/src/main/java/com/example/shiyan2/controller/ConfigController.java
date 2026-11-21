package com.example.shiyan2.controller;

import com.example.shiyan2.config.AppProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST 接口：把配置文件中的自定义配置以 JSON 形式返回，用于验证配置绑定成功。
 *
 * @RestController = @Controller + @ResponseBody，方法返回值会直接序列化为 JSON。
 * @RequestMapping("/api")：类级别的路径前缀，最终访问地址为 /api/info。
 */
@RestController
@RequestMapping("/api")
public class ConfigController {

    /**
     * 被注入的配置对象。用 final 修饰，配合构造器注入，
     * 这是 Spring 官方推荐的依赖注入方式（属性不可变、便于测试）。
     */
    private final AppProperties appProperties;

    /**
     * 构造器注入：Spring 容器创建 Controller 时，会自动把容器里的 AppProperties 传进来。
     * 只有一个构造器时不需要额外写 @Autowired。
     *
     * @param appProperties 已绑定好配置值的 AppProperties 对象
     */
    public ConfigController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * 处理 GET /api/info 请求。
     * 返回 AppProperties 对象，Spring 会自动把它转换成 JSON：
     * {"name":"学生信息管理系统","version":"1.0.0","author":"实验组"}
     *
     * @return 绑定了配置值的 AppProperties
     */
    @GetMapping("/info")
    public AppProperties getAppInfo() {
        return appProperties;
    }
}
