package com.example.taskManager.config;

import com.example.taskManager.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF (since we use JWT, not cookies)
                .csrf(csrf -> csrf.disable())

                // Allow embedded H2 console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Stateless sessions (no session storage)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define which endpoints are public and which are protected
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",         // login/logout APIs
                                "/h2-console/**",   // H2 DB console
                                "/error"            // allow Spring default error page
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Register custom JWT filter BEFORE the username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable built-in form login and HTTP Basic popup
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    // Optional: expose AuthenticationManager bean if needed in the future
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
