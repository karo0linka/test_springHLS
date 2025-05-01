package com.hls.springboot_app.service;

import com.hls.springboot_app.model.HelloResponse;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public HelloResponse sayHello() {
        return new HelloResponse("¡Hola desde Spring Boot y Docker!");
    }
}
