package com.example.minip3poe.model.player;

import com.example.minip3poe.model.Card;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a player in the Cincuentazo game.
 * Implements the IPlayer interface.
 *
 * @author Juan David Salazar
 * @version 1.0
 */
public abstract class Player implements IPlayer {

    protected String name;
    protected List<Card> hand;
    protected boolean isEliminated;

    /**
     * Creates a new player with the specified name.
     *
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isEliminated = false;
    }

    @Override
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    @Override
    public boolean removeCard(Card card) {
        return hand.remove(card);
    }

    @Override
    public boolean hasValidCard(int currentTableSum) {
        for (Card card : hand) {
            if (card.canBePlayed(currentTableSum)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public abstract Card selectCard(int currentTableSum);

    @Override
    public void eliminate() {
        isEliminated = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    @Override
    public boolean isEliminated() {
        return isEliminated;
    }

    @Override
    public int getHandSize() {
        return hand.size();
    }

    @Override
    public List<Card> clearHand() {
        List<Card> cards = new ArrayList<>(hand);
        hand.clear();
        return cards;
    }
}
