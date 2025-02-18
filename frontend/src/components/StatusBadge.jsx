// Purpose: Displays task status with color-coded visual indicators
// Props:
// - status: The current status of the task (PENDING, IN_PROGRESS, COMPLETED)
// Returns: A styled badge element showing the task's status

export default function StatusBadge({ status }) {
    // Define color styles for each status type
    const styles = {
        PENDING: 'bg-yellow-100 text-yellow-600',
        IN_PROGRESS: 'bg-blue-100 text-blue-600',
        COMPLETED: 'bg-green-100 text-green-600'
    };

    // Define display labels for each status
    const labels = {
        PENDING: 'Pending',
        IN_PROGRESS: 'In Progress',
        COMPLETED: 'Completed'
    };

    return (
        <span className={`px-3 py-1 rounded-full text-sm font-medium ${styles[status]}`}>
            {labels[status]}
        </span>
    );
}