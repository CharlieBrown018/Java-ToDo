package com.todo.controller;

import com.todo.enums.Status;
import com.todo.model.Task;
import com.todo.service.TaskService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Handles UI events and calls TaskService methods
public class MainController {
    private final TaskService taskService = new TaskService();
    private Task selectedTask; // Stores currently selected task

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<Status> statusComboBox;
    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        // Populate the status dropdown with displayNames
        statusComboBox.getItems().setAll(Status.values());
        statusComboBox.setPromptText("Select Status");

        // Modern table bindings
        titleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescription()));
        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().getDisplayName()));
        dueDateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDueDate() != null ?
                        data.getValue().getDueDate().format(DATE_FORMAT) : "No date"));

        // Make columns fill width
        titleColumn.prefWidthProperty().bind(taskTableView.widthProperty().multiply(0.3));
        descriptionColumn.prefWidthProperty().bind(taskTableView.widthProperty().multiply(0.4));
        dueDateColumn.prefWidthProperty().bind(taskTableView.widthProperty().multiply(0.15));
        statusColumn.prefWidthProperty().bind(taskTableView.widthProperty().multiply(0.15));

        // Add some padding to cells
        taskTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTaskList();
    }

    // Populate fields when selecting a task
    private void populateTaskFields(Task task) {
        selectedTask = task;
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        dueDatePicker.setValue(task.getDueDate() != null ? task.getDueDate() : null);
        statusComboBox.setValue(task.getStatus());
    }

    // Handle adding a task
    @FXML
    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();  // DatePicker directly gives LocalDate

        if (title.isEmpty()) {
            showAlert("Validation Error", "Task title cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        try {
            taskService.addTask(title, description, dueDate != null ? dueDate.toString() : null);
            refreshTaskList();
            clearInputFields();
        } catch (Exception e) {
            showAlert("Error", "Failed to add task: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Update an existing task
    @FXML
    private void updateTask() {
        if (selectedTask == null) {
            showAlert("No Selection", "Please select a task to update.", Alert.AlertType.WARNING);
            return;
        }

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();
        Status status = statusComboBox.getValue();

        if (title.isEmpty()) {
            showAlert("Validation Error", "Task title cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        selectedTask.setTitle(title);
        selectedTask.setDescription(description);
        selectedTask.setDueDate(dueDate);  // Now directly using LocalDate
        selectedTask.setStatus(status);

        try {
            taskService.updateTask(selectedTask);
            refreshTaskList();
            taskTableView.getSelectionModel().select(selectedTask);
        } catch (Exception e) {
            showAlert("Error", "Failed to update task: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Delete selected task
    @FXML
    private void deleteTask() {
        if (selectedTask == null) {
            showAlert("No Selection", "Please select a task to delete.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Task");
        confirmDialog.setHeaderText("Are you sure you want to delete this task?");
        confirmDialog.setContentText("Task: " + selectedTask.getTitle());

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                taskService.deleteTask(selectedTask.getId());
                refreshTaskList();
                clearInputFields();
            }
        });
    }

    // Refresh the task table with data from the database
    private void refreshTaskList() {
        ObservableList<Task> tasks = FXCollections.observableArrayList(taskService.getAllTasks());
        int selectedIndex = taskTableView.getSelectionModel().getSelectedIndex();
        taskTableView.setItems(tasks);

        if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
            taskTableView.getSelectionModel().select(selectedIndex);
        }
    }

    @FXML
    private void onTaskSelected() {
        selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            titleField.setText(selectedTask.getTitle());
            descriptionField.setText(selectedTask.getDescription());
            dueDatePicker.setValue(
                    selectedTask.getDueDate() != null ? selectedTask.getDueDate() : null
            );
            statusComboBox.setValue(selectedTask.getStatus());
        }
    }


    // Show alert popups
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    // Clear input fields
    private void clearInputFields() {
        titleField.clear();
        descriptionField.clear();
        dueDatePicker.setValue(null);
        statusComboBox.setValue(null);
        selectedTask = null; // Reset selected task
        taskTableView.getSelectionModel().clearSelection(); // clear selection when adding new task
    }

    @FXML
    private void clearForm() {
        clearInputFields();
    }
}
