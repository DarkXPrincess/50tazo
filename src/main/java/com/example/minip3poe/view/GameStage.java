package com.example.minip3poe.view;

import com.example.minip3poe.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A singleton Stage for the main game window.
 * This class ensures that only one instance of the game window can exist.
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class GameStage extends Stage {

    private GameController controller;

    /**
     * Private constructor to enforce the singleton pattern.
     * Loads the FXML view, sets up the scene, and configures the stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    private GameStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/minip3poe/game-view.fxml"));

        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - Game");
        setResizable(false);
        show();

        Image icono = new Image(getClass().getResourceAsStream("/com/example/minip3poe/images/icono_poker.png"));
        getIcons().add(icono);
    }


    /**
     * Returns the controller associated with this stage's view.
     *
     * @return the GameController instance
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static GameStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton GameStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return the single instance of GameStage
     * @throws IOException if the FXML file cannot be loaded during the first creation
     */
    public static GameStage getInstance() throws IOException {
        Holder.INSTANCE = Holder.INSTANCE != null ?
                Holder.INSTANCE : new GameStage();
        return Holder.INSTANCE;
    }

    /**
     * Closes the stage and deletes the instance.
     */
    public static void deleteInstance() {
        if (Holder.INSTANCE != null) {
            Holder.INSTANCE.close();
            Holder.INSTANCE = null;
        }
    }
}
