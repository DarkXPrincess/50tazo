package org.example.eiscuno.listener;

/**
 * Listener interface for tracking changes to the current color in the Uno game.
 * <p>
 * This listener provides a callback method to notify the controller or UI when
 * the current active color on the table changes. This may happen when:
 * <ul>
 *     <li>A color-changing card (e.g., Wild or +4) is played.</li>
 *     <li>A card of a different color is placed on the table.</li>
 * </ul>
 */
public interface CurrentColorListener {
    /**
     * Notifies the controller if the color has changed
     */
    void onColorChanged();
}
