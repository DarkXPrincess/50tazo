package org.example.eiscuno.model.game;

/**
 * Represents the possible states of an ongoing UNO game.
 * <p>
 * This enumeration is used to indicate whether the game is still in progress,
 * if a player has won, or if the deck has been depleted, ending the game.
 * </p>
 *
 * @see GameUno
 * @see GameUnoAdapter
 */
public enum GameStateEnum {
    /**
     * Indicates that the human player has played all their cards and won the game.
     */
    PLAYER_WON,
    /**
     * Indicates that the machine player has played all their cards and won the game.
     */
    MACHINE_WON,
    /**
     * Indicates that the draw pile (deck) has run out of cards,
     * preventing further play.
     */
    DECK_EMPTY,
    /**
     * Indicates that the game is still active and ongoing.
     */
    GAME_ONGOING
}
