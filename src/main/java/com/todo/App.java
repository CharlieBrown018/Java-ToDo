package com.todo;

import com.todo.config.SpringFXMLLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main application class that integrates JavaFX with Spring Boot.
 * Handles initialization of both frameworks and scene setup.
 */
@SpringBootApplication
public class App extends Application {

    private ConfigurableApplicationContext springContext;

    /**
     * JavaFX initialization method.
     * Sets up the Spring context before JavaFX starts.
     */
    @Override
    public void init() {
        springContext = SpringFXMLLoader.getContext();
    }

    /**
     * Main entry point for the JavaFX application.
     * Sets up the primary stage and loads the FXML.
     */
    @Override
    public void start(Stage stage) {
        try {
            // Create FXML loader with Spring context
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/fxml/main.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);

            // Set up the scene
            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            stage.setTitle("ToDo App");
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setScene(scene);

            // Handle application shutdown
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                springContext.close();
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    /**
     * Cleanup method for JavaFX application.
     * Ensures Spring context is properly closed.
     */
    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }

    /**
     * Main method to launch the application.
     * Initializes Spring Boot context before launching JavaFX.
     */
    public static void main(String[] args) {
        // Initialize Spring context
        SpringFXMLLoader.initContext(App.class);

        // Launch JavaFX application
        launch(args);
    }
}