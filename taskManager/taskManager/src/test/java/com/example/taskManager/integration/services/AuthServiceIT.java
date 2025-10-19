package com.example.taskManager.integration.services;

import com.example.taskManager.model.User;
import com.example.taskManager.repository.UserRepository;
import com.example.taskManager.security.JwtUtil;
import com.example.taskManager.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceIT {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userRepository.save(new User(null, "anirban", "1234", false));
    }

    @Test
    void shouldLoginSuccessfullyAndGenerateValidJwt() {
        String token = authService.login("anirban", "1234");

        assertNotNull(token, "Token should not be null");
        assertTrue(jwtUtil.validateToken(token, "anirban"), "Generated token should be valid");
    }

    @Test
    void shouldThrowWhenInvalidPassword() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("anirban", "wrong"));

        assertTrue(ex.getMessage().toLowerCase().contains("invalid"), "Should indicate invalid credentials");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("ghost", "pass"));

        assertTrue(ex.getMessage().toLowerCase().contains("not found"), "Should indicate user not found");
    }
}
