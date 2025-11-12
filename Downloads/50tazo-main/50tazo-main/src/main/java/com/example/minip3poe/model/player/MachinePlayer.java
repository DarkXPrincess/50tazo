package com.example.minip3poe.model.player;

import com.example.minip3poe.model.Card;

import java.util.Random;

/**
 * Represents a machine (AI) player in the Cincuentazo game.
 * The machine automatically selects valid cards from its hand.
 * @author Juan David Salazar
 * @version 1.0
 */
public class MachinePlayer extends Player {

    private Random random;

    /**
     * Creates a new machine player with the specified name.
     *
     * @param name the player's name (e.g., "Machine 1", "Machine 2")
     */
    public MachinePlayer(String name) {
        super(name);
        this.random = new Random();
    }

    /**
     * Automatically selects a valid card from the machine's hand.
     * Strategy: Randomly selects one of the valid cards that can be played.
     *
     * @param currentTableSum the current sum on the table
     * @return a valid card to play, or null if no valid card exists
     */
    @Override
    public Card selectCard(int currentTableSum) {
        // Find all valid cards
        java.util.List<Card> validCards = new java.util.ArrayList<>();

        for (Card card : hand) {
            if (card.canBePlayed(currentTableSum)) {
                validCards.add(card);
            }
        }

        // If no valid cards, return null
        if (validCards.isEmpty()) {
            return null;
        }

        // Randomly select one of the valid cards
        return validCards.get(random.nextInt(validCards.size()));
    }

    /**
     * Gets a random delay time for the machine to "think" before playing.
     * Returns a value between 2000ms (2 seconds) and 4000ms (4 seconds).
     *
     * @return delay time in milliseconds
     */
    public int getThinkingDelay() {
        return 2000 + random.nextInt(2001); // 2000-4000ms
    }

    /**
     * Gets a random delay time for drawing a card from the deck.
     * Returns a value between 1000ms (1 second) and 2000ms (2 seconds).
     *
     * @return delay time in milliseconds
     */
    public int getDrawingDelay() {
        return 1000 + random.nextInt(1001); // 1000-2000ms
    }
}
