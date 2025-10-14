package com.example.taskManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class TaskManagerApplicationTest {

    @Test
    void contextLoadsSuccessfully() {
        assertDoesNotThrow(() -> {
            // Context loading verification
        });
    }

    @Test
    void applicationStartsWithoutError() {
        assertDoesNotThrow(() -> TaskManagerApplication.main(new String[]{}));
    }
}
