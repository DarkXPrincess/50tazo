package org.example.eiscuno.model;

import org.example.eiscuno.exception.NoPlayableCardException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Core game engine implementing the rules of "Cincuentazo" (50-taZo).
 */
public class GameEngine {
    private final Deck deck;
    private final List<Player> players = new ArrayList<>();
    private final LinkedList<Card> tablePile = new LinkedList<>();
    private int currentSum = 0;

    /**
     * Create a new game engine with a human player and N machine players.
     * @param humanName name of human player
     * @param machines number of machine players (1..3)
     */
    public GameEngine(String humanName, int machines) {
        if (machines < 1 || machines > 3) throw new IllegalArgumentException("machines must be 1..3");
        this.deck = new Deck();
        players.add(new Player(humanName, true));
        for (int i = 1; i <= machines; i++) players.add(new Player("CPU-" + i, false));
    }

    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }

    public int getCurrentSum() { return currentSum; }

    public Deck getDeck() { return deck; }

    public Card getTableTop() { return tablePile.isEmpty() ? null : tablePile.getLast(); }

    /**
     * Deal initial hands (4 cards each) and place one card on the table to start the sum.
     */
    public void dealInitial() {
        // deal 4 cards to each player
        for (Player p : players) {
            List<Card> drawn = deck.draw(4);
            p.addCards(drawn);
        }

        // place one card on table to start sum
        Card starter = deck.draw();
        tablePile.add(starter);
        // special rule: initial Ace counts as 1 for starting sum (per spec), otherwise use valueFor(0)
        switch (starter.getRank()) {
            case A:
                currentSum = 1;
                break;
            default:
                currentSum = starter.valueFor(0);
        }
    }

    /**
     * Attempt to play a card for a player. For the human player the chosen card must be passed.
     * For tests and CPU usage this API enforces the rule and updates deck/table/currentSum.
     * @param player player performing the play
     * @param card card to play
     * @throws NoPlayableCardException when the provided card cannot be played (or player has none playable)
     */
    public synchronized void playCard(Player player, Card card) throws NoPlayableCardException {
        Objects.requireNonNull(player);
        Objects.requireNonNull(card);
        if (player.isEliminated()) throw new NoPlayableCardException("Player is eliminated");
        if (!player.getHand().contains(card)) throw new NoPlayableCardException("Card not in player's hand");

        int v = card.valueFor(currentSum);
        if (currentSum + v > 50) throw new NoPlayableCardException("Play would exceed 50");

        // perform play
        player.removeCard(card);
        tablePile.add(card);
        currentSum += v;

        // draw a replacement card if possible
        if (deck.isEmpty()) replenishDeckFromTable();
        if (!deck.isEmpty()) {
            player.addCard(deck.draw());
        }
    }

    /**
     * If a player's hand contains no playable card at the start of their turn, eliminate them.
     * Eliminated player's cards are sent to the bottom of the deck.
     */
    public synchronized boolean checkEliminateIfNoPlay(Player player) {
        if (player.isEliminated()) return false;
        if (!player.hasPlayableCard(currentSum)) {
            // send cards to bottom
            List<Card> removed = player.removeAllCards();
            deck.addToBottom(removed);
            player.eliminate();
            return true;
        }
        return false;
    }

    /**
     * Replenish the deck from the table pile EXCEPT the last card, as specified.
     */
    public synchronized void replenishDeckFromTable() {
        if (tablePile.size() <= 1) return; // nothing to replenish
        List<Card> toMove = new ArrayList<>();
        while (tablePile.size() > 1) {
            toMove.add(tablePile.removeFirst());
        }
        // shuffle and add to deck
        Collections.shuffle(toMove);
        deck.addToBottomAndShuffle(toMove);
    }

    /**
     * Returns number of active (non-eliminated) players.
     */
    public long activePlayersCount() {
        return players.stream().filter(p -> !p.isEliminated()).count();
    }

    /**
     * Simple helper: find player by id.
     */
    public Player findById(String id) {
        return players.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
}
