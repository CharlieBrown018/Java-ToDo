// frontend/src/services/taskService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/tasks';

export const taskService = {
    getAllTasks: async () => {
        const response = await axios.get(API_URL);
        return response.data;
    },

    createTask: async (task) => {
        const response = await axios.post(API_URL, task);
        return response.data;
    },

    updateTask: async (id, task) => {
        const response = await axios.put(`${API_URL}/${id}`, task);
        return response.data;
    },

    deleteTask: async (id) => {
        await axios.delete(`${API_URL}/${id}`);
    }
};