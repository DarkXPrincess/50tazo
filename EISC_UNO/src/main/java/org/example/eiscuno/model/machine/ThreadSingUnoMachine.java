package org.example.eiscuno.model.machine;

import org.example.eiscuno.listener.UnoEventListener;
import org.example.eiscuno.model.exceptions.EmptyDeck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

/**
 * Runnable que vigila si el jugador humano declara "UNO" cuando le queda una carta.
 * Si no lo hace dentro de un intervalo aleatorio, come una carta y notifica al listener.
 */
public class ThreadSingUnoMachine implements Runnable {

    /** Cantidad de bots (máquinas) contra los que se juega. */
    private int botsCount = 1;

    /** Bandera: el jugador ya cantó UNO. */
    private volatile boolean alreadySangUno = false;

    /** Control del ciclo del hilo. */
    private volatile boolean running = true;

    /** Referencias al juego y al jugador humano. */
    private final GameUno gameUno;
    private final Player humanPlayer;

    /** Listener opcional para eventos de “olvidó cantar UNO”. */
    private UnoEventListener unoEventListener;

    /**
     * @param humanPlayer jugador humano
     * @param gameUno instancia de la lógica del juego
     */
    public ThreadSingUnoMachine(Player humanPlayer, GameUno gameUno) {
        this.humanPlayer = humanPlayer;
        this.gameUno = gameUno;
    }

    /* ===================== Setters / Getters públicos ===================== */

    /** Establece la cantidad de bots (mínimo 1). */
    public void setBotsCount(int n) {
        this.botsCount = Math.max(1, n);
    }

    public int getBotsCount() {
        return botsCount;
    }

    /** Inyecta el listener de eventos UNO. */
    public void setUnoEventListener(UnoEventListener listener) {
        this.unoEventListener = listener;
    }

    /** Marca si el jugador ya cantó UNO. */
    public void setAlreadySangUno(boolean value) {
        this.alreadySangUno = value;
    }

    public boolean getAlreadySangUno() {
        return this.alreadySangUno;
    }

    /** Solicita finalizar el hilo. */
    public void stopThread() {
        running = false;
    }

    /* ============================ Lógica del hilo ========================= */

    @Override
    public void run() {
        while (running) {
            try {
                // Evita ocupar CPU si no hay condición que vigilar
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }

            if (humanPlayer.getCardsPlayer().size() == 1 && !alreadySangUno) {
                try {
                    // Espera aleatoria para dar oportunidad de cantar UNO
                    Thread.sleep(2000 + (long) (Math.random() * 2000));
                    System.out.println("Sleeping waiting for player to say UNO");
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }

                if (!alreadySangUno) {
                    handleForgotToSayUno();
                }
            }
        }
    }

    /**
     * Aplica la penalización y notifica cuando el humano no cantó UNO.
     */
    private void handleForgotToSayUno() {
        System.out.println("UNO (penalty)!");
        try {
            gameUno.eatCard(humanPlayer, 1);
        } catch (EmptyDeck e) {
            e.printStackTrace();
        }

        if (unoEventListener != null) {
            unoEventListener.onPlayerForgotToSayUno();
        }
        setAlreadySangUno(true);
    }
}
