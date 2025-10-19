package com.example.taskManager.unit.services;

import com.example.taskManager.model.User;
import com.example.taskManager.repository.UserRepository;
import com.example.taskManager.security.JwtUtil;
import com.example.taskManager.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceUnitTest {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private AuthService authService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        jwtUtil = new JwtUtil();
        authService = new AuthService(userRepository, jwtUtil);
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = new User(1L, "anirban", "1234", false);
        when(userRepository.findByUsername("anirban"))
                .thenReturn(Optional.of(user));

        String token = authService.login("anirban", "1234");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, "anirban"));
        verify(userRepository).findByUsername("anirban");
        verify(userRepository).save(user);
        assertTrue(user.isLoggedIn());
    }

    @Test
    void shouldThrowWhenInvalidPassword() {
        User user = new User(1L, "anirban", "1234", false);
        when(userRepository.findByUsername("anirban"))
                .thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.login("anirban", "wrong"));

        assertTrue(ex.getMessage().toLowerCase().contains("invalid"));
        verify(userRepository).findByUsername("anirban");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenUserNotFoundDuringLogin() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.login("unknown", "pass"));

        assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        verify(userRepository).findByUsername("unknown");
    }


    @Test
    void shouldLogoutSuccessfully() {
        User user = new User(1L, "anirban", "1234", true);
        when(userRepository.findByUsername("anirban"))
                .thenReturn(Optional.of(user));

        authService.logout("anirban");

        verify(userRepository).findByUsername("anirban");
        verify(userRepository).save(user);
        assertFalse(user.isLoggedIn(), "User should be marked as logged out");
    }

    @Test
    void shouldThrowWhenUserNotFoundDuringLogout() {
        when(userRepository.findByUsername("ghost"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.logout("ghost"));

        assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        verify(userRepository).findByUsername("ghost");
        verify(userRepository, never()).save(any());
    }
}
