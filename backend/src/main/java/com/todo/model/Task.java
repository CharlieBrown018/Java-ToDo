package com.todo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.todo.enums.Status;
import java.time.LocalDate;

/**
 * Task entity class representing a todo item in the application.
 * Uses JPA annotations for database mapping and Lombok for reducing boilerplate code.
 */
@Entity  // Marks this class as JPA entity - will be mapped to database table
@Table(name = "task")  // Explicitly names the database table
@Data  // Lombok: Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor  // Lombok: Generates no-args constructor (required by JPA)
public class Task {

    @Id  // Designates this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Uses database auto-increment
    private Integer id;  // Unique identifier for each task

    @Column(nullable = false)  // Database constraint: title cannot be null
    private String title;  // The task's title/name

    @Column(length = 1000)  // Limits description to 1000 characters
    private String description;  // Detailed description of the task

    private LocalDate dueDate;  // When the task is due (optional)

    @Enumerated(EnumType.STRING)  // Stores the enum as a STRING in the database
    @Column(nullable = false)  // Database constraint: status cannot be null
    private Status status = Status.PENDING;  // Task status with default value

    @CreationTimestamp  // Automatically sets timestamp when task is created
    @Column(updatable = false)  // Ensures creation date never changes
    private LocalDate createdAt;  // When the task was created

    /**
     * Constructor for creating new tasks with essential fields.
     * Status defaults to PENDING and createdAt is automatically set.
     *
     * @param title       The task's title (required)
     * @param description The task's detailed description (optional)
     * @param dueDate     When the task is due (optional)
     */
    public Task(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}