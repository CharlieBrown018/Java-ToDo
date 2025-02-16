package com.todo.model;

import com.todo.enums.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Task model represents a ToDo task in the application.
public class Task {
    private Integer id;
    private String title;
    private String description;
    private LocalDate dueDate;  // Changed from LocalDateTime to LocalDate
    private Status status;
    private LocalDateTime createdAt;

    // Default constructor
    public Task() {}

    // Constructor for new tasks (without ID)
    public Task(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = Status.PENDING; // Default status
        this.createdAt = LocalDateTime.now(); // Default timestamp
    }

    // Full constructor (with ID)
    public Task(Integer id, String title, String description, LocalDate dueDate, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title.trim();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }  // Changed return type
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }  // Changed parameter type

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Task: %s (Due: %s) - %s",
                title,
                dueDate != null ? dueDate.toString() : "No due date",
                status);
    }
}