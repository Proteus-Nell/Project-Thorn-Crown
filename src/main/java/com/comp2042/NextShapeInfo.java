package com.comp2042;

/**
 * Holds information about the next shape rotation.
 * Contains the shape matrix and its rotation index.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a new NextShapeInfo object.
     *
     * @param shape    The shape matrix.
     * @param position The rotation index.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets the shape matrix.
     * 
     * @return A copy of the shape matrix.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation position index.
     * 
     * @return The position index.
     */
    public int getPosition() {
        return position;
    }
}
