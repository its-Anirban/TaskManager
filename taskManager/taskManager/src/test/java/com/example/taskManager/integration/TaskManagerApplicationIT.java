package com.example.taskManager.integration;

import com.example.taskManager.TaskManagerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class TaskManagerApplicationIT {

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void contextLoadsSuccessfully() {
        assertThatCode(() -> {
            UserDetails dummyUser = User.withUsername("testuser")
                    .password("testpass")
                    .roles("USER")
                    .build();

            when(userDetailsService.loadUserByUsername("testuser"))
                    .thenReturn(dummyUser);
        }).doesNotThrowAnyException();
    }

    @Test
    void applicationStartsWithoutError() {
        assertThatCode(() -> TaskManagerApplication.main(new String[]{}))
                .doesNotThrowAnyException();
    }

    @Test
    void startMethodShouldReturnValidApplicationContext() {
        ConfigurableApplicationContext context = null;
        try {
            context = TaskManagerApplication.start(new String[]{});
            assertThat(context).isNotNull();
            assertThat(context.isActive()).isTrue();
        } finally {
            if (context != null) {
                context.close(); // ensure proper cleanup
            }
        }
    }
}
