package org.example.eiscuno.ai;

import org.example.eiscuno.model.GameEngine;
import org.example.eiscuno.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game loop and turn execution for both human and AI players.
 * Handles player elimination checks and game end conditions.
 */
public class GameLoopManager {
    private final GameEngine engine;
    private final List<Thread> aiThreads = new ArrayList<>();
    private volatile boolean gameRunning = true;
    private int currentPlayerIndex = 0;

    /**
     * Create game loop manager.
     */
    public GameLoopManager(GameEngine engine) {
        this.engine = engine;
    }

    /**
     * Start game loop in a background thread.
     * Handles turn management and AI player execution.
     */
    public void startGameLoop() {
        Thread gameThread = new Thread(() -> {
            while (gameRunning && engine.activePlayersCount() > 1) {
                try {
                    advanceTurn();
                    Thread.sleep(500); // Slight delay between turns
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    gameRunning = false;
                }
            }

            // Game ended
            Player winner = engine.getPlayers().stream()
                .filter(p -> !p.isEliminated())
                .findFirst()
                .orElse(null);
            if (winner != null) {
                System.out.println("Game Over! Winner: " + winner.getId());
                onGameEnd(winner);
            }
        });
        gameThread.setDaemon(false);
        gameThread.start();
    }

    /**
     * Advance to next player's turn, execute AI if needed.
     */
    private void advanceTurn() {
        // Find next non-eliminated player
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % engine.getPlayers().size();
        } while (currentPlayerIndex < engine.getPlayers().size() &&
                 engine.getPlayers().get(currentPlayerIndex).isEliminated());

        Player currentPlayer = engine.getPlayers().get(currentPlayerIndex);

        // Check if player should be eliminated (no playable cards)
        if (engine.checkEliminateIfNoPlay(currentPlayer)) {
            return; // Skip this player
        }

        // If AI player, execute AI turn in background thread
        if (!currentPlayer.isHuman()) {
            AIPlayer aiPlayer = new AIPlayer(currentPlayer, engine);
            Thread aiThread = new Thread(aiPlayer);
            aiThreads.add(aiThread);
            aiThread.start();
            
            // Wait for AI to complete turn
            try {
                aiThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            // Human player turn: controller handles input
            System.out.println("Human player turn: " + currentPlayer.getId());
        }
    }

    /**
     * Stop the game loop.
     */
    public void stopGameLoop() {
        gameRunning = false;
        // Join AI threads
        for (Thread t : aiThreads) {
            try {
                t.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Callback when game ends (override in subclass or use callback).
     */
    protected void onGameEnd(Player winner) {
        // Placeholder for UI update
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
