package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.GameEngine;

/**
 * Manages loading and display of the game UI.
 */
public class GameUnoView {
    private final Stage stage;
    private final GameEngine engine;
    private GameUnoController controller;

    public GameUnoView(Stage stage, GameEngine engine) {
        this.stage = stage;
        this.engine = engine;
    }

    /**
     * Load FXML, set up controller, and display game stage.
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/game-uno-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setEngine(engine);
        controller.initialize();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Cincuentazo");
        stage.show();
    }

    public GameUnoController getController() {
        return controller;
    }
}
