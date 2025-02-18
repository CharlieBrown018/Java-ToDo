// Purpose: Main application component that orchestrates all other components
// Features:
// - Task state management
// - Automatic refresh mechanism
// - Layout structure
// - Component coordination
// - Task selection for editing

import { useState } from 'react';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';

function App() {
    // State Management
    // selectedTask: Tracks currently selected task for editing
    // refreshTrigger: Counter to trigger task list refresh
    const [selectedTask, setSelectedTask] = useState(null);
    const [refreshTrigger, setRefreshTrigger] = useState(0);

    // Handler Functions

    // Handles successful task addition
    // Clears selection and triggers refresh
    const handleTaskAdded = () => {
        setSelectedTask(null);
        setRefreshTrigger(prev => prev + 1);
    };

    // Handles successful task update
    // Clears selection and triggers refresh
    const handleTaskUpdated = () => {
        setSelectedTask(null);
        setRefreshTrigger(prev => prev + 1);
    };

    // Handles form clear action
    // Resets selection state
    const handleClear = () => {
        setSelectedTask(null);
    };

    // Main Render
    return (
        <div className="min-h-screen bg-gray-50 py-8">
            <div className="max-w-7xl mx-auto px-4">
                {/* Application Header */}
                <h1 className="text-2xl font-bold text-gray-900 mb-8">Todo App</h1>

                {/* Task Form Section */}
                <div className="bg-white rounded-lg shadow p-6 mb-8">
                    <TaskForm
                        initialTask={selectedTask}
                        onTaskAdded={handleTaskAdded}
                        onTaskUpdated={handleTaskUpdated}
                        onClear={handleClear}
                    />
                </div>

                {/* Task List Section */}
                <TaskList
                    onTaskSelect={setSelectedTask}
                    refreshTrigger={refreshTrigger}
                />
            </div>
        </div>
    );
}

export default App;