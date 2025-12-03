package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'Z' shaped Tetris brick.
 * This brick is Z-shaped.
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a new ZBrick.
     * Initializes the shape matrix with all the possible rotation states for the
     * Z-brick.
     */
    public ZBrick() {
        brickMatrix.add(new int[][] {
                { 0, 0, 0, 0 },
                { 7, 7, 0, 0 },
                { 0, 7, 7, 0 },
                { 0, 0, 0, 0 }
        });
        brickMatrix.add(new int[][] {
                { 0, 7, 0, 0 },
                { 7, 7, 0, 0 },
                { 7, 0, 0, 0 },
                { 0, 0, 0, 0 }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
