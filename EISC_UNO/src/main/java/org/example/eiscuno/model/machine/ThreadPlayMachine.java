package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.listener.GameOverListener;
import org.example.eiscuno.listener.MachinePlayListener;
import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.Serializable.SerializableFileHandler;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.game.GameStateEnum;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.gameState.GameState;
import org.example.eiscuno.model.game.TurnEnum;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.*;

/**
 * Class {@code ThreadPlayMachine}
 *
 * <p>Represents a separate thread controlling the behavior of the machine player in the UNO game.
 * It automatically attempts to play a valid card whenever it is the machine's turn.
 * If no card can be played, the machine draws a card and passes the turn.</p>
 *
 * <p>This class continuously runs while {@link #running} is {@code true} and communicates
 * with various event listeners to notify about significant events such as machine plays,
 * drawing cards, or the game ending.</p>
 *
 * @see GameUno
 * @see Player
 * @see Card
 * @see MachinePlayListener
 * @see GameOverListener
 */
public class ThreadPlayMachine extends Thread {
    /** The table where the game is being played, used to check the current top card. */
    private Table table;
    /** The machine-controlled player. */
    private Player playerMachine;
    /** The human-controlled player. */
    private Player humanPlayer;
    /** The {@link ImageView} displaying the top card on the table. */
    private ImageView tableImageView;
    /** The active {@link GameUno} instance managing game logic. */
    private GameUno gameUno;
    /** Tracks whose turn it is in the game. */
    private volatile TurnEnum turn;
    /** Controls whether this thread's execution loop should continue running. */
    private volatile boolean running;
    /** Optional listener for game-over events. */
    private GameOverListener gameOverListener;
    /** Holds the current saved state of the game for persistence. */
    private GameState gameState;
    /** Utility handler for serializing the game state. */
    private SerializableFileHandler serializableFileHandler;
    /** Listener for notifying when the machine has played or drawn a card. */
    private MachinePlayListener machinePlayListener;


    /**
     * Constructs a {@code ThreadPlayMachine} instance.
     *
     * @param table         the {@link Table} representing the playing area
     * @param playerMachine the {@link Player} representing the machine
     * @param tableImageView the {@link ImageView} for updating the top card display
     * @param gameUno       the active {@link GameUno} instance
     * @param HumanPlayer   the {@link Player} representing the human opponent
     */
    public ThreadPlayMachine(Table table, Player playerMachine, ImageView tableImageView, GameUno gameUno
            , Player HumanPlayer) {
        this.table = table;
        this.playerMachine = playerMachine;
        this.tableImageView = tableImageView;
        this.gameUno = gameUno;
        this.turn = gameUno.getTurn();
        this.running = true;
        this.humanPlayer = HumanPlayer;
    }


    /**
     * Main execution loop for the machine's behavior.
     * <p>
     * Continuously checks if it's the machine's turn. If it is, waits 2 seconds before
     * attempting to play a valid card via {@link #putCardOnTable()}.
     * </p>
     */
    public void run() {
        while (running) {
            turn = gameUno.getTurn();
            if (turn == TurnEnum.MACHINE) {
                System.out.println("MACHINE TURN!");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                putCardOnTable();
            }
        }
    }


    /**
     * Stops the execution loop by setting {@code running} to false.
     */
    public void stopThread() {
        this.running = false;
    }

    /**
     * Handles the machine's decision-making process to play a card.
     * <ul>
     *     <li>Finds the first playable card in the machine's hand.</li>
     *     <li>Chooses a random color for wild cards.</li>
     *     <li>Plays the card and updates the {@link ImageView} table image.</li>
     *     <li>If no cards can be played, draws one card and passes the turn.</li>
     * </ul>
     *
     * @see GameUno#isCardPlayable(Card, Card)
     * @see GameUno#eatCard(Player, int)
     */
    public void putCardOnTable() {
        ArrayList<Card> cards = playerMachine.getCardsPlayer();
        Card cardOnTable = table.getCurrentCardOnTheTable();
        boolean machinePlayed = false;

        if (gameUno.isGameOver() != GameStateEnum.GAME_ONGOING) {
            return;
        }

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            boolean isCardPlayable = gameUno.isCardPlayable(card, cardOnTable);
            if (isCardPlayable) {
                if (card.getValue().equals("NEWCOLOR") || card.getValue().equals("EAT4")) {
                    List<String> colors = List.of("GREEN", "YELLOW", "BLUE", "RED");
                    Random random = new Random();
                    String chosenColor = colors.get(random.nextInt(colors.size()));

                    System.out.println("Máquina: Color elegido: " + chosenColor);
                    card.applyEffect(card.new CardEffectContext(gameUno, humanPlayer, chosenColor));

                } else {
                    card.applyEffect(card.new CardEffectContext(gameUno, humanPlayer));
                }

                if (card.getEffect() == null) {
                    gameUno.changeTurn();
                }

                gameUno.playCard(card);
                //setHasPlayerPlayed(card.getValue().equals("SKIP") || card.getValue().equals("REVERSE"));

                tableImageView.setImage(card.getImage());
                playerMachine.removeCard(i);


                if (gameOverListener != null) {
                    gameOverListener.onGameOver();
                }
                //Boolean flag to exit while loop
                machinePlayed = true;

            }
        }

        if (!machinePlayed) {
            try {
                gameUno.eatCard(playerMachine, 1);
            }catch (EmptyDeck e){
                e.printStackTrace();
            }
            if(machinePlayListener != null){
                machinePlayListener.onMachineDrewCard();
            }
            gameUno.changeTurn();
            System.out.println("No hay cartas jugables.");

        }

        if(machinePlayListener != null){
            machinePlayListener.onMachinePlayed();
        }
        saveGameState();
    }

    /**
     * Sets the listener to be notified when the game ends.
     *
     * @param gameOverListener the listener to handle game over behavior
     */
    public void setGameOverListener(GameOverListener gameOverListener) {
        this.gameOverListener = gameOverListener;
    }

    /**
     * Sets the listener to be notified when the machine plays or draws a card.
     *
     * @param machinePlayListener the {@link MachinePlayListener} instance
     */
    public void setMachinePlayListener(MachinePlayListener machinePlayListener) {
        this.machinePlayListener = machinePlayListener;
    }

    /**
     * Saves the current state of the game to a serialized file named {@code GameState.ser}.
     * This method is called after each machine action.
     */
    public void saveGameState() {
        serializableFileHandler = new SerializableFileHandler();
        this.gameState = new GameState(this.gameUno.getDeck(), this.gameUno, this.table, this.humanPlayer, this.playerMachine);
        serializableFileHandler.serialize("GameState.ser", this.gameState);
        System.out.println("Saving machine movement...");
    }
}


