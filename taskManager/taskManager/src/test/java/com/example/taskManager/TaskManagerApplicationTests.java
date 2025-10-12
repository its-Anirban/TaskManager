package com.example.taskManager;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TaskManagerApplicationTest {

    @Test
    void mainMethod_runsWithoutError() {
        assertDoesNotThrow(() -> {
            // run the main() directly → covers "start(args)" line
            TaskManagerApplication.main(new String[]{});
        });
    }

    @Test
    void startMethod_runsAndClosesContext() {
        assertDoesNotThrow(() -> {
            System.setProperty("server.port", "0"); // random port
            ConfigurableApplicationContext ctx = TaskManagerApplication.start(new String[]{});
            ctx.close();
            System.clearProperty("server.port");
        });
    }
}
