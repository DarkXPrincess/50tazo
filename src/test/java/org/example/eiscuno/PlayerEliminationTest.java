package org.example.eiscuno;

import org.example.eiscuno.model.Card;
import org.example.eiscuno.model.Player;
import org.example.eiscuno.model.Rank;
import org.example.eiscuno.model.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerEliminationTest {
    @Test
    void playerWithNoPlayableCardsIsDetected() {
        Player p = new Player("tester", true);
        // give cards that would always bust: all adding 10+ when sum is already 45
        p.addCard(new Card(Rank.TEN, Suit.HEARTS));
        p.addCard(new Card(Rank.TEN, Suit.SPADES));
        p.addCard(new Card(Rank.TEN, Suit.CLUBS));
        p.addCard(new Card(Rank.TEN, Suit.DIAMONDS));

        int currentSum = 45;
        assertFalse(p.hasPlayableCard(currentSum));
    }
}
