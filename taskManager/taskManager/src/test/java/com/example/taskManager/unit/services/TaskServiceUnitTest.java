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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        task.setCompleted(false);
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task saved = taskService.createTask(task);

        assertNotNull(saved);
        assertEquals("Mocked Task", saved.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldRetrieveTaskByIdSuccessfully() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task found = taskService.getTaskById(1L);

        assertEquals("Mocked Task", found.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        List<Task> tasks = taskService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Mocked Task", tasks.get(0).getTitle());
        verify(taskRepository).findAll();
    }

    @Test
    void shouldUpdateTaskWhenPresent() {
        Task updated = new Task();
        updated.setId(1L);
        updated.setTitle("Updated Title");
        updated.setDescription("Updated description");
        updated.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);

        Task result = taskService.updateTask(1L, updated);

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isCompleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTask() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        Task updated = new Task();
        updated.setTitle("Updated Title");

        assertThrows(RuntimeException.class, () -> taskService.updateTask(2L, updated));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldDeleteWhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(any(Task.class));

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository).delete(any(Task.class));
    }

    @Test
    void shouldThrowWhenDeletingNonExisting() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(99L));
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
