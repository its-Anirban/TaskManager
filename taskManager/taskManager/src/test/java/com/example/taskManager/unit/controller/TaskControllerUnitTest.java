package com.example.taskManager.unit.controller;

import com.example.taskManager.controller.TaskController;
import com.example.taskManager.exception.GlobalExceptionHandler;
import com.example.taskManager.model.Task;
import com.example.taskManager.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TaskController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {TaskController.class, GlobalExceptionHandler.class})
class TaskControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task createSampleTask(Long id) {
        Task t = new Task();
        t.setId(id);
        t.setTitle("Sample Task " + id);
        t.setDescription("Description " + id);
        t.setCompleted(false);
        return t;
    }

    @Test
    @DisplayName("GET /api/tasks - should return list of tasks")
    void shouldReturnListOfTasks() throws Exception {
        Task t1 = createSampleTask(1L);
        Task t2 = createSampleTask(2L);

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Sample Task 1")))
                .andExpect(jsonPath("$[1].title", is("Sample Task 2")));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - existing id returns task")
    void shouldReturnTaskById() throws Exception {
        Task t = createSampleTask(10L);
        when(taskService.getTaskById(10L)).thenReturn(t);

        mockMvc.perform(get("/api/tasks/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("Sample Task 10")));

        verify(taskService).getTaskById(10L);
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - missing id returns 404")
    void shouldReturn404WhenTaskMissing() throws Exception {
        when(taskService.getTaskById(999L)).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(get("/api/tasks/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Task not found")));

        verify(taskService).getTaskById(999L);
    }

    @Test
    @DisplayName("POST /api/tasks - create a new task, returns 201")
    void shouldCreateNewTask() throws Exception {
        Task toCreate = new Task();
        toCreate.setTitle("New Task");
        toCreate.setDescription("New Desc");

        Task saved = createSampleTask(123L);
        saved.setTitle("New Task");
        saved.setDescription("New Desc");

        when(taskService.createTask(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(taskService).createTask(any(Task.class));
    }

    @Test
    @DisplayName("POST /api/tasks - should handle exception gracefully")
    void shouldHandlePostException() throws Exception {
        when(taskService.createTask(any(Task.class)))
                .thenThrow(new RuntimeException("Failed to save"));

        Task bad = new Task();
        bad.setTitle("Bad");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("Failed to save")));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} - update existing task")
    void shouldUpdateTask() throws Exception {
        Task update = new Task();
        update.setTitle("Updated Title");
        update.setDescription("Updated Desc");
        update.setCompleted(true);

        Task updated = createSampleTask(5L);
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Desc");
        updated.setCompleted(true);

        when(taskService.updateTask(eq(5L), any(Task.class))).thenReturn(updated);


        mockMvc.perform(put("/api/tasks/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(taskService).updateTask(eq(5L), any(Task.class));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} - not found case")
    void shouldReturn404WhenUpdateFails() throws Exception {
        Task update = new Task();
        update.setTitle("Missing");
        update.setDescription("Task not found");

        when(taskService.updateTask(eq(404L), any(Task.class)))
                .thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(put("/api/tasks/{id}", 404L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Task not found")));
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - delete existing task returns 204")
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(7L);

        mockMvc.perform(delete("/api/tasks/{id}", 7L))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(7L);
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - not found returns 404")
    void shouldReturn404WhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("Task not found")).when(taskService).deleteTask(888L);

        mockMvc.perform(delete("/api/tasks/{id}", 888L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Task not found")));
    }
}
