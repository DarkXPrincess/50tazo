/*package org.example.eiscuno.model.card;

import javafx.application.Platform;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import static org.junit.jupiter.api.Assertions.*;


class CardTest {
    @BeforeAll
    public static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    void cardCreatesCorrectlyWithInitialValues() {
        Card card = new Card(EISCUnoEnum.RED_1.getFilePath(), "1", "RED");
        assertEquals("1", card.getValue());
        assertEquals("RED", card.getColor());
    }

    @Test
    void cardChangesColorCorrectly() {
        Card card = new Card(EISCUnoEnum.RED_1.getFilePath(), "1", "RED");
        card.setColor("GREEN");
        assertEquals("GREEN", card.getColor());
    }

    @Test
    void cardCreatesCorrectlyImageView(){
        Card card = new Card(EISCUnoEnum.RED_1.getFilePath(), "1", "RED");
        assertNotNull(card.getCard());
        assertEquals(90, card.getCard().getFitHeight());
        assertEquals(60, card.getCard().getFitWidth());
    }
}
    */