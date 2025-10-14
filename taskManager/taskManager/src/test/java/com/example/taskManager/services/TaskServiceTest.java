package com.example.taskManager.services;

import com.example.taskManager.model.Task;
import com.example.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceTest {

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
        assertEquals("Database Integration Test", allTasks.get(0).getTitle());
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
}
