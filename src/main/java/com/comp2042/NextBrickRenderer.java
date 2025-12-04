package com.comp2042;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Renders the preview of the next tetromino piece.
 * Displays a small version of the upcoming brick in a 4x4 grid.
 */
public class NextBrickRenderer {
    private static final int BRICK_SIZE = GameConfig.BRICK_SIZE;

    private static final Color[] BRICK_COLORS = {
            Color.TRANSPARENT, Color.AQUA, Color.BLUEVIOLET,
            Color.DARKGREEN, Color.YELLOW, Color.RED,
            Color.BEIGE, Color.BURLYWOOD
    };

    private Rectangle[][] rectangles;

    /**
     * Initializes the next brick preview renderer.
     * Creates a 4x4 grid of rectangles for displaying the next piece.
     * 
     * @param nextBrick      Initial next brick data
     * @param nextBrickPanel The GridPane to render into
     */
    public void initialize(int[][] nextBrick, GridPane nextBrickPanel) {
        // Create a 4x4 grid for preview (max tetromino size)
        rectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }

        refresh(nextBrick);
    }

    /**
     * Refreshes the next brick preview display.
     * 
     * @param nextBrick The next brick data to display
     */
    public void refresh(int[][] nextBrick) {
        // Clear all cells first
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rectangles[i][j].setFill(Color.TRANSPARENT);
                rectangles[i][j].setArcHeight(0);
                rectangles[i][j].setArcWidth(0);
            }
        }

        // Draw the next brick
        if (nextBrick != null) {
            for (int i = 0; i < nextBrick.length && i < 4; i++) {
                for (int j = 0; j < nextBrick[i].length && j < 4; j++) {
                    if (nextBrick[i][j] > 0) {
                        setRectangleData(nextBrick[i][j], rectangles[i][j]);
                    }
                }
            }
        }
    }

    private void setRectangleData(int colorIndex, Rectangle rectangle) {
        rectangle.setFill(getFillColor(colorIndex));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private Color getFillColor(int colorIndex) {
        return (colorIndex >= 0 && colorIndex < BRICK_COLORS.length)
                ? BRICK_COLORS[colorIndex]
                : Color.WHITE;
    }
}
