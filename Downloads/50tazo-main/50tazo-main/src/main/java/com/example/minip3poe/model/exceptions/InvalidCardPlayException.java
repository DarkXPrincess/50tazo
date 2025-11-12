package com.example.minip3poe.model.exceptions;

/**
 * Checked exception thrown when a player attempts to play an invalid card.
 * This exception must be caught and handled by the caller.
 *
 * Examples:
 * - Playing a card that would exceed the table sum of 50
 * - Playing a card that doesn't belong to the player's hand
 *
 * @author Juan David Salazar
 * @version 1.0
 */
public class InvalidCardPlayException extends Exception {

    /**
     * Creates a new InvalidCardPlayException with a default message.
     */
    public InvalidCardPlayException() {
        super("Invalid card play: The card cannot be played.");
    }

    /**
     * Creates a new InvalidCardPlayException with a custom message.
     *
     * @param message the detail message
     */
    public InvalidCardPlayException(String message) {
        super(message);
    }

    /**
     * Creates a new InvalidCardPlayException with a custom message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidCardPlayException(String message, Throwable cause) {
        super(message, cause);
    }
}
