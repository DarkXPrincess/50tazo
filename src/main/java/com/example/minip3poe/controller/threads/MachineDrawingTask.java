package com.example.minip3poe.controller.threads;

import com.example.minip3poe.model.Card;
import com.example.minip3poe.model.GameModel;
import com.example.minip3poe.model.player.MachinePlayer;
import javafx.application.Platform;

import java.util.function.Consumer;

/**
 * Task that simulates machine player's drawing time (1-2 seconds)
 * after playing a card.
 * Implements HU-4: Machine player drawing card with delay.
 *
 * @author Juan David Salazar
 * @author Veronica Granados
 * @author Freddy Alexander Melo Buitrago
 * @version 1.0
 */
public class MachineDrawingTask extends Thread {

    private final MachinePlayer machine;
    private final GameModel gameModel;
    private final Consumer<String> logger;
    private final Runnable uiUpdater;
    private final Runnable nextTurnProcessor;

    /**
     * Creates a new machine drawing task.
     *
     * @param machine the machine player
     * @param gameModel the game model
     * @param logger callback to log messages
     * @param uiUpdater callback to update UI
     * @param nextTurnProcessor callback to process next turn
     */
    public MachineDrawingTask(MachinePlayer machine,
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
    public void run() {
        try {
            // Simulate drawing delay (1-2 seconds)
            Thread.sleep(machine.getDrawingDelay());

            Platform.runLater(() -> {
                // Draw card
                Card drawnCard = gameModel.drawCard();

                if (drawnCard != null) {
                    logger.accept(machine.getName() + " robó una carta del mazo.");
                } else {
                    logger.accept(machine.getName() + " intentó robar pero no hay cartas.");
                }

                uiUpdater.run();



                // Move to next turn
                gameModel.nextTurn();
                nextTurnProcessor.run();
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
