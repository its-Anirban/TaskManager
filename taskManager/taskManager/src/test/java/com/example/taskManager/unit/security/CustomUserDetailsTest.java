package com.example.taskManager.unit.security;

import com.example.taskManager.model.User;
import com.example.taskManager.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "anirban", "password123", true);
        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    void shouldReturnUsernameAndPasswordCorrectly() {
        assertEquals("anirban", customUserDetails.getUsername());
        assertEquals("password123", customUserDetails.getPassword());
    }

    @Test
    void shouldReturnEmptyAuthoritiesList() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldAlwaysReturnTrueForAccountChecks() {
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());
    }

    @Test
    void shouldReturnOriginalUserObject() {
        assertSame(user, customUserDetails.getUser());
    }
}