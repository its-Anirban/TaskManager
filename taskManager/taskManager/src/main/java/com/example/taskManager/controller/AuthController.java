package com.example.taskManager.controller;

import com.example.taskManager.dto.LoginRequest;
import com.example.taskManager.dto.LoginResponse;
import com.example.taskManager.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest req) {
        authService.logout(req.getUsername());
        return ResponseEntity.ok().body("Logged out");
    }
}
