package org.example.eiscuno.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.example.eiscuno.controller.GameUnoController;

import java.io.IOException;

/**
 * Ventana principal del juego UNO.
 */
public class GameUnoStage extends Stage {

    private static GameUnoStage INSTANCE;
    private GameUnoController controller;

    /** Constructor privado: carga el FXML y deja el controller listo. */
    private GameUnoStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/eiscuno/game-uno-view.fxml"));
        Parent root = loader.load();
        this.controller = loader.getController();

        Scene scene = new Scene(root);
        setTitle("EISC Uno");

        // Icono (opcional si el recurso existe)
        var iconUrl = getClass().getResource("/org/example/eiscuno/images/icono_poker.png");
        if (iconUrl != null) {
            getIcons().add(new Image(String.valueOf(iconUrl)));
        }

        setScene(scene);
        setResizable(false);

        setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        show();
    }

    /** Obtiene (y si no existe crea) la instancia única. */
    public static synchronized GameUnoStage getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new GameUnoStage();
        }
        return INSTANCE;
    }

    /**
     * Obtiene la instancia y aplica la cantidad de bots.
     * Garantiza que el controller no sea null.
     */
    public static synchronized GameUnoStage getInstance(int numPlayers) throws IOException {
        GameUnoStage stage = getInstance();
        stage.controller.setBotsCount(numPlayers);
        return stage;
    }

    /** Cierra y limpia la instancia. */
    public static synchronized void deleteInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }

    /** Acceso opcional al controller. */
    public GameUnoController getController() {
        return controller;
    }
}
