import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Cincuentazo game application.
 * Launches the welcome screen where the player selects opponents and game mode.
 */
public class Main extends Application {

    /**
     * Start the JavaFX application with the welcome stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/welcome-stage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 730, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cincuentazo - Welcome");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
