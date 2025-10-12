package com.example.taskManager.controller;

import com.example.taskManager.model.Task;
import com.example.taskManager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void createTask_returnsCreatedResponse() {
        Task in = new Task();
        in.setTitle("New Task");

        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle("New Task");

        when(taskService.createTask(any(Task.class))).thenReturn(saved);

        ResponseEntity<Task> resp = taskController.createTask(in);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        // use requireNonNull so static analyzers know body is non-null
        Task body = Objects.requireNonNull(resp.getBody(), "Response body should not be null for created task");
        assertEquals(1L, body.getId());
        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void getAllTasks_returnsList() {
        Task t = new Task();
        t.setId(1L);
        t.setTitle("T1");

        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(t));

        ResponseEntity<List<Task>> resp = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        List<Task> result = Objects.requireNonNull(resp.getBody(), "Response body should not be null for getAllTasks");
        assertEquals(1, result.size());
        assertEquals("T1", result.get(0).getTitle());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_whenFound_returnsOk() {
        Task t = new Task();
        t.setId(1L);
        t.setTitle("Found");

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(t));

        ResponseEntity<Task> resp = taskController.getTaskById(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Task body = Objects.requireNonNull(resp.getBody(), "Response body should not be null when task is found");
        assertEquals("Found", body.getTitle());
        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_whenNotFound_returnsNotFound() {
        when(taskService.getTaskById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Task> resp = taskController.getTaskById(2L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNull(resp.getBody(), "Response body should be null when not found");
        verify(taskService, times(1)).getTaskById(2L);
    }

    @Test
    void updateTask_returnsOkWithUpdatedTask() {
        Task in = new Task();
        in.setTitle("In");

        Task updated = new Task();
        updated.setId(1L);
        updated.setTitle("Updated");

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updated);

        ResponseEntity<Task> resp = taskController.updateTask(1L, in);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        Task body = Objects.requireNonNull(resp.getBody(), "Response body should not be null after update");
        assertEquals("Updated", body.getTitle());
        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    void deleteTask_returnsNoContent() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<Void> resp = taskController.deleteTask(1L);

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        assertNull(resp.getBody(), "No content responses have null body");
        verify(taskService, times(1)).deleteTask(1L);
    }
}
