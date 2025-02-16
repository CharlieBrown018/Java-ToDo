package com.todo.config;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Helper class that manages Spring Boot context for JavaFX application.
 * Ensures proper initialization and cleanup of Spring context.
 */
public class SpringFXMLLoader {

    private static ConfigurableApplicationContext applicationContext;

    /**
     * Initializes Spring Boot context
     * @param javaFXApplication the JavaFX application class
     */
    public static void initContext(Class<? extends Application> javaFXApplication) {
        applicationContext = new SpringApplicationBuilder()
                .sources(javaFXApplication)
                .run();
    }

    /**
     * Gets the Spring application context
     * @return the Spring context
     */
    public static ConfigurableApplicationContext getContext() {
        return applicationContext;
    }

    /**
     * Closes the Spring context properly
     */
    public static void closeContext() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }
}