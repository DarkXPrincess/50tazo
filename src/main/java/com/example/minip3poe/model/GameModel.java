package com.example.minip3poe.model;

import com.example.minip3poe.model.exceptions.InvalidCardPlayException;
import com.example.minip3poe.model.exceptions.InvalidPlayerCountException;
import com.example.minip3poe.model.player.HumanPlayer;
import com.example.minip3poe.model.player.MachinePlayer;
import com.example.minip3poe.model.player.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Main model class for the Cincuentazo game.
 * Manages game state, player turns, card plays, and game flow using MVC architecture.
 *
 * Data structures used:
 * - Stack: for deck and discard pile (in Deck class)
 * - ArrayList: for player hands (in Player class)
 * - Queue: for managing turn order
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class GameModel {

    private Deck deck;
    private List<Player> allPlayers;
    private Queue<Player> turnQueue;
    private Player currentPlayer;
    private int tableSum;
    private boolean gameStarted;
    private boolean gameFinished;
    private Player winner;

    /**
     * Creates a new GameModel instance.
     */
    public GameModel() {
        this.allPlayers = new ArrayList<>();
        this.turnQueue = new LinkedList<>();
        this.tableSum = 0;
        this.gameStarted = false;
        this.gameFinished = false;
    }

    /**
     * Initializes a new game with the specified number of machine players.
     * Creates 1 human player and the specified number of machine players.
     *
     * @param machineCount number of machine players (1-3)
     * @throws InvalidPlayerCountException if machineCount is not between 1 and 3
     */
    public void initializeGame(int machineCount) throws InvalidPlayerCountException {
        if (machineCount < 1 || machineCount > 3) {
            throw new InvalidPlayerCountException(machineCount);
        }

        // Reset game state
        allPlayers.clear();
        turnQueue.clear();
        tableSum = 0;
        gameStarted = false;
        gameFinished = false;
        winner = null;

        // Create deck
        deck = new Deck();

        // Create human player
        HumanPlayer human = new HumanPlayer("Human Player");
        allPlayers.add(human);

        // Create machine players
        for (int i = 1; i <= machineCount; i++) {
            MachinePlayer machine = new MachinePlayer("Machine " + i);
            allPlayers.add(machine);
        }
    }

    /**
     * Starts the game by dealing cards and placing initial card on table.
     * Implements HU-2: Preparación del juego
     */
    public void startGame() {
        // Deal 4 cards to each player
        for (Player player : allPlayers) {
            for (int i = 0; i < 4; i++) {
                Card card = deck.drawCard();
                player.addCard(card);
            }
        }

        // Place initial card on table
        Card initialCard = deck.drawCard();
        deck.addToDiscardPile(initialCard);
        tableSum = initialCard.getGameValue(0);

        // Initialize turn queue
        turnQueue.addAll(allPlayers);
        currentPlayer = turnQueue.peek();

        gameStarted = true;
    }

    /**
     * Plays a card from the current player's hand.
     * Implements HU-3: Jugar una carta
     *
     * @param card the card to play
     * @throws InvalidCardPlayException if the card cannot be played
     */
    public void playCard(Card card) throws InvalidCardPlayException {
        if (!gameStarted || gameFinished) {
            throw new InvalidCardPlayException("Game is not in progress.");
        }

        if (currentPlayer == null) {
            throw new InvalidCardPlayException("No current player.");
        }

        // Validate card belongs to player
        if (!currentPlayer.getHand().contains(card)) {
            throw new InvalidCardPlayException("Card does not belong to current player.");
        }

        // Validate card can be played
        if (!card.canBePlayed(tableSum)) {
            throw new InvalidCardPlayException(
                    "Card would exceed table sum of 50. Current: " + tableSum +
                            ", Card value: " + card.getGameValue(tableSum)
            );
        }

        // Play the card
        currentPlayer.removeCard(card);
        deck.addToDiscardPile(card);
        tableSum += card.getGameValue(tableSum);
    }

    /**
     * Current player draws a card from the deck.
     * Implements HU-4: Tomar una carta del mazo
     *
     * @return the drawn card
     */
    public Card drawCard() {
        if (currentPlayer == null) {
            return null;
        }

        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            currentPlayer.addCard(drawnCard);
        }

        return drawnCard;
    }

    /**
     * Advances to the next player's turn.
     * Checks if current player should be eliminated first.
     */
    public void nextTurn() {
        if (currentPlayer == null) {
            return;
        }

        // Check if current player has valid cards
        if (!currentPlayer.hasValidCard(tableSum)) {
            eliminatePlayer(currentPlayer);
        }

        // Move to next player in queue
        turnQueue.poll(); // Remove current player

        // Skip eliminated players
        while (!turnQueue.isEmpty() && turnQueue.peek().isEliminated()) {
            turnQueue.poll();
        }

        // If queue is empty, refill with non-eliminated players
        if (turnQueue.isEmpty()) {
            for (Player player : allPlayers) {
                if (!player.isEliminated()) {
                    turnQueue.offer(player);
                }
            }
        }

        // Set next current player
        currentPlayer = turnQueue.isEmpty() ? null : turnQueue.peek();

        // Check for game end
        checkGameEnd();
    }

    /**
     * Eliminates a player from the game.
     * Implements HU-5: Eliminación de un jugador
     *
     * @param player the player to eliminate
     */
    public void eliminatePlayer(Player player) {
        player.eliminate();

        // Send player's cards to bottom of deck
        List<Card> playerCards = player.clearHand();
        deck.addCardsToBottom(playerCards);

        // Remove from turn queue
        turnQueue.remove(player);
    }

    /**
     * Checks if the game should end.
     * Implements HU-6: Fin del juego
     */
    private void checkGameEnd() {
        int activePlayers = 0;
        Player lastPlayer = null;

        for (Player player : allPlayers) {
            if (!player.isEliminated()) {
                activePlayers++;
                lastPlayer = player;
            }
        }

        // Game ends when only 1 player remains
        if (activePlayers == 1) {
            gameFinished = true;
            winner = lastPlayer;
        } else if (activePlayers == 0) {
            // Edge case: all players eliminated simultaneously
            gameFinished = true;
            winner = null;
        }
    }

    /**
     * Gets the current player whose turn it is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the current sum on the table.
     *
     * @return the table sum
     */
    public int getTableSum() {
        return tableSum;
    }

    /**
     * Gets the top card on the table.
     *
     * @return the top card
     */
    public Card getTopCard() {
        return deck.getTopCard();
    }

    /**
     * Gets all players in the game.
     *
     * @return list of all players
     */
    public List<Player> getAllPlayers() {
        return new ArrayList<>(allPlayers);
    }

    /**
     * Gets the list of active (non-eliminated) players.
     *
     * @return list of active players
     */
    public List<Player> getActivePlayers() {
        List<Player> active = new ArrayList<>();
        for (Player player : allPlayers) {
            if (!player.isEliminated()) {
                active.add(player);
            }
        }
        return active;
    }

    /**
     * Checks if the game has started.
     *
     * @return true if game has started
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Checks if the game has finished.
     *
     * @return true if game has finished
     */
    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Gets the winner of the game.
     *
     * @return the winning player, or null if game not finished or no winner
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Gets the number of cards remaining in the deck.
     *
     * @return number of cards in deck
     */
    public int getRemainingCards() {
        return deck.getRemainingCards();
    }

    /**
     * Gets the human player.
     *
     * @return the human player, or null if not found
     */
    public HumanPlayer getHumanPlayer() {
        for (Player player : allPlayers) {
            if (player instanceof HumanPlayer) {
                return (HumanPlayer) player;
            }
        }
        return null;
    }
}
