package com.comp2042;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Renders the game board background (locked pieces).
 * Responsible for displaying the static game matrix.
 */
public class BoardRenderer {
    private static final int BRICK_SIZE = GameConfig.BRICK_SIZE;
    private static final int BOARD_ROW_OFFSET = GameConfig.BOARD_ROW_OFFSET;

    private static final Color[] BRICK_COLORS = {
            Color.TRANSPARENT, // 0
            Color.AQUA, // 1
            Color.BLUEVIOLET, // 2
            Color.DARKGREEN, // 3
            Color.YELLOW, // 4
            Color.RED, // 5
            Color.BEIGE, // 6
            Color.BURLYWOOD // 7
    };

    private Rectangle[][] displayMatrix;

    /**
     * Initializes the board display matrix.
     * 
     * @param boardMatrix The game board matrix
     * @param gamePanel   The GridPane to render on
     */
    public void initialize(int[][] boardMatrix, GridPane gamePanel) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int i = BOARD_ROW_OFFSET; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - BOARD_ROW_OFFSET);
            }
        }
    }

    /**
     * Refreshes the board display with current game state.
     * 
     * @param board The current board matrix
     */
    public void refresh(int[][] board) {
        for (int i = BOARD_ROW_OFFSET; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Gets the display matrix for ghost rendering.
     * 
     * @return The rectangle matrix
     */
    public Rectangle[][] getDisplayMatrix() {
        return displayMatrix;
    }

    private void setRectangleData(int colorIndex, Rectangle rectangle) {
        rectangle.setFill(getFillColor(colorIndex));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private Paint getFillColor(int colorIndex) {
        return (colorIndex >= 0 && colorIndex < BRICK_COLORS.length)
                ? BRICK_COLORS[colorIndex]
                : Color.WHITE;
    }
}
