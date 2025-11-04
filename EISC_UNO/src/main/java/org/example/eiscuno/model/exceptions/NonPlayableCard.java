package org.example.eiscuno.model.exceptions;

/**
 * Exception thrown when a player attempts to play a card
 * that is not valid according to the game rules.
 *
 * <p>This exception is typically used in the game logic
 * to prevent the execution of an invalid move and notify
 * the player or controlling mechanism.</p>
 *
 * @see org.example.eiscuno.model.game.GameUnoAdapter#isCardPlayable(org.example.eiscuno.model.card.Card, org.example.eiscuno.model.card.Card)
 */
public class NonPlayableCard extends Exception {
    /**
     * Constructs a new {@code NonPlayableCard} exception with a default message.
     */
    public NonPlayableCard() {
        super("NON PLAYABLE CARD");
    }
    /**
     * Constructs a new {@code NonPlayableCard} exception with a specified message
     * and a cause.
     *
     * @param message The detail message providing more information about the exception.
     * @param cause   The underlying cause of this exception (may be {@code null}).
     */
    public NonPlayableCard(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructs a new {@code NonPlayableCard} exception with a specified message.
     *
     * @param message The detail message providing more information about the exception.
     */
    public NonPlayableCard(String message) {
        super(message);
    }
    /**
     * Constructs a new {@code NonPlayableCard} exception with a specified cause.
     *
     * @param cause The underlying cause of this exception (may be {@code null}).
     */
    public NonPlayableCard(Throwable cause) {
        super(cause);
    }

}
