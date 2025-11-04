package org.example.eiscuno.model.game;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.io.Serializable;

/**
 * Abstract adapter class implementing the basic logic and rules of an UNO game.
 * <p>
 * This class provides core functionalities such as distributing cards, verifying
 * if a card is playable, handling turn changes, managing the table state, and
 * detecting game-ending conditions.
 * </p>
 * <p>
 * It acts as a base for the {@link GameUno} class, encapsulating shared logic
 * while allowing specific game behaviors to be extended or modified.
 * </p>
 *
 * @see GameUno
 * @see Player
 * @see Deck
 * @see Table
 * @see Card
 */
public abstract class GameUnoAdapter implements IGameUno, Serializable {

    /**
     * The human player participating in the game.
     */
    protected Player humanPlayer;
    /**
     * The machine-controlled player participating in the game.
     */
    protected Player machinePlayer;
    /**
     * The deck containing all cards to be drawn during the game.
     */
    protected Deck deck;
    /**
     * The table where cards are placed during gameplay.
     */
    protected Table table;
    /**
     * Stores the current color in play (e.g., after a Wild or +4 card is played).
     */
    protected String currentColor;
    /**
     * Indicates whose turn it currently is ({@link TurnEnum#PLAYER} or {@link TurnEnum#MACHINE}).
     */
    protected volatile TurnEnum turn;

    /**
     * Constructs a {@code GameUnoAdapter} with the specified players, deck, and table.
     *
     * @param humanPlayer   the human player
     * @param machinePlayer the machine player
     * @param deck          the deck used in the game
     * @param table         the table where cards are played
     */
    public GameUnoAdapter(Player humanPlayer, Player machinePlayer, Deck deck, Table table) {
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.deck = deck;
        this.table = table;
        this.turn = TurnEnum.PLAYER;
        currentColor = "";

    }

    /**
     * {@inheritDoc}
     * <p>
     * Each player is dealt 10 cards (first 5 to the human player, next 5 to the machine).
     * The first card is then placed on the table.
     * </p>
     *
     * @throws EmptyDeck if the deck runs out of cards while dealing
     */
    @Override
    public void startGame() throws EmptyDeck {
        //Reparte las cartas iniciales al jugador humano y máquina
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                humanPlayer.addCard(this.deck.takeCard());
            } else {
                machinePlayer.addCard(this.deck.takeCard());
            }
        }
        putFirstCard();
    }

    /**
     * Verifies is a card satisfy the game rules
     * @param cardToPlay the card being played
     * @param currentCardOnTable the card compared to
     * @return true if it can be played, false if not
     */
    @Override
    public boolean isCardPlayable(Card cardToPlay, Card currentCardOnTable) {
        // Si la mesa está vacía (inicio del juego), cualquier carta es válida.
        if (currentCardOnTable == null) {
            return true;
        }

        // Coincidencia en color o valor
        boolean colorMatch = cardToPlay.getColor().equals(currentCardOnTable.getColor());
        //System.out.println(colorMatch);
        boolean valueMatch = cardToPlay.getValue().equals(currentCardOnTable.getValue());
        //System.out.println(valueMatch);

        // Cartas especiales (como "WILD" o "+4") pueden jugarse en cualquier momento
        boolean isSpecialCard = cardToPlay.getValue().equals("NEWCOLOR") ||
                cardToPlay.getValue().equals("EAT4");

        return colorMatch || valueMatch || isSpecialCard;
    }

    /**
     * Allows a player to draw a specified number of cards from the deck.
     *
     * @param player        The player who will draw cards.
     * @param numberOfCards The number of cards to draw.
     */
    @Override
    public void eatCard(Player player, int numberOfCards) throws  EmptyDeck {
        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }
    }

    /**
     * Places the first valid (non-special) card from the deck onto the table.
     *
     * @throws EmptyDeck if no cards are available
     */
    @Override
    public void putFirstCard() throws EmptyDeck {
        Card cardToPlay = this.deck.viewCard();
        while(cardToPlay.getEffect() != null) {
            this.deck.shuffle();
            cardToPlay = this.deck.viewCard();
        }
        playCard(this.deck.takeCard());
    }

    /**
     * Places a card on the table during the game.
     *
     * @param card The card to be placed on the table.
     */
    @Override
    public void playCard(Card card) {
        this.table.addCardOnTheTable(card);
    }

    /**
     * Alternates the turn between {@link TurnEnum#PLAYER} and {@link TurnEnum#MACHINE}.
     */
    @Override
    public void changeTurn() {
        if(turn ==  TurnEnum.PLAYER) {
            turn = TurnEnum.MACHINE;
        } else if(turn ==  TurnEnum.MACHINE) {
            turn = TurnEnum.PLAYER;
        }
    }

    /**
     * Gets the player turn currently active.
     *
     * @return the current {@link TurnEnum}
     */
    public TurnEnum getTurn() {
        return turn;
    }
    /**
     * Sets the turn manually.
     *
     * @param turn the {@link TurnEnum} to set
     */
    public void setTurn(TurnEnum turn) {
        this.turn = turn;
    }

    /**
     * Handles the scenario when a player shouts "Uno", forcing the other player to draw a card.
     *
     * @param playerWhoSang The player who shouted "Uno".
     */
    @Override
    public void haveSungOne(String playerWhoSang) {
        try {
            if (playerWhoSang.equals("HUMAN_PLAYER")) {
                machinePlayer.addCard(this.deck.takeCard());
            } else {
                humanPlayer.addCard(this.deck.takeCard());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the current visible cards of the human player starting from a specific position.
     *
     * @param posInitCardToShow The initial position of the cards to show.
     * @return An array of cards visible to the human player.
     */
    @Override
    public Card[] getCurrentVisibleCardsHumanPlayer(int posInitCardToShow) {
        int totalCards = this.humanPlayer.getCardsPlayer().size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = this.humanPlayer.getCard(posInitCardToShow + i);
        }

        return cards;
    }

    /**
     * Checks if the game is over.
     *
     * @return 0 if the game is not over, 1 if the deck is empty, 2 if the human player has
     * played all his card or 3 if the machine player has played all his cards.
     */
    @Override
    public GameStateEnum isGameOver() {
        if(deck.isEmpty()){
            return GameStateEnum.DECK_EMPTY;
        } else if (humanPlayer.getCardsPlayer().isEmpty()) {
            return GameStateEnum.PLAYER_WON;
        } else if (machinePlayer.getCardsPlayer().isEmpty()) {
            return GameStateEnum.MACHINE_WON;

        }
        return GameStateEnum.GAME_ONGOING;
    }

    /**
     * Returns the deck
     * @return the deck of cards
     */
    public Deck getDeck(){
        return this.deck;
    }

    /**
     * Sets the current color of the game
     */
    public void setCurrentColor(){
        currentColor = table.getCurrentCardOnTheTable().getColor();
    }

    /**
     * Returns the current color of the game
     * @return current color of the card on the table
     */
    public String getCurrentColor(){
        return currentColor;
    }

}
