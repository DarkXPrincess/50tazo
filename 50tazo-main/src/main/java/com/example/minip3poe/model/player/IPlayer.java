package com.example.minip3poe.model.player;

import com.example.minip3poe.model.Card;
import java.util.List;

/**
 * Interface defining the contract for all player types in the Cincuentazo game.
 * Provides common operations that both human and machine players must implement.
 *
 * @author Juan David Salazar
 * @version 1.0
 */
public interface IPlayer {

    /**
     * Selects a card to play from the player's hand.
     *
     * @param currentTableSum the current sum on the table
     * @return the selected card, or null if no valid card
     */
    Card selectCard(int currentTableSum);

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    void addCard(Card card);

    /**
     * Removes a card from the player's hand.
     *
     * @param card the card to remove
     * @return true if the card was removed, false otherwise
     */
    boolean removeCard(Card card);

    /**
     * Checks if the player has any valid card to play.
     *
     * @param currentTableSum the current sum on the table
     * @return true if at least one card can be played
     */
    boolean hasValidCard(int currentTableSum);

    /**
     * Eliminates this player from the game.
     */
    void eliminate();

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    String getName();

    /**
     * Gets the player's hand of cards.
     *
     * @return a copy of the player's hand
     */
    List<Card> getHand();

    /**
     * Checks if the player is eliminated.
     *
     * @return true if eliminated, false otherwise
     */
    boolean isEliminated();

    /**
     * Gets the number of cards in the player's hand.
     *
     * @return the hand size
     */
    int getHandSize();

    /**
     * Clears all cards from the player's hand.
     *
     * @return the list of cards that were in the hand
     */
    List<Card> clearHand();
}
