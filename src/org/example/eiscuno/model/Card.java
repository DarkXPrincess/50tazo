package org.example.eiscuno.model;

/**
 * Represents a playing card with a rank and suit.
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    /**
     * Get the numeric effect of this card on the table sum. For Ace the value depends on the current sum.
     * @param currentSum current table sum
     * @return an int representing how this card changes the sum (can be negative)
     */
    public int valueFor(int currentSum) {
        switch (rank) {
            case TWO: return 2;
            case THREE: return 3;
            case FOUR: return 4;
            case FIVE: return 5;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 0; // neutral
            case TEN: return 10;
            case J:
            case Q:
            case K:
                return -10;
            case A:
                // return 10 when it doesn't bust (>50), otherwise 1
                if (currentSum + 10 <= 50) return 10;
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return rank.getSymbol() + " of " + suit.name();
    }
}
