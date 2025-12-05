package com.comp2042.app;

import com.comp2042.config.GameConfig;
import com.comp2042.controller.GameController;
import com.comp2042.controller.GuiController;
import com.comp2042.controller.MainMenuController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main application class.
 * Initializes the JavaFX stage and manages scene switching between menu and
 * game.
 */
public class Main extends Application {

    private static Stage primaryStage;
    private static GuiController currentGuiController;
    private static Scene gameScene; // Store the game scene for reuse
    private static boolean hasActiveGame = false;

    /**
     * Starts the JavaFX application.
     * Loads the main menu first, then displays the primary stage.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("TetrisJFX");
        showMenu();
        primaryStage.show();
    }

    /**
     * Switches to the main menu scene.
     */
    public static void showMenu() {
        try {
            URL location = Main.class.getClassLoader().getResource("MainMenu.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            // Pass game state to menu controller
            MainMenuController menuController = loader.getController();
            menuController.setHasActiveGame(hasActiveGame);

            Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resumes the currently paused game by restoring its scene.
     */
    public static void resumeGame() {
        if (hasActiveGame && gameScene != null && currentGuiController != null) {
            primaryStage.setScene(gameScene);
            currentGuiController.showResumePanel();
        }
    }

    /**
     * Starts a new game, discarding any active game.
     */
    public static void startNewGame() {
        hasActiveGame = false;
        currentGuiController = null;
        gameScene = null;
        showGame();
    }

    /**
     * Switches to the game scene and initializes the game controller.
     */
    public static void showGame() {
        try {
            URL location = Main.class.getClassLoader().getResource("gameLayout.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(location);
            Parent root = fxmlLoader.load();
            currentGuiController = fxmlLoader.getController();

            gameScene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
            primaryStage.setScene(gameScene);

            // Initialize the game controller which starts the game logic
            new GameController(currentGuiController);
            hasActiveGame = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if there is an active game in progress.
     * 
     * @return true if a game is active, false otherwise
     */
    public static boolean hasActiveGame() {
        return hasActiveGame;
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
