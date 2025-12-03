package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main application class.
 * Initializes the JavaFX stage and loads the game scene.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the FXML layout, sets up the scene, and initializes the game
     * controller.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If loading the FXML fails.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
        new GameController(c);
    }

    /**
     * Main entry point for the application.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
