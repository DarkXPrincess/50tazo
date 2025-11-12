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
    private Label descripcion;

    @FXML
    private Label turnLabel;

    @FXML
    private Label colorLabel;

    @FXML
    private GridPane gridPaneCardsPlayer;

    private GameModel gameModel;
    private boolean isProcessingTurn = false;
    private boolean waitingForHumanDraw = false;

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

    // 👉 Mensaje en consola cada vez que le toca a alguien
    if (currentPlayer != null) {
        System.out.println("===== TURNO DE: " + currentPlayer.getName() + " =====");
    }

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
        waitingForHumanDraw = false;

        HumanPlayer human = (HumanPlayer) gameModel.getCurrentPlayer();
        
        // Nota: mensaje de turno humano ahora se imprime en processTurn()

        // Verificar si tiene cartas válidas
        if (!human.hasValidCard(gameModel.getTableSum())) {
            // ELIMINACIÓN: No tiene cartas válidas
            logMessage(human.getName() + " no tiene cartas válidas. ¡Has sido eliminado!");

            // Enviar cartas del jugador eliminado al mazo
            gameModel.eliminatePlayer(human);

            // Pasar al siguiente turno
            gameModel.nextTurn();
            processTurn();
            return;
        }

        logMessage(human.getName() + ": Tu turno. Selecciona una carta para jugar.");
        updateUI();
    }


    /**
     * Processes machine player's turn using a separate thread.
     * Implements thread requirement and HU-3 + HU-4.
     *
     * @param machine the machine player
     */
    private void processMachineTurn(MachinePlayer machine) {
        isProcessingTurn = true;

        // ✅ Actualizar label de turno INMEDIATAMENTE
        turnLabel.setText(machine.getName());

        // Verificar si tiene cartas válidas (HU-5: Eliminación)
        if (!machine.hasValidCard(gameModel.getTableSum())) {
            logMessage(machine.getName() + " no tiene cartas válidas. ¡Ha sido eliminado!");

            // Enviar cartas del jugador eliminado al mazo
            gameModel.eliminatePlayer(machine);

            // Pasar al siguiente turno
            gameModel.nextTurn();
            processTurn();
            return;
        }

        logMessage(machine.getName() + " está pensando...");

        Task<Void> machineTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // HU-3: Simular "pensamiento" de 2-4 segundos
                Thread.sleep(machine.getThinkingDelay());

                Platform.runLater(() -> {
                    try {
                        // Seleccionar carta válida
                        Card selectedCard = machine.selectCard(gameModel.getTableSum());

                        if (selectedCard != null) {
                            // Jugar la carta
                            gameModel.playCard(selectedCard);
                            logMessage(machine.getName() + " jugó: " + selectedCard);
                            updateUI();

                            // HU-4: Esperar 1-2 segundos antes de robar (segundo hilo)
                            logMessage(machine.getName() + " va a robar una carta...");

                            new Thread(() -> {
                                try {
                                    Thread.sleep(machine.getDrawingDelay()); // 1-2 segundos

                                    Platform.runLater(() -> {
                                        // Robar carta
                                        Card drawnCard = gameModel.drawCard();

                                        if (drawnCard != null) {
                                            logMessage(machine.getName() + " robó una carta del mazo.");
                                        } else {
                                            logMessage(machine.getName() + " intentó robar pero no hay cartas.");
                                        }

                                        updateUI();

                                        // Pasar al siguiente turno
                                        gameModel.nextTurn();
                                        processTurn();
                                    });

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();

                        } else {
                            // No debería llegar aquí porque ya verificamos hasValidCard
                            logMessage("Error: " + machine.getName() + " no pudo seleccionar carta.");
                            gameModel.nextTurn();
                            processTurn();
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

        // ← NUEVO: Si ya jugó carta, no puede jugar otra hasta robar
        if (waitingForHumanDraw) {
            logMessage("Debes robar una carta del mazo antes de continuar.");
            return;
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
            logMessage("Jugaste: " + card + ". Ahora roba una carta del mazo."); // ← MENSAJE ACTUALIZADO
            updateUI();

            // ← NUEVO: Activar flag de espera
            waitingForHumanDraw = true;
            // NO pasar turno aquí, esperar a que robe

        } catch (InvalidCardPlayException e) {
            logMessage("Error: " + e.getMessage());
        }
    }


    /**
     * Handles human player clicking on the deck.
     * Implements HU-4: Tomar una carta del mazo
     */
    /**
     * Handles human player clicking on the deck.
     * Implements HU-4: Tomar una carta del mazo (solo después de jugar)
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

        // Solo se puede robar después de haber jugado una carta
        if (!waitingForHumanDraw) {
            logMessage("Primero debes jugar una carta.");
            return;
        }

        // Robar carta obligatoriamente después de jugar
        Card drawnCard = gameModel.drawCard();

        if (drawnCard != null) {
            logMessage("Robaste: " + drawnCard);
            updateUI();
        } else {
            logMessage("No hay más cartas en el mazo.");
        }

        // Resetear flag y pasar turno
        waitingForHumanDraw = false;
        gameModel.nextTurn();
        processTurn();
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
        colorLabel.setText(String.valueOf(gameModel.getTableSum()));

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
            String imagePath = "/com/example/minip3poe/cartas-poker/" + card.getImageName();
            System.out.println("Intentando cargar: " + imagePath);

            // Método alternativo usando URL
            java.net.URL imageUrl = getClass().getResource(imagePath);

            if (imageUrl == null) {
                System.err.println("❌ ERROR: Archivo no encontrado en classpath: " + imagePath);
                System.err.println("Carta: " + card);

                // Intentar listar contenido de la carpeta
                java.net.URL folderUrl = getClass().getResource("/com/example/minip3poe/cartas-poker/");
                if (folderUrl != null) {
                    System.out.println("✓ La carpeta cartas-poker SÍ existe");
                } else {
                    System.err.println("❌ La carpeta cartas-poker NO existe en el classpath");
                }
                return;
            }

            Image image = new Image(imageUrl.toExternalForm());
            imageView.setImage(image);
            System.out.println("✓ Imagen cargada exitosamente!");

        } catch (Exception e) {
            System.err.println("Failed to load card image: " + card.getImageName());
            e.printStackTrace();
        }
    }


    /**
     * Logs a message to the communication label.
     *
     * @param message the message to log
     */
    private void logMessage(String message) {
        Platform.runLater(() -> {
            descripcion.setText(message);
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
