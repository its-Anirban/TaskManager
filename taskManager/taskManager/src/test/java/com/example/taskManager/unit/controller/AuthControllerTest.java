package com.example.taskManager.unit.controller;

import com.example.taskManager.controller.AuthController;
import com.example.taskManager.dto.LoginRequest;
import com.example.taskManager.dto.LoginResponse;
import com.example.taskManager.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setup() {
        authService = mock(AuthService.class);
        authController = new AuthController();
        authController.setAuthService(authService);
    }

    @Test
    void shouldReturnJwtToken_WhenLoginIsSuccessful() {
        LoginRequest request = new LoginRequest("anirban", "1234");
        when(authService.login("anirban", "1234")).thenReturn("mockToken123");

        ResponseEntity<LoginResponse> response = authController.login(request);
        LoginResponse body = response.getBody();

        assertNotNull(body, "Response body should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mockToken123", body.getToken());
        verify(authService).login("anirban", "1234");
    }
}
