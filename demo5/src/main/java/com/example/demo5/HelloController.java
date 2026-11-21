// ==========================================================================
// 文件作用：
//   demo5（服务 A）对外的两个接口：
//     /hello —— 谁访问就返回一句介绍自己的话
//     /callB —— 服务 A 主动去调用服务 B（demo6），把 B 的回应拼在自己后面返回
//   这个文件主要是用来演示"微服务之间怎么互相调用"。
// ==========================================================================
package com.example.demo5;

// @Autowired：让 Spring 自动把容器里现成的对象塞进这个字段，不用自己 new
import org.springframework.beans.factory.annotation.Autowired;
// @GetMapping：把方法绑定到"GET 请求 + 某个路径"上
import org.springframework.web.bind.annotation.GetMapping;
// @RestController：告诉 Spring 这个类是"接口类"，
//   方法返回的字符串会直接作为响应内容写回浏览器（而不是跳转页面）
import org.springframework.web.bind.annotation.RestController;
// 发 HTTP 请求的工具（这里用的是启动类里加过 @LoadBalanced 的那个）
import org.springframework.web.client.RestTemplate;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

// @RestController = @Controller + @ResponseBody 的组合
//   有了它，下面的方法 return 什么，浏览器就看到什么
@RestController
public class HelloController {

    // @Autowired：从 Spring 容器里找出一个 RestTemplate 对象注入进来。
    // 注意：这个对象是启动类里用 @Bean @LoadBalanced 创建的那个，
    //   所以它自带"按服务名查 Nacos"的能力。
    @Autowired
    private RestTemplate restTemplate;

    // @GetMapping("/hello")：浏览器访问 GET http://localhost:8081/hello 时会执行这个方法
    //   方法返回的字符串会原样显示在浏览器里
    @GetMapping("/hello")
    public String hello() {
        return "你好，我是服务A（端口8081）";
    }

    // @GetMapping("/callB")：浏览器访问 GET http://localhost:8081/callB 时执行
    @GetMapping("/callB")
    public String callB() {
        // getForObject(地址, 返回类型)：发 GET 请求并拿到响应内容
        // 关键点：地址里写的是服务名 demo6，不是 IP！
        //   @LoadBalanced 会自动去 Nacos 查到 demo6 的真实地址，再发请求
        String result = restTemplate.getForObject("http://demo6/hello", String.class);
        // 把 B 的回应拼到 A 自己的话后面，一起返回给浏览器
        return "我是服务A,我去问了服务B(通过Nacos电话簿找到它),他说:" + result;
    }
// @GetMapping("/callBslow")：访问这个地址，服务A去调服务B的慢接口
// @SentinelResource：
//   value="callB_slow" —— 监控名，Nacos里的熔断规则按这个名字找它
//   blockHandler="callBslowBlocked" —— 被熔断时改调那个兜底方法
    @GetMapping("/callBslow")
    @SentinelResource(value = "callB_slow",blockHandler = "callBslowBlocked")
    public String callBslow() {
        String result = restTemplate.getForObject("http://demo6/slow", String.class);
        return "服务A去问服务B(慢接口),他说:" + result;
    }
    public String callBslowBlocked(BlockException e) {
        return "【已熔断】服务B太慢,我先给你兜底:稍后再试";}

    @GetMapping("/callBfast")
    @SentinelResource(value = "callB_fast",blockHandler ="callBfastBlocked")
    public String callBfast() {
        String result = restTemplate.getForObject("http://demo6/hello", String.class);
        return "服务A去问服务B(快接口),他说:" + result;
    }
    // 兜底方法：被熔断时执行，直接返回这句话，不再去调 demo6
// 三条硬规则：public、返回值是String、参数只有 BlockException
    public String callBfastBlocked(BlockException e) {
        return "【已熔断】请求太多了，缓一缓再来";
    }

}
