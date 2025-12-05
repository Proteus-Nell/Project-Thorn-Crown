package com.comp2042.view.panel;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Displays a resume prompt overlay when returning from the menu.
 * Instructs the player to press ESC to continue playing.
 */
public class ResumePanel extends VBox {

    /**
     * Creates the resume panel with instruction text.
     */
    public ResumePanel() {
        setSpacing(10);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
        setPrefSize(200, 100);

        Label titleLabel = new Label("PAUSED");
        titleLabel.setFont(Font.font("Arial", 24));
        titleLabel.setTextFill(Color.WHITE);

        Label instructionLabel = new Label("Press ESC to continue");
        instructionLabel.setFont(Font.font("Arial", 14));
        instructionLabel.setTextFill(Color.LIGHTGRAY);

        getChildren().addAll(titleLabel, instructionLabel);
        setVisible(false);
    }
}
