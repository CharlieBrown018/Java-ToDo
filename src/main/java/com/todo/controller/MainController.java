package com.todo.controller;

import com.todo.enums.Status;
import com.todo.model.Task;
import com.todo.service.TaskService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * JavaFX Controller integrated with Spring Boot.
 * Handles UI events and coordinates with TaskService.
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    private final TaskService taskService;
    private Task selectedTask;

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<Status> statusComboBox;
    @FXML private TableView<Task> taskTableView;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> descriptionColumn;
    @FXML private TableColumn<Task, String> statusColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Initializes the controller.
     * Called after FXML fields are populated.
     */
    @FXML
    public void initialize() {
        setupStatusComboBox();
        setupTableColumns();
        refreshTaskList();
    }

    /**
     * Sets up the status dropdown with enum values
     */
    private void setupStatusComboBox() {
        statusComboBox.getItems().setAll(Status.values());
        statusComboBox.setPromptText("Select Status");
    }

    /**
     * Configures table columns with cell value factories
     */
    private void setupTableColumns() {
        titleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitle()));

        descriptionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescription()));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().name()));

        dueDateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDueDate() != null ?
                        data.getValue().getDueDate().format(DATE_FORMAT) : "No date"));

        setupTableColumnWidths();
    }

    /**
     * Sets up table column widths
     */
    private void setupTableColumnWidths() {
        titleColumn.prefWidthProperty().bind(
                taskTableView.widthProperty().multiply(0.3));
        descriptionColumn.prefWidthProperty().bind(
                taskTableView.widthProperty().multiply(0.4));
        dueDateColumn.prefWidthProperty().bind(
                taskTableView.widthProperty().multiply(0.15));
        statusColumn.prefWidthProperty().bind(
                taskTableView.widthProperty().multiply(0.15));

        taskTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Handles adding a new task
     */
    @FXML
    private void addTask() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty()) {
            showAlert("Validation Error",
                    "Task title cannot be empty!",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            taskService.addTask(title, description,
                    dueDate != null ? dueDate.toString() : null);
            refreshTaskList();
            clearInputFields();
        } catch (Exception e) {
            showAlert("Error",
                    "Failed to add task: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles updating an existing task
     */
    @FXML
    private void updateTask() {
        if (selectedTask == null) {
            showAlert("No Selection",
                    "Please select a task to update.",
                    Alert.AlertType.WARNING);
            return;
        }

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();
        Status status = statusComboBox.getValue();

        if (title.isEmpty()) {
            showAlert("Validation Error",
                    "Task title cannot be empty!",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            selectedTask.setTitle(title);
            selectedTask.setDescription(description);
            selectedTask.setDueDate(dueDate);
            selectedTask.setStatus(status);

            taskService.updateTask(selectedTask);
            refreshTaskList();
            taskTableView.getSelectionModel().select(selectedTask);
        } catch (Exception e) {
            showAlert("Error",
                    "Failed to update task: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles deleting a task
     */
    @FXML
    private void deleteTask() {
        if (selectedTask == null) {
            showAlert("No Selection",
                    "Please select a task to delete.",
                    Alert.AlertType.WARNING);
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Task");
        confirmDialog.setHeaderText("Are you sure you want to delete this task?");
        confirmDialog.setContentText("Task: " + selectedTask.getTitle());

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    taskService.deleteTask(selectedTask.getId());
                    refreshTaskList();
                    clearInputFields();
                } catch (Exception e) {
                    showAlert("Error",
                            "Failed to delete task: " + e.getMessage(),
                            Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Refreshes the task table with current data
     */
    private void refreshTaskList() {
        try {
            var tasks = FXCollections.observableArrayList(
                    taskService.getAllTasks());
            int selectedIndex = taskTableView.getSelectionModel()
                    .getSelectedIndex();
            taskTableView.setItems(tasks);

            if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
                taskTableView.getSelectionModel().select(selectedIndex);
            }
        } catch (Exception e) {
            showAlert("Error",
                    "Failed to refresh tasks: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles task selection in the table
     */
    @FXML
    private void onTaskSelected() {
        selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            populateFormWithTask(selectedTask);
        }
    }

    /**
     * Populates form fields with task data
     */
    private void populateFormWithTask(Task task) {
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        dueDatePicker.setValue(task.getDueDate());
        statusComboBox.setValue(task.getStatus());
    }

    /**
     * Shows alert dialog
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Clears all input fields
     */
    @FXML
    private void clearForm() {
        clearInputFields();
    }

    private void clearInputFields() {
        titleField.clear();
        descriptionField.clear();
        dueDatePicker.setValue(null);
        statusComboBox.setValue(null);
        selectedTask = null;
        taskTableView.getSelectionModel().clearSelection();
    }
}