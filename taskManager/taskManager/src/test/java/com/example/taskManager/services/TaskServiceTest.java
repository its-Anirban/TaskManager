package com.example.taskManager.services;

import com.example.taskManager.model.Task;
import com.example.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateNewTask() {
        Task taskToSave = new Task();
        taskToSave.setTitle("Test Task");

        when(taskRepository.save(any(Task.class))).thenReturn(taskToSave);

        Task savedTask = taskService.createTask(taskToSave);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        verify(taskRepository, times(1)).save(taskToSave);
    }

    @Test
    void shouldReturnAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateExistingTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("New Title", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> taskService.updateTask(999L, updatedTask));

        assertEquals("Task not found with id 999", ex.getMessage());
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void shouldDeleteTaskById() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
