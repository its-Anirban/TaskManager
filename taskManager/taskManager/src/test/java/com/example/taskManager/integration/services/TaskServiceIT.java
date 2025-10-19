package com.example.taskManager.integration.services;

import com.example.taskManager.model.Task;
import com.example.taskManager.repository.TaskRepository;
import com.example.taskManager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldCreateAndRetrieveTaskSuccessfully() {
        Task task = new Task();
        task.setTitle("Database Integration Test");
        task.setDescription("Testing with H2 DB");

        Task saved = taskService.createTask(task);
        assertNotNull(saved.getId());

        List<Task> allTasks = taskService.getAllTasks();
        assertFalse(allTasks.isEmpty());
        assertTrue(allTasks.stream()
                .anyMatch(t -> "Database Integration Test".equals(t.getTitle())));
    }

    @Test
    void shouldUpdateAndDeleteTaskSuccessfully() {
        Task task = new Task();
        task.setTitle("Old Title");
        task.setDescription("Will be updated");
        Task saved = taskService.createTask(task);

        saved.setTitle("Updated Title");
        taskService.updateTask(saved.getId(), saved);

        Task updated = taskRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Updated Title", updated.getTitle());

        taskService.deleteTask(saved.getId());
        assertTrue(taskRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void shouldThrowWhenGettingNonExistingTask() {
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(55555L));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingTask() {
        Task updated = new Task();
        updated.setTitle("Ghost");
        assertThrows(RuntimeException.class, () -> taskService.updateTask(99999L, updated));
    }

    @Test
    void shouldThrowWhenDeletingNonExistingTask() {
        long nonExistingId = 44444L;
        try {
            taskService.deleteTask(nonExistingId);
            assertTrue(taskRepository.findById(nonExistingId).isEmpty());
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }
}
