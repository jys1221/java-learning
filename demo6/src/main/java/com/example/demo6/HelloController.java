package com.example.demo6;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "你好，我是服务B（端口8082）";  // 返回内容自己可以改
    }
    @GetMapping("/slow")
    public String slow()throws InterruptedException {
        Thread.sleep(1000);
        return "我是服务B,我很慢,让你等了1秒";
    }
}

