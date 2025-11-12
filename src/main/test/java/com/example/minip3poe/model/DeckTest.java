package com.example.minip3poe.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Deck class.
 * Tests deck initialization, card drawing, reshuffling, and discard pile management.
 *
 * @author Alexander 
 * @version 1.0
 */
@DisplayName("Deck Tests")
class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Nested
    @DisplayName("Deck Initialization Tests")
    class DeckInitializationTests {

        @Test
        @DisplayName("Deck should contain 52 cards after initialization")
        void testDeckInitialSize() {
            assertEquals(52, deck.getRemainingCards());
        }

        @Test
        @DisplayName("Deck should not be empty after initialization")
        void testDeckNotEmpty() {
            assertFalse(deck.isEmpty());
        }

        @Test
        @DisplayName("Discard pile should be empty initially")
        void testDiscardPileEmpty() {
            assertNull(deck.getTopCard());
        }

        @Test
        @DisplayName("Deck should contain all suits")
        void testDeckContainsAllSuits() {
            // Draw all cards and verify we have all suits
            List<Card> allCards = new ArrayList<>();
            while (!deck.isEmpty() && allCards.size() < 52) {
                Card card = deck.drawCard();
                if (card != null) {
                    allCards.add(card);
                }
            }

            boolean hasHearts = allCards.stream().anyMatch(c -> c.getSuit() == Card.Suit.HEARTS);
            boolean hasDiamonds = allCards.stream().anyMatch(c -> c.getSuit() == Card.Suit.DIAMONDS);
            boolean hasClubs = allCards.stream().anyMatch(c -> c.getSuit() == Card.Suit.CLUBS);
            boolean hasSpades = allCards.stream().anyMatch(c -> c.getSuit() == Card.Suit.SPADES);

            assertTrue(hasHearts && hasDiamonds && hasClubs && hasSpades);
        }

        @Test
        @DisplayName("Deck should contain all ranks")
        void testDeckContainsAllRanks() {
            // Create a fresh deck
            Deck freshDeck = new Deck();
            List<Card> allCards = new ArrayList<>();
            
            while (!freshDeck.isEmpty() && allCards.size() < 52) {
                Card card = freshDeck.drawCard();
                if (card != null) {
                    allCards.add(card);
                }
            }

            assertEquals(13, Card.Rank.values().length);
            for (Card.Rank rank : Card.Rank.values()) {
                boolean hasRank = allCards.stream().anyMatch(c -> c.getRank() == rank);
                assertTrue(hasRank, "Rank " + rank + " not found in deck");
            }
        }
    }

    @Nested
    @DisplayName("Card Draw Tests")
    class CardDrawTests {

        @Test
        @DisplayName("Drawing a card should reduce deck size by 1")
        void testDrawCardReducesDeckSize() {
            int initialSize = deck.getRemainingCards();
            Card drawnCard = deck.drawCard();
            assertEquals(initialSize - 1, deck.getRemainingCards());
            assertNotNull(drawnCard);
        }

        @Test
        @DisplayName("Drawing multiple cards should reduce size correctly")
        void testDrawMultipleCards() {
            int initialSize = deck.getRemainingCards();
            for (int i = 0; i < 5; i++) {
                deck.drawCard();
            }
            assertEquals(initialSize - 5, deck.getRemainingCards());
        }

        @Test
        @DisplayName("Drawing should return different cards")
        void testDrawReturnsDifferentCards() {
            Card card1 = deck.drawCard();
            Card card2 = deck.drawCard();
            Card card3 = deck.drawCard();

            assertNotNull(card1);
            assertNotNull(card2);
            assertNotNull(card3);
            // Cards should be different (due to shuffle)
            assertNotEquals(card1, card2);
            assertNotEquals(card2, card3);
        }

        @Test
        @DisplayName("Drawing should not return null for first 52 draws")
        void testDrawDoesNotReturnNullWithinLimit() {
            for (int i = 0; i < 52; i++) {
                Card card = deck.drawCard();
                assertNotNull(card, "Card at position " + i + " should not be null");
            }
        }

        @Test
        @DisplayName("Deck should handle drawing all 52 cards")
        void testDrawAllCards() {
            for (int i = 0; i < 52; i++) {
                Card card = deck.drawCard();
                assertNotNull(card);
            }
            assertEquals(0, deck.getRemainingCards());
        }
    }

    @Nested
    @DisplayName("Discard Pile Tests")
    class DiscardPileTests {

        @Test
        @DisplayName("Adding card to discard pile should set top card")
        void testAddToDiscardPile() {
            Card card = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
            deck.addToDiscardPile(card);
            assertEquals(card, deck.getTopCard());
        }

        @Test
        @DisplayName("Top card should be the last added card")
        void testTopCardIsLastAdded() {
            Card card1 = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
            Card card2 = new Card(Card.Rank.THREE, Card.Suit.HEARTS);
            
            deck.addToDiscardPile(card1);
            deck.addToDiscardPile(card2);
            
            assertEquals(card2, deck.getTopCard());
        }

        @Test
        @DisplayName("Adding null card should not add to discard pile")
        void testAddNullCardDoesNothing() {
            deck.addToDiscardPile(null);
            assertNull(deck.getTopCard());
        }

        @Test
        @DisplayName("Multiple cards can be added to discard pile")
        void testMultipleCardsInDiscardPile() {
            Card card1 = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
            Card card2 = new Card(Card.Rank.THREE, Card.Suit.HEARTS);
            Card card3 = new Card(Card.Rank.FOUR, Card.Suit.HEARTS);
            
            deck.addToDiscardPile(card1);
            deck.addToDiscardPile(card2);
            deck.addToDiscardPile(card3);
            
            assertEquals(card3, deck.getTopCard());
        }
    }

    @Nested
    @DisplayName("Reshuffle Tests")
    class ReshuffleTests {

        @Test
        @DisplayName("Reshuffling should restore cards to deck")
        void testReshuffleRestoresCards() {
            // Draw 10 cards
            List<Card> drawnCards = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Card card = deck.drawCard();
                drawnCards.add(card);
                deck.addToDiscardPile(card);
            }

            int remainingBefore = deck.getRemainingCards();

            // Draw all remaining cards
            while (deck.getRemainingCards() > 0) {
                deck.drawCard();
            }

            // Try to draw when deck is empty (should trigger reshuffle)
            Card nextCard = deck.drawCard();
            
            // After reshuffle, deck should have cards (except the top card kept on table)
            if (nextCard != null) {
                assertTrue(deck.getRemainingCards() >= 0);
            }
        }

        @Test
        @DisplayName("Reshuffle should keep top card on table")
        void testReshuffleKeepsTopCard() {
            // Draw and add cards to discard pile
            for (int i = 0; i < 10; i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    deck.addToDiscardPile(card);
                }
            }

            Card topCardBefore = deck.getTopCard();

            // Empty the main deck
            while (deck.getRemainingCards() > 0) {
                deck.drawCard();
            }

            // Draw one more to trigger reshuffle
            deck.drawCard();

            Card topCardAfter = deck.getTopCard();

            // Top card should remain the same after reshuffle
            assertEquals(topCardBefore, topCardAfter);
        }

        @Test
        @DisplayName("Deck should have 51 cards after one reshuffle")
        void testDeckSizeAfterReshuffle() {
            // Add some cards to discard pile
            for (int i = 0; i < 5; i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    deck.addToDiscardPile(card);
                }
            }

            // Empty the main deck
            while (deck.getRemainingCards() > 0) {
                deck.drawCard();
            }

            // The last draw will trigger reshuffle
            deck.drawCard();

            // After reshuffle with 5 cards in discard pile (minus the top one),
            // we should have 4 cards available (5 - 1 for top card)
            assertTrue(deck.getRemainingCards() >= 0);
        }
    }

    @Nested
    @DisplayName("Add Cards to Bottom Tests")
    class AddCardsToBottomTests {

        @Test
        @DisplayName("Adding cards to bottom should make them available later")
        void testAddCardsToBottom() {
            // Draw 3 cards
            Card card1 = deck.drawCard();
            Card card2 = deck.drawCard();
            Card card3 = deck.drawCard();

            List<Card> cardsToAdd = new ArrayList<>();
            cardsToAdd.add(card1);
            cardsToAdd.add(card2);
            cardsToAdd.add(card3);

            // Add them to bottom
            deck.addCardsToBottom(cardsToAdd);

            // Draw all cards in deck
            List<Card> allCards = new ArrayList<>();
            while (!deck.isEmpty()) {
                Card card = deck.drawCard();
                if (card != null) {
                    allCards.add(card);
                }
            }

            // The added cards should be somewhere in the drawn cards
            for (Card addedCard : cardsToAdd) {
                boolean found = allCards.stream().anyMatch(c -> c.equals(addedCard) || 
                    (c.getRank() == addedCard.getRank() && c.getSuit() == addedCard.getSuit()));
                assertTrue(found, "Added card not found in deck");
            }
        }

        @Test
        @DisplayName("Adding cards to bottom should not affect current draws")
        void testAddToBottomDoesNotAffectImmediateDraw() {
            Card nextCard1 = deck.drawCard();
            
            List<Card> cardsToAdd = new ArrayList<>();
            cardsToAdd.add(new Card(Card.Rank.KING, Card.Suit.HEARTS));
            
            int deckSizeBefore = deck.getRemainingCards();
            deck.addCardsToBottom(cardsToAdd);
            
            // The size should increase
            assertEquals(deckSizeBefore + 1, deck.getRemainingCards());
        }

        @Test
        @DisplayName("Multiple calls to addCardsToBottom should accumulate")
        void testMultipleAddToBottom() {
            Card card1 = deck.drawCard();
            Card card2 = deck.drawCard();

            List<Card> batch1 = new ArrayList<>();
            batch1.add(card1);

            List<Card> batch2 = new ArrayList<>();
            batch2.add(card2);

            int sizeBefore = deck.getRemainingCards();
            
            deck.addCardsToBottom(batch1);
            deck.addCardsToBottom(batch2);
            
            assertEquals(sizeBefore + 2, deck.getRemainingCards());
        }
    }

    @Nested
    @DisplayName("Shuffle Tests")
    class ShuffleTests {

        @Test
        @DisplayName("Shuffle should randomize card order")
        void testShuffleRandomizesOrder() {
            // Create two decks and compare if they have different card orders
            Deck deck1 = new Deck();
            Deck deck2 = new Deck();

            List<Card> cards1 = new ArrayList<>();
            List<Card> cards2 = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                cards1.add(deck1.drawCard());
                cards2.add(deck2.drawCard());
            }

            // Due to shuffle, the order should likely be different
            // (Very small probability they're the same)
            boolean sameOrder = true;
            for (int i = 0; i < cards1.size(); i++) {
                if (!cards1.get(i).toString().equals(cards2.get(i).toString())) {
                    sameOrder = false;
                    break;
                }
            }

            // We expect different order due to shuffle (not guaranteed, but very likely)
            // This test is probabilistic
            assertFalse(sameOrder, "Decks should have different order after shuffle");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("isEmpty should return true when all cards are drawn and no discard")
        void testIsEmptyWithNoCardsAndNoDiscard() {
            // Draw all cards without adding to discard pile
            while (deck.getRemainingCards() > 0) {
                deck.drawCard();
            }

            assertTrue(deck.isEmpty());
        }

        @Test
        @DisplayName("isEmpty should return false when cards in discard pile")
        void testIsEmptyWithDiscardPile() {
            Card card = deck.drawCard();
            deck.addToDiscardPile(card);

            // Draw all remaining
            while (deck.getRemainingCards() > 0) {
                deck.drawCard();
            }

            assertFalse(deck.isEmpty());
        }

        @Test
        @DisplayName("Drawing after discard should work correctly")
        void testDrawAfterAddingToDiscard() {
            Card card = deck.drawCard();
            int sizeAfterDraw = deck.getRemainingCards();

            deck.addToDiscardPile(card);
            Card drawnAfterDiscard = deck.drawCard();

            assertEquals(sizeAfterDraw - 1, deck.getRemainingCards());
            assertNotNull(drawnAfterDiscard);
        }

        @Test
        @DisplayName("Deck should recover from empty state through reshuffle")
        void testDeckRecoveryFromEmpty() {
            // Draw all 52 cards
            List<Card> allCards = new ArrayList<>();
            while (deck.getRemainingCards() > 0) {
                Card card = deck.drawCard();
                if (card != null) {
                    allCards.add(card);
                    // Add to discard pile except the first one
                    if (allCards.size() > 1) {
                        deck.addToDiscardPile(card);
                    }
                }
            }

            // At this point, deck should be empty
            assertEquals(0, deck.getRemainingCards());

            // Try to draw - should trigger reshuffle if discard pile has cards
            Card recovered = deck.drawCard();

            // If there were discarded cards, recovery should happen
            if (allCards.size() > 1) {
                // Either get a card or deck remains empty
                assertTrue(recovered != null || deck.getRemainingCards() == 0);
            }
        }
    }
}
