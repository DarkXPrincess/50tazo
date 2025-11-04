package org.example.eiscuno.listener;

/**
 * Listener interface for monitoring the machine player's actions during the game.
 * <p>
 * This listener provides callback methods to notify the controller or UI
 * whenever the machine draws a card or successfully plays a card.
 * </p>
 */
public interface MachinePlayListener {
    /**
     * Called when the machine player draws a card because no valid card
     * could be played during its turn.
     * <p>
     * This event can be used to:
     * <ul>
     *     <li>Update the UI to reflect the machine's new hand size.</li>
     *     <li>Trigger animations or sounds for drawing a card.</li>
     * </ul>
     */
    void onMachineDrewCard();
    /**
     * Called when the machine player successfully plays a card on the table.
     * <p>
     * This event can be used to:
     * <ul>
     *     <li>Update the table's displayed card.</li>
     *     <li>Advance the turn to the next player.</li>
     *     <li>Trigger any UI feedback or animations.</li>
     * </ul>
     */
    void onMachinePlayed();
}
