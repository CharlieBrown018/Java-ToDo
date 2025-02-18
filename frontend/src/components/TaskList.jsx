// Purpose: Displays and manages the list of tasks in a table format
// Props:
// - onTaskSelect: Function to handle task selection for editing
// - refreshTrigger: Value that triggers task list refresh when changed
// Features:
// - Automatic data refresh when tasks are modified
// - Clickable rows for task editing
// - Formatted date display
// - Status badges

import { useState, useEffect } from 'react';
import { taskService } from '../services/taskService';
import StatusBadge from './StatusBadge';

export default function TaskList({ onTaskSelect, refreshTrigger }) {
    // State to store the list of tasks
    const [tasks, setTasks] = useState([]);

    // Load tasks on component mount and when refresh is triggered
    useEffect(() => {
        loadTasks();
    }, [refreshTrigger]);

    // Function to fetch tasks from the backend
    const loadTasks = async () => {
        try {
            const data = await taskService.getAllTasks();
            setTasks(data);
        } catch (error) {
            console.error('Error loading tasks:', error);
        }
    };

    return (
        <div className="mt-8">
            <h2 className="text-lg font-bold text-gray-700 mb-4">Tasks</h2>
            <div className="bg-white rounded-xl shadow-sm overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    {/* Table Header */}
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">Title</th>
                            <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">Description</th>
                            <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">Due Date</th>
                            <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">Status</th>
                        </tr>
                    </thead>
                    {/* Table Body */}
                    <tbody className="divide-y divide-gray-200">
                        {tasks.map((task) => (
                            <tr
                                key={task.id}
                                onClick={() => onTaskSelect(task)}
                                className="hover:bg-gray-50 cursor-pointer"
                            >
                                <td className="px-6 py-4 text-sm text-gray-700">{task.title}</td>
                                <td className="px-6 py-4 text-sm text-gray-700">{task.description}</td>
                                <td className="px-6 py-4 text-sm text-gray-700">
                                    {task.dueDate ? new Date(task.dueDate).toLocaleDateString() : '-'}
                                </td>
                                <td className="px-6 py-4 text-sm">
                                    <StatusBadge status={task.status} />
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}