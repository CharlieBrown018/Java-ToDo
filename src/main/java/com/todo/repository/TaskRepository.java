package com.todo.repository;

import com.todo.model.Task;
import com.todo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Task entity using Spring Data JPA.
 * Extends JpaRepository to inherit basic CRUD operations and more.
 * Methods are automatically implemented by Spring Data JPA based on method names.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    /**
     * Finds all tasks with a specific status.
     * Spring Data JPA automatically creates implementation based on method name.
     *
     * @param status the status to filter by
     * @return list of tasks with the specified status
     */
    List<Task> findByStatus(Status status);

    /**
     * Finds all tasks due before or on a specific date.
     *
     * @param date the date to check against
     * @return list of tasks due by the specified date
     */
    List<Task> findByDueDateLessThanEqual(LocalDate date);

    /**
     * Finds all tasks created on a specific date.
     *
     * @param date the creation date to search for
     * @return list of tasks created on the specified date
     */
    List<Task> findByCreatedAt(LocalDate date);

    /**
     * Finds tasks containing the given title (case-insensitive).
     *
     * @param title the title to search for
     * @return list of tasks with matching titles
     */
    List<Task> findByTitleContainingIgnoreCase(String title);
}