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
        ACE(1),TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
        NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);

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
     * Calculates the resource number for this card.
     * Pattern: CLUBS (1-13), SPADES (14-26), HEARTS (27-39), DIAMONDS (40-52)
     * Within each suit: ACE=0, TWO=1, THREE=2, ..., TEN=9, JACK=10, QUEEN=11, KING=12
     *
     * @return the resource number
     */
    private int calculateResourceNumber() {
        // Base number for each suit (matching file order: CLUBS, SPADES, HEARTS, DIAMONDS)
        int suitBase;
        switch (suit) {
            case CLUBS:
                suitBase = 1;    // Recurso_1 to Recurso_13
                break;
            case SPADES:
                suitBase = 14;   // Recurso_14 to Recurso_26
                break;
            case HEARTS:
                suitBase = 27;   // Recurso_27 to Recurso_39
                break;
            case DIAMONDS:
                suitBase = 40;   // Recurso_40 to Recurso_52
                break;
            default:
                suitBase = 1;
        }

        // Offset within the suit based on rank order
        // File order: ACE, TWO, THREE, ..., TEN, JACK, QUEEN, KING
        int rankOffset;
        switch (rank) {
            case ACE:
                rankOffset = 0;  // As va primero
                break;
            case TWO:
                rankOffset = 1;
                break;
            case THREE:
                rankOffset = 2;
                break;
            case FOUR:
                rankOffset = 3;
                break;
            case FIVE:
                rankOffset = 4;
                break;
            case SIX:
                rankOffset = 5;
                break;
            case SEVEN:
                rankOffset = 6;
                break;
            case EIGHT:
                rankOffset = 7;
                break;
            case NINE:
                rankOffset = 8;
                break;
            case TEN:
                rankOffset = 9;
                break;
            case JACK:
                rankOffset = 10;  // J después del 10
                break;
            case QUEEN:
                rankOffset = 11;  // Q después de J
                break;
            case KING:
                rankOffset = 12;  // K al final
                break;
            default:
                rankOffset = 0;
        }

        return suitBase + rankOffset;
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
            case NINE: return 9;
            case TEN: return 10;
            case JACK:return -10;
            case QUEEN:return -10;
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
        int resourceNumber = calculateResourceNumber();
        return "Recurso_" + resourceNumber + ".png";
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
