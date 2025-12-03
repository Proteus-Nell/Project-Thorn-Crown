package com.comp2042;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the main menu screen.
 * Handles user navigation between menu, game, and settings.
 * Dynamically updates button visibility based on game state.
 */
public class MainMenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button quitButton;

    private boolean hasActiveGame = false;

    /**
     * Sets whether there is an active game.
     * Updates button text and visibility accordingly.
     * 
     * @param hasActiveGame true if there's a paused game
     */
    public void setHasActiveGame(boolean hasActiveGame) {
        this.hasActiveGame = hasActiveGame;
        updateButtonState();
    }

    /**
     * Updates button text based on game state.
     */
    private void updateButtonState() {
        if (hasActiveGame) {
            startButton.setText("CONTINUE GAME");
        } else {
            startButton.setText("START GAME");
        }
    }

    /**
     * Handles Start/Continue Game button click.
     * Either resumes existing game or starts new one.
     */
    @FXML
    void onStartGame(ActionEvent event) {
        if (hasActiveGame) {
            Main.resumeGame();
        } else {
            Main.showGame();
        }
    }

    /**
     * Handles Settings button click.
     * Currently a placeholder for future settings implementation.
     */
    @FXML
    void onSettings(ActionEvent event) {
        System.out.println("Settings clicked - placeholder for future implementation");
    }

    /**
     * Handles Quit button click.
     * Exits the application.
     */
    @FXML
    void onQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
