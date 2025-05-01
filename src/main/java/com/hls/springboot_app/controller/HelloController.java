package com.hls.springboot_app.controller;

import com.hls.springboot_app.model.HelloResponse;
import com.hls.springboot_app.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/")
    public HelloResponse getHello() {
        return helloService.sayHello();
    }
}
