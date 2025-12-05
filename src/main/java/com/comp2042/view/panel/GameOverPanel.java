package com.comp2042.view.panel;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Panel displayed when the game is over.
 * Shows the "GAME OVER" message.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs a new GameOverPanel.
     * Initializes the "GAME OVER" label and adds it to the center of the panel.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
