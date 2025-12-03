package com.comp2042;

/**
 * Data record representing the view state of a brick.
 * Contains the brick's shape, position, next brick, and ghost position.
 *
 * @param brickData      The shape matrix of the current brick.
 * @param xPosition      The x-coordinate of the brick.
 * @param yPosition      The y-coordinate of the brick.
 * @param nextBrickData  The shape matrix of the next brick.
 * @param ghostYPosition The y-coordinate of the ghost brick.
 */
public record ViewData(
        int[][] brickData,
        int xPosition,
        int yPosition,
        int[][] nextBrickData,
        int ghostYPosition) {

    /**
     * Gets a copy of the brick data.
     * 
     * @return A copy of the brick matrix.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x-coordinate.
     * 
     * @return The x-coordinate.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y-coordinate.
     * 
     * @return The y-coordinate.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a copy of the next brick data.
     * 
     * @return A copy of the next brick matrix.
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the ghost y-coordinate.
     * 
     * @return The ghost y-coordinate.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }
}
