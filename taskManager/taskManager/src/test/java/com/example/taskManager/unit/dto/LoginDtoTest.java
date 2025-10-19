package com.example.taskManager.unit.dto;

import com.example.taskManager.dto.LoginRequest;
import com.example.taskManager.dto.LoginResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginDtoTest {

    @Test
    void testLoginRequest() {
        LoginRequest req = new LoginRequest("anirban", "1234");
        assertEquals("anirban", req.getUsername());
        assertEquals("1234", req.getPassword());
    }

    @Test
    void testLoginResponseTwoArgsConstructor() {
        LoginResponse res = new LoginResponse("token123", "success");
        assertEquals("token123", res.getToken());
        assertEquals("success", res.getMessage());
    }

    @Test
    void testLoginResponseSingleArgConstructorSetsDefaultMessage() {
        LoginResponse res = new LoginResponse("tokenABC");
        assertEquals("tokenABC", res.getToken());
        assertEquals("Login successful", res.getMessage());
    }

    @Test
    void testLoginRequestSettersAndGetters() {
        LoginRequest req = new LoginRequest();
        req.setUsername("test");
        req.setPassword("pass");
        assertEquals("test", req.getUsername());
        assertEquals("pass", req.getPassword());
    }

    @Test
    void testLoginResponseSettersAndGetters() {
        LoginResponse res = new LoginResponse();
        res.setToken("tokenXYZ");
        res.setMessage("done");
        assertEquals("tokenXYZ", res.getToken());
        assertEquals("done", res.getMessage());
    }

    @Test
    void testToStringAndEquals() {
        LoginRequest req1 = new LoginRequest("a", "b");
        LoginRequest req2 = new LoginRequest("a", "b");

        assertEquals(req1, req2);
        assertTrue(req1.toString().contains("a"));
        assertTrue(req1.toString().contains("b"));
    }
}
