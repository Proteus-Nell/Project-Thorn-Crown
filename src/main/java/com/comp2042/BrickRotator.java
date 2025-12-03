package com.comp2042;

import com.comp2042.logic.bricks.Brick;

/**
 * Handles the rotation of bricks.
 * Manages the current rotation state of a brick and calculates the next shape.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates the next shape rotation for the current brick.
     *
     * @return A {@link NextShapeInfo} object containing the next shape matrix and
     *         its index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the current shape matrix of the brick.
     *
     * @return The 2D array representing the current brick shape.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation index of the brick.
     *
     * @param currentShape The index of the rotation state.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick to be rotated.
     * Resets the rotation index to 0.
     *
     * @param brick The {@link Brick} to rotate.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

}
