package org.example.eiscuno.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a player (human or machine) and manages the player's hand.
 */
public class Player {
    private final String id;
    private final boolean human;
    private final List<Card> hand = new ArrayList<>();
    private boolean eliminated = false;

    public Player(String id, boolean human) {
        this.id = id;
        this.human = human;
    }

    public String getId() {
        return id;
    }

    public boolean isHuman() {
        return human;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card c) {
        hand.add(c);
    }

    public void addCards(List<Card> cards) {
        hand.addAll(cards);
    }

    public void removeCard(Card c) {
        hand.remove(c);
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void eliminate() {
        eliminated = true;
    }

    /**
     * Returns whether the player has any playable card given the current table sum.
     */
    public boolean hasPlayableCard(int currentSum) {
        return getPlayableCard(currentSum).isPresent();
    }

    /**
     * Return an optional card that is playable. Selection strategy: first that doesn't bust.
     */
    public Optional<Card> getPlayableCard(int currentSum) {
        for (Card c : hand) {
            int v = c.valueFor(currentSum);
            if (currentSum + v <= 50) return Optional.of(c);
        }
        return Optional.empty();
    }

    /**
     * Remove and return the specified card from the hand.
     */
    public boolean playCard(Card c) {
        return hand.remove(c);
    }

    /**
     * Remove all cards and return them (used when a player is eliminated).
     */
    public List<Card> removeAllCards() {
        List<Card> copy = new ArrayList<>(hand);
        hand.clear();
        return copy;
    }
}
