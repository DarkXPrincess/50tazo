package com.example.minip3poe.model.exceptions;

/**
 * Checked exception thrown when an invalid number of machine players is selected.
 * Valid range: 1 to 3 machine players.
 *
 * @author Juan David Salazar
 * @author Veronica Granados
 * @author Freddy Alexander Melo Buitrago
 * @version 1.0
 */
public class InvalidPlayerCountException extends Exception {

    private int attemptedCount;

    /**
     * Creates a new InvalidPlayerCountException with a default message.
     */
    public InvalidPlayerCountException() {
        super("Invalid number of machine players. Must be between 1 and 3.");
    }

    /**
     * Creates a new InvalidPlayerCountException with the attempted count.
     *
     * @param attemptedCount the invalid number of machines the user tried to create
     */
    public InvalidPlayerCountException(int attemptedCount) {
        super("Invalid number of machine players: " + attemptedCount +
                ". Must be between 1 and 3.");
        this.attemptedCount = attemptedCount;
    }

    /**
     * Creates a new InvalidPlayerCountException with a custom message.
     *
     * @param message the detail message
     */
    public InvalidPlayerCountException(String message) {
        super(message);
    }

    /**
     * Gets the number of machines that was attempted to be created.
     *
     * @return the attempted count, or 0 if not set
     */
    public int getAttemptedCount() {
        return attemptedCount;
    }
}
