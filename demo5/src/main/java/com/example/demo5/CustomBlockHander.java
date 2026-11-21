// ==========================================================================
// 文件作用：
//   当用户的请求被 Sentinel 限流拦下时，决定给用户返回什么内容。
//   如果不写这个类，被拦下的请求只会返回一个空白的 429 页面，
//   用户看到白屏，不知道发生了什么。
// ==========================================================================
package com.example.demo5;

// ------------------------- 下面这些 import 是干什么的 -------------------------
// Sentinel 提供的"限流处理岗位说明书"（接口）：本类要应聘这个岗位才能被调用
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
// Sentinel 的"拦截原因"类型：用来说明这次是被限流拦的、还是被熔断拦的
import com.alibaba.csp.sentinel.slots.block.BlockException;
// Servlet 的"请求"对象：代表用户发过来的这一次请求
import jakarta.servlet.http.HttpServletRequest;
// Servlet 的"响应"对象：代表要返回给用户的内容，我们往它里面写东西
import jakarta.servlet.http.HttpServletResponse;
// Spring 的注解：给类贴标签，让 Spring 启动时创建它并放进容器管理
import org.springframework.stereotype.Component;

// @Component：告诉 Spring "这个类归你管"。
//   Spring 启动时会扫描所有带这个注解的类，自动 new 一个对象放进容器。
//   如果没有它，Spring 根本不知道有这个类，被限流时也就不会调到下面的 handle 方法。
@Component
// implements BlockExceptionHandler：应聘"限流处理"这个岗位。
//   岗位说明书（接口）只要求会一件事：handle —— 被拦下时怎么回应。
public class CustomBlockHander implements BlockExceptionHandler {
    // @Override：表示下面这个方法是"覆盖"接口里规定必须实现的那个方法，
    //   加它可以让编译器帮我们核对方法签名有没有写错。
    //
    // 三个参数都是 Sentinel 主动递进来的，不需要我们自己 new：
    //   request  —— 用户发来的请求（这里一般用不到，但接口规定了必须收）
    //   response —— 要返回给用户的响应，内容就写进它
    //   e        —— 本次被拦截的原因对象（限流、熔断、热点参数等）
    //
    // throws Exception：这个方法内部可能出错，出错时就抛出去，交给调用它的 Sentinel 处理
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {

        // 第 1 步：把 HTTP 状态码设为 429
        //   429 = Too Many Requests（请求太多），是业界表示"被限流"的标准状态码
        response.setStatus(429);

        // 第 2 步：告诉浏览器"我返回的是 JSON 格式，并且用 UTF-8 编码"
        //   如果不写 charset=UTF-8，浏览器可能把中文显示成乱码
        response.setContentType("application/json;charset=UTF-8");

        // 第 3 步：把内容写进响应体，浏览器最终就会看到这段文字
        //   注意：Java 字符串里的双引号必须写成 \" 进行转义，
        //   否则 Java 会以为字符串提前结束了，导致语法错误
        response.getWriter().print("{\"code\":429,\"msg\":\"系统繁忙，请稍后再试\"}");
    }
}
