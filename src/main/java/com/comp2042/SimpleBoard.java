package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private static final int TETRONIMO_STARTPOS_X = 4;
    private static final int TETRONIMO_STARTPOS_Y = 10;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        return moveBrick(0, 1);
    }

    @Override
    public boolean moveBrickLeft() {
        return moveBrick(-1, 0);
    }

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

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(TETRONIMO_STARTPOS_X, TETRONIMO_STARTPOS_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(),
                getCurrentX(), getCurrentY());
    }

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

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix); // Returns an encapsulated copy of the current game matrix.
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), getCurrentX(), getCurrentY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
}
