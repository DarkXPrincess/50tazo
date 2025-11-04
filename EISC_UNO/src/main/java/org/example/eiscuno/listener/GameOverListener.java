package org.example.eiscuno.listener;

/**
 * Listener interface for monitoring when the Uno game ends.
 * <p>
 * This listener provides a callback method to notify the controller or UI
 * when the game reaches a terminal state, such as:
 * <ul>
 *     <li>The deck runs out of cards.</li>
 *     <li>The human player wins by playing all their cards.</li>
 *     <li>The machine player wins by playing all their cards.</li>
 * </ul>
 */
public interface GameOverListener {

    /**
     * Notifies the controller if the game has ended
     */
    void onGameOver();
}
