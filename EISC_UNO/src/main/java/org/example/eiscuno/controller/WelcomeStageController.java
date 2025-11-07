package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import org.example.eiscuno.model.planeTextFiles.PlaneTextFileHandler;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.WelcomeStage;

import java.io.IOException;

/**
 * Controller for the Welcome Stage of the Uno game.
 * <p>
 * Handles the initial interactions where the user can:
 * <ul>
 *     <li>Enter a username and start a new game.</li>
 *     <li>Continue a previously saved game.</li>
 *     <li>Quit the application.</li>
 *     <li>Access credits (future implementation).</li>
 * </ul>
 */
public class WelcomeStageController {

    @FXML
    private TextField usernameField;
    private String nickName;
    @FXML
    private String player;
    @FXML
    private TextField playersField;
    private PlaneTextFileHandler planeTextFileHandler;
    private Boolean isContinue;

    /**
     * Initializes the controller, setting default values.
     */
    @FXML
    public void initialize() {
        nickName = "";
        planeTextFileHandler = new PlaneTextFileHandler();
    }

    /**
     * Handles the "Play" button click event.
     * <p>
     * Starts a new game if a username is entered, otherwise shows a warning alert.
     * @throws IOException If the plain text file fails to load.
     */
    @FXML
    public void onHandlePlayButton() throws IOException {
        if(!usernameField.getText().isEmpty() && !playersField.getText().isEmpty()) {
            nickName = usernameField.getText();
            planeTextFileHandler.write("PlayerData.csv", nickName);
            isContinue = false;
            player= playersField.getText();
            GameUnoStage.getInstance();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "¡Ingresa un nombre de usuario y cantidad de jugadores!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("warning-label");
            alert.setTitle("Warning");
            alert.showAndWait();
        }

        try{
            int numPlayers = Integer.parseInt(playersField.getText().trim());
            if(numPlayers < 1 || numPlayers > 3){
                showWarning("¡La cantidad de jugadores debe estar entre 1 y 3!");
               GameUnoStage.getInstance(numPlayers);
            }
        } catch (NumberFormatException e){
            showWarning("¡Ingresa un número válido para la cantidad de jugadores!");
        }

    }

    /**
     * Handles the "Continue" button click event.
     * <p>
     * Attempts to resume a previous game session.
     * @throws IOException If the saving files fail to load.
     */
    @FXML
    public void onHandleContinueButton() throws IOException {
        isContinue = true;
        GameUnoStage.getInstance();
    }

    /**
     * Handles the "Quit" button click event.
     * <p>
     * Closes the application.
     * </p>
     */
    @FXML
    public void onHandleQuitButton(){
        WelcomeStage.deleteInstance();
        System.exit(0);
    }

    /**
     * Handles the "Credits" button click event.
     * <p>
     * (Future implementation: show a credits dialog or scene.)
     * </p>
     */
    @FXML
    public void onHandleCreditsButton(){
        showWarning("Credits!");
    }

    /**
     * Returns whether the user chose to continue a saved game.
     *
     * @return true if continuing a saved game, false otherwise
     */
    public Boolean returnIsContinue(){
        return this.isContinue;
    }

    /**
     * Utility method to display a warning alert.
     *
     * @param message The message to display in the alert.
     */
    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("warning-label");
        alert.setTitle("Warning");
        alert.showAndWait();
    }
}
