package com.comp2042;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Renders the active brick and ghost preview.
 * Responsible for displaying the falling piece and its landing preview.
 */
public class BrickRenderer {
    private static final int BRICK_SIZE = GameConfig.BRICK_SIZE;
    private static final int BOARD_ROW_OFFSET = GameConfig.BOARD_ROW_OFFSET;
    private static final int BRICK_PANEL_Y_OFFSET = GameConfig.BRICK_PANEL_Y_OFFSET;

    private static final Color[] BRICK_COLORS = {
            Color.TRANSPARENT, Color.AQUA, Color.BLUEVIOLET,
            Color.DARKGREEN, Color.YELLOW, Color.RED,
            Color.BEIGE, Color.BURLYWOOD
    };

    private Rectangle[][] rectangles;
    private GridPane brickPanel;
    private GridPane gamePanel;
    private ViewData currentViewData;
    private BoardRenderer boardRenderer;

    /**
     * Initializes the brick renderer.
     * 
     * @param brick         Initial brick data
     * @param brickPanel    The GridPane for the brick
     * @param gamePanel     The main game panel
     * @param boardRenderer The board renderer for ghost access
     */
    public void initialize(ViewData brick, GridPane brickPanel, GridPane gamePanel,
            BoardRenderer boardRenderer) {
        this.brickPanel = brickPanel;
        this.gamePanel = gamePanel;
        this.boardRenderer = boardRenderer;
        this.currentViewData = brick;

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        calculateLayout(brick);
    }

    /**
     * Refreshes the brick display.
     * 
     * @param brick Updated brick data
     */
    public void refresh(ViewData brick) {
        currentViewData = brick;
        calculateLayout(brick);

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

    /**
     * Refreshes brick and renders ghost preview.
     * 
     * @param brick Updated brick data
     * @param board Current board matrix
     */
    public void refreshWithGhost(ViewData brick, int[][] board) {
        refresh(brick);
        renderGhost(brick, board);
    }

    /**
     * Renders the ghost preview at landing position.
     * 
     * @param brick Brick data
     * @param board Current board matrix
     */
    public void renderGhost(ViewData brick, int[][] board) {
        int ghostY = brick.getGhostYPosition();
        int ghostX = brick.getxPosition();
        int currentY = brick.getyPosition();
        int[][] shape = brick.getBrickData();
        Rectangle[][] displayMatrix = boardRenderer.getDisplayMatrix();

        if (ghostY > currentY) {
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] > 0) {
                        int boardY = ghostY + i;
                        int boardX = ghostX + j;

                        if (isValidGhostPosition(board, boardX, boardY)) {
                            Rectangle rect = displayMatrix[boardY][boardX];
                            Color ghostColor = createGhostColor(BRICK_COLORS[shape[i][j]]);
                            rect.setFill(ghostColor);
                            rect.setArcHeight(9);
                            rect.setArcWidth(9);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the current view data.
     * 
     * @return Current ViewData
     */
    public ViewData getCurrentViewData() {
        return currentViewData;
    }

    private void calculateLayout(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap()
                + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(BRICK_PANEL_Y_OFFSET + gamePanel.getLayoutY()
                + brick.getyPosition() * brickPanel.getHgap()
                + brick.getyPosition() * BRICK_SIZE);
    }

    private boolean isValidGhostPosition(int[][] board, int x, int y) {
        return y >= BOARD_ROW_OFFSET && y < board.length
                && x >= 0 && x < board[0].length
                && board[y][x] == 0;
    }

    private Color createGhostColor(Color baseColor) {
        return Color.color(
                baseColor.getRed(),
                baseColor.getGreen(),
                baseColor.getBlue(),
                GameConfig.GHOST_OPACITY);
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
