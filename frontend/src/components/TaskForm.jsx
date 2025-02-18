// Purpose: Provides form interface for creating and editing tasks
// Props:
// - onTaskAdded: Callback when a new task is created
// - onTaskUpdated: Callback when an existing task is updated
// - onClear: Callback when form is cleared
// - initialTask: Task data for editing (null for new tasks)
// Features:
// - Dynamic form state management
// - Form validation
// - Clear form functionality
// - Support for both create and update operations

import { useState } from 'react';
import { taskService } from '../services/taskService';

export default function TaskForm({ onTaskAdded, onTaskUpdated, onClear, initialTask = null }) {
    // Form state management
    const [task, setTask] = useState(initialTask || {
        title: '',
        description: '',
        dueDate: '',
        status: 'PENDING'
    });

    // Handle form submission for both create and update
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (initialTask) {
                // Update existing task
                await taskService.updateTask(initialTask.id, task);
                onTaskUpdated(task);
            } else {
                // Create new task
                const newTask = await taskService.createTask(task);
                onTaskAdded(newTask);
            }
            if (!initialTask) handleClear();
        } catch (error) {
            console.error('Error saving task:', error);
        }
    };

    // Reset form to initial state
    const handleClear = () => {
        setTask({
            title: '',
            description: '',
            dueDate: '',
            status: 'PENDING'
        });
        onClear?.();
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4 bg-white rounded-xl shadow-sm p-6">
            {/* Title Input */}
            <input
                type="text"
                placeholder="Task title..."
                value={task.title}
                onChange={(e) => setTask({ ...task, title: e.target.value })}
                className="w-full p-3 bg-gray-50/50 border border-gray-200 rounded-lg
                         focus:border-blue-500 focus:bg-white focus:ring-2 focus:ring-blue-500/20"
                required
            />

            {/* Description Input */}
            <textarea
                placeholder="Add details..."
                value={task.description}
                onChange={(e) => setTask({ ...task, description: e.target.value })}
                className="w-full p-3 bg-gray-50/50 border border-gray-200 rounded-lg
                         focus:border-blue-500 focus:bg-white focus:ring-2 focus:ring-blue-500/20 min-h-[100px]"
            />

            {/* Form Controls Row */}
            <div className="flex gap-4">
                {/* Due Date Input */}
                <input
                    type="date"
                    value={task.dueDate}
                    onChange={(e) => setTask({ ...task, dueDate: e.target.value })}
                    className="flex-1 p-3 bg-gray-50/50 border border-gray-200 rounded-lg
                             focus:border-blue-500 focus:bg-white focus:ring-2 focus:ring-blue-500/20"
                />

                {/* Status Select */}
                <select
                    value={task.status}
                    onChange={(e) => setTask({ ...task, status: e.target.value })}
                    className="flex-1 p-3 bg-gray-50/50 border border-gray-200 rounded-lg
                             focus:border-blue-500 focus:bg-white focus:ring-2 focus:ring-blue-500/20"
                >
                    <option value="PENDING">Pending</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                </select>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-3">
                <button
                    type="submit"
                    className="px-6 py-3 bg-blue-500 text-white rounded-lg
                             hover:bg-blue-600 focus:ring-4 focus:ring-blue-500/20"
                >
                    {initialTask ? 'Update Task' : 'Add Task'}
                </button>
                <button
                    type="button"
                    onClick={handleClear}
                    className="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg
                             hover:bg-gray-200 focus:ring-4 focus:ring-gray-500/20"
                >
                    Clear
                </button>
            </div>
        </form>
    );
}