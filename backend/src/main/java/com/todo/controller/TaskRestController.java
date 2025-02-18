// backend/src/main/java/com/todo/controller/TaskRestController.java

/**
 * REST Controller for handling HTTP requests related to Tasks.
 * Provides endpoints for CRUD operations on Task entities.
 * Works alongside existing MainController to support both REST and JavaFX interfaces.
 */
package com.todo.controller;

import com.todo.model.Task;
import com.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {
    // Inject the existing TaskService
    private final TaskService taskService;

    /**
     * Retrieve all tasks
     * @return List of all tasks
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Create a new task
     * @param task Task object from request body
     * @return Created task with generated ID
     */
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.addTask(
                task.getTitle(),
                task.getDescription(),
                task.getDueDate() != null ? task.getDueDate().toString() : null
        );
    }

    /**
     * Update an existing task
     * @param id Task ID
     * @param task Updated task data
     * @return Updated task
     */
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable int id, @RequestBody Task task) {
        task.setId(id);
        return taskService.updateTask(task);
    }

    /**
     * Delete a task
     * @param id Task ID to delete
     * @return Empty response with OK status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}