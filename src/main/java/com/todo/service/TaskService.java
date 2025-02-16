package com.todo.service;

import com.todo.dao.TaskDAO;
import com.todo.dao.TaskDAOImpl;
import com.todo.enums.Status;
import com.todo.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

// Bridge between the DAO layer and the UI/Controller
public class TaskService {
    private final TaskDAO taskDAO;

    public TaskService() {
        this.taskDAO = new TaskDAOImpl();
    }

    // Add a new task (with validation)
    public void addTask(String title, String description, String dueDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty.");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);

        // Parse the date if it exists
        if (dueDate != null && !dueDate.isEmpty()) {
            task.setDueDate(LocalDate.parse(dueDate));  // Changed to LocalDate parse
        }

        task.setStatus(Status.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        taskDAO.insert(task);
    }

    // Update an existing task
    public void updateTask(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task ID is required for updating.");
        }
        taskDAO.update(task);
    }

    // Delete a task by ID
    public void deleteTask(int id) {
        taskDAO.delete(id);
    }

    // Get a task by ID
    public Task getTaskById(int id) {
        return taskDAO.findById(id);
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    // Get tasks by status (Pending, Completed)
    public List<Task> getTasksByStatus(Status status) {
        return taskDAO.findByStatus(status);
    }

}
