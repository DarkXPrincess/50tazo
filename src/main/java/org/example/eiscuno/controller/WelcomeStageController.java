package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.eiscuno.model.GameEngine;
import org.example.eiscuno.view.GameUnoView;

/**
 * Controller for the welcome/main menu stage.
 * Allows the human player to enter their name and select number of AI opponents (1-3).
 */
public class WelcomeStageController {
    @FXML private TextField usernameField;
    @FXML private TextField playersField;
    @FXML private Button playButton;
    @FXML private Button quitButton;

    private Stage primaryStage;

    /**
     * Initialize controller with reference to primary stage.
     */
    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Handle Play button click: validate inputs, create game, and launch game view.
     */
    @FXML
    private void onHandlePlayButton() {
        String username = usernameField.getText().trim();
        String playersStr = playersField.getText().trim();

        if (username.isEmpty()) {
            showAlert("Username required", "Please enter a username.");
            return;
        }

        int numPlayers;
        try {
            numPlayers = Integer.parseInt(playersStr);
            if (numPlayers < 1 || numPlayers > 3) {
                showAlert("Invalid number", "Number of AI players must be 1-3.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Please enter a number between 1 and 3.");
            return;
        }

        // Create game and switch to game view
        GameEngine engine = new GameEngine(username, numPlayers);
        engine.dealInitial();

        try {
            GameUnoView gameView = new GameUnoView(primaryStage, engine);
            gameView.show();
        } catch (Exception e) {
            showAlert("Error", "Could not load game view: " + e.getMessage());
        }
    }

    /**
     * Handle Quit button click.
     */
    @FXML
    private void onHandleQuitButton() {
        if (primaryStage != null) {
            primaryStage.close();
        }
    }

    private void showAlert(String title, String message) {
        // Simple alert for now (would use Alert dialog in full impl)
        System.out.println("[Alert] " + title + ": " + message);
    }
}
