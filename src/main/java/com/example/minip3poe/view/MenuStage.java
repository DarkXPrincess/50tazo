package com.example.minip3poe.view;

import com.example.minip3poe.controller.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 * A singleton Stage for the main menu window.
 * This class ensures that only one instance of the menu window can exist.
 *
 * @author Juan David Salazar
 * @author Veronica Granados
 * @author Freddy Alexander Melo Buitrago
 * @version 1.0
 */
public class MenuStage extends Stage {

    private MenuController controller;

    /**
     * Private constructor to enforce the singleton pattern.
     * Loads the FXML view, sets up the scene, and configures the stage properties.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    private MenuStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/minip3poe/welcome-view.fxml"));

        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Cincuentazo - Menu");
        setResizable(false);
        show();

        Image icono = new Image(getClass().getResourceAsStream("/com/example/minip3poe/images/icono_poker.png"));
        getIcons().add(icono);

    }


    /**
     * Returns the controller associated with this stage's view.
     *
     * @return the MenuController instance
     */
    public MenuController getController() {
        return controller;
    }

    /**
     * Inner static class to hold the singleton instance (lazy initialization).
     */
    private static class Holder {
        private static MenuStage INSTANCE = null;
    }

    /**
     * Provides global access to the singleton MenuStage instance.
     * Creates the instance if it doesn't exist yet.
     *
     * @return the single instance of MenuStage
     * @throws IOException if the FXML file cannot be loaded during the first creation
     */
    public static MenuStage getInstance() throws IOException {
        Holder.INSTANCE = Holder.INSTANCE != null ?
                Holder.INSTANCE : new MenuStage();
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
