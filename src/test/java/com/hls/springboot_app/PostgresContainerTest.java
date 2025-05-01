package com.hls.springboot_app;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostgresContainerTest {

    @Test
    void postgresContainerIsRunning() {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")) {
            postgres.start();
            assertTrue(postgres.isRunning());
        }
    }
}
