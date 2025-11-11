package org.example.eiscuno;

import org.example.eiscuno.model.Card;
import org.example.eiscuno.model.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    @Test
    void drawReducesSize() {
        Deck deck = new Deck();
        int before = deck.size();
        Card c = deck.draw();
        assertNotNull(c);
        assertEquals(before - 1, deck.size());
    }
}
