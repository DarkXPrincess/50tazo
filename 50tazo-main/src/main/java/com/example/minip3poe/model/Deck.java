package com.example.minip3poe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a deck of cards for the Cincuentazo game.
 * Manages the main deck and discard pile.
 *
 * @author Juan David Salazar
 * @version 1.0
 */
public class Deck {

    private Stack<Card> mainDeck;
    private Stack<Card> discardPile;

    /**
     * Creates a new deck with all 52 cards and shuffles them.
     */
    public Deck() {
        mainDeck = new Stack<>();
        discardPile = new Stack<>();
        initializeDeck();
        shuffle();
    }

    /**
     * Initializes the deck with all 52 cards.
     */
    private void initializeDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                mainDeck.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Shuffles the deck using Collections.shuffle().
     */
    public void shuffle() {
        Collections.shuffle(mainDeck);
    }

    /**
     * Draws a card from the deck.
     * If the main deck is empty, reshuffles the discard pile (except the top card).
     *
     * @return the drawn card, or null if no cards available
     */
    public Card drawCard() {
        if (mainDeck.isEmpty()) {
            reshuffleDiscardPile();
        }

        if (!mainDeck.isEmpty()) {
            return mainDeck.pop();
        }

        return null;
    }

    /**
     * Adds a card to the discard pile (the table).
     *
     * @param card the card to add
     */
    public void addToDiscardPile(Card card) {
        if (card != null) {
            discardPile.push(card);
        }
    }

    /**
     * Reshuffles the discard pile back into the main deck,
     * keeping the top card on the table.
     */
    private void reshuffleDiscardPile() {
        if (discardPile.size() > 1) {
            Card topCard = discardPile.pop();

            List<Card> cardsToReshuffle = new ArrayList<>(discardPile);
            mainDeck.addAll(cardsToReshuffle);
            shuffle();

            discardPile.clear();
            discardPile.push(topCard);
        }
    }

    /**
     * Adds multiple cards to the bottom of the deck.
     * Used when a player is eliminated.
     *
     * @param cards the cards to add (cards that were previuosly held by the eliminated player)
     */
    public void addCardsToBottom(List<Card> cards) {
        mainDeck.addAll(0, cards);
    }

    /**
     * Gets the top card of the discard pile without removing it.
     *
     * @return the top card, or null if discard pile is empty
     */
    public Card getTopCard() {
        return discardPile.isEmpty() ? null : discardPile.peek();
    }

    /**
     * Gets the number of cards remaining in the main deck.
     *
     * @return the number of cards
     */
    public int getRemainingCards() {
        return mainDeck.size();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if both main deck and discard pile are empty
     */
    public boolean isEmpty() {
        return mainDeck.isEmpty() && discardPile.isEmpty();
    }
}
