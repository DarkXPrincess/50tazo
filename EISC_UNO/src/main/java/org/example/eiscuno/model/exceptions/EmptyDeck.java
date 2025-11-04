package org.example.eiscuno.model.exceptions;

/**
 * Exception thrown when an attempt is made to draw a card
 * from an empty deck in the Uno game.
 *
 * <p>This exception is used to indicate that no more cards
 * are available in the deck, and the operation cannot be completed.</p>
 *
 * @see org.example.eiscuno.model.game.GameUnoAdapter#eatCard(org.example.eiscuno.model.player.Player, int)
 * @see org.example.eiscuno.model.deck.Deck#takeCard()
 */
public class EmptyDeck extends Exception {
    /**
     * Constructs a new {@code EmptyDeck} exception with a default message.
     */
    public EmptyDeck() {
        super("Empty Deck");
    }
    /**
     * Constructs a new {@code EmptyDeck} exception with a specified message
     * and a cause.
     *
     * @param message The detail message providing more information about the exception.
     * @param cause   The underlying cause of this exception (may be {@code null}).
     */
    public EmptyDeck (String message,  Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructs a new {@code EmptyDeck} exception with a specified cause.
     *
     * @param cause The underlying cause of this exception (may be {@code null}).
     */
    public EmptyDeck (Throwable cause) {
        super(cause);
    }
    /**
     * Constructs a new {@code EmptyDeck} exception with a specified message.
     *
     * @param message The detail message providing more information about the exception.
     */
    public EmptyDeck(String message) {
        super(message);
    }
}
