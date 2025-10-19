package com.example.taskManager.integration.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class GlobalExceptionHandlerIT {

    @Autowired private MockMvc mockMvc;

    @Test
    void shouldHandleTaskNotFoundException() throws Exception {
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 404 || status == 500,
                            "Expected HTTP 404 or 500, but got " + status);
                })
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void shouldHandleBadRequestForMalformedId() throws Exception {
        mockMvc.perform(get("/api/tasks/invalid-id"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 400, "Expected 400 Bad Request");
                })
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void shouldHandleMalformedJsonError() throws Exception {
        String badJson = "{\"title\":\"Broken}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/api/tasks")
                        .contentType("application/json")
                        .content(badJson))
                .andExpect(result -> assertTrue(result.getResponse().getStatus() == 400))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    void shouldHandleGenericServerError() throws Exception {
        mockMvc.perform(get("/api/tasks/-1")) // endpoint likely to fail
                .andExpect(result -> assertTrue(result.getResponse().getStatus() == 500))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}
