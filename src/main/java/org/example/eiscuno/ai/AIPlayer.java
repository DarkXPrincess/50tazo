package org.example.eiscuno.ai;

import org.example.eiscuno.model.Card;
import org.example.eiscuno.model.GameEngine;
import org.example.eiscuno.model.Player;
import org.example.eiscuno.exception.NoPlayableCardException;

import java.util.Optional;
import java.util.Random;

/**
 * AI player that makes decisions with realistic timing delays.
 * Simulates thinking time (2-4 seconds for play decision, 1-2 seconds for draw).
 */
public class AIPlayer implements Runnable {
    private final Player player;
    private final GameEngine engine;
    private final Random random = new Random();
    private volatile boolean isRunning = true;

    /**
     * Create AI player controller.
     */
    public AIPlayer(Player player, GameEngine engine) {
        this.player = player;
        this.engine = engine;
    }

    /**
     * Run AI turn: decide on card to play, play it, then draw replacement.
     */
    @Override
    public void run() {
        try {
            // Simulate thinking time for card selection (2-4 seconds)
            long thinkTime = 2000 + random.nextInt(2000);
            Thread.sleep(thinkTime);

            // Find playable card
            Optional<Card> playableCard = player.getPlayableCard(engine.getCurrentSum());
            if (playableCard.isPresent()) {
                // Play the card
                synchronized (engine) {
                    engine.playCard(player, playableCard.get());
                }
                
                // Simulate draw time (1-2 seconds)
                long drawTime = 1000 + random.nextInt(1000);
                Thread.sleep(drawTime);
            } else {
                // No playable card: player is eliminated
                synchronized (engine) {
                    engine.checkEliminateIfNoPlay(player);
                }
            }

            isRunning = false;
        } catch (NoPlayableCardException e) {
            // Card play failed (shouldn't happen with our logic), eliminate player
            synchronized (engine) {
                engine.checkEliminateIfNoPlay(player);
            }
            isRunning = false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
    }
}
