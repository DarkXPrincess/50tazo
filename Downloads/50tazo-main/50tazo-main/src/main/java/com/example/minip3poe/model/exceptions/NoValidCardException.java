package com.example.minip3poe.model.exceptions;

/**
 * Unchecked exception thrown when a player has no valid cards to play.
 * This typically indicates that the player should be eliminated from the game.
 *
 * This is a RuntimeException, so it does not need to be explicitly caught.
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class NoValidCardException extends RuntimeException {

    private String playerName;

    /**
     * Creates a new NoValidCardException with a default message.
     */
    public NoValidCardException() {
        super("Player has no valid cards to play and must be eliminated.");
    }

    /**
     * Creates a new NoValidCardException with a custom message.
     *
     * @param message the detail message
     */
    public NoValidCardException(String message) {
        super(message);
    }

    /**
     * Creates a new NoValidCardException with player information.
     *
     * @param playerName the name of the player with no valid cards
     * @param message the detail message
     */
    public NoValidCardException(String playerName, String message) {
        super(message);
        this.playerName = playerName;
    }

    /**
     * Gets the name of the player who has no valid cards.
     *
     * @return the player's name, or null if not set
     */
    public String getPlayerName() {
        return playerName;
    }
}
