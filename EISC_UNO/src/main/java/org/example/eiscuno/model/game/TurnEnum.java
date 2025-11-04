package org.example.eiscuno.model.game;

/**
 * Enum {@code TurnEnum}
 * <p>
 * Represents the possible turns in an UNO game.
 * Used to determine which player is allowed to make a move during gameplay.
 * </p>
 *
 * <ul>
 *     <li>{@link #PLAYER} – Indicates it is the human player's turn.</li>
 *     <li>{@link #MACHINE} – Indicates it is the machine (AI) player's turn.</li>
 * </ul>
 *
 * @see GameUno
 */
public enum TurnEnum {
    /**
     * Indicates that it is the human player's turn to play.
     */
    PLAYER,
    /**
     * Indicates that it is the machine (AI) player's turn to play.
     */
    MACHINE
}
