package com.example.demo4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/sum")
    public String sum(@RequestParam int a, @RequestParam int b) {
        return "结果是" +(a+b);
    }

}