package com.example.minip3poe.controller;

import com.example.minip3poe.model.GameModel;
import com.example.minip3poe.model.exceptions.InvalidPlayerCountException;
import com.example.minip3poe.view.GameStage;
import com.example.minip3poe.view.MenuStage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller for the main menu scene.
 * Handles machine player count input and game initialization.
 * Implements HU-1: Inicio del juego
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class MenuController {

    @FXML
    private TextField playerCountField;

    @FXML
    private TextField jugador;

    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;

    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    public void initialize() {
        // Set default value
        playerCountField.setText("1");

        // Add text field validation (only numbers)
        playerCountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                playerCountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Add validation for player name: only letters and spaces
        if (jugador != null) {
            jugador.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\p{L} ]*")) {
                    jugador.setText(newValue.replaceAll("[^\\p{L} ]", ""));
                }
            });
        }
    }

    /**
     * Handles the Play button click.
     * Validates input, initializes game model, and transitions to game stage.
     */
    @FXML
    private void handlePlay() {
        try {
            // Validate player name
            String playerName = "TÚ";
            if (jugador != null) {
                String entered = jugador.getText().trim();
                if (entered.isEmpty()) {
                    showError("Invalid Input", "Por favor ingresa tu nombre.");
                    return;
                }
                playerName = entered;
            }
            // Parse machine count from text field
            String input = playerCountField.getText().trim();

            if (input.isEmpty()) {
                showError("Invalid Input", "Please enter the number of machine players (1-3).");
                return;
            }

            int machineCount = Integer.parseInt(input);

            // Create and initialize game model
            GameModel gameModel = new GameModel();
            gameModel.initializeGame(machineCount);

            // Set human player's name from input
            if (playerName != null && !playerName.isEmpty()) {
                if (gameModel.getHumanPlayer() != null) {
                    gameModel.getHumanPlayer().setName(playerName);
                }
            }

            // Close menu stage and open game stage
            MenuStage.deleteInstance();
            GameStage gameStage = GameStage.getInstance();
            gameStage.getController().setGameModel(gameModel);
            gameStage.getController().startGame();

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter a valid number (1-3).");
        } catch (InvalidPlayerCountException e) {
            showError("Invalid Selection", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Failed to start game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Quit Game button click.
     * Exits the application.
     */
    @FXML
    private void onHandleQuitButton() {
        System.exit(0);
    }

    /**
     * Shows an error dialog to the user.
     *
     * @param title the dialog title
     * @param message the error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
