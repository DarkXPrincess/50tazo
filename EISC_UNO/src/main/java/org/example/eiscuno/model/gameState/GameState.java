package org.example.eiscuno.model.gameState;

import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class {@code GameState}
 * <p>
 * Represents the full state of an ongoing UNO game.
 * This class is used for saving and restoring the game's progress,
 * making it possible to resume gameplay from a previously serialized state.
 * </p>
 *
 * <p>It contains all relevant components of the game, including:</p>
 * <ul>
 *     <li>The current {@link Deck}</li>
 *     <li>The main {@link GameUno} instance containing game logic</li>
 *     <li>The current state of the {@link Table}</li>
 *     <li>The {@link Player} instances for both the human and the machine</li>
 * </ul>
 *
 * <p>This class implements {@link Serializable}, allowing it to be persisted
 * to a file using serialization.</p>
 *
 * @see Serializable
 * @see GameUno
 * @see Table
 * @see Player
 * @see Deck
 */
public class GameState implements Serializable {
    /** The current deck of UNO cards in play. */
    private Deck deck;
    /** The main game logic handler for UNO gameplay. */
    private GameUno gameUno;
    /** The table representing the cards currently in play. */
    private Table table;
    /** The human player participating in the game. */
    private Player humanPlayer;
    /** The machine (AI) player participating in the game. */
    private Player machinePlayer;

    /**
     * Constructs a new {@code GameState} instance with the provided game components.
     *
     * @param deck          the current {@link Deck} in play
     * @param gameUno       the {@link GameUno} logic instance
     * @param table         the {@link Table} where cards are placed
     * @param humanPlayer   the human {@link Player}
     * @param machinePlayer the machine (AI) {@link Player}
     */
    public GameState(Deck deck, GameUno gameUno, Table table, Player humanPlayer, Player machinePlayer) {
        this.deck = deck;
        this.gameUno = gameUno;
        this.table = table;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
    }

    /**
     * Returns the current deck of cards.
     *
     * @return the {@link Deck} instance
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Returns the current state of the table.
     *
     * @return the {@link Table} instance
     */
    public Table getTable() {
        return table;
    }

    /**
     * Returns the human player of the game.
     *
     * @return the {@link Player} instance representing the human player
     */
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * Returns the machine (AI) player of the game.
     *
     * @return the {@link Player} instance representing the machine player
     */
    public Player getMachinePlayer() {
        return machinePlayer;
    }

    /**
     * Returns the main game logic handler for this UNO game.
     *
     * @return the {@link GameUno} instance
     */
    public GameUno getGameUno() {
        return gameUno;
    }
}
