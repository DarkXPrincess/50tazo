package org.example.eiscuno.model;

import org.example.eiscuno.exception.EmptyDeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Deck of standard 52 playing cards. Supports shuffle, draw and replenish.
 */
public class Deck {
    private final LinkedList<Card> cards = new LinkedList<>();

    public Deck() {
        initializeFullDeck();
        shuffle();
    }

    private void initializeFullDeck() {
        cards.clear();
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                cards.add(new Card(r, s));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public Card draw() {
        if (cards.isEmpty()) throw new EmptyDeckException("Deck is empty");
        return cards.removeFirst();
    }

    public List<Card> draw(int n) {
        List<Card> out = new ArrayList<>();
        for (int i = 0; i < n; i++) out.add(draw());
        return out;
    }

    /**
     * Add cards to bottom of the deck and shuffle.
     */
    public void addToBottomAndShuffle(List<Card> toAdd) {
        cards.addAll(toAdd);
        shuffle();
    }

    /**
     * Add cards to the bottom without shuffling (used when returning eliminated player cards to end).
     */
    public void addToBottom(List<Card> toAdd) {
        cards.addAll(toAdd);
    }
}
