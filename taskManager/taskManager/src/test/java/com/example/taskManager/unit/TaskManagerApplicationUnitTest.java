package com.example.taskManager.unit;

import com.example.taskManager.TaskManagerApplication;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskManagerApplicationUnitTest {

    @Test
    void mainMethodShouldInvokeStartSuccessfully() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(TaskManagerApplication.class, new String[]{}))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            assertDoesNotThrow(() -> TaskManagerApplication.main(new String[]{}));

            mocked.verify(() -> SpringApplication.run(TaskManagerApplication.class, new String[]{}));
        }
    }

    @Test
    void startMethodShouldReturnNonNullContext() {
        ConfigurableApplicationContext fakeContext = mock(ConfigurableApplicationContext.class);

        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(TaskManagerApplication.class, new String[]{}))
                    .thenReturn(fakeContext);

            ConfigurableApplicationContext result = TaskManagerApplication.start(new String[]{});

            assertNotNull(result);
            assertEquals(fakeContext, result);
            mocked.verify(() -> SpringApplication.run(TaskManagerApplication.class, new String[]{}));
        }
    }
}
