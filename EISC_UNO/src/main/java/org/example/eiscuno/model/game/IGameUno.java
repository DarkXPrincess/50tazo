package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.exceptions.NonPlayableCard;
import org.example.eiscuno.model.player.Player;

/**
 * Interface representing the Uno game functionality.
 */
public interface IGameUno {

    /**
     * Starts the Uno game.
     * @throws EmptyDeck if the deck empties during the initial dealing process.
     */
    void startGame() throws EmptyDeck;


    /**
     * Verifies is a card satisfy the game rules
     * @param cardToPlay the card being played
     * @param currentCardOnTable the card compared to
     * @return true if it can be played, false if not
     */
    boolean isCardPlayable(Card cardToPlay, Card currentCardOnTable);

    /**
     * Makes a player draw a specified number of cards from the deck.
     *
     * @param player the player who will draw the cards
     * @param numberOfCards the number of cards to be drawn
     * @throws EmptyDeck if the deck is empty.
     */
    void eatCard(Player player, int numberOfCards) throws EmptyDeck;

    /**
     * Put the first card on the table
     * @throws EmptyDeck if there are no cards in the deck.
     */
    void putFirstCard() throws EmptyDeck;


    /**
     * Plays a card in the game, adding it to the table.
     *
     * @param card the card to be played
     */
    void playCard(Card card);

    /**
     * Changes the state of the game turn
     */
    void changeTurn();

    /**
     * Handles the action when a player shouts "Uno".
     *
     * @param playerWhoSang the identifier of the player who shouted "Uno"
     */
    void haveSungOne(String playerWhoSang);

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow the starting position of the cards to be shown
     * @return an array of cards that are currently visible to the human player
     */
    Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow);

    /**
     * Checks if the game is over.
     *
     * @return 0 if the game is not over, 1 if the deck is empty, 2 if the human player has
     * played all his card or 3 if the machine player has played all his cards.
     */
    GameStateEnum isGameOver();
}
