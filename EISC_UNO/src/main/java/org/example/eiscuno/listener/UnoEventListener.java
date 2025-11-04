package org.example.eiscuno.listener;

/**
 * Listener interface for UNO events in the game.
 * <p>
 * This listener is triggered when a player forgets to press the "UNO" button
 * while having only one card left in hand. The controller can use this event
 * to apply penalties or update the game's UI accordingly.
 * </p>
 */
public interface UnoEventListener {

    /**
     * Called when the player forgets to say "UNO" after playing a card and
     * having only one card left.
     * <p>
     * This event allows the controller to:
     * <ul>
     *   <li>Show or hide the UNO button dynamically.</li>
     *   <li>Apply penalties to the player.</li>
     *   <li>Trigger relevant UI feedback.</li>
     * </ul>
     */
    void onPlayerForgotToSayUno();
}
