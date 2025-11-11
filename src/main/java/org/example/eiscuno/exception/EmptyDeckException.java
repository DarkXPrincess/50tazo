package org.example.eiscuno.exception;

/**
 * Thrown when attempting to draw from an empty deck.
 * This is an unchecked exception because the engine will attempt to replenish the deck.
 */
public class EmptyDeckException extends RuntimeException {
    public EmptyDeckException(String message) {
        super(message);
    }
}
