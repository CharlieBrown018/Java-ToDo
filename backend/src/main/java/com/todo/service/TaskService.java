package com.todo.service;

import com.todo.model.Task;
import com.todo.repository.TaskRepository;
import com.todo.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Task management.
 * Handles business logic and coordinates repository operations.
 * Uses @Transactional for data consistency.
 */
@Service
@RequiredArgsConstructor  // Lombok: Generates constructor for final fields
@Transactional  // Ensures database operations occur in transactions
public class TaskService {

    // Final field for constructor injection
    private final TaskRepository taskRepository;

    /**
     * Creates a new task.
     * Validates input and sets default values.
     *
     * @param title       required task title
     * @param description optional task description
     * @param dueDate     optional due date (as string)
     * @return the created task
     * @throws IllegalArgumentException if title is empty
     */
    public Task addTask(String title, String description, String dueDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }

        LocalDate parsedDueDate = null;
        if (dueDate != null && !dueDate.isEmpty()) {
            // Handle potential time component in date string
            if (dueDate.contains("T")) {
                dueDate = dueDate.substring(0, dueDate.indexOf("T"));
            }
            parsedDueDate = LocalDate.parse(dueDate);
        }

        Task task = new Task(
                title.trim(),
                description != null ? description.trim() : null,
                parsedDueDate
        );

        return taskRepository.save(task);
    }

    /**
     * Updates an existing task.
     * Validates input and ensures task exists.
     *
     * @param task the task to update with new values
     * @return the updated task
     * @throws IllegalArgumentException if task or ID is null
     */
    public Task updateTask(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task and ID are required for updating.");
        }

        // Verify task exists
        taskRepository.findById(task.getId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + task.getId()));

        return taskRepository.save(task);
    }

    /**
     * Deletes a task by ID.
     *
     * @param id the ID of the task to delete
     * @throws IllegalArgumentException if task doesn't exist
     */
    public void deleteTask(int id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Retrieves a task by ID.
     *
     * @param id the ID of the task to retrieve
     * @return the task if found
     * @throws IllegalArgumentException if task doesn't exist
     */
    public Task getTaskById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
    }

    /**
     * Retrieves all tasks.
     *
     * @return list of all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves tasks by status.
     *
     * @param status the status to filter by
     * @return list of tasks with the specified status
     */
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Searches for tasks by title.
     *
     * @param title the title to search for
     * @return list of tasks with matching titles
     */
    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }
}