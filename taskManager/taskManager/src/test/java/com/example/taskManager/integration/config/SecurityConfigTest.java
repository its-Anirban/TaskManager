package com.example.taskManager.integration.config;

import com.example.taskManager.config.SecurityConfig;
import com.example.taskManager.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


@SpringBootTest
class SecurityConfigTest {

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void shouldBuildSecurityFilterChainWithoutErrors() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        SecurityFilterChain chain = securityConfig.securityFilterChain(http);
        assertThat(chain).isNotNull();
    }

    @Test
    void shouldProvideAuthenticationManagerBean() throws Exception {
        AuthenticationManager manager = securityConfig.authenticationManager(
                mock(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration.class)
        );
        assertThat(manager).isNotNull();
    }
}
