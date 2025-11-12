package com.example.minip3poe.controller.threads;

import com.example.minip3poe.model.Card;
import com.example.minip3poe.model.GameModel;
import com.example.minip3poe.model.exceptions.InvalidCardPlayException;
import com.example.minip3poe.model.player.MachinePlayer;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

/**
 * Task that simulates machine player's thinking time (2-4 seconds)
 * before selecting and playing a card.
 * Implements HU-3: Machine player card selection with delay.
 *
 * @author [Tu nombre]
 * @version 1.0
 */
public class MachineThinkingTask extends Task<Void> {

    private final MachinePlayer machine;
    private final GameModel gameModel;
    private final Consumer<String> logger;
    private final Runnable uiUpdater;
    private final Runnable nextTurnProcessor;

    /**
     * Creates a new machine thinking task.
     *
     * @param machine the machine player
     * @param gameModel the game model
     * @param logger callback to log messages
     * @param uiUpdater callback to update UI
     * @param nextTurnProcessor callback to process next turn
     */
    public MachineThinkingTask(MachinePlayer machine,
                               GameModel gameModel,
                               Consumer<String> logger,
                               Runnable uiUpdater,
                               Runnable nextTurnProcessor) {
        this.machine = machine;
        this.gameModel = gameModel;
        this.logger = logger;
        this.uiUpdater = uiUpdater;
        this.nextTurnProcessor = nextTurnProcessor;
    }

    @Override
    protected Void call() throws Exception {
        // Simulate thinking delay (2-4 seconds)
        Thread.sleep(machine.getThinkingDelay());

        Platform.runLater(() -> {
            try {
                // Select valid card
                Card selectedCard = machine.selectCard(gameModel.getTableSum());

                if (selectedCard != null) {
                    // Play the card
                    gameModel.playCard(selectedCard);
                    logger.accept(machine.getName() + " jugó: " + selectedCard + " Cuenta actual: " + gameModel.getTableSum());
                    uiUpdater.run();

                    // Start drawing task
                    logger.accept(machine.getName() + " va a robar una carta...");
                    MachineDrawingTask drawingTask = new MachineDrawingTask(
                            machine,
                            gameModel,
                            logger,
                            uiUpdater,
                            nextTurnProcessor
                    );
                    new Thread(drawingTask).start();

                } else {
                    logger.accept("Error: " + machine.getName() + " no pudo seleccionar carta.");
                    nextTurnProcessor.run();
                }

            } catch (InvalidCardPlayException e) {
                logger.accept("Error: " + e.getMessage());
                nextTurnProcessor.run();
            }
        });

        return null;
    }
}
