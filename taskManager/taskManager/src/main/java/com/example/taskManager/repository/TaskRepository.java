package com.example.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskManager.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // You can define custom queries here if needed
}
