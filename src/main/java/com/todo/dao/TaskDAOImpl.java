package com.todo.dao;

import com.todo.model.Task;
import com.todo.enums.Status;
import com.todo.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Implements TaskDAO and connects Java objects to the SQLite database
public class TaskDAOImpl implements TaskDAO {

    // Insert a new task in the database
    @Override
    public void insert(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        String sql = """
        INSERT INTO task (title, description, due_date, status, created_at)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);  // Using LocalDate toString
            pstmt.setString(4, task.getStatus().name());
            pstmt.setString(5, task.getCreatedAt().toString());

            // Execute the insert
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            // Retrieve the last inserted ID
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    task.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting task", e);
        }
    }


    // Update an existing task in the database
    @Override
    public void update(Task task) {
        String sql = """
                UPDATE task
                SET title = ?, description = ?, due_date = ?, status = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set values for the UPDATE statement
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getDueDate() != null ? task.getDueDate().toString() : null);  // Using LocalDate toString
            pstmt.setString(4, task.getStatus().name());
            pstmt.setInt(5, task.getId());

            // Execute the UPDATE statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating task", e);
        }
    }

    // Delete a task from the database using its ID
    @Override
    public void delete(int id) {
        String sql = """
                DELETE FROM task WHERE id = ?
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // Set the task ID to delete
            // Execute the UPDATE statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task" ,e);
        }
    }

    // Helper method to convert ResultSet row into a Task object
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));

        // Fix date parsing - handle database format
        String dueDateStr = rs.getString("due_date");
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            // Parse just the date part if it contains time
            if (dueDateStr.contains("T")) {
                dueDateStr = dueDateStr.substring(0, dueDateStr.indexOf("T"));
            }
            task.setDueDate(LocalDate.parse(dueDateStr));
        }

        task.setStatus(Status.valueOf(rs.getString("status")));

        // Handle created_at timestamp
        String createdAtStr = rs.getString("created_at");
        if (createdAtStr != null) {
            if (!createdAtStr.contains("T")) {
                createdAtStr = createdAtStr.replace(" ", "T");
            }
            task.setCreatedAt(LocalDate.parse(createdAtStr));
        }

        return task;
    }

    // Find a task by its ID
    @Override
    public Task findById(int id) {
        String sql = "SELECT * FROM task WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // Set the task ID to find
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTaskFromResultSet(rs); // Convert result into Task object
            }
            return null; // Return null if task not found

        } catch (SQLException e) {
            throw new RuntimeException("Error finding task by ID" ,e);
        }
    }

    // Retrieve all tasks from the database
    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task ORDER BY created_at DESC"; // Order tasks by creation date

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){

            // Loop through results and convert them into Task objects
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all tasks" ,e);
        }
    }

    // Retrieve tasks based on their status (PENDING / COMPLETED)
    @Override
    public List<Task> findByStatus(Status status) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE status = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

             pstmt.setString(1, status.name()); // Set the status to filter tasks
            ResultSet rs = pstmt.executeQuery();

            // Loop through results and convert them into Task objects
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tasks by status" ,e);
        }
    }








}
