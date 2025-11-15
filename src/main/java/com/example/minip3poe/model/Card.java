package com.example.minip3poe.model;

/**
 * Represents a playing card in the Cincuentazo game.
 * Each card has a rank, suit, and a value that affects the table sum.
 *
 * @author Juan David Salazar
 * @author Veronica Granados
 * @author Freddy Alexander Melo Buitrago
 * @version 1.0
 */
public class Card {

    /**
     * Enum representing card ranks
     */
    public enum Rank {
        ACE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
        NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13);

        private final int numericValue;

        Rank(int numericValue) {
            this.numericValue = numericValue;
        }

        /**
         * Returns the numeric value associated with the card rank (e.g., TWO returns
         * 2).
         * 
         * @return the numeric value of the rank
         */
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
     * Calculates the resource number for this card, used to retrieve the image
     * file.
     * The pattern is based on file order: CLUBS (1-13), SPADES (14-26), HEARTS
     * (27-39), DIAMONDS (40-52).
     *
     * @return the resource number corresponding to the card's image
     */
    private int calculateResourceNumber() {
        // Base number for each suit (matching file order: CLUBS, SPADES, HEARTS,
        // DIAMONDS)
        int suitBase;
        switch (suit) {
            case CLUBS:
                suitBase = 1;
                break;
            case SPADES:
                suitBase = 14;
                break;
            case HEARTS:
                suitBase = 27;
                break;
            case DIAMONDS:
                suitBase = 40;
                break;
            default:
                suitBase = 1;
        }

        // Offset within the suit based on rank order (ACE=0, TWO=1, ..., KING=12)
        int rankOffset;
        switch (rank) {
            case ACE:
                rankOffset = 0;
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
                rankOffset = 10;
                break;
            case QUEEN:
                rankOffset = 11;
                break;
            case KING:
                rankOffset = 12;
                break;
            default:
                rankOffset = 0;
        }

        return suitBase + rankOffset;
    }

    /**
     * Calculates the value this card adds or subtracts from the table sum, based on
     * Cincuentazo rules.
     * Rules:
     * <ul>
     * <li>Cards 2-8 and 10: add their numeric value.</li>
     * <li>Card 9: adds 0 (neutral).</li>
     * <li>J, Q, K: subtract 10.</li>
     * <li>Ace: adds 10 if currentTableSum + 10 <= 50, otherwise adds 1.</li>
     * </ul>
     *
     * @param currentTableSum the current sum on the table
     * @return the value to add to the table sum (can be negative or zero)
     */
    public int getGameValue(int currentTableSum) {
        switch (rank) {
            case NINE:
                return 0;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case TEN:
                return 10;
            case JACK:
            case QUEEN:
            case KING:
                return -10;
            case ACE:
                // Ace is 10 or 1, choose the best option without exceeding 50
                return (currentTableSum + 10 <= 50) ? 10 : 1;
            default:
                return 0;
        }
    }

    /**
     * Checks if this card can be played without the resulting sum exceeding 50.
     *
     * @param currentTableSum the current sum on the table
     * @return true if the card can be played, false otherwise
     */
    public boolean canBePlayed(int currentTableSum) {
        return (currentTableSum + getGameValue(currentTableSum)) <= 50;
    }

    /**
     * Gets the rank of the card.
     * 
     * @return the card rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Gets the suit of the card.
     * 
     * @return the card suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the image filename for this card (e.g., "Recurso_1.png").
     *
     * @return the filename string
     */
    public String getImageName() {
        int resourceNumber = calculateResourceNumber();
        return "Recurso_" + resourceNumber + ".png";
    }

    /**
     * Returns a string representation of the card (e.g., "TWO of HEARTS").
     * 
     * @return a descriptive string for the card
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}