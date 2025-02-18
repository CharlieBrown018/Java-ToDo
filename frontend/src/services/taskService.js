// Purpose: Centralizes all API communications with the backend
// Features:
// - CRUD operations for tasks
// - Async/await pattern for clean promise handling
// - Consistent error propagation
// - Axios for HTTP requests

import axios from 'axios';

// Base URL for all task-related API endpoints
const API_URL = 'http://localhost:8080/api/tasks';

// Service object containing all API interactions
export const taskService = {
    // GET: Retrieves all tasks from the backend
    // Returns: Promise<Array<Task>>
    getAllTasks: async () => {
        const response = await axios.get(API_URL);
        return response.data;
    },

    // POST: Creates a new task
    // Params: task - Task object without ID
    // Returns: Promise<Task> with generated ID
    createTask: async (task) => {
        const response = await axios.post(API_URL, task);
        return response.data;
    },

    // PUT: Updates an existing task
    // Params: id - Task ID, task - Updated task data
    // Returns: Promise<Task> with updated data
    updateTask: async (id, task) => {
        const response = await axios.put(`${API_URL}/${id}`, task);
        return response.data;
    },

    // DELETE: Removes a task
    // Params: id - Task ID to delete
    // Returns: Promise<void>
    deleteTask: async (id) => {
        await axios.delete(`${API_URL}/${id}`);
    }
};