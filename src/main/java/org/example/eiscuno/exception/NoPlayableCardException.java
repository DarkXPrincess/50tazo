package org.example.eiscuno.exception;

/**
 * Thrown when a player has no playable card given the current table sum.
 */
public class NoPlayableCardException extends Exception {
    public NoPlayableCardException(String message) {
        super(message);
    }
}
