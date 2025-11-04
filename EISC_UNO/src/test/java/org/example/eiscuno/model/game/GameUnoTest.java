package org.example.eiscuno.model.game;

import javafx.application.Platform;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;

class GameUnoTest {
    @BeforeAll
    public static void initJFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    void testStartGameDealsCorrectly() throws EmptyDeck {
        Player human = new Player("HUMAN_PLAYER");
        Player machine = new Player("MACHINE_PLAYER");
        Deck deck = new Deck();
        Table table = new Table();
        GameUno game = new GameUno(human, machine, deck, table);

        game.startGame();

        assertEquals(5, human.getCardsPlayer().size());
        assertEquals(5, machine.getCardsPlayer().size());
    }

    @Test
    void testIsCardPlayable() {
        Player human = new Player("HUMAN_PLAYER");
        Player machine = new Player("MACHINE_PLAYER");
        Deck deck = new Deck();
        Table table = new Table();
        GameUno game = new GameUno(human, machine, deck, table);

      /* Card current = new Card(EISCUnoEnum.RED_5.getFilePath(),"5", "RED");
        Card sameColor = new Card(EISCUnoEnum.RED_7.getFilePath(),"7", "RED");
        Card sameValue = new Card(EISCUnoEnum.BLUE_5.getFilePath(),"5", "BLUE");
        Card specialEat4 = new Card(EISCUnoEnum.FOUR_WILD_DRAW.getFilePath(), "EAT4", "UNKNOWN");
        Card specialNewcolor = new Card(EISCUnoEnum.WILD.getFilePath(), "NEWCOLOR", "UNKNOWN");
        Card specialReverse = new Card(EISCUnoEnum.RESERVE_RED.getFilePath(), "REVERSE", "RED");
        Card specialSkip = new Card(EISCUnoEnum.SKIP_RED.getFilePath(), "SKIP", "RED");
        Card specialEat2 = new Card(EISCUnoEnum.TWO_WILD_DRAW_RED.getFilePath(), "EAT2", "RED");
        Card invalid = new Card(EISCUnoEnum.BLUE_7.getFilePath(),"BLUE", "7");

        assertTrue(game.isCardPlayable(sameColor, current));
        assertTrue(game.isCardPlayable(sameValue, current));
        assertTrue(game.isCardPlayable(specialEat4, current));
        assertTrue(game.isCardPlayable(specialNewcolor, current));
        assertTrue(game.isCardPlayable(specialReverse, current));
        assertTrue(game.isCardPlayable(specialSkip, current));
        assertTrue(game.isCardPlayable(specialEat2, current));
        assertFalse(game.isCardPlayable(invalid, current));
    }
        */
    }

    @Test
    void testEatCardAddsCards() throws EmptyDeck {
        Player human = new Player("HUMAN_PLAYER");
        Player machine = new Player("MACHINE_PLAYER");
        Deck deck = new Deck();
        Table table = new Table();
        GameUno game = new GameUno(human, machine, deck, table);

        game.eatCard(human, 3);

        assertEquals(3, human.getCardsPlayer().size());
    }

    @Test
    void testChangeTurn() {
        Player human = new Player("HUMAN_PLAYER");
        Player machine = new Player("MACHINE_PLAYER");
        Deck deck = new Deck();
        Table table = new Table();
        GameUno game = new GameUno(human, machine, deck, table);

        game.setTurn(TurnEnum.PLAYER);
        game.changeTurn();
        assertEquals(TurnEnum.MACHINE, game.getTurn());

        game.changeTurn();
        assertEquals(TurnEnum.PLAYER, game.getTurn());
    }

}