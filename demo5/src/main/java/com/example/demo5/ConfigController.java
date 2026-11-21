// ==========================================================================
// 文件作用：
//   演示"配置中心"。这个类负责从 Nacos 配置中心读取配置项，并通过 /config 接口显示出来。
//   核心目的：证明"改 Nacos 里的配置，服务不重启，接口返回值就会自动变"。
// ==========================================================================
package com.example.demo5;

// @Value：从配置文件（这里是从 Nacos 拉下来的配置）里读取某个键的值，赋给字段
import org.springframework.beans.factory.annotation.Value;
// @RefreshScope：开启"热更新"能力。
//   加了它，Nacos 里的配置一改动，Spring 会重新创建这个对象，
//   让字段重新读取最新值；不加它，配置改了也不会生效（除非重启服务）
import org.springframework.cloud.context.config.annotation.RefreshScope;
// @GetMapping：把方法绑定到 GET 请求的某个路径
import org.springframework.web.bind.annotation.GetMapping;
// @RestController：这个类里的方法返回值直接作为响应内容
import org.springframework.web.bind.annotation.RestController;

// @RestController：声明这是接口类
// @RefreshScope：这个类里的配置值支持热更新（顺序无所谓，都是注解）
@RestController
@RefreshScope
public class ConfigController {

    // @Value("${键名:默认值}")：
    //   从配置里读取 custom.message 这一项的值，赋给下面的 message 字段。
    //   冒号后面的部分是默认值：如果 Nacos 里还没配这一项，就用这个默认文字。
    //   这个值来源于 application.properties 里配置的
    //     spring.config.import=optional:nacos:demo5.properties
    //   也就是 Nacos 配置列表里 Data ID 为 demo5.properties 的那条配置。
    @Value("${custom.message:（Nacos里还没配这一项）}")
    private String message;

    // @GetMapping("/config")：浏览器访问 GET http://localhost:8081/config 时执行
    //   返回当前读到的配置值，用来验证"改了 Nacos，这里马上跟着变"
    @GetMapping("/config")
    public String config() {
        return "当前配置的值: " + message;
    }
}
