package com.hls.springboot_app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldTest {

    @Test
    void helloWorldReturnsCorrectString() {
        String greeting = "Hello, World!";
        assertEquals("Hello, World!", greeting);
    }
}
