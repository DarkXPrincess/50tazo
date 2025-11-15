package com.example.minip3poe;

import com.example.minip3poe.view.MenuStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for Cincuentazo game.
 * Entry point for the JavaFX application.
 *
 * @author Juan David Salazar
 * @author Veronica Granados
 * @author Freddy Alexander Melo Buitrago
 * @version 1.0
 */
public class Main extends Application {

    /**
     * The main entry point for the JavaFX application.
     * Called after the JavaFX runtime is initialized.
     *
     * @param primaryStage the primary stage (not used, we use our singleton stages)
     * @throws IOException if the menu stage cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create and show the menu stage (singleton)
        MenuStage.getInstance();
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
