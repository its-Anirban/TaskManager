package com.example.taskManager.services;

import com.example.taskManager.model.User;
import com.example.taskManager.repository.UserRepository;
import com.example.taskManager.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Plain text password check
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        user.setLoggedIn(true);
        userRepository.save(user);
        return jwtUtil.generateToken(username);
    }

    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLoggedIn(false);
        userRepository.save(user);
    }
}
