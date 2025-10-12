package com.example.taskManager.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntimeException_returnsNotFoundWithMessageAndTimestamp() {
        // Arrange
        RuntimeException ex = new RuntimeException("Task not found");

        // Act
        ResponseEntity<Object> response = handler.handleRuntimeException(ex);

        // Assert: HTTP status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 NOT_FOUND");

        // Check body not null before casting
        assertNotNull(response.getBody(), "Response body should not be null");

        Object responseBody = response.getBody();
        assertTrue(responseBody instanceof Map, "Response body should be a Map");

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) responseBody;

        // Assert key presence
        assertNotNull(body.get("timestamp"), "Timestamp should not be null");
        assertEquals("Task not found", body.get("message"));
    }
}
