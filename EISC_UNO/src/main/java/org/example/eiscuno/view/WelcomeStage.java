package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.eiscuno.controller.WelcomeStageController;

import java.io.IOException;

/**
 * Represents the welcome window (stage) of the UNO game application.
 * <p>
 * This stage is responsible for displaying the initial welcome screen to the user
 * before starting the game. It loads its layout from an FXML file and assigns the
 * appropriate controller for managing UI interactions.
 * </p>
 *
 * @see Stage
 * @see Scene
 * @see FXMLLoader
 */
public class WelcomeStage extends Stage{

    /**
     * Controller associated with the welcome stage.
     * <p>
     * Provides access to UI logic and event handling defined in
     * {@code welcome-stage.fxml}.
     * </p>
     *
     * @see WelcomeStageController
     */
    private final WelcomeStageController welcomeStageController;

    /**
     * Constructs a new {@code WelcomeStage} instance.
     * <p>
     * This constructor initializes the welcome stage by:
     * <ul>
     *     <li>Loading the corresponding FXML layout file via {@link FXMLLoader}</li>
     *     <li>Retrieving its {@link WelcomeStageController}</li>
     *     <li>Creating and setting a {@link Scene}</li>
     *     <li>Applying a window icon using {@link Image}</li>
     *     <li>Displaying the {@link Stage} immediately</li>
     * </ul>
     *
     * @throws IOException if there is an error while loading the FXML file or its resources.
     */
    public WelcomeStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/welcome-stage.fxml"));
        Parent root;
        try{
            root = fxmlLoader.load();
        }catch(IOException e){
            throw new IOException("Error loading FXML file", e);
        }
        welcomeStageController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        setTitle("Welcome");

        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/org/example/eiscuno/images/icono_poker.png"))));
        setScene(scene);
        setResizable(false);
        show();
    }

    /**
     * Retrieves the controller associated with the welcome stage.
     *
     * @return the {@link WelcomeStageController} managing this {@link Stage}.
     */
    public WelcomeStageController getWelcomeStageController() {
        return this.welcomeStageController;
    }


    /**
     * Closes the instance of WelcomeStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        WelcomeStageHolder.INSTANCE.close();
        WelcomeStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of WelcomeStage.
     *
     * @return the singleton instance of WelcomeStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static WelcomeStage getInstance() throws IOException {
        return WelcomeStageHolder.INSTANCE != null ?
                WelcomeStageHolder.INSTANCE :
                (WelcomeStageHolder.INSTANCE = new WelcomeStage());
    }

    /**
     * Holder class for the singleton instance of WelcomeStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }

}
