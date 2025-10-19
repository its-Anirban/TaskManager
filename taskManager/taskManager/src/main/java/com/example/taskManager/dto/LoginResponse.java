package com.example.taskManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String message;

    public LoginResponse(String token) {
        this.token = token;
        this.message = "Login successful";
    }
}
