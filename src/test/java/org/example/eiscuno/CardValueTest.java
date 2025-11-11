package org.example.eiscuno;

import org.example.eiscuno.model.Card;
import org.example.eiscuno.model.Rank;
import org.example.eiscuno.model.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardValueTest {
    @Test
    void faceCardsAreNegativeTen() {
        Card j = new Card(Rank.J, Suit.HEARTS);
        Card q = new Card(Rank.Q, Suit.SPADES);
        Card k = new Card(Rank.K, Suit.DIAMONDS);
        assertEquals(-10, j.valueFor(0));
        assertEquals(-10, q.valueFor(0));
        assertEquals(-10, k.valueFor(0));
    }

    @Test
    void nineIsNeutral() {
        Card nine = new Card(Rank.NINE, Suit.CLUBS);
        assertEquals(0, nine.valueFor(0));
    }

    @Test
    void aceChooses10WhenNotBustOtherwise1() {
        Card a = new Card(Rank.A, Suit.SPADES);
        assertEquals(10, a.valueFor(0));
        // if current sum 45 then 10 would bust -> returns 1
        assertEquals(1, a.valueFor(45));
    }
}
