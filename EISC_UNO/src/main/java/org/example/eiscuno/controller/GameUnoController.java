package org.example.eiscuno.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.eiscuno.listener.MachinePlayListener;
import org.example.eiscuno.model.Serializable.SerializableFileHandler;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.exceptions.NonPlayableCard;
import org.example.eiscuno.model.game.GameStateEnum;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.game.TurnEnum;
import org.example.eiscuno.model.gameState.GameState;
import org.example.eiscuno.model.machine.ThreadCurrentColorMachine;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUnoMachine;
import org.example.eiscuno.model.planeTextFiles.PlaneTextFileHandler;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.view.WelcomeStage;
import org.example.eiscuno.view.drawers.ShapeDrawer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing the main Uno game scene.
 * <p>
 * Handles player actions, machine actions, game state updates, animations, and event listeners.
 * </p>
 */
public class GameUnoController {
// =============================
// ======  UI COMPONENTS  ======
// =============================

    /** GridPane representing the machine player's cards (displayed face down). */
    @FXML
    private GridPane gridPaneCardsMachine;
    /** GridPane representing the human player's cards (displayed face up). */
    @FXML
    private GridPane gridPaneCardsPlayer;
    /** ImageView displaying the current top card on the table. */
    @FXML
    private ImageView tableImageView;
    /** ImageView representing the deck of cards from which players can draw. */
    @FXML
    private ImageView deckImageView;
    /** ImageView used for graphical representation of stacked cards in the deck. */
    @FXML
    private ImageView deckCards;
    /** Button that appears when the player needs to declare "UNO". */
    @FXML
    private Button unoButton;
    /** ImageView associated with the UNO button for visual emphasis. */
    @FXML
    private ImageView unoImageView;
    /** Label used to display temporary error or notification messages to the player. */
    @FXML
    private Label errorLabel;
    /** Label that shows whose turn it currently is (player or machine). */
    @FXML
    private Label turnLabel;
    /** Container (HBox) holding the turn label and corresponding player/machine icon. */
    @FXML
    private HBox turnHBox;
    /** Container (HBox) holding the circle that displays the current card color in play. */
    @FXML
    private HBox colorHBox;
    /** Circle used as a visual indicator of the active card color (green, yellow, red, or blue). */
    @FXML
    private Circle colorCircle;

    // =============================
    // =======  GAME LOGIC  ========
    // =============================

    /** Instance representing the human player in the game. */
    private Player humanPlayer;
    /** Instance representing the machine (AI) player in the game. */
    private Player machinePlayer;
    /** The deck object managing all available Uno cards to be drawn. */
    private Deck deck;
    /** The table object that stores cards played during the game. */
    private Table table;
    /** Core game logic class controlling turn order, rules, and playable actions. */
    private GameUno gameUno;
    /** Current starting index of the visible set of cards displayed to the player. */
    private int posInitCardToShow;
    /** Tracks the currently active color on the table (updated dynamically). */
    private String currentColor;
    // =============================
    // ========  THREADING  ========
    // =============================

    /** Background thread responsible for controlling machine (AI) moves. */
    private ThreadPlayMachine threadPlayMachine;
    /** Runnable controlling logic for handling "UNO" declarations from the player. */
    private ThreadSingUnoMachine  threadSingUnoMachine;
    /** Runnable that continuously monitors and updates the currently active color in the game. */
    private ThreadCurrentColorMachine threadCurrentColorMachine;
    /** Thread instance managing the asynchronous execution of the UNO declaration logic. */
    private Thread threadSingUno;
    /** Thread instance managing asynchronous execution of color monitoring logic. */
    private Thread threadCurrentColor;
    // =============================
    // =======  GAME STATE  ========
    // =============================

    /** Stores the nickname of the human player, loaded from saved data. */
    private String nickname;
    /** Serializable object representing a snapshot of the entire game state. */
    private GameState gameState;
    /** Indicates whether the player is continuing a previously saved game session. */
    private Boolean isContinue;
    /** Handles serialization and deserialization of the game state for saving/loading. */
    private SerializableFileHandler serializableFileHandler;
    /** Utility class for reading and writing plain text data, such as player information. */
    private PlaneTextFileHandler planeTextFileHandler;
    /** Helper class for dynamically drawing shapes (icons for player and machine turns). */
    private ShapeDrawer shapeDrawer = new ShapeDrawer();

    /**
     * Initializes the controller when the FXML scene loads.
     * @throws IOException if the save state files fail to load correctly.
     */
    @FXML
    public void initialize() throws IOException {
        //initVariables();
        isContinue = WelcomeStage.getInstance().getWelcomeStageController().returnIsContinue();
        WelcomeStage.deleteInstance();
        serializableFileHandler = new SerializableFileHandler();
        planeTextFileHandler = new PlaneTextFileHandler();

        if(!isContinue){
            try {
                initVariables();
                this.gameUno.startGame();
                threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno, this.humanPlayer);
                threadPlayMachine.start();

                threadSingUnoMachine = new ThreadSingUnoMachine(this.humanPlayer, this.gameUno);
                threadSingUno = new Thread(threadSingUnoMachine);
                threadSingUno.setDaemon(true);
                threadSingUno.start();
            }catch (Exception e){
                showError(errorLabel, e.getMessage());
            }

            threadCurrentColorMachine = new ThreadCurrentColorMachine(this.gameUno, this.table);
            threadCurrentColor = new Thread(threadCurrentColorMachine);
            threadCurrentColor.setDaemon(true);
            threadCurrentColor.start();

            setUnoListener();
            setGameOverListener();
            setCurrentColorListener();

            setMachineListener();
            showUnoButton();
            refreshUI();

        }
        else{
            loadGameState();
        }
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
        this.nickname = planeTextFileHandler.read("PlayerData.csv")[0];
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);
        Card currentCardOnTable = null;

        //Controlando excepción
        try {
            currentCardOnTable = this.table.getCurrentCardOnTheTable();
            tableImageView.setImage(currentCardOnTable.getImage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Mesa vacía...");
        }

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();
            Card finalCurrentCardOnTable = currentCardOnTable;
            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                try {
                    boolean isPlayable = gameUno.isCardPlayable(card, finalCurrentCardOnTable);
                    if (!isPlayable) {
                        throw new NonPlayableCard("¡Carta inválida!");
                    }
                    if (gameUno.getTurn() == TurnEnum.PLAYER
                            && gameUno.isGameOver() == GameStateEnum.GAME_ONGOING) {
                        String color = "";
                        // Aplicar efecto si es una carta especial
                        Player targetPlayer = machinePlayer;
                        if (card.getValue().equals("NEWCOLOR") || card.getValue().equals("EAT4")) {
                            color = askColor();
                        }
                        if (card.getEffect() != null) {
                            card.applyEffect(card.new CardEffectContext(gameUno, targetPlayer, color));
                        } else {
                            gameUno.changeTurn();
                            System.out.println("Turn: " + gameUno.getTurn());
                        }
                        gameUno.playCard(card);
                        saveGameState();
                        tableImageView.setImage(card.getImage());
                        humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        showUnoButton();

                        if (gameUno.isGameOver() != GameStateEnum.GAME_ONGOING) {
                            threadPlayMachine.stopThread();
                            threadPlayMachine.interrupt();

                            threadSingUnoMachine.stopThread();
                            threadSingUno.interrupt();

                            threadCurrentColorMachine.stopThread();
                            threadCurrentColor.interrupt();

                            gameHasEndedAlert();
                        }

                    }
                }catch (NonPlayableCard e) {
                    showError(errorLabel, e.getMessage());
                }
                refreshUI();
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }

    }

    /**

     * Shows a mini windows asking for the color to change
     * @return the new color
     * Displays a dialog window to ask for a color change for color changing cards.
     */
    public String askColor (){
        while (true) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("GREEN", List.of("GREEN", "YELLOW", "BLUE", "RED"));
            dialog.setTitle("Cambio de color");
            dialog.setHeaderText("Elige un nuevo color");
            dialog.setContentText("Color:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                return result.get();
            }
        }
    }

    /**
     * Refreshes UI components (cards, UNO button, labels).
     */
    private void refreshUI() {
        printCardsHumanPlayer();
        printCardsMachinePlayer();
        updateLabels();
        showUnoButton();
    }

    /**
     * Refreshes UI labels displaying the current player's turn and the current playable color.
     */
    public void updateLabels(){
        String color = table.getCurrentCardOnTheTable().getColor();
        color = switch (color) {
            case "GREEN" -> "#379711";
            case "YELLOW" -> "#ECD407";
            case "RED" -> "#D72600";
            case "BLUE" -> "#0956BF";
            default -> "#8a08fc";
        };

        colorCircle.setFill(Color.web(color));
        turnHBox.getChildren().clear();
        Group turnGroup = null;
        if(gameUno.getTurn() == TurnEnum.PLAYER){
            turnLabel.setText("Turno: " + nickname );
            turnGroup = shapeDrawer.drawPerson();
        }else if(gameUno.getTurn() == TurnEnum.MACHINE){
            turnLabel.setText("Turno: Máquina");
            turnGroup = shapeDrawer.drawRobot();
        }
        turnHBox.getChildren().addAll(turnLabel, turnGroup);
    }

    /**
     * Prints the cards of the machine (face down).
     */
    private void printCardsMachinePlayer(){
        this.gridPaneCardsMachine.getChildren().clear();
        int maxCards = Math.min(this.machinePlayer.getCardsPlayer().size(), 4);

        for(int i=0; i < maxCards; i++) {
            ImageView backCardUno = new ImageView(EISCUnoEnum.RECURSO_39.getFilePath());
            backCardUno.setY(16);
            backCardUno.setFitHeight(90);
            backCardUno.setFitWidth(60);

            this.gridPaneCardsMachine.add(backCardUno, i, 0);
        }
    }

    /**
     * Notifies the controller if the uno button should appear or disappear and if his functionality should work because
     * 1) The machine sang uno
     * 2) The player sang uno
     * 3) The player no longer has one card
     */
    private void setUnoListener(){
        threadSingUnoMachine.setUnoEventListener(() -> {
            Platform.runLater(() -> {
                showUnoButton();
                refreshUI();
                showError(errorLabel, "¡Olvidaste decir UNO!");
            });
        });

    }

    /**
     * Notifies the controller if the machinePlayer has made a move, including:
     * 1) The machine played a valid hard.
     * 2) The machine had no valid cards to play and drew up a card from the deck.
     */
    private void setMachineListener(){
        threadPlayMachine.setMachinePlayListener(new MachinePlayListener() {
            /**
             * Called when the machine draws a card because it had no playable moves.
             * This action is performed on the JavaFX Application Thread to safely
             * update the UI.
             */
            @Override
            public void onMachineDrewCard() {
                Platform.runLater(() -> {
                    showError(errorLabel, "¡Máquina tomó una carta!");
                    refreshUI();
                });
            }

            /**
             * Called when the machine successfully plays a card onto the table.
             * Updates the UI to reflect the new state of the table and cards.
             */
            @Override
            public void onMachinePlayed() {
                Platform.runLater(() -> {
                    refreshUI();
                    showUnoButton();
                });
            }

        });
    }

    /**
     * Notifies the controller if the game has ended in the turn of the machine (the machine played all his cards) or if the cards ran out
     */
    private void setGameOverListener(){
        threadPlayMachine.setGameOverListener(() -> {
            Platform.runLater(() -> {
                if(gameUno.isGameOver() != GameStateEnum.GAME_ONGOING){
                    threadPlayMachine.stopThread();
                    threadPlayMachine.interrupt();

                    threadSingUnoMachine.stopThread();
                    threadSingUno.interrupt();

                    threadCurrentColorMachine.stopThread();
                    threadCurrentColor.interrupt();

                    gameHasEndedAlert();
                }
            });
        });

        deck.setGameOverListener(() -> {
            if(gameUno.isGameOver() != GameStateEnum.GAME_ONGOING){
                threadPlayMachine.stopThread();
                threadPlayMachine.interrupt();

                threadSingUnoMachine.stopThread();
                threadSingUno.interrupt();

                threadCurrentColorMachine.stopThread();
                threadCurrentColor.interrupt();

                deckImageView.setVisible(false);
                gameHasEndedAlert();
            }
        });
    }

    /**
     * Saves the new color if it changed
     */
    public void setCurrentColorListener(){
        threadCurrentColorMachine.setCurrentColorListener(() -> {
            currentColor = gameUno.getCurrentColor();
        });
    }

    /**
     * Shows a visual alert if the game has ended
     */
    private void gameHasEndedAlert() {
        GameStateEnum gameState = gameUno.isGameOver();

        if (gameState != GameStateEnum.GAME_ONGOING) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Juego terminado!");

            String winnerName;
            switch (gameState) {
                case PLAYER_WON:
                    winnerName = "Jugador humano";
                    alert.setHeaderText("🎉 ¡Tenemos un ganador! 🎉");
                    alert.setContentText("El ganador es el: " + winnerName);
                    break;
                case MACHINE_WON:
                    winnerName = "Jugador máquina";
                    alert.setHeaderText("🎉 ¡Tenemos un ganador! 🎉");
                    alert.setContentText("El ganador es el: " + winnerName);
                    break;
                case DECK_EMPTY:
                    alert.setHeaderText("Se acabaron las cartas...  :(");
                    alert.setContentText("No hay ganador. El juego ha terminado");
            }


        ImageView imageView = new ImageView(new Image(getClass().getResource("/org/example/eiscuno/favicon.png").toString()));
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        alert.setGraphic(imageView);

        alert.showAndWait();
        Stage stage = (Stage) tableImageView.getScene().getWindow();
        stage.close();
        }

    }

    /**
     * Shows the uno button if the game is on going and the human player has one card left.
     */
    private void showUnoButton(){
        if(humanPlayer.getCardsPlayer().size() > 1){
            threadSingUnoMachine.setAlreadySangUno(false);
        }
        if(humanPlayer.getCardsPlayer().size() == 1 && !threadSingUnoMachine.getAlreadySangUno()
            && gameUno.isGameOver() == GameStateEnum.GAME_ONGOING){
            System.out.println("Showing UNO BUTTON");
            unoButton.setVisible(true);
            //unoButton.setManaged(true);

            unoImageView.setVisible(true);
            //unoImageView.setManaged(true);
        }else{
            System.out.println("Hiding UNO BUTTON");
            unoButton.setVisible(false);
            //unoButton.setManaged(false);

            unoImageView.setVisible(false);
            //unoImageView.setManaged(false);
        }

    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        boolean areCardsPlayable = false;

        for(int i=0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            Card card = this.humanPlayer.getCardsPlayer().get(i);
            if(gameUno.isCardPlayable(card, table.getCurrentCardOnTheTable())){
                areCardsPlayable = true;
                showError(errorLabel, "¡Aún tienes jugadas posibles!");
                break;
            }
        }
        if(!areCardsPlayable && gameUno.isGameOver() == GameStateEnum.GAME_ONGOING){
            try {
                gameUno.eatCard(humanPlayer, 1);
            }catch (EmptyDeck e){
                gameHasEndedAlert();
            }
            saveGameState();
            showUnoButton();
            printCardsHumanPlayer();
            gameUno.changeTurn();
            updateLabels();
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        System.out.println("Cantar UNO presionado");
        showError(errorLabel, "¡Gritaste UNO!");
        threadSingUnoMachine.setAlreadySangUno(true);
        saveGameState();
        showUnoButton();
    }

    /**
     * Saves the current state of the game
     */
    public void saveGameState(){
        System.out.println("Saving gameState...");
        this.gameState = new GameState(this.deck,this.gameUno,this.table,this.humanPlayer,this.machinePlayer);
        serializableFileHandler.serialize("GameState.ser", gameState);
    }

    /**
     * Loads a saved state of the game
     */
    public void loadGameState(){
        System.out.println("Loading gameState...");
        this.gameState = (GameState) serializableFileHandler.deserialize("GameState.ser");
        this.nickname = planeTextFileHandler.read("PlayerData.csv")[0];

        if(gameState != null){
            this.deck = gameState.getDeck();
            this.gameUno = gameState.getGameUno();
            this.table = gameState.getTable();
            this.humanPlayer = gameState.getHumanPlayer();
            this.machinePlayer = gameState.getMachinePlayer();

            Card cardOnTable = table.getCurrentCardOnTheTable();
            if (cardOnTable != null) {
                tableImageView.setImage(cardOnTable.getImage());
            }

            threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.gameUno, this.humanPlayer);
            threadPlayMachine.start();

            threadSingUnoMachine = new ThreadSingUnoMachine(this.humanPlayer, this.gameUno);
            threadSingUno = new Thread(threadSingUnoMachine);
            threadSingUno.setDaemon(true);
            threadSingUno.start();

            threadCurrentColorMachine = new ThreadCurrentColorMachine(this.gameUno, this.table);
            threadCurrentColor = new Thread(threadCurrentColorMachine);
            threadCurrentColor.setDaemon(true);
            threadCurrentColor.start();

            setUnoListener();
            setGameOverListener();
            setCurrentColorListener();
            refreshUI();
            showUnoButton();

        }
    }

    /**
     * Displays an error message using a fading label animation.
     * The message fades in, stays visible for a short time, then fades out.
     *
     * @param label   the label to display the message on
     * @param message the error message to show
     */
    public void showError(Label label, String message) {
        label.setText(message);
        label.setOpacity(0);
        label.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(1)); //Label stays visible for a second

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> label.setVisible(false)); //Leave label hidden after fade out

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        sequence.play();
    }
}
