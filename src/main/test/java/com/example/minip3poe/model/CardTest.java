package com.example.minip3poe.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Card class.
 * Tests card value calculations and game rules validation.
 *
 * @author Alexander 
 * @version 1.0
 */
@DisplayName("Card Tests")
class CardTest {

    private Card cardTwo;
    private Card cardNine;
    private Card cardTen;
    private Card cardJack;
    private Card cardAce;

    @BeforeEach
    void setUp() {
        cardTwo = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
        cardNine = new Card(Card.Rank.NINE, Card.Suit.DIAMONDS);
        cardTen = new Card(Card.Rank.TEN, Card.Suit.CLUBS);
        cardJack = new Card(Card.Rank.JACK, Card.Suit.SPADES);
        cardAce = new Card(Card.Rank.ACE, Card.Suit.HEARTS);
    }

    @Test
    @DisplayName("Card number 2 should return value 2")
    void testCardTwoValue() {
        assertEquals(2, cardTwo.getGameValue(0));
    }

    @Test
    @DisplayName("Card number 9 should return value 0 (neutral)")
    void testCardNineValue() {
        assertEquals(0, cardNine.getGameValue(0));
    }

    @Test
    @DisplayName("Card number 10 should return value 10")
    void testCardTenValue() {
        assertEquals(10, cardTen.getGameValue(0));
    }

    @Test
    @DisplayName("Jack should return value -10")
    void testCardJackValue() {
        assertEquals(-10, cardJack.getGameValue(0));
    }

    @Test
    @DisplayName("Queen should return value -10")
    void testCardQueenValue() {
        Card queen = new Card(Card.Rank.QUEEN, Card.Suit.HEARTS);
        assertEquals(-10, queen.getGameValue(0));
    }

    @Test
    @DisplayName("King should return value -10")
    void testCardKingValue() {
        Card king = new Card(Card.Rank.KING, Card.Suit.CLUBS);
        assertEquals(-10, king.getGameValue(0));
    }

    @Test
    @DisplayName("Ace should return 10 when table sum + 10 <= 50")
    void testAceValueWhenCanAdd10() {
        // When table sum is 35, adding 10 gives 45, which is <= 50
        assertEquals(10, cardAce.getGameValue(35));
    }

    @Test
    @DisplayName("Ace should return 1 when table sum + 10 > 50")
    void testAceValueWhenCannotAdd10() {
        // When table sum is 41, adding 10 gives 51, which is > 50, so it returns 1
        assertEquals(1, cardAce.getGameValue(41));
    }

    @Test
    @DisplayName("Card 2-8 should return their numeric values")
    void testCardsWithNumericValues() {
        Card three = new Card(Card.Rank.THREE, Card.Suit.DIAMONDS);
        Card four = new Card(Card.Rank.FOUR, Card.Suit.SPADES);
        Card five = new Card(Card.Rank.FIVE, Card.Suit.HEARTS);
        Card six = new Card(Card.Rank.SIX, Card.Suit.CLUBS);
        Card seven = new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS);
        Card eight = new Card(Card.Rank.EIGHT, Card.Suit.SPADES);

        assertEquals(3, three.getGameValue(0));
        assertEquals(4, four.getGameValue(0));
        assertEquals(5, five.getGameValue(0));
        assertEquals(6, six.getGameValue(0));
        assertEquals(7, seven.getGameValue(0));
        assertEquals(8, eight.getGameValue(0));
    }

    @Test
    @DisplayName("Card 2 can be played when table sum is 0")
    void testCardCanBePlayedValidScenario() {
        assertTrue(cardTwo.canBePlayed(0));
    }

    @Test
    @DisplayName("Card 2 cannot be played when sum would exceed 50")
    void testCardCannotBePlayedInvalidScenario() {
        // Table sum is 49, adding 2 would give 51, exceeding 50
        assertFalse(cardTwo.canBePlayed(49));
    }

    @Test
    @DisplayName("Card cannot be played when exactly at limit")
    void testCardCanBePlayedAtExactLimit() {
        // Table sum is 48, adding 2 would give 50, which is allowed
        assertTrue(cardTwo.canBePlayed(48));
    }

    @Test
    @DisplayName("Jack can be played even when table sum is high")
    void testJackCanReduceTableSum() {
        // Table sum is 40, Jack subtracts 10, resulting in 30
        assertTrue(cardJack.canBePlayed(40));
        assertEquals(30, 40 + cardJack.getGameValue(40));
    }

    @Test
    @DisplayName("Nine doesn't affect table sum")
    void testNineDoesNotChangeTableSum() {
        int initialSum = 25;
        int resultSum = initialSum + cardNine.getGameValue(initialSum);
        assertEquals(initialSum, resultSum);
    }

    @Test
    @DisplayName("Card can be played with negative resulting sum")
    void testCardWithNegativeResultingSum() {
        // If table sum is 5 and we play Jack (-10), result is -5 which should be allowed
        assertTrue(cardJack.canBePlayed(5));
    }

    @Test
    @DisplayName("Card toString returns readable format")
    void testCardToString() {
        String cardString = cardTwo.toString();
        assertTrue(cardString.contains("TWO"));
        assertTrue(cardString.contains("HEARTS"));
    }

    @Test
    @DisplayName("Card image name is generated correctly")
    void testCardImageName() {
        String imageName = cardTwo.getImageName();
        assertTrue(imageName.contains("Recurso_"));
        assertTrue(imageName.contains(".png"));
    }

    @Test
    @DisplayName("Different suits have different resource numbers")
    void testDifferentSuitsHaveDifferentResources() {
        Card twoHearts = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
        Card twoDiamonds = new Card(Card.Rank.TWO, Card.Suit.DIAMONDS);
        Card twoClubs = new Card(Card.Rank.TWO, Card.Suit.CLUBS);
        Card twoSpades = new Card(Card.Rank.TWO, Card.Suit.SPADES);

        assertNotEquals(twoHearts.getImageName(), twoDiamonds.getImageName());
        assertNotEquals(twoHearts.getImageName(), twoClubs.getImageName());
        assertNotEquals(twoHearts.getImageName(), twoSpades.getImageName());
    }

    @ParameterizedTest
    @DisplayName("All number cards can be played at start of game")
    @ValueSource(ints = {2, 3, 4, 5, 6, 7, 8})
    void testAllNumberCardsPlayableAtStart(int rankNumericValue) {
        Card.Rank rank = Card.Rank.values()[rankNumericValue - 1];
        Card card = new Card(rank, Card.Suit.HEARTS);
        assertTrue(card.canBePlayed(0));
    }
}
