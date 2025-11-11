package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.example.eiscuno.model.Card;
import org.example.eiscuno.model.GameEngine;
import org.example.eiscuno.model.Player;

/**
 * Controller for the main game view.
 * Handles card display, turns, and user interactions during gameplay.
 */
public class GameUnoController {
    @FXML private GridPane gridPaneCardsPlayer;
    @FXML private GridPane gridPaneCardsMachine;
    @FXML private ImageView tableImageView;
    @FXML private ImageView deckImageView;
    @FXML private Label turnLabel;
    @FXML private Label colorLabel;
    @FXML private Circle colorCircle;
    @FXML private Label errorLabel;
    @FXML private HBox turnHBox;
    @FXML private HBox colorHBox;
    @FXML private Button unoButton;

    private GameEngine engine;
    private Player currentPlayer;
    private int currentPlayerIndex = 0;

    /**
     * Set the game engine and prepare for game start.
     */
    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    /**
     * Initialize the game UI with initial card state.
     */
    public void initialize() {
        if (engine == null) {
            showError("Engine not initialized");
            return;
        }

        // Display initial state
        updatePlayerHand();
        updateTableCard();
        updateTurnInfo();
    }

    /**
     * Handle taking a card from the deck (when no playable card).
     */
    @FXML
    private void onHandleTakeCard() {
        if (currentPlayer == null || !currentPlayer.isHuman()) return;

        try {
            // Try to draw from deck if available
            if (!engine.getDeck().isEmpty()) {
                currentPlayer.addCard(engine.getDeck().draw());
                updatePlayerHand();
                clearError();
            } else {
                showError("Deck is empty");
            }
        } catch (Exception e) {
            showError("Error taking card: " + e.getMessage());
        }
    }

    /**
     * Handle UNO button (placeholder for future "UNO" game state).
     */
    @FXML
    private void onHandleUno() {
        // Future implementation for UNO special cases
        System.out.println("UNO button clicked");
    }

    /**
     * Handle Back button.
     */
    @FXML
    private void onHandleBack() {
        // Navigate back to welcome screen (future impl)
        System.out.println("Back button clicked");
    }

    /**
     * Handle Next button (advance turn).
     */
    @FXML
    private void onHandleNext() {
        advanceTurn();
    }

    /**
     * Advance to next player's turn.
     */
    private void advanceTurn() {
        // Simple round-robin turn advance
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % engine.getPlayers().size();
            currentPlayer = engine.getPlayers().get(currentPlayerIndex);
        } while (currentPlayer.isEliminated());

        updateTurnInfo();
        checkGameEnd();
    }

    /**
     * Update displayed player hand in UI.
     */
    private void updatePlayerHand() {
        gridPaneCardsPlayer.getChildren().clear();
        Player humanPlayer = engine.getPlayers().stream()
            .filter(Player::isHuman)
            .findFirst()
            .orElse(null);

        if (humanPlayer != null) {
            int col = 0;
            for (Card card : humanPlayer.getHand()) {
                Label cardLabel = new Label(card.toString());
                gridPaneCardsPlayer.add(cardLabel, col++, 0);
            }
        }
    }

    /**
     * Update displayed table card.
     */
    private void updateTableCard() {
        Card tableTop = engine.getTableTop();
        if (tableTop != null) {
            // TODO: load card image based on tableTop
            // For now, just update label
            System.out.println("Table card: " + tableTop);
        }
    }

    /**
     * Update turn and sum display.
     */
    private void updateTurnInfo() {
        if (currentPlayer == null) {
            currentPlayer = engine.getPlayers().get(0);
        }

        turnLabel.setText("Turno: " + currentPlayer.getId());
        Label sumLabel = new Label("Suma: " + engine.getCurrentSum());
        turnHBox.getChildren().clear();
        turnHBox.getChildren().add(sumLabel);
    }

    /**
     * Check if game has ended (only 1 player left).
     */
    private void checkGameEnd() {
        if (engine.activePlayersCount() == 1) {
            Player winner = engine.getPlayers().stream()
                .filter(p -> !p.isEliminated())
                .findFirst()
                .orElse(null);
            if (winner != null) {
                showError("Game Over! Winner: " + winner.getId());
            }
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
        }
        System.err.println("[Error] " + message);
    }

    private void clearError() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }
}
