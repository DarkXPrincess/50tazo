package com.example.minip3poe.model.player;

import com.example.minip3poe.model.Card;

/**
 * Represents a human player in the Cincuentazo game.
 * The card selection is done through the GUI interaction.
 *
 * @author Juan David Salazar
 * @version 1.0
 */
public class HumanPlayer extends Player {

    private Card selectedCard;

    /**
     * Creates a new human player with the specified name.
     *
     * @param name the player's name
     */
    public HumanPlayer(String name) {
        super(name);
        this.selectedCard = null;
    }

    /**
     * Sets the card selected by the human player through the GUI.
     *
     * @param card the card selected
     */
    public void setSelectedCard(Card card) {
        this.selectedCard = card;
    }

    /**
     * Returns the card previously selected by the human player.
     * This method is called by the game logic after the player clicks a card in the GUI.
     *
     * @param currentTableSum the current sum on the table (not used for human, validated in GUI)
     * @return the selected card, or null if no card has been selected
     */
    @Override
    public Card selectCard(int currentTableSum) {
        Card cardToPlay = selectedCard;
        selectedCard = null; // Reset for next turn
        return cardToPlay;
    }

    /**
     * Checks if a specific card from the hand can be played.
     * Used for GUI validation before allowing the player to select a card.
     *
     * @param card the card to check
     * @param currentTableSum the current sum on the table
     * @return true if the card can be played, false otherwise
     */
    public boolean canPlayCard(Card card, int currentTableSum) {
        return hand.contains(card) && card.canBePlayed(currentTableSum);
    }
}
