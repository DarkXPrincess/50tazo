package org.example.eiscuno.model.machine;

import org.example.eiscuno.listener.CurrentColorListener;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.table.Table;

/**
 * Class {@code ThreadCurrentColorMachine}
 * <p>
 * This class represents a separate thread responsible for monitoring and saving
 * the current color of the top card on the table in an UNO game.
 * It periodically checks whether the color of the previously played card
 * differs from the current card and updates the {@link GameUno} state accordingly.
 * </p>
 *
 * <p>If a color change is detected, an optional {@link CurrentColorListener}
 * can be notified to handle UI or game logic updates.</p>
 *
 * @see GameUno
 * @see Table
 * @see CurrentColorListener
 */
public class ThreadCurrentColorMachine implements Runnable{

    /** Reference to the active UNO game instance for updating the current color. */
    private GameUno gameUno;
    /** Reference to the game {@link Table}, used to access current and previous cards. */
    private Table table;
    /** Controls whether this thread should keep running. */
    private volatile boolean running;
    /** Listener to be triggered whenever the color of the card on the table changes. */
    private CurrentColorListener currentColorListener;
    /** Ensures that the "first card" message is displayed only once. */
    private boolean firstTextShown;

    /**
     * Thread responsible for saving the current color of the card.
     * It periodically checks whether the color of the previous and the current cards have changed, and if so it saves it.
     * @param gameUno The game currently being played.
     * @param table The table from the currently played game.
     */
    public ThreadCurrentColorMachine(GameUno gameUno, Table table) {
        this.gameUno = gameUno;
        running = true;
        this.table = table;
        firstTextShown = false;
    }

    /**
     * Main execution loop of the thread.
     * Continuously runs while {@code running} is true. If the color of the card has changed,
     * the machine waits for a short period before saving it.
     */
    @Override
    public void run() {
        while(running){
            try {
                String previousColor = table.getpreviousCardOnTheTable().getColor();
                String actualColor = table.getCurrentCardOnTheTable().getColor();
                if (previousColor != actualColor) {
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    gameUno.setCurrentColor();

                    if (currentColorListener != null) {
                        currentColorListener.onColorChanged();
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                if(!firstTextShown){
                    System.out.println("Primera carta! ");
                    firstTextShown = true;
                }
            }
        }
    }

    /**
     * Sets the listener to be triggered when the color of the cards have changed.
     *
     * @param currentColorListener the listener to handle COLOR-related behavior
     */
    public void setCurrentColorListener(CurrentColorListener currentColorListener) {
        this.currentColorListener = currentColorListener;
    }

    /**
     * Sets the boolean running false, therefore, ends the run loop
     */
    public void stopThread() {
        this.running = false;
    }
}
