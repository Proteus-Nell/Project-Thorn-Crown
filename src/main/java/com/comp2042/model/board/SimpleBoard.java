package com.comp2042.model.board;

import com.comp2042.config.GameConfig;
import com.comp2042.model.brick.Brick;
import com.comp2042.model.brick.BrickGenerator;
import com.comp2042.model.brick.BrickRotator;
import com.comp2042.model.brick.RandomBrickGenerator;
import com.comp2042.model.data.NextShapeInfo;
import com.comp2042.model.data.ViewData;
import com.comp2042.model.score.ClearRow;
import com.comp2042.model.score.Score;

import java.awt.*;

/**
 * Implementation of the Board interface.
 * Manages the game grid, active brick, and game logic.
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private static final int TETRONIMO_STARTPOS_X = GameConfig.TETROMINO_START_X;
    private static final int TETRONIMO_STARTPOS_Y = GameConfig.TETROMINO_START_Y;

    /**
     * Constructs a new SimpleBoard.
     *
     * @param width  The width of the board.
     * @param height The height of the board.
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveBrickDown() {
        return moveBrick(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveBrickLeft() {
        return moveBrick(-1, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveBrickRight() {
        return moveBrick(1, 0);
    }

    // [Refactoring Job: Code Duplication] Method to move the tetronimo.
    private boolean moveBrick(int dx, int dy) {
        Point newPosition = new Point(getCurrentX(), getCurrentY());
        newPosition.translate(dx, dy);
        return tryAction(brickRotator.getCurrentShape(), (int) newPosition.getX(), (int) newPosition.getY(),
                () -> currentOffset = newPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        return tryAction(nextShape.getShape(), getCurrentX(), getCurrentY(),
                () -> brickRotator.setCurrentShape(nextShape.getPosition()));
    }

    /*
     * [Refactoring Job: Code Duplication]
     * Helper Method for collision detection and conditional state updates, called
     * by rotateLeftBrick and moveBrick.
     */
    private boolean tryAction(int[][] shape, int x, int y, Runnable onSuccess) {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        boolean conflict = MatrixOperations.intersect(currentMatrix, shape, x, y);
        if (!conflict) {
            onSuccess.run();
        }
        return !conflict;
    }

    /**
     * Check if the brick can move down without actually moving it.
     * Used to verify if a piece should be locked.
     */
    public boolean canMoveDown() {
        Point testPosition = new Point(getCurrentX(), getCurrentY());
        testPosition.translate(0, 1);
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        return !MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(),
                (int) testPosition.getX(), (int) testPosition.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(TETRONIMO_STARTPOS_X, TETRONIMO_STARTPOS_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(),
                getCurrentX(), getCurrentY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(),
                getCurrentX(), getCurrentY());
    }

    // Additional get helper methods.
    private int getCurrentX() {
        return (int) currentOffset.getX();
    }

    private int getCurrentY() {
        return (int) currentOffset.getY();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix); // Returns an encapsulated copy of the current game matrix.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ViewData getViewData() {
        int ghostY = calculateGhostPosition();
        return new ViewData(brickRotator.getCurrentShape(), getCurrentX(), getCurrentY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0), ghostY);
    }

    private int calculateGhostPosition() {
        int ghostY = getCurrentY();
        int[][] shape = brickRotator.getCurrentShape();
        int currentX = getCurrentX();

        while (!MatrixOperations.intersect(currentGameMatrix, shape, currentX, ghostY + 1)) {
            ghostY++;
        }

        return ghostY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
}
