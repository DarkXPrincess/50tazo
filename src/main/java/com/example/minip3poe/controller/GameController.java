package com.example.minip3poe.controller;

import com.example.minip3poe.model.Card;
import com.example.minip3poe.model.GameModel;
import com.example.minip3poe.model.exceptions.InvalidCardPlayException;
import com.example.minip3poe.model.player.HumanPlayer;
import com.example.minip3poe.model.player.MachinePlayer;
import com.example.minip3poe.model.player.Player;
import com.example.minip3poe.view.MenuStage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Controller for the main game scene.
 * Handles game flow, turn management, card plays, and UI updates.
 * Implements HU-2 through HU-6.
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class GameController {

    @FXML
    private ImageView tableImageView;

    @FXML
    private ImageView deckImageView;

    @FXML
    private ImageView exitImage;

    @FXML
    private ImageView machineHandLeft;

    @FXML
    private ImageView machineHandTop;

    @FXML
    private ImageView machineHandRight;

    @FXML
    private TextArea commTextArea;

    @FXML
    private Label turnLabel;

    @FXML
    private Label colorLabel;

    @FXML
    private GridPane gridPaneCardsPlayer;

    private GameModel gameModel;
    private boolean isProcessingTurn = false;

    /**
     * Initializes the controller.
     * Called automatically by JavaFX after FXML loading.
     */
    @FXML
    public void initialize() {
        // Set up click handlers
        deckImageView.setOnMouseClicked(event -> handleDrawCard());
        exitImage.setOnMouseClicked(event -> handleExit());

        // Make deck image show pointer cursor
        deckImageView.setStyle("-fx-cursor: hand;");
        exitImage.setStyle("-fx-cursor: hand;");
    }

    /**
     * Sets the game model and prepares the controller.
     * Called by MenuController before starting the game.
     *
     * @param gameModel the initialized game model
     */
    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Starts the game.
     * Implements HU-2: Preparación del juego
     */
    public void startGame() {
        // Start the game (deals cards, places initial card)
        gameModel.startGame();

        // Configure visible machine hands based on player count
        configureMachineHands();

        // Update UI
        updateUI();

        // Show welcome message
        logMessage("¡Bienvenido a Cincuentazo! El juego ha comenzado.");

        // Start turn logic
        processTurn();
    }

    /**
     * Configures which machine hands are visible based on player count.
     */
    private void configureMachineHands() {
        int machineCount = gameModel.getAllPlayers().size() - 1; // -1 for human

        // All hands invisible by default
        machineHandLeft.setVisible(false);
        machineHandTop.setVisible(false);
        machineHandRight.setVisible(false);

        // Show hands based on count
        if (machineCount >= 1) {
            machineHandTop.setVisible(true);
        }
        if (machineCount >= 2) {
            machineHandLeft.setVisible(true);
        }
        if (machineCount >= 3) {
            machineHandRight.setVisible(true);
        }
    }

    /**
     * Processes the current turn (human or machine).
     */
    private void processTurn() {
        if (gameModel.isGameFinished()) {
            showGameOver();
            return;
        }

        Player currentPlayer = gameModel.getCurrentPlayer();

        if (currentPlayer instanceof HumanPlayer) {
            processHumanTurn();
        } else if (currentPlayer instanceof MachinePlayer) {
            processMachineTurn((MachinePlayer) currentPlayer);
        }
    }

    /**
     * Processes human player's turn.
     * Enables card selection and deck drawing.
     */
    private void processHumanTurn() {
        isProcessingTurn = false;
        logMessage("Tu turno. Selecciona una carta o roba del mazo.");
        updateUI();
    }

    /**
     * Processes machine player's turn using a separate thread.
     * Implements thread requirement.
     *
     * @param machine the machine player
     */
    private void processMachineTurn(MachinePlayer machine) {
        isProcessingTurn = true;
        logMessage(machine.getName() + " está pensando...");

        Task<Void> machineTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Simulate thinking delay
                Thread.sleep(machine.getThinkingDelay());

                Platform.runLater(() -> {
                    try {
                        // Try to play a card
                        Card selectedCard = machine.selectCard(gameModel.getTableSum());

                        if (selectedCard != null) {
                            // Machine has a valid card
                            gameModel.playCard(selectedCard);
                            logMessage(machine.getName() + " jugó una carta: " + selectedCard);
                            updateUI();

                            // Move to next turn
                            gameModel.nextTurn();
                            processTurn();

                        } else {
                            // Machine has no valid card, must draw
                            logMessage(machine.getName() + " no tiene carta válida. Robando...");

                            // Wait before drawing
                            new Thread(() -> {
                                try {
                                    Thread.sleep(machine.getDrawingDelay());
                                    Platform.runLater(() -> {
                                        Card drawnCard = gameModel.drawCard();
                                        logMessage(machine.getName() + " robó una carta del mazo.");
                                        updateUI();

                                        // Check if drawn card can be played
                                        if (drawnCard != null && drawnCard.canBePlayed(gameModel.getTableSum())) {
                                            try {
                                                gameModel.playCard(drawnCard);
                                                logMessage(machine.getName() + " jugó la carta robada: " + drawnCard);
                                                updateUI();
                                            } catch (InvalidCardPlayException e) {
                                                // Should not happen, but handle just in case
                                                e.printStackTrace();
                                            }
                                        }

                                        // Move to next turn
                                        gameModel.nextTurn();
                                        processTurn();
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }

                    } catch (InvalidCardPlayException e) {
                        logMessage("Error: " + e.getMessage());
                        gameModel.nextTurn();
                        processTurn();
                    }
                });

                return null;
            }
        };

        new Thread(machineTask).start();
    }

    /**
     * Handles human player clicking on a card.
     * Implements HU-3: Jugar una carta
     *
     * @param card the card to play
     * @param cardImageView the ImageView of the clicked card
     */
    private void handleCardClick(Card card, ImageView cardImageView) {
        if (isProcessingTurn) {
            return; // Not human's turn
        }

        if (!(gameModel.getCurrentPlayer() instanceof HumanPlayer)) {
            logMessage("No es tu turno.");
            return;
        }

        HumanPlayer human = (HumanPlayer) gameModel.getCurrentPlayer();

        // Check if card can be played
        if (!card.canBePlayed(gameModel.getTableSum())) {
            logMessage("No puedes jugar esa carta. Excedería 50. Suma actual: " + gameModel.getTableSum());
            return;
        }

        try {
            // Play the card
            gameModel.playCard(card);
            logMessage("Jugaste: " + card);
            updateUI();

            // Move to next turn
            gameModel.nextTurn();
            processTurn();

        } catch (InvalidCardPlayException e) {
            logMessage("Error: " + e.getMessage());
        }
    }

    /**
     * Handles human player clicking on the deck.
     * Implements HU-4: Tomar una carta del mazo
     */
    private void handleDrawCard() {
        if (isProcessingTurn) {
            return; // Not human's turn
        }

        if (!(gameModel.getCurrentPlayer() instanceof HumanPlayer)) {
            logMessage("No es tu turno.");
            return;
        }

        HumanPlayer human = (HumanPlayer) gameModel.getCurrentPlayer();

        // Check if player has valid cards
        if (human.hasValidCard(gameModel.getTableSum())) {
            logMessage("Tienes cartas válidas para jugar. No puedes robar.");
            return;
        }

        // Draw card
        Card drawnCard = gameModel.drawCard();

        if (drawnCard != null) {
            logMessage("Robaste: " + drawnCard);
            updateUI();

            // Check if drawn card can be played
            if (drawnCard.canBePlayed(gameModel.getTableSum())) {
                logMessage("Puedes jugar la carta robada o pasar turno.");
                // Player can choose to play it or pass
            } else {
                logMessage("La carta robada no se puede jugar. Pasando turno...");
                gameModel.nextTurn();
                processTurn();
            }
        } else {
            logMessage("No hay más cartas en el mazo.");
        }
    }

    /**
     * Updates all UI elements based on current game state.
     */
    private void updateUI() {
        // Update table card
        Card topCard = gameModel.getTopCard();
        if (topCard != null) {
            loadCardImage(tableImageView, topCard);
        }

        // Update table sum
        colorLabel.setText("Cuenta: " + gameModel.getTableSum());

        // Update turn label
        Player currentPlayer = gameModel.getCurrentPlayer();
        if (currentPlayer != null) {
            turnLabel.setText(currentPlayer.getName());
        }

        // Update human player's hand
        updatePlayerHand();
    }

    /**
     * Updates the human player's hand in the GridPane.
     */
    private void updatePlayerHand() {
        gridPaneCardsPlayer.getChildren().clear();

        HumanPlayer human = gameModel.getHumanPlayer();
        if (human == null) return;

        int column = 0;
        for (Card card : human.getHand()) {
            ImageView cardImageView = new ImageView();
            cardImageView.setFitWidth(98);
            cardImageView.setFitHeight(134);
            cardImageView.setPreserveRatio(true);

            loadCardImage(cardImageView, card);

            // Add click handler
            cardImageView.setOnMouseClicked(event -> handleCardClick(card, cardImageView));
            cardImageView.setStyle("-fx-cursor: hand;");

            // Add hover effect
            cardImageView.setOnMouseEntered(event -> cardImageView.setOpacity(0.8));
            cardImageView.setOnMouseExited(event -> cardImageView.setOpacity(1.0));

            gridPaneCardsPlayer.add(cardImageView, column++, 0);
        }
    }

    /**
     * Loads a card image into an ImageView.
     *
     * @param imageView the ImageView to load into
     * @param card the card whose image to load
     */
    private void loadCardImage(ImageView imageView, Card card) {
        try {
            String imagePath = "/view/cartas-poker/" + card.getImageName();
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load card image: " + card.getImageName());
            e.printStackTrace();
        }
    }

    /**
     * Logs a message to the communication text area.
     *
     * @param message the message to log
     */
    private void logMessage(String message) {
        Platform.runLater(() -> {
            commTextArea.appendText(message + "\n");
            commTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    /**
     * Shows game over screen.
     * Implements HU-6: Fin del juego
     */
    private void showGameOver() {
        Player winner = gameModel.getWinner();

        String message;
        if (winner != null) {
            message = "¡Juego terminado!\n\n" +
                    "Ganador: " + winner.getName() + "\n\n" +
                    "¿Quieres jugar de nuevo?";
        } else {
            message = "¡Juego terminado!\n\nNo hay ganador.\n\n¿Quieres jugar de nuevo?";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin del Juego");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        handleExit();
    }

    /**
     * Handles exit button click.
     * Returns to menu.
     */
    private void handleExit() {
        try {
            com.example.minip3poe.view.GameStage.deleteInstance();
            MenuStage.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
