package com.example.minip3poe.model;

/**
 * Represents a playing card in the Cincuentazo game.
 * Each card has a rank, suit, and a value that affects the table sum.
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class Card {

    /**
     * Enum representing card ranks
     */
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
        NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int numericValue;

        Rank(int numericValue) {
            this.numericValue = numericValue;
        }

        public int getNumericValue() {
            return numericValue;
        }
    }

    /**
     * Enum representing card suits
     */
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    private final Rank rank;
    private final Suit suit;

    /**
     * Creates a new card with the specified rank and suit.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Calculates the value this card adds or subtracts from the table sum.
     * Rules:
     * - Cards 2-8 and 10: add their numeric value
     * - Card 9: adds 0
     * - J, Q, K: subtract 10
     * - Ace: adds 1 or 10 (calculated based on current table sum)
     *
     * @param currentTableSum the current sum on the table
     * @return the value to add to the table sum
     */
    public int getGameValue(int currentTableSum) {
        switch (rank) {
            case TWO: return 2;
            case THREE: return 3;
            case FOUR: return 4;
            case FIVE: return 5;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 0;
            case TEN: return 10;
            case JACK:
            case QUEEN:
            case KING: return -10;
            case ACE:
                // Ace is 1 or 10, choose the best option
                return (currentTableSum + 10 <= 50) ? 10 : 1;
            default: return 0;
        }
    }

    /**
     * Checks if this card can be played without exceeding 50.
     *
     * @param currentTableSum the current sum on the table
     * @return true if the card can be played, false otherwise
     */
    public boolean canBePlayed(int currentTableSum) {
        return (currentTableSum + getGameValue(currentTableSum)) <= 50;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the image filename for this card.
     *
     * @return the filename string
     */
    public String getImageName() {
        String rankStr = rank.name().toLowerCase();
        String suitStr = suit.name().toLowerCase();
        return rankStr + "_of_" + suitStr + ".png";
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
