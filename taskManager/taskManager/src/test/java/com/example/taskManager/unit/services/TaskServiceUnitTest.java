package com.example.taskManager.unit.services;

import com.example.taskManager.model.Task;
import com.example.taskManager.repository.TaskRepository;
import com.example.taskManager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setup() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Mocked Task");
        task.setDescription("Unit test description");
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task saved = taskService.createTask(task);

        assertNotNull(saved);
        assertEquals("Mocked Task", saved.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldRetrieveTaskByIdSuccessfully() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task found = taskService.getTaskById(1L);
        assertEquals("Mocked Task", found.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(99L));
    }
}
