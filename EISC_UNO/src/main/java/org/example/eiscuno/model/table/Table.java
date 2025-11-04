package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table implements Serializable{
    /** Represents the played cards on the current table. */
    private ArrayList<Card> cardsTable;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){
        this.cardsTable = new ArrayList<Card>();
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
    }

    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    /**
     * Retrieves the previous card on the table
     *
     * @return The card previously played.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getpreviousCardOnTheTable() throws IndexOutOfBoundsException {
        return this.cardsTable.get(this.cardsTable.size()-2);
    }
}
