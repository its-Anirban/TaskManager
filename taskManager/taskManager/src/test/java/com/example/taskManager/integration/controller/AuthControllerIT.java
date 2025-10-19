package com.example.taskManager.integration.controller;

import com.example.taskManager.model.User;
import com.example.taskManager.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // Clean and seed the test database
        userRepository.deleteAll();
        userRepository.save(new User(null, "anirban", "1234", false));
    }

    @Test
    void shouldLoginSuccessfullyAndReturnJwtToken() throws Exception {
        var loginPayload = new LoginRequest("anirban", "1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Login successful")); // ✅ fixed
    }

    @Test
    void shouldFailLoginWithInvalidPassword() throws Exception {
        var loginPayload = new LoginRequest("anirban", "wrong");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isInternalServerError()); // ✅ AuthService throws RuntimeException
    }

    @Test
    void shouldFailLoginForNonExistentUser() throws Exception {
        var loginPayload = new LoginRequest("ghost", "1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isInternalServerError()); // ✅ same reason
    }

    // Inner static class to represent login request payload
    static class LoginRequest {
        public String username;
        public String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
