package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.exceptions.NonPlayableCard;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.io.Serializable;

/**
 * Represents a full game of Uno, extending the base logic provided by {@link GameUnoAdapter}.
 * <p>
 * This class serves as the main implementation of a UNO game, connecting all necessary
 * components such as the {@link Player human player}, {@link Player machine player},
 * {@link Deck deck}, and {@link Table table}.
 * </p>
 *
 * <p>
 * Unlike {@link GameUnoAdapter}, this class does not override any behavior yet, but
 * provides a dedicated instantiable implementation for the game.
 * </p>
 *
 * @see GameUnoAdapter
 * @see Player
 * @see Deck
 * @see Table
 * @see Card
 */
public class GameUno extends GameUnoAdapter implements Serializable {

    /**
     * Constructs a new {@code GameUno} instance by initializing both players,
     * the deck, and the table where the game will take place.
     * <p>
     * This constructor delegates initialization of core logic to the parent
     * {@link GameUnoAdapter} constructor.
     * </p>
     *
     * @param humanPlayer   the {@link Player} controlled by a human participant
     * @param machinePlayer the {@link Player} controlled by the AI opponent
     * @param deck          the {@link Deck} containing the cards for this game
     * @param table         the {@link Table} on which cards will be played
     */
    public GameUno(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        super(humanPlayer, machinePlayer, deck, table);
    }

}
