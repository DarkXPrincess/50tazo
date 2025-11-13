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
    protected boolean hasPlayed;

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
        System.out.println("la variable hasPlayed para " + name + " es: " + hasPlayed);
        System.out.println("Checking valid cards for player: " + name);

        // Revisar todas las cartas de la mano: si alguna puede jugarse, devuelve true.
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

    public void setHasPlayed(boolean played) {
        this.hasPlayed = played;
    }
    public boolean getHasPlayed() {
        return hasPlayed;
    }
    /**
     * Sets the player's name.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public List<Card> clearHand() {
        List<Card> cards = new ArrayList<>(hand);
        hand.clear();
        return cards;
    }
}
