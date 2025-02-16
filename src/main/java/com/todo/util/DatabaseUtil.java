package com.todo.util;

import org.sqlite.core.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    // Define the database URL for SQLite
    private static final String DB_URL = "jdbc:sqlite:src/main/db/todo.db";

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Initialize the database and create the task table if it doesn't exist
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); // Establish connection
             Statement stmt = conn.createStatement()) { // Create statement object

            // Create tasks table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS task (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    due_date TEXT,
                    status TEXT DEFAULT 'PENDING',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) { // Handle and log SQL errors
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
