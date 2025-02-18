package com.todo.dao;

import com.todo.model.Task;
import java.util.List;
import com.todo.enums.Status;

// Data Access Object responsible for database operations
public interface TaskDAO {
    void insert(Task task);
    void update(Task task);
    void delete(int id);
    Task findById(int id);
    List<Task> findAll();
    List<Task> findByStatus(Status status);
}
