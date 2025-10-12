package com.example.taskManager.services;

import com.example.taskManager.model.Task;
import com.example.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldSaveNewTask() {
        Task taskToSave = new Task();
        taskToSave.setTitle("Test Task");

        when(taskRepository.save(any(Task.class))).thenReturn(taskToSave);

        Task savedTask = taskService.createTask(taskToSave);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        verify(taskRepository, times(1)).save(taskToSave);
    }
}
